package cn.edu.buaa.patpat.boot.extensions.messages;

import cn.edu.buaa.patpat.boot.common.utils.Strings;
import cn.edu.buaa.patpat.boot.extensions.cookies.ICookieSetter;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LocaleInterceptor implements HandlerInterceptor {
    private final ICookieSetter localeCookieSetter;

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) {
        String language = localeCookieSetter.get(request);
        if (Strings.isNullOrEmpty(language)) {
            language = "zh";
            response.addCookie(localeCookieSetter.set(language));
        }
        LocaleContextHolder.setLocale(new Locale(language));
        return true;
    }
}
