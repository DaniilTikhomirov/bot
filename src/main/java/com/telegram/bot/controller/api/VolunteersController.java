package com.telegram.bot.controller.api;

import com.telegram.bot.models.Animal;
import com.telegram.bot.models.Habit;
import com.telegram.bot.models.Volunteers;
import com.telegram.bot.services.VolunteersService;
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
 * Контроллер для управления волонтерами в системе приюта.
 *
 * <p>Этот класс предоставляет REST API для добавления новых волонтеров и получения списка всех волонтеров.</p>
 * <p>Все запросы обрабатываются по пути {@code /volunteers}.</p>
 */
@RestController
@RequestMapping("volunteers")
public class VolunteersController {

    private final VolunteersService volunteersService;

    public VolunteersController(VolunteersService volunteersService) {
        this.volunteersService = volunteersService;
    }

    @Operation(summary = "добавление волонтера",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "добавление волонтера в базу данных указанный id будет проигнорирован",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = Volunteers.class
                            )
                    )
            ))
    @PostMapping()
    public ResponseEntity<Volunteers> add(@RequestBody Volunteers volunteers) {
        return ResponseEntity.ok(volunteersService.addVolunteers(volunteers));
    }

    @Operation(summary = "получение всех волонтеров", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "получение всех волонтеров",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = Volunteers.class)
                                    )
                            )
                    }
            )
    })
    @GetMapping("/get")
    public ResponseEntity<Collection<Volunteers>> get() {
        return ResponseEntity.ok(volunteersService.getAllVolunteers());
    }
}
