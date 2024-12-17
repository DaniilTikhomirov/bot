package com.telegram.bot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "volunteers_telegram_id", unique = true, referencedColumnName = "telegram_id")
    private Volunteers volunteer;

}
