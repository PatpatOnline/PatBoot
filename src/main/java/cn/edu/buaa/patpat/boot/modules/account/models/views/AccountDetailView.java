package cn.edu.buaa.patpat.boot.modules.account.models.views;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccountDetailView extends AccountView {
    private int gender;
    private String school;
    private String avatar;
}
