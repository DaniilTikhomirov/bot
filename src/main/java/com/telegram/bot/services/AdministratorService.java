package com.telegram.bot.services;

import com.telegram.bot.models.Administrator;
import com.telegram.bot.repositories.AdministratorRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AdministratorService {

    private final AdministratorRepository administratorRepository;

    public AdministratorService(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    public Administrator addAdministrator(Administrator administrator) {
        return administratorRepository.save(administrator);
    }

    public Collection<Administrator> getAllAdministrators() {
        return administratorRepository.findAll();
    }


}
