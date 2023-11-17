package com.avada.myHouse24.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;


@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Value("${upload.path}")
    private String uploadPath;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/img/**")
                .addResourceLocations("file:///" + uploadPath + "/", "file:///" + uploadPath + "/img/");
        registry
                .addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        registry
                .addResourceHandler("/files/**")
                .addResourceLocations("classpath:/static/files/");
        registry
                .addResourceHandler("/uploads/**")
                .addResourceLocations("file:/var/photos/");
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

}