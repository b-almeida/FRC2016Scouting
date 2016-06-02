package com.example.brunoalmeida.frc2016scouting.data;

/**
 * A profile of a single team and robot, with information on both the team and robot.
 * <p/>
 * Created by Bruno on 2016-02-21.
 */
public class Profile {

    private long id;
    private int teamNumber;
    private String description;
    private String robotFunction;
    private String notes;


    public Profile(long id,
                   int teamNumber,
                   String description,
                   String robotFunction,
                   String notes) {

        this.id = id;
        this.teamNumber = teamNumber;
        this.description = description;
        this.robotFunction = robotFunction;
        this.notes = notes;
    }

    public Profile(int teamNumber,
                   String description,
                   String robotFunction,
                   String notes) {
        this(
                -1,
                teamNumber,
                description,
                robotFunction,
                notes);
    }


    @Override
    public String toString() {
        String str = "";

        str += "id = " + id;
        str += ", teamNumber = " + teamNumber;
        str += ", description = " + description;
        str += ", robotFunction = " + robotFunction;
        str += ", notes = " + notes;

        return str;
    }


    public long getID() {
        return id;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public String getDescription() {
        return description;
    }

    public String getRobotFunction() {
        return robotFunction;
    }

    public String getNotes() {
        return notes;
    }

}
