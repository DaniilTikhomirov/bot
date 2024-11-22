package com.telegram.bot.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.util.Collection;

/**
 * Модель приюта.
 * Поле <b>kind</b> должно содержать значения "cat" или "dog".
 * Другие значения не допускаются (ограничение {@link Check}).
 * Поле <b>animals</b> представляет связь {@link OneToMany} с классом {@link Animal},
 * позволяя связать приют с несколькими животными.
 * Поле <b>volunteers</b> представляет связь {@link OneToMany} с классом {@link Volunteers},
 * позволяя связать приют с несколькими волонтёрами.
 * Поле <b>schedules</b> представляет связь {@link OneToOne} с классом {@link Schedules},
 * описывая расписание работы приюта.
 */

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Check(constraints = "kind IN ('cat', 'dog')")
@Entity(name = "shelters")
public class Shelters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // название приюта
    @Column(nullable = false)
    private String name;

    // описание приюта
    private String description;

    // контакты охраны
    private String securityContact;

    // рекомендации по входу
    private String safetyRecommendation;

    // контакты
    private String contact;

    @OneToMany(mappedBy = "shelters")
    private Collection<Animal> animals;

    @JsonIgnore
    @OneToMany(mappedBy = "shelters")
    private Collection<Volunteers> volunteers;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "schedule_id")
    private Schedules schedules;

    // вид приюты cat или dog
    private String kind;
}
