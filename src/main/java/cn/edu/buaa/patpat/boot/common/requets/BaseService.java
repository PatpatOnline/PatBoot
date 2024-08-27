package cn.edu.buaa.patpat.boot.common.requets;

import cn.edu.buaa.patpat.boot.extensions.mappers.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseService {
    @Autowired
    protected Mappers mappers;
}
