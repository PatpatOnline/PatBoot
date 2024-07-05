package cn.edu.buaa.patpat.boot.modules.judge.services;

import cn.edu.buaa.patpat.boot.common.utils.Generator;
import cn.edu.buaa.patpat.boot.config.RabbitMqConfig;
import cn.edu.buaa.patpat.boot.modules.judge.dto.SubmissionDto;
import cn.edu.buaa.patpat.boot.modules.stream.api.StreamApi;
import cn.edu.buaa.patpat.boot.modules.stream.dto.WebSocketPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JudgeService {
    private final RabbitTemplate rabbitTemplate;
    private final StreamApi streamApi;

    public SubmissionDto initiateSubmission(int id) {
        SubmissionDto submissionDto = new SubmissionDto();
        submissionDto.setId(id);
        submissionDto.setTaskId(Generator.randomInt(10000, 99999));
        rabbitTemplate.convertAndSend(RabbitMqConfig.PENDING, submissionDto);
        log.info("Sent submission {}", submissionDto.getTaskId());
        return submissionDto;
    }

    @RabbitListener(queues = RabbitMqConfig.RESULT)
    public void receive(SubmissionDto submissionDto) {
        streamApi.send(submissionDto.getId(), WebSocketPayload.of("judge", submissionDto));
        log.info("Received result {}", submissionDto);
    }
}
