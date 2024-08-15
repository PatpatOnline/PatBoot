package cn.edu.buaa.patpat.boot.modules.problem.api;

import cn.edu.buaa.patpat.boot.modules.problem.dto.ProblemDto;
import cn.edu.buaa.patpat.boot.modules.problem.services.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProblemApi {
    private final ProblemService problemService;

    public ProblemDto query(int id) {
        return problemService.query(id, true);
    }
}
