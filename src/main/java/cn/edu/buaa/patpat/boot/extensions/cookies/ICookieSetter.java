/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.extensions.cookies;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public interface ICookieSetter {
    Cookie set(String value);

    Cookie clean();

    String get(HttpServletRequest request);
}
