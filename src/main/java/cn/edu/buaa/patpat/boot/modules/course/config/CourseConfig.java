package cn.edu.buaa.patpat.boot.modules.course.config;

import cn.edu.buaa.patpat.boot.config.options.CookiesOptions;
import cn.edu.buaa.patpat.boot.extensions.cookies.CookieSetter;
import cn.edu.buaa.patpat.boot.extensions.cookies.ICookieSetter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CourseConfig {
    @Bean("courseCookieSetter")
    public ICookieSetter getCourseCookieSetter(CookiesOptions cookiesOptions) {
        return new CookieSetter(
                CookiesOptions.COURSE_COOKIE,
                cookiesOptions.getDomain(),
                CookiesOptions.PATH,
                Integer.MAX_VALUE,
                false,
                cookiesOptions.isSecure());
    }
}
