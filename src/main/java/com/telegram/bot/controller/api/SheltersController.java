package com.telegram.bot.controller.api;

import com.telegram.bot.models.Animal;
import com.telegram.bot.models.Habit;
import com.telegram.bot.models.Shelters;
import com.telegram.bot.services.SheltersService;
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
 * Контроллер для управления приютами в системе.
 *
 * <p>Этот класс предоставляет REST API для добавления новых приютов и получения списка всех приютов.</p>
 * <p>Все запросы обрабатываются по пути {@code /shelters}.</p>
 */

@RestController
@RequestMapping("shelters")
public class SheltersController {

    private final SheltersService sheltersService;

    public SheltersController(SheltersService sheltersService) {
        this.sheltersService = sheltersService;
    }

    @Operation(summary = "добавление расписания",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "добавление расписания в базу данных указанный id будет проигнорирован",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = Shelters.class
                            )
                    )
            ))
    @PostMapping()
    public ResponseEntity<Shelters> add(@RequestBody Shelters shelters) {
        return ResponseEntity.ok(sheltersService.addShelter(shelters));
    }

    @Operation(summary = "получение всех расписаний", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "получение всех расписаний",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = Shelters.class)
                                    )
                            )
                    }
            )
    })
    @GetMapping("/get")
    public ResponseEntity<Collection<Shelters>> get() {
        return ResponseEntity.ok(sheltersService.getShelters());
    }
}
