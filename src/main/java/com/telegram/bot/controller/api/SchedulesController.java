package com.telegram.bot.controller.api;

import com.telegram.bot.models.Animal;
import com.telegram.bot.models.Habit;
import com.telegram.bot.models.Schedules;
import com.telegram.bot.services.SchedulesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Контроллер для управления расписаниями приютов.
 *
 * <p>Этот класс предоставляет REST API для добавления новых расписаний и получения списка всех расписаний.</p>
 * <p>Все запросы обрабатываются по пути {@code /schedules}.</p>
 */

@RestController
@RequestMapping("schedules")
public class SchedulesController {

    private final SchedulesService schedulesService;

    public SchedulesController(SchedulesService schedulesService) {
        this.schedulesService = schedulesService;
    }

    @Operation(summary = "добавление приюта",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "добавление приюта в базу данных указанный id будет проигнорирован",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = Schedules.class
                            )
                    )
            ))
    @PostMapping()
    public ResponseEntity<Schedules> addSchedules(@RequestBody Schedules schedules) {
        return ResponseEntity.ok(schedulesService.addSchedule(schedules));
    }

    @Operation(summary = "получение всех приютов", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "получение всех приютов",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = Schedules.class)
                                    )
                            )
                    }
            )
    })
    @GetMapping("/get")
    public ResponseEntity<Collection<Schedules>> get() {
        return ResponseEntity.ok(schedulesService.getAllSchedules());
    }
}
