package com.telegram.bot.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Модель привычек животного.
 * Поле <b>title</b> представляет название привычки, обязательное для заполнения.
 * Поле <b>description</b> содержит описание привычки, может быть пустым.
 * Класс связан с {@link Animal} с помощью связи {@link ManyToOne},
 * где каждое животное может иметь несколько записей о привычках.
 */

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Entity(name = "habits")
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // название привычки
    @Column(nullable = false)
    private String title;

    private String description;

    @ManyToOne()
    @JoinColumn(name = "animal_id")
    private Animal animal;

}
