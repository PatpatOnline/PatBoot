package cn.edu.buaa.patpat.boot.modules.problem.services;

import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.exceptions.InternalServerErrorException;
import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import cn.edu.buaa.patpat.boot.modules.problem.dto.CreateProblemRequest;
import cn.edu.buaa.patpat.boot.modules.problem.exceptions.ProblemInitializeException;
import cn.edu.buaa.patpat.boot.modules.problem.models.entities.Problem;
import cn.edu.buaa.patpat.boot.modules.problem.models.mappers.ProblemMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProblemService extends BaseService {
    private final BucketApi bucketApi;
    private final ProblemInitializer problemInitializer;
    private final ProblemMapper problemMapper;

    public Problem createProblem(CreateProblemRequest request) {
        // validate problem configuration
        ProblemInitializer.InitializeResult result;
        try {
            result = problemInitializer.initialize(request.getFile());
        } catch (ProblemInitializeException e) {
            throw new BadRequestException(M("problem.init.error", e.getMessage()));
        }

        // save the problem to the database
        Problem problem = mappers.map(request, Problem.class);
        try {
            problem.setData(mappers.toJson(result.descriptor()));
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize problem descriptor", e);
            throw new InternalServerErrorException(M("system.error.internal"));
        }
        problemMapper.save(problem);

        // finalize the problem by moving the files to the correct location
        try {
            problemInitializer.finalize(result, problem.getId());
        } catch (ProblemInitializeException e) {
            throw new InternalServerErrorException(M("problem.init.error", e.getMessage()));
        }

        return problem;
    }
}
