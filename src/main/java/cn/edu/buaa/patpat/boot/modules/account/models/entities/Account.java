package cn.edu.buaa.patpat.boot.modules.account.models.entities;


import lombok.Data;

/**
 * Table: account
 */
@Data
public class Account {
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
    private boolean isTeacher;

    /**
     * Whether the account is a teacher assistant.
     * A teacher is also a teacher assistant.
     */
    private boolean isTa;

    /**
     * Avatar path, a relative path to avatar base directory.
     */
    private String avatar;

    public boolean isStudent() {
        return !isTa;
    }
}
