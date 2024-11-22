package com.telegram.bot.config;

import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * кофигурация пути бота
 */
public class BotApiConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/bot", c -> true); // Все запросы для бота будут начинаться с /bot
    }
}
