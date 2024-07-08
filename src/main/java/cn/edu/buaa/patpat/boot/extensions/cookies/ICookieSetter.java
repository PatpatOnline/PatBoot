package cn.edu.buaa.patpat.boot.extensions.cookies;

import jakarta.servlet.http.Cookie;

public interface ICookieSetter {
    Cookie set(String value);

    Cookie clean();
}
