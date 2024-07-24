package cn.edu.buaa.patpat.boot.config;

import cn.edu.buaa.patpat.boot.options.CorsOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final CorsOptions corsOptions;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods(corsOptions.getAllowedMethods())
                .allowedHeaders(corsOptions.getAllowedHeaders())
                .allowedOrigins(corsOptions.getAllowedOrigins())
                .allowCredentials(corsOptions.isAllowCredentials())
                .maxAge(3600);
    }
}
