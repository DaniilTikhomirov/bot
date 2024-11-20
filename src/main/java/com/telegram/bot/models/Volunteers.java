package com.telegram.bot.models;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
