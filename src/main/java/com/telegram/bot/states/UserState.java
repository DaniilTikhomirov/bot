package com.telegram.bot.states;

public enum UserState {
    registrationOwner,
    registrationOwnerName,
    registrationOwnerDescription,
    registrationOwnerWait,
    registrationOwnerAccepted,
    registrationOwnerDenied,

    registrationVolunteerName,
    registrationVolunteerDescription,
    registrationVolunteerSearchShelter,
    registrationVolunteerContact,
    registrationVolunteerWait,

    WAIT_ANIMAL_ACCEPTED,
}
