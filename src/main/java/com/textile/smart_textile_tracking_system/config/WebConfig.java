package com.textile.smart_textile_tracking_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

/**
 * Internationalization (i18n) configuration.
 *
 * Provides:
 *  - CookieLocaleResolver : resolves the current locale from a cookie,
 *                           defaulting to English, so the selected
 *                           language persists across all pages/sessions.
 *  - LocaleChangeInterceptor : allows switching language via the
 *                           "?lang=ta" query parameter emitted by the
 *                           navbar language selector.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        resolver.setCookieName("LOCALE");
        resolver.setCookieMaxAge(365 * 24 * 60 * 60); // 1 year
        resolver.setCookiePath("/");
        return resolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
