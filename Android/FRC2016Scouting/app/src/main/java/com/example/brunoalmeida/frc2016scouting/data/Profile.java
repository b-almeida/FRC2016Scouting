package com.example.brunoalmeida.frc2016scouting.data;

/**
 * A profile of a single team and robot, with information on both the team and robot.
 *
 * Created by Bruno on 2016-02-21.
 */
public class Profile {

    private int teamNumber;
    private String robotFunction;


    public Profile(int teamNumber, String robotFunction) {
        this.teamNumber = teamNumber;
        this.robotFunction = robotFunction;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public String getRobotFunction() {
        return robotFunction;
    }

}
