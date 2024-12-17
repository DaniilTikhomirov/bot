package com.telegram.bot.controller.api;

import com.telegram.bot.models.Habit;
import com.telegram.bot.services.HabitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Контроллер для управления привычками животных в системе приюта.
 *
 * <p>Этот класс предоставляет REST API для добавления новых привычек и получения списка всех привычек.</p>
 * <p>Все запросы обрабатываются по пути {@code /habits}</p>
 */
@RestController
@RequestMapping("habits")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @Operation(summary = "добавление привычки",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "добавление привычки в базу данных указанный id будет проигнорирован",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = Habit.class
                            )
                    )
            ))
    @PostMapping()
    public ResponseEntity<Habit> addHabit(@RequestBody Habit habit) {
        return ResponseEntity.ok(habitService.addHabit(habit));
    }

    @Operation(summary = "получение всех привычек", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "получение всех привычек",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = Habit.class)
                                    )
                            )
                    }
            )
    })
    @GetMapping("/get")
    public ResponseEntity<Collection<Habit>> getHabits() {
        return ResponseEntity.ok(habitService.getAllHabits());
    }
}
