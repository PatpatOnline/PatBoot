package cn.edu.buaa.patpat.boot.modules.judge.services;

import cn.edu.buaa.patpat.boot.common.Tuple;
import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.common.utils.Medias;
import cn.edu.buaa.patpat.boot.common.utils.Zips;
import cn.edu.buaa.patpat.boot.config.Globals;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.exceptions.InternalServerErrorException;
import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import cn.edu.buaa.patpat.boot.modules.judge.dto.*;
import cn.edu.buaa.patpat.boot.modules.judge.models.entities.Score;
import cn.edu.buaa.patpat.boot.modules.judge.models.entities.Submission;
import cn.edu.buaa.patpat.boot.modules.judge.models.mappers.ScoreMapper;
import cn.edu.buaa.patpat.boot.modules.judge.models.mappers.SubmissionMapper;
import cn.edu.buaa.patpat.boot.modules.stream.api.StreamApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Slf4j
public abstract class SubmissionService extends BaseService {
    @Autowired
    protected BucketApi bucketApi;
    @Autowired
    protected SubmissionMapper submissionMapper;
    @Autowired
    protected RabbitTemplate rabbitTemplate;
    @Autowired
    protected StreamApi streamApi;
    @Autowired
    protected ScoreMapper scoreMapper;
    @Value("${judge.judge-root}")
    private String judgeRoot;

    public Submission submit(SubmitRequest request) {
        var temp = saveSubmissionInTemp(request.getFile());

        Submission submission = mappers.map(request, Submission.class);
        submissionMapper.initialize(submission);

        String submissionPath = saveSubmission(temp, submission.getProblemId(), request.getBuaaId());
        String judgePath = saveToJudge(submissionPath, submission.getId());
        JudgeRequestDto dto = mappers.map(submission, JudgeRequestDto.class);
        JudgePayload payload = mappers.map(request, JudgePayload.class);
        payload.setSandboxPath(judgePath);
        dto.setPayload(payload);

        log.info("Send judge request: {}", dto.getId());
        sendImpl(dto);

        return submission;
    }

    private Tuple<String, String> saveSubmissionInTemp(MultipartFile file) {
        String record = bucketApi.toRecord(Globals.TEMP_TAG, "");
        String root = bucketApi.recordToPrivatePath(record);
        Path path = Path.of(root, file.getOriginalFilename());
        try {
            Medias.save(path, file);
        } catch (IOException e) {
            log.error("Failed to save submission file.", e);
            throw new InternalServerErrorException(M("system.error.io"));
        }

        return Tuple.of(root, path.toString());
    }

    private String saveSubmission(Tuple<String, String> temp, int problemId, String buaaId) {
        String record = bucketApi.toRecord(Globals.SUBMISSION_TAG, String.valueOf(problemId));
        String root = bucketApi.recordToPrivatePath(record);
        String target = Path.of(root, buaaId).toString();
        String path = Path.of(temp.second).toString();
        try {
            Medias.ensureEmptyPath(target);
            if (path.endsWith(".java")) {
                Medias.copyFile(path, target);
            } else if (path.endsWith(".zip")) {
                Zips.unzip(path, target);
            } else {
                throw new BadRequestException(M("submission.file.invalid"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            Medias.removeSilently(temp.first);
        }
        return target;
    }

    private String saveToJudge(String submissionPath, int submissionId) {
        String sandboxPath = Path.of(judgeRoot, String.valueOf(submissionId)).toString();
        try {
            Medias.ensureEmptyPath(sandboxPath);
            Medias.copyDirectory(Path.of(submissionPath), Path.of(sandboxPath, "src"));
        } catch (IOException e) {
            throw new InternalServerErrorException(M("system.error.io"));
        }
        return sandboxPath;
    }

    protected abstract void sendImpl(JudgeRequestDto request);

    protected void receiveImpl(JudgeResponseDto response) {
        log.info("Received judge response: {}", response.getId());

        Submission submission = mappers.map(response, Submission.class);
        submission.setData(mappers.toJson(response.getResult(), TestResult.DEFAULT));
        if (submissionMapper.finalize(submission) == 0) {
            log.error("Missing submission when finalizing: {}", response.getId());
            return;
        }
        SubmitResponse dto = mappers.map(response, SubmitResponse.class);
        dto.setCourseId(response.getPayload().getCourseId());
        String buaaId = response.getPayload().getBuaaId();
        streamApi.send(buaaId, dto.toWebSocketPayload());

        Medias.removeSilently(response.getPayload().getSandboxPath());

        Score score = new Score(submission.getProblemId(), submission.getAccountId(), response.getResult().getScore());
        scoreMapper.saveOrUpdate(score);
    }
}
