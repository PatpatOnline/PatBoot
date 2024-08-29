/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.account.api;

import cn.edu.buaa.patpat.boot.modules.account.models.entities.Account;
import cn.edu.buaa.patpat.boot.modules.account.models.mappers.AccountFilterMapper;
import cn.edu.buaa.patpat.boot.modules.account.models.views.TeacherIndexView;
import cn.edu.buaa.patpat.boot.modules.account.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountApi {
    private final AccountService accountService;
    private final AccountFilterMapper accountFilterMapper;

    public List<TeacherIndexView> queryTeachers() {
        return accountService.queryTeachers();
    }

    public TeacherIndexView queryTeacher(int id) {
        return accountService.queryTeacher(id);
    }

    public Account findByName(String name) {
        return accountFilterMapper.findByName(name);
    }

    public Account findByBuaaId(String buaaId) {
        return accountFilterMapper.findByBuaaId(buaaId);
    }

    public Account findRole(int id) {
        return accountFilterMapper.findRole(id);
    }
}
