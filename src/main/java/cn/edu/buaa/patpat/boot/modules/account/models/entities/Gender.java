package cn.edu.buaa.patpat.boot.modules.account.models.entities;

import cn.edu.buaa.patpat.boot.modules.account.config.AccountConfig;

public class Gender {
    public static final int SECRET = 0;
    public static final int BOY = 1;
    public static final int GIRL = 2;

    public static int fromString(String gender) {
        return switch (gender) {
            case "男" -> BOY;
            case "女" -> GIRL;
            default -> SECRET;
        };
    }

    public static int fromInt(int gender) {
        return switch (gender) {
            case BOY -> BOY;
            case GIRL -> GIRL;
            default -> SECRET;
        };
    }

    public static String toAvatar(int gender) {
        return switch (gender) {
            case BOY -> AccountConfig.BOY_AVATAR;
            case GIRL -> AccountConfig.GIRL_AVATAR;
            default -> AccountConfig.DEFAULT_AVATAR;
        };
    }
}
