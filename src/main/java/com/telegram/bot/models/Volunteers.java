package com.telegram.bot.models;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Модель волонтёра для приютов.
 * Поле <b>shelters</b> представляет связь {@link ManyToOne} с классом {@link Shelters},
 * указывая, в каком приюте работает волонтёр.
 * Поле <b>name</b> обязательно для заполнения и хранит имя волонтёра.
 * Поле <b>contact</b> обязательно для заполнения и содержит контактную информацию волонтёра.
 * Поле <b>description</b> может использоваться для хранения дополнительной информации о волонтёре.
 */

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Entity(name = "volunteers")
public class Volunteers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "shelters_id")
    private Shelters shelters;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private String contact;
}
