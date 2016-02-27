package com.example.brunoalmeida.frc2016scouting.data;

/**
 * A profile of a single team and robot, with information on both the team and robot.
 *
 * Created by Bruno on 2016-02-21.
 */
public class Profile {

    private long id;

    private int teamNumber;
    private String robotFunction;


    public Profile(long id, int teamNumber, String robotFunction) {
        this.id = id;
        this.teamNumber = teamNumber;
        this.robotFunction = robotFunction;
    }

    public Profile(int teamNumber, String robotFunction) {
        this(-1, teamNumber, robotFunction);
    }

    @Override
    public String toString() {
        String str = "";

        str += "id = " + id;

        str += "\n" + "teamNumber = " + teamNumber;
        str += "\n" + "robotFunction = " + robotFunction;

        return str;
    }

    public long getId() {
        return id;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public String getRobotFunction() {
        return robotFunction;
    }

}
