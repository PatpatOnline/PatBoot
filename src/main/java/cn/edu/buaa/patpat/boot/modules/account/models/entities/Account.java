package cn.edu.buaa.patpat.boot.modules.account.models.entities;


import cn.edu.buaa.patpat.boot.common.models.HasCreated;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Table: account
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Account extends HasCreated implements Serializable {
    private int id;

    /**
     * BUAA ID. (e.g. 21370000)
     */
    private String buaaId;

    /**
     * Real name. (Stored in Chinese)
     */
    private String name;

    /**
     * Currently password is stored in plain text.
     * TODO: Store password with hash.
     */
    private String password;

    /**
     * User gender, available values are defined in {@link Gender}.
     */
    private int gender;

    /**
     * School name.
     */
    private String school;

    /**
     * Whether the account belongs to a teacher.
     */
    private boolean teacher;

    /**
     * Whether the account is a teacher assistant.
     * A teacher is also a teacher's assistant.
     */
    private boolean ta;

    /**
     * Avatar path, a relative path to avatar base directory.
     */
    private String avatar;

    public boolean isStudent() {
        return !ta;
    }
}
