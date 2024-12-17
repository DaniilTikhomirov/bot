package com.telegram.bot.controller.api;

import com.telegram.bot.models.OwnerShelters;
import com.telegram.bot.models.Shelters;
import com.telegram.bot.services.OwnerSheltersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("owners_shelters")
public class OwnersSheltersController {

    private final OwnerSheltersService ownerSheltersService;

    public OwnersSheltersController(OwnerSheltersService ownerSheltersService) {
        this.ownerSheltersService = ownerSheltersService;
    }

    @PostMapping()
    public ResponseEntity<OwnerShelters> addOwner(@RequestBody OwnerShelters ownerShelters) {
        return ResponseEntity.ok(ownerSheltersService.addOwnerShelter(ownerShelters));
    }

    @GetMapping()
    public ResponseEntity<Collection<OwnerShelters>> getOwners() {
        return ResponseEntity.ok(ownerSheltersService.getAllOwnerShelters());
    }

    @GetMapping("page/{page}/size/{size}/id/{id}")
    public ResponseEntity<List<Shelters>> getShelters(@PathVariable int page,
                                                      @PathVariable int size,
                                                      @PathVariable int id) {
        return ResponseEntity.ok(ownerSheltersService.getPageShelters(page, size, id));
    }
}
