package com.tassel.ingambe.knuthsconjecture.Model;

import java.util.Random;

public class GameState {

    private double current;
    private double goal;
    private Random random;

    public GameState() {
        current = 4;
        random = new Random();
        goal = random.nextInt(995) + 5;
    }


    public void operation(Operator operator){
        switch (operator){
            case FLOOR:
                current = Math.floor(current);
                break;
            case SQUARE:
                current = current * current;
                break;
            case FACTORIAL:
                current = factorial(current);
                break;
            default:
                current = Math.sqrt(current);
                break;
        }
    }

    private double factorial(double n) {
        return (n == 1 || n == 0) ? 1 : n * factorial(n - 1);

    }

    public enum Operator {
        SQUARE_ROOT, FACTORIAL, FLOOR, SQUARE
    }

}
