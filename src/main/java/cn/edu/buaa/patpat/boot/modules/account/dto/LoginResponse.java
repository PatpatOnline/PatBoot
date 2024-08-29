/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.account.dto;

import cn.edu.buaa.patpat.boot.modules.course.models.entities.Course;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginResponse {
    private AccountDto account;
    private List<Course> courses;
}
