package cn.edu.buaa.patpat.boot.modules.auth.models;

import lombok.Data;

@Data
public class AuthPayload {
    private int id;
    private String buaaId;
    private boolean isTa;
    private boolean isTeacher;

    boolean isStudent() {
        return !isTa;
    }
}
