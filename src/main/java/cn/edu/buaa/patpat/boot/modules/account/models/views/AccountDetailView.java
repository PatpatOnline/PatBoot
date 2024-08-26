package cn.edu.buaa.patpat.boot.modules.account.models.views;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccountDetailView extends AccountView implements Serializable {
    private int gender;
    private String school;
    private String avatar;
}
