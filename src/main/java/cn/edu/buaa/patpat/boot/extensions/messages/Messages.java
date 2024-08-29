/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.extensions.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class Messages {
    private static MessageSource messageSource;

    public static String M(String key) {
        return get(key);
    }

    public static String M(String key, Object... args) {
        return get(key, args);
    }

    public static String get(String key) {
        return get(LocaleContextHolder.getLocale(), key);
    }

    public static String get(String key, Object... args) {
        return get(LocaleContextHolder.getLocale(), key, args);
    }

    public static String get(Locale locale, String key) {
        return messageSource.getMessage(key, null, locale);
    }

    public static String get(Locale locale, String key, Object... args) {
        return messageSource.getMessage(key, args, locale);
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        Messages.messageSource = messageSource;
    }
}
