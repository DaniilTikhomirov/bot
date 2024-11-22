package com.telegram.bot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Модель расписания для приютов.
 * Поля <b>monday</b>, <b>tuesday</b>, <b>wednesday</b>, <b>thursday</b>, <b>friday</b>, <b>saturday</b>, <b>sunday</b>
 * содержат расписание работы приюта по дням недели.
 * Класс связан с {@link Shelters} через связь {@link OneToOne},
 * где каждое расписание соответствует одному приюту.
 */

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Entity(name = "schedules")
public class Schedules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(mappedBy = "schedules")
    @JsonIgnore
    private Shelters shelters;

    private String monday;

    private String tuesday;

    private String wednesday;

    private String thursday;

    private String friday;

    private String saturday;

    private String sunday;
}
