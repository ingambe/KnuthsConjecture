package com.tassel.ingambe.knuthsconjecture.Solver;

import com.tassel.ingambe.knuthsconjecture.Model.GameState;

import java.util.ArrayList;
import java.util.List;

public class Solver {

    public List<GameState.Operator> solve;

    public Solver() {
        solve = new ArrayList<>();
    }

    private class Node {

        private double currentValue;
        private List<GameState.Operator> oldOperations;

        public Node(double currentValue, List<GameState.Operator> oldOperations) {
            this.currentValue = currentValue;
            this.oldOperations = oldOperations;
        }

        public Node(double currentValue, GameState.Operator operation) {
            this.currentValue = currentValue;
            this.oldOperations = new ArrayList<>();
            this.oldOperations.add(operation);
        }

    }

}
