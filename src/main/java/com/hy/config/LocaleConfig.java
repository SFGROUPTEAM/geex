package com.hy.config;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@ComponentScan
@EnableAutoConfiguration
@Configuration
public class LocaleConfig extends WebMvcAutoConfiguration{
    @Bean(name="localeResolver")
    public LocaleResolver localeResolverBean() {
        SessionLocaleResolver slr= new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.CHINA);
        return slr;
    }
//    @Bean(name="messageSource")
//    public ResourceBundleMessageSource resourceBundleMessageSource(){
//        ResourceBundleMessageSource source=new ResourceBundleMessageSource();
//        source.setBasename("messages");
//        return source;
//    }
}