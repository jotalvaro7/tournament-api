package com.personal.tournament_api.player.domain.model;

import com.personal.tournament_api.player.domain.model.vo.IdentificationNumber;
import com.personal.tournament_api.player.domain.model.vo.PlayerLastName;
import com.personal.tournament_api.player.domain.model.vo.PlayerName;

public class Player {

    private final Long id;
    private PlayerName name;
    private PlayerLastName lastName;
    private IdentificationNumber identificationNumber;
    private final Long teamId;

    private Player(Long id, PlayerName name, PlayerLastName lastName,
                   IdentificationNumber identificationNumber, Long teamId) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.identificationNumber = identificationNumber;
        this.teamId = teamId;
    }

    // --- Factory Methods ---
    public static Player create(String name, String lastName,
                                String identificationNumber, Long teamId) {
        return new Player(
                null,
                new PlayerName(name),
                new PlayerLastName(lastName),
                new IdentificationNumber(identificationNumber),
                teamId
        );
    }

    public static Player reconstitute(Long id, String name, String lastName,
                                       String identificationNumber, Long teamId) {
        return new Player(
                id,
                new PlayerName(name),
                new PlayerLastName(lastName),
                new IdentificationNumber(identificationNumber),
                teamId
        );
    }

    // --- Domain Behavior ---
    public void updateDetails(String name, String lastName, String identificationNumber) {
        this.name = new PlayerName(name);
        this.lastName = new PlayerLastName(lastName);
        this.identificationNumber = new IdentificationNumber(identificationNumber);
    }

    // --- Getters ---
    public Long getId() {
        return id;
    }

    public String getName() {
        return name.value();
    }

    public String getLastName() {
        return lastName.value();
    }

    public String getIdentificationNumber() {
        return identificationNumber.value();
    }

    public Long getTeamId() {
        return teamId;
    }
}
