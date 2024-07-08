package cn.edu.buaa.patpat.boot.extensions.cookies;

import cn.edu.buaa.patpat.boot.common.utils.Strings;
import jakarta.servlet.http.Cookie;

public class CookieSetter implements ICookieSetter {
    private final String name;
    private final String domain;
    private final String path;
    private final int maxAge;
    private final boolean httpOnly;
    private final boolean secure;

    public CookieSetter(String name, String path) {
        this(name, path, 3600);
    }

    public CookieSetter(String name, String path, int maxAge) {
        this(name, path, maxAge, false, false);
    }

    public CookieSetter(String name, String path, int maxAge, boolean httpOnly, boolean secure) {
        this(name, null, path, maxAge, httpOnly, secure);
    }

    public CookieSetter(String name, String domain, String path, int maxAge, boolean httpOnly, boolean secure) {
        if (Strings.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Cookie name cannot be null or empty");
        }
        this.name = name;

        if (Strings.isNullOrEmpty(domain) || "none".equals(domain)) {
            domain = null;
        }
        this.domain = domain;

        if (Strings.isNullOrEmpty(path)) {
            throw new IllegalArgumentException("Cookie path cannot be null or empty");
        }
        this.path = path;

        if (maxAge < 0) {
            throw new IllegalArgumentException("Cookie max age cannot be negative");
        }
        this.maxAge = maxAge;
        this.httpOnly = httpOnly;
        this.secure = secure;
    }

    @Override
    public Cookie set(String value) {
        if (Strings.isNullOrEmpty(value)) {
            value = "";
        }

        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setSecure(secure);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(httpOnly);
        return cookie;
    }

    @Override
    public Cookie clean() {
        return set("");
    }
}
