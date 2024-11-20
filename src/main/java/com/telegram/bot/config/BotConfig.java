package com.telegram.bot.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Configuration
@Data
public class BotConfig {


    @Value("${telegram.bot.name}")
    private String BotName;

    @Value("${telegram.bot.token}")
    private String BotToken;


}
