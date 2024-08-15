package cn.edu.buaa.patpat.boot.options;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cookies")
@Data
public class CookiesOptions {
    public static final String JWT_COOKIE = "jwt";
    public static final String JWT_REFRESH_COOKIE = "refresh";

    public static final String COURSE_COOKIE = "course";

    public static final String LANGUAGE_COOKIE = "language";

    public static final String PATH = "/";

    private String domain;
    private boolean secure;
    private boolean httpOnly;
}
