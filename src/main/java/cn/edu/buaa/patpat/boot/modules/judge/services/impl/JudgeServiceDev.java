/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.judge.services.impl;

import cn.edu.buaa.patpat.boot.config.RabbitMqConfigDev;
import cn.edu.buaa.patpat.boot.modules.judge.dto.JudgeRequestDto;
import cn.edu.buaa.patpat.boot.modules.judge.dto.JudgeResponseDto;
import cn.edu.buaa.patpat.boot.modules.judge.services.JudgeService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({ "dev" })
public class JudgeServiceDev extends JudgeService {
    @RabbitListener(queues = RabbitMqConfigDev.RESULT, concurrency = "4")
    public void receive(JudgeResponseDto response) {
        receiveImpl(response);
    }

    @Override
    protected void sendImpl(JudgeRequestDto request) {
        rabbitTemplate.convertAndSend(RabbitMqConfigDev.PENDING, request);
    }
}
