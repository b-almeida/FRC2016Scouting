package com.example.brunoalmeida.frc2016scouting;

/**
 * The data for one robot's match.
 * Includes the robot, its allies, and its statistics.
 *
 * Created by Bruno on 2016-02-25.
 */
public class Match {

    private int teamNumber;
    private int ally1TeamNumber;
    private int ally2TeamNumber;
    private int opponent1TeamNumber;
    private int opponent2TeamNumber;
    private int opponent3TeamNumber;

    private Fraction lowShootingSuccess;
    private Fraction highShootingSuccess;

    private Fraction defenseLowBarBreachSuccess;
    private Fraction defenseCategoryABreachSuccess;
    private Fraction defenseCategoryBBreachSuccess;
    private Fraction defenseCategoryCBreachSuccess;
    private Fraction defenseCategoryDBreachSuccess;

}
