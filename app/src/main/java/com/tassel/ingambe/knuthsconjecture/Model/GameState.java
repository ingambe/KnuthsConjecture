package com.tassel.ingambe.knuthsconjecture.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameState {

    private double current;
    private double goal;
    private List<Operator> operationsList;

    public GameState() {
        Random random = new Random();
        this.current = 4;
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

}
