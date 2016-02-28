package com.example.brunoalmeida.frc2016scouting.data;

import android.util.Log;

import java.util.EnumMap;

/**
 * The data for a match, focusing on one team's statistics.
 * Includes the team, its allies/opponents, and its statistics.
 *
 * Created by Bruno on 2016-02-25.
 */
public class Match {

    public enum Team {
        ALLY_1,
        ALLY_2,
        ALLY_3,
        OPPONENT_1,
        OPPONENT_2,
        OPPONENT_3
    }

    public enum Shooting {
        LOW_GOAL,
        HIGH_GOAL
    }

    public enum DefenseBreach {
        LOW_BAR,
        CATEGORY_A,
        CATEGORY_B,
        CATEGORY_C,
        CATEGORY_D
    }




    private long id;
    private EnumMap<Team, Integer> teamNumbers = new EnumMap<>(Team.class);
    private EnumMap<Shooting, SuccessRate> shootingRates = new EnumMap<>(Shooting.class);
    private EnumMap<DefenseBreach, SuccessRate> defenseBreachRates = new EnumMap<>(DefenseBreach.class);




    public Match(long id,
                 int ally1TeamNumber,
                 int ally2TeamNumber,
                 int ally3TeamNumber,
                 int opponent1TeamNumber,
                 int opponent2TeamNumber,
                 int opponent3TeamNumber) {

        this.id = id;

        this.teamNumbers.put(Team.ALLY_1, ally1TeamNumber);
        this.teamNumbers.put(Team.ALLY_2, ally2TeamNumber);
        this.teamNumbers.put(Team.ALLY_3, ally3TeamNumber);
        this.teamNumbers.put(Team.OPPONENT_1, opponent1TeamNumber);
        this.teamNumbers.put(Team.OPPONENT_2, opponent2TeamNumber);
        this.teamNumbers.put(Team.OPPONENT_3, opponent3TeamNumber);

        for (Shooting shooting : Shooting.values()) {
            this.shootingRates.put(shooting, new SuccessRate());
        }

        for (DefenseBreach defenseBreach : DefenseBreach.values()) {
            this.defenseBreachRates.put(defenseBreach, new SuccessRate());
        }
    }

    public Match(int ally1TeamNumber,
                 int ally2TeamNumber,
                 int ally3TeamNumber,
                 int opponent1TeamNumber,
                 int opponent2TeamNumber,
                 int opponent3TeamNumber) {

        this(
                -1,
                ally1TeamNumber,
                ally2TeamNumber,
                ally3TeamNumber,
                opponent1TeamNumber,
                opponent2TeamNumber,
                opponent3TeamNumber);
    }


    public long getID() {
        return id;
    }

    public int getTeamNumber(Team team) {
        return teamNumbers.get(team);
    }

    public EnumMap<Shooting, SuccessRate> getShootingRates() {
        return shootingRates;
    }

    public EnumMap<DefenseBreach, SuccessRate> getDefenseBreachRates() {
        return defenseBreachRates;
    }

    public SuccessRate getShootingRate(Shooting shooting) {
        return shootingRates.get(shooting);
    }

    public SuccessRate getDefenseBreachRate(DefenseBreach defenseBreach) {
        return defenseBreachRates.get(defenseBreach);
    }


    public void setShootingRate(Shooting shooting, int successes, int attempts) {
        shootingRates.put(shooting, new SuccessRate(successes, attempts));
    }

    public void setDefenseBreachRate(DefenseBreach defenseBreach, int successes, int attempts) {
        defenseBreachRates.put(defenseBreach, new SuccessRate(successes, attempts));
    }


    @Override
    public String toString() {
        String str = "";

        str += "id = " + id;

        for (Team team : Team.values()) {
            str += "\n" + team.toString() + " teamNumber = " +
                    getTeamNumber(team);
        }

        for (Shooting shooting : Shooting.values()) {
            str += "\n" + shooting.toString() + " shootingRate = " +
                    getShootingRate(shooting);
        }

        for (DefenseBreach defenseBreach : DefenseBreach.values()) {
            str += "\n" + defenseBreach.toString() + " defenseBreachRate = " +
                    getDefenseBreachRate(defenseBreach);
        }

        return str;
    }

}
