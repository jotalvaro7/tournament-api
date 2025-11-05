package com.personal.tournament_api.team.infrastructure.adapters.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "teams")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;
    @Column(nullable = false, length = 100)
    private String coach;
    @Column(name = "tournament_id", nullable = false)
    private Long tournamentId;
    @Column(nullable = false)
    private Integer points;
    @Column(name = "matches_played", nullable = false, columnDefinition = "integer default 0")
    private Integer matchesPlayed;
    @Column(name = "matches_win", nullable = false, columnDefinition = "integer default 0")
    private Integer matchesWin;
    @Column(name = "matches_draw", nullable = false, columnDefinition = "integer default 0")
    private Integer matchesDraw;
    @Column(name = "matches_lost", nullable = false, columnDefinition = "integer default 0")
    private Integer matchesLost;
    @Column(name = "goals_for", nullable = false, columnDefinition = "integer default 0")
    private Integer goalsFor;
    @Column(name = "goals_against", nullable = false, columnDefinition = "integer default 0")
    private Integer goalsAgainst;
    @Column(name = "goal_difference", nullable = false, columnDefinition = "integer default 0")
    private Integer goalDifference;

}
