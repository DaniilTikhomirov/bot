package com.telegram.bot.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Check(constraints = "kind IN ('cat', 'dog')")
@Entity(name = "shelters")
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

    @JsonIgnore
    @OneToMany(mappedBy = "shelters")
    private Collection<Animal> animals;

    @JsonIgnore
    @OneToMany(mappedBy = "shelters")
    private Collection<Volunteers> volunteers;

    @OneToOne(mappedBy = "shelters")
    private Schedules schedules;

    private String kind;
}
