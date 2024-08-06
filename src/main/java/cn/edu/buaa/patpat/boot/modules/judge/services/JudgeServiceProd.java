package cn.edu.buaa.patpat.boot.modules.judge.services;

import cn.edu.buaa.patpat.boot.config.RabbitMqConfigProd;
import cn.edu.buaa.patpat.boot.modules.judge.dto.JudgeRequestDto;
import cn.edu.buaa.patpat.boot.modules.judge.dto.JudgeResponseDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({ "stag", "prod" })
public class JudgeServiceProd extends JudgeService {
    @RabbitListener(queues = RabbitMqConfigProd.RESULT, concurrency = "4")
    public void receive(JudgeResponseDto response) {
        receiveImpl(response);
    }

    @Override
    protected void sendImpl(JudgeRequestDto request) {
        rabbitTemplate.convertAndSend(RabbitMqConfigProd.PENDING, request);
    }
}
