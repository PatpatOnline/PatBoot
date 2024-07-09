package cn.edu.buaa.patpat.boot.modules.account.dto;

import lombok.Data;

@Data
public class AccountDto {
    private int id;
    private String buaaId;
    private String name;
    private int gender;
    private String school;
    private boolean teacher;
    private boolean ta;
    private String avatar;
}
