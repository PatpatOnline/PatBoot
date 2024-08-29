/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.common.requets;

import cn.edu.buaa.patpat.boot.extensions.mappers.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {
    @Autowired
    protected Mappers mappers;
}
