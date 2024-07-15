package cn.edu.buaa.patpat.boot.modules.auth.models;

import lombok.Data;

@Data
public class AuthPayload {
    private int id;
    private String buaaId;
    private boolean ta;
    private boolean teacher;

    boolean isStudent() {
        return !ta;
    }
}
