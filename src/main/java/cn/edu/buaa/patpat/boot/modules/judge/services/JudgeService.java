/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.judge.services;

import cn.edu.buaa.patpat.boot.common.Globals;
import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.common.utils.Medias;
import cn.edu.buaa.patpat.boot.common.utils.Zips;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.exceptions.ForbiddenException;
import cn.edu.buaa.patpat.boot.exceptions.InternalServerErrorException;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import cn.edu.buaa.patpat.boot.modules.judge.dto.*;
import cn.edu.buaa.patpat.boot.modules.judge.models.entities.ProblemScore;
import cn.edu.buaa.patpat.boot.modules.judge.models.entities.Submission;
import cn.edu.buaa.patpat.boot.modules.judge.models.mappers.ProblemScoreMapper;
import cn.edu.buaa.patpat.boot.modules.judge.models.mappers.SubmissionMapper;
import cn.edu.buaa.patpat.boot.modules.problem.models.entities.Problem;
import cn.edu.buaa.patpat.boot.modules.problem.models.mappers.ProblemMapper;
import cn.edu.buaa.patpat.boot.modules.stream.api.StreamApi;
import cn.edu.buaa.patpat.boot.modules.task.models.mappers.TaskScoreMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Slf4j
public abstract class JudgeService extends BaseService {
    @Autowired
    protected RabbitTemplate rabbitTemplate;
    @Autowired
    protected StreamApi streamApi;
    @Autowired
    private BucketApi bucketApi;
    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private ProblemScoreMapper problemScoreMapper;
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private TaskScoreMapper taskScoreMapper;

    protected abstract void sendImpl(JudgeRequestDto request);

    /**
     * Submit a solution to a problem.
     * This can be invoked by Controller or internal API.
     *
     * @param request      SubmitRequest
     * @param removeSource Whether to remove submitted files after submission.
     *                     If invoked from internal API, submitted files may still be necessary for Task.
     * @return Submission
     */
    public Submission submit(SubmitRequest request, boolean removeSource) {
        // Initialize submission in database.
        Submission submission = mappers.map(request, Submission.class);
        submissionMapper.initialize(submission);

        // Save submission to submission bucket.
        // It is mapped to as a volume for PatJudge.
        String record = saveSubmission(request.getFilePath(), submission.getProblemId(), request.getBuaaId(), removeSource);

        JudgeRequestDto dto = mappers.map(submission, JudgeRequestDto.class);
        JudgePayload payload = mappers.map(request, JudgePayload.class);
        dto.setRecord(record);
        dto.setPayload(payload);

        log.info("Send judge request: {}", dto.getId());
        sendImpl(dto);

        return submission;
    }

    public String saveSubmissionInTemp(MultipartFile file) {
        String record = bucketApi.toRecord(Globals.TEMP_TAG, "");
        String root = bucketApi.recordToPrivatePath(record);
        Path path = Path.of(root, file.getOriginalFilename());
        try {
            Medias.save(path, file);
        } catch (IOException e) {
            log.error("Failed to save submission file", e);
            throw new InternalServerErrorException(M("system.error.io"));
        }
        return path.toString();
    }

    /**
     * Call this to prevent multiple ongoing submissions.
     */
    public void checkLastSubmission(int problemId, int accountId) {
        Submission submission = submissionMapper.findCheckLast(problemId, accountId);

        // OK if no submission or submission has completed.
        if ((submission != null) && (submission.getEndTime() == null)) {
            throw new ForbiddenException(M("judge.submit.frequency"));
        }
    }

    public void checkProblem(int problemId, boolean allowHidden) {
        Problem problem = problemMapper.findJudge(problemId);
        if (problem == null) {
            throw new NotFoundException(M("problem.exists.not"));
        }
        if (problem.isHidden() && (!allowHidden)) {
            throw new ForbiddenException(M("judge.submit.forbidden"));
        }
    }

    protected void receiveImpl(JudgeResponseDto response) {
        log.info("Received judge response: {}", response.getId());
        JudgePayload payload = response.getPayload();

        Submission submission = mappers.map(response, Submission.class);
        submission.setData(mappers.toJson(response.getResult(), TestResult.DEFAULT));
        if (submissionMapper.finalize(submission) == 0) {
            log.error("Missing submission when finalizing: {}", response.getId());
            return;
        }
        SubmitResponse dto = mappers.map(response, SubmitResponse.class);
        dto.setCourseId(payload.getCourseId());
        streamApi.send(payload.getBuaaId(), dto.toWebSocketPayload());

        updateProblemScore(submission.getProblemId(), payload.getAccountId(), response.getResult().getScore());
        if (response.getPayload().getTaskId() != 0) {
            updateTaskScore(payload.getTaskId(), payload.getAccountId(), response.getResult().getScore());
        }
    }

    private String saveSubmission(String filePath, int problemId, String buaaId, boolean removeSource) {
        String record = bucketApi.toRecord(Globals.SUBMISSION_TAG, String.valueOf(problemId));
        String root = bucketApi.recordToPrivatePath(record);
        String target = Path.of(root, buaaId).toString();
        try {
            Medias.ensureEmptyPath(target);
            if (filePath.endsWith(".java")) {
                Medias.copyFile(filePath, target);
            } else if (filePath.endsWith(".zip")) {
                Zips.unzip(filePath, target);
            } else {
                throw new BadRequestException(M("judge.file.invalid"));
            }
        } catch (IOException e) {
            throw new InternalServerErrorException(M("system.error.io"));
        } finally {
            if (removeSource) {
                Medias.removeSilently(Medias.getParentPath(filePath));
            }
        }

        return bucketApi.toRecord(String.valueOf(problemId), buaaId);
    }

    private void updateProblemScore(int problemId, int accountId, int score) {
        ProblemScore problemScore = new ProblemScore(problemId, accountId, score);
        problemScoreMapper.saveOrUpdate(problemScore);
    }

    private void updateTaskScore(int taskId, int accountId, int score) {
        // This will overwrite the old score.
        int updated = taskScoreMapper.updateScoreByAccount(taskId, accountId, score);
        if (updated == 0) {
            log.error("Task score not found: {} {}", taskId, accountId);
        }
    }
}
