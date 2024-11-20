package com.telegram.bot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.util.Collection;

@Check(constraints = "kind in ('cat', 'dog')")
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Entity(name = "animals")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String color;

    private String description;

    @Column(nullable = false)
    private String kind;

    @ManyToOne
    @JoinColumn(name = "shelters_id", nullable = false)
    private Shelters shelters;

    @OneToMany(mappedBy = "animal")
    @JsonIgnore
    private Collection<Habit> habits;
}
