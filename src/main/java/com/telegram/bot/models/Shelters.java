package com.telegram.bot.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
@Entity(name = "shelters")
@ToString
public class Shelters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    private String description;
    private String securityContact;
    private String safetyRecommendation;
    private String contact;

    @OneToMany(mappedBy = "shelters", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<Animal> animals;

    @JsonIgnore
    @OneToMany(mappedBy = "shelters", fetch = FetchType.EAGER)
    private Collection<Volunteers> volunteers;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_id")
    private Schedules schedules;

    @ManyToMany(mappedBy = "shelters", fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
    private Collection<OwnerShelters> ownerShelters;

    private String kind;
}
