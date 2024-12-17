package com.telegram.bot.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Entity(name = "owners_shelters")
@ToString
public class OwnerShelters {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    @Column(name = "telegram_id")
    private long telegramId;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "owners_many_to_many_shelters",
            joinColumns = {@JoinColumn(name = "owners_shelters_id")},
            inverseJoinColumns = {@JoinColumn(name = "shelters_id")})
    @ToString.Exclude
    private Collection<Shelters> shelters;

    public long countAnimals(){
        long count = 0;
        for (Shelters shelter : shelters) {
            count += shelter.getAnimals().size();
        }

        return count;
    }
}
