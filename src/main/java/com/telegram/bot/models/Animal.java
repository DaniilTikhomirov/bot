package com.telegram.bot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.util.Collection;


/**
 * модель животоного для приюта
 * поле <b>kind</b> можно заполнить значениями ("dog", "cat")
 * с другими значениями класс не работает
 * класс связан с {@link Shelters} с помошью связи {@link ManyToOne} и с классом
 * {@link Habit} с помошью  связи {@link OneToMany}
 */
@Check(constraints = "kind in ('cat', 'dog')")
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Entity(name = "animals")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // цвет животного
    private String color;

    // описание животного
    private String description;


    // вид животного только cat или dog
    @Column(nullable = false)
    private String kind;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "shelters_id")
    private Shelters shelters;

    @OneToMany(mappedBy = "animal")
    @JsonIgnore
    private Collection<Habit> habits;
}
