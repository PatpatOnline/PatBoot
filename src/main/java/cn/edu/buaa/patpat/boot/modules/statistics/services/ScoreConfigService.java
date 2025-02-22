/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.statistics.services;

import cn.edu.buaa.patpat.boot.common.Globals;
import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.modules.statistics.dto.UpdateScoreConfigRequest;
import cn.edu.buaa.patpat.boot.modules.statistics.models.entities.ScoreConfig;
import cn.edu.buaa.patpat.boot.modules.statistics.models.mappers.ScoreConfigMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Service
@RequiredArgsConstructor
public class ScoreConfigService extends BaseService {
    private static final ScoreConfig DEFAULT_CONFIG;

    static {
        DEFAULT_CONFIG = new ScoreConfig();
        DEFAULT_CONFIG.setLabScore(10);
        DEFAULT_CONFIG.setIterScore(10);
        DEFAULT_CONFIG.setProjScore(30);
        DEFAULT_CONFIG.setLatePercent(50);
    }

    private final ScoreConfigMapper scoreConfigMapper;

    public ScoreConfig get(int courseId) {
        ScoreConfig config = scoreConfigMapper.find(courseId);
        if (config == null) {
            config = DEFAULT_CONFIG;
            scoreConfigMapper.saveOrUpdate(config);
        }
        return config;
    }

    public ScoreConfig update(int courseId, UpdateScoreConfigRequest request) {
        ScoreConfig config = get(courseId);
        mappers.map(request, config);
        if (config.getTotalScore() > Globals.FULL_SCORE) {
            throw new BadRequestException(M("score.total.exceed"));
        }
        scoreConfigMapper.saveOrUpdate(config);
        return config;
    }
}
