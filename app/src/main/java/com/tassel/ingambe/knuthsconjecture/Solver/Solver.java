package com.tassel.ingambe.knuthsconjecture.Solver;

import com.tassel.ingambe.knuthsconjecture.MainActivity;
import com.tassel.ingambe.knuthsconjecture.Model.GameState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Solver {

    public static List<GameState.Operator> BFS(GameState gameState){
        Queue<Node> queue = new LinkedList<>();
        queue.add(new Node(gameState.getCurrent(), new ArrayList<>()));
        while (!queue.isEmpty()){
            Node current = queue.remove();

            List<GameState.Operator> square_root_list = new ArrayList<>(current.oldOperations);
            square_root_list.add(GameState.Operator.SQUARE_ROOT);
            Node square_root = new Node(Math.sqrt(current.currentValue), square_root_list);

            List<GameState.Operator> square_list = new ArrayList<>(current.oldOperations);
            square_list.add(GameState.Operator.SQUARE);
            Node square = new Node(current.currentValue * current.currentValue, square_list);

            List<GameState.Operator> floor_list = new ArrayList<>(current.oldOperations);
            floor_list.add(GameState.Operator.FLOOR);
            Node floor = new Node(Math.floor(current.currentValue), floor_list);

            if(square_root.currentValue != gameState.getGoal()) {
                queue.add(square_root);
            } else {
                return square_root_list;
            }

            if(square.currentValue != gameState.getGoal()) {
                queue.add(square);
            } else {
                return square_list;
            }

            if(floor.currentValue != gameState.getGoal()) {
                queue.add(floor);
            } else {
                return floor_list;
            }

            if(current.currentValue < 30) {
                List<GameState.Operator> fact_list = new ArrayList<>(current.oldOperations);
                fact_list.add(GameState.Operator.FACTORIAL);
                Node fact = new Node(GameState.factorial(Math.floor(current.currentValue)), fact_list);
                queue.add(fact);
                if(fact.currentValue != gameState.getGoal()) {
                    queue.add(fact);
                } else {
                    return fact_list;
                }
            }
        }
        return null;
    }

    /**
     * Same as the normal BFS, but we had a reference to the AsyncTask to stop the
     * evaluation if the task is canceled
     * @param gameState the game to solve
     * @param task the task
     * @return list of operation to execute to get a solution
     */
    public static List<GameState.Operator> BFS(GameState gameState, MainActivity.HintTask task){
        Queue<Node> queue = new LinkedList<>();
        queue.add(new Node(gameState.getCurrent(), new ArrayList<>()));
        while (!queue.isEmpty() && !task.isCancelled()){
            Node current = queue.remove();

            List<GameState.Operator> square_root_list = new ArrayList<>(current.oldOperations);
            square_root_list.add(GameState.Operator.SQUARE_ROOT);
            Node square_root = new Node(Math.sqrt(current.currentValue), square_root_list);

            List<GameState.Operator> square_list = new ArrayList<>(current.oldOperations);
            square_list.add(GameState.Operator.SQUARE);
            Node square = new Node(current.currentValue * current.currentValue, square_list);

            List<GameState.Operator> floor_list = new ArrayList<>(current.oldOperations);
            floor_list.add(GameState.Operator.FLOOR);
            Node floor = new Node(Math.floor(current.currentValue), floor_list);

            if(square_root.currentValue != gameState.getGoal()) {
                queue.add(square_root);
            } else {
                return square_root_list;
            }

            if(square.currentValue != gameState.getGoal()) {
                queue.add(square);
            } else {
                return square_list;
            }

            if(floor.currentValue != gameState.getGoal()) {
                queue.add(floor);
            } else {
                return floor_list;
            }

            if(current.currentValue < 30) {
                List<GameState.Operator> fact_list = new ArrayList<>(current.oldOperations);
                fact_list.add(GameState.Operator.FACTORIAL);
                Node fact = new Node(GameState.factorial(Math.floor(current.currentValue)), fact_list);
                queue.add(fact);
                if(fact.currentValue != gameState.getGoal()) {
                    queue.add(fact);
                } else {
                    return fact_list;
                }
            }
        }
        return null;
    }

    private static class Node {

        final double currentValue;
        final List<GameState.Operator> oldOperations;

        private Node(double currentValue, List<GameState.Operator> oldOperations) {
            this.currentValue = currentValue;
            this.oldOperations = oldOperations;
        }

    }

}
