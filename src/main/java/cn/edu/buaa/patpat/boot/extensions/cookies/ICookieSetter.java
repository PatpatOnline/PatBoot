package cn.edu.buaa.patpat.boot.extensions.cookies;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public interface ICookieSetter {
    Cookie set(String value);

    Cookie clean();

    String get(HttpServletRequest request);
}
