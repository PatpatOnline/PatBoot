package cn.edu.buaa.patpat.boot.modules.account.models.views;

import cn.edu.buaa.patpat.boot.common.models.HasCreated;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccountListView extends HasCreated implements Serializable {
    private int id;
    private String buaaId;

    private String name;
    private int gender;

    private String school;

    private boolean teacher;
    private boolean ta;
}
