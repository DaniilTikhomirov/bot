package com.telegram.bot.controller.api;

import com.telegram.bot.models.Animal;
import com.telegram.bot.services.AnimalService;
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
 * Контроллер для управления животными в системе приюта.
 *
 * <p>Этот класс предоставляет REST API для добавления животных в базу данных и получения списка всех животных.</p>
 * <p>Все запросы обрабатываются по пути {@code /animals}</p>
 **/
@RestController
@RequestMapping("animals")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @Operation(summary = "добавление животного",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "добавление животного в базу данных указанный id будет проигнорирован",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = Animal.class
                    )
            )
    ))
    @PostMapping()
    public ResponseEntity<Animal> addAnimal(@RequestBody Animal animal) {
        return ResponseEntity.ok(animalService.addAnimal(animal));
    }

    @Operation(summary = "получение всех животных", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "получение всех животных",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = Animal.class)
                                    )
                            )
                    }
            )
    })
    @GetMapping("/get")
    public ResponseEntity<Collection<Animal>> getAllAnimals() {
        return ResponseEntity.ok(animalService.getAllAnimals());
    }


}
