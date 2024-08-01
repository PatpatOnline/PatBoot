package cn.edu.buaa.patpat.boot.config;

import cn.edu.buaa.patpat.boot.options.CorsOptions;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final CorsOptions corsOptions;

    @Value("${springdoc.swagger-ui.enabled}")
    private boolean swaggerEnabled;

    @Override
    public void addResourceHandlers(@Nonnull ResourceHandlerRegistry registry) {
        if (swaggerEnabled) {
            registry.addResourceHandler("/swagger-ui/swagger-ui.css")
                    .addResourceLocations("classpath:/static/")
                    .resourceChain(false);
        }
    }

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
