package com.telegram.bot.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "telegram_users")
public class TelegramUser {

    @GeneratedValue
    @Id
    private long id;

    @Column(nullable = false)
    private long telegramId;

    @Column(columnDefinition = "int default 0")
    private int warningCounter;

}
