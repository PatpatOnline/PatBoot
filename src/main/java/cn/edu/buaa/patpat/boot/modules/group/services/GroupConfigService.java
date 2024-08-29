/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.group.services;

import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.modules.group.dto.UpdateGroupConfigRequest;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupConfig;
import cn.edu.buaa.patpat.boot.modules.group.models.mappers.GroupConfigMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Service
@RequiredArgsConstructor
public class GroupConfigService extends BaseService {
    private static final GroupConfig DEFAULT_CONFIG;

    static {
        DEFAULT_CONFIG = new GroupConfig();
        DEFAULT_CONFIG.setMaxSize(3);
        DEFAULT_CONFIG.setEnabled(false);
        DEFAULT_CONFIG.setMinWeight(50);
        DEFAULT_CONFIG.setMaxWeight(150);
    }

    private final GroupConfigMapper groupConfigMapper;

    /**
     * Get group configuration by course id. Won't return null.
     */
    public GroupConfig get(int courseId) {
        GroupConfig config = groupConfigMapper.find(courseId);
        if (config == null) {
            config = mappers.map(DEFAULT_CONFIG, GroupConfig.class);
            config.setCourseId(courseId);
            groupConfigMapper.saveOrUpdate(config);
        }
        return config;
    }

    public GroupConfig update(GroupConfig config, UpdateGroupConfigRequest request) {
        mappers.map(request, config);
        if (config.getMinWeight() > config.getMaxWeight()) {
            throw new BadRequestException(M("group.weight.invalid"));
        }
        groupConfigMapper.saveOrUpdate(config);
        return config;
    }
}
