/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.aspect;

import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.util.Arrays;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Component
@Aspect
@Order(102)
@RequiredArgsConstructor
@Slf4j
public class ValidateMultipartFileAspect {
    @Before("@annotation(cn.edu.buaa.patpat.boot.aspect.ValidateMultipartFile)")
    public void intercept(final JoinPoint point) {
        Object[] args = point.getArgs();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        ValidateMultipartFile rule = method.getAnnotation(ValidateMultipartFile.class);
        for (Object arg : args) {
            if (arg instanceof MultipartFile file) {
                validateMultipartFile(file, rule);
                break;
            }
        }
    }

    private void validateMultipartFile(MultipartFile file, ValidateMultipartFile rule) {
        if (file == null) {
            if (!rule.allowNull()) {
                throw new BadRequestException(M("validation.file.null"));
            }
            return;
        }

        if (file.isEmpty()) {
            if (!rule.allowEmpty()) {
                throw new BadRequestException(M("validation.file.empty"));
            }
            return;
        }

        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new BadRequestException(M("validation.file.name"));
        }

        if (rule.extensions().length > 0) {
            String extension = FilenameUtils.getExtension(filename);
            if (Arrays.stream(rule.extensions()).noneMatch(extension::equalsIgnoreCase)) {
                throw new BadRequestException(M("validation.file.type"));
            }
        }

        if (rule.maxSize() > 0 && file.getSize() > rule.maxSize() * 1024 * 1024) {
            throw new BadRequestException(M("validation.file.size"));
        }
    }
}
