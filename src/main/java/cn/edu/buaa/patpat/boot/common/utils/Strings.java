package cn.edu.buaa.patpat.boot.common.utils;

public class Strings {
    private Strings() {}

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isBlank();
    }
}
