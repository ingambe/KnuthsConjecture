package com.tassel.ingambe.knuthsconjecture.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameState {

    private double current;
    private double goal;
    private final List<Operator> operationsList;
    private final List<Double> valueList;
    // list of easy goal (reachable in approximately 8 steps starting from 4)
    private int[] easyGoals = new int[]{5,6,9,15,16,22,23,24,30,36,46,81,120,225,255,256,484,528,529,575,576,633,720,900,942};

    public GameState() {
        Random random = new Random();
        this.current = 4;
        valueList = new ArrayList<>();
        valueList.add(current);
        //this.goal = random.nextInt(96) + 5;
        this.goal = 5;
        operationsList = new ArrayList<>();
    }

    public void operation(Operator operator){
        operationsList.add(operator);
        switch (operator){
            case FLOOR:
                current = Math.floor(current);
                break;
            case SQUARE:
                current = current * current;
                break;
            case FACTORIAL:
                current = Math.floor(current);
                current = factorial(current);
                break;
            default:
                current = Math.sqrt(current);
                break;
        }
        valueList.add(current);
    }

    public boolean gameEnded(){
        return current == goal;
    }

    // If the current is inferior at one we have lost !
    public boolean gameLost(){
        return current <= 1;
    }

    public static double factorial(double n) {
        return (n <= 1) ? 1 : n * factorial(n - 1);
    }

    public enum Operator {
        SQUARE_ROOT, FACTORIAL, FLOOR, SQUARE
    }

    public double getCurrent() {
        return current;
    }

    public double getGoal() {
        return goal;
    }

    public int getOperationCount(){
        return operationsList.size();
    }

    public void setGoal(double goal){
        this.goal = goal;
    }

    public void undoLastMove(){
        if(valueList.size() > 1) {
            current = valueList.get(valueList.size() - 2);
            operationsList.remove(operationsList.size() - 1);
            valueList.remove(valueList.size() - 1);
        }
    }

}
