package cn.edu.buaa.patpat.boot.extensions.messages;

import cn.edu.buaa.patpat.boot.config.options.CookiesOptions;
import cn.edu.buaa.patpat.boot.extensions.cookies.CookieSetter;
import cn.edu.buaa.patpat.boot.extensions.cookies.ICookieSetter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageConfig implements WebMvcConfigurer {
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return localeResolver;
    }

    @Bean("localeCookieSetter")
    public ICookieSetter getLocaleCookieSetter(CookiesOptions cookiesOptions) {
        return new CookieSetter(
                CookiesOptions.LANGUAGE_COOKIE,
                cookiesOptions.getDomain(),
                CookiesOptions.PATH,
                Integer.MAX_VALUE,
                false,
                cookiesOptions.isSecure());
    }
}
