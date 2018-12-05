package com.tassel.ingambe.knuthsconjecture.Solver;

import com.tassel.ingambe.knuthsconjecture.Model.GameState;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class SolverTest {

    private GameState gameState;

    @Before
    public void setUp() throws Exception {
        gameState = new GameState();
    }

    @Test
    public void testBFSExampleTo5() {
        gameState.setGoal(5);
        List<GameState.Operator> liste = Solver.BFS(gameState);
        assertEquals(liste.get(0), GameState.Operator.FACTORIAL);
        assertEquals(liste.get(1), GameState.Operator.FACTORIAL);
        assertEquals(liste.get(2), GameState.Operator.SQUARE_ROOT);
        assertEquals(liste.get(3), GameState.Operator.SQUARE_ROOT);
        assertEquals(liste.get(4), GameState.Operator.SQUARE_ROOT);
        assertEquals(liste.get(5), GameState.Operator.SQUARE_ROOT);
        assertEquals(liste.get(6), GameState.Operator.SQUARE_ROOT);
        assertEquals(liste.get(7), GameState.Operator.FLOOR);
        for(GameState.Operator operator : liste){
            gameState.operation(operator);
        }
        // When we apply the solution we get the goal
        assertEquals(gameState.getCurrent(), gameState.getGoal(),0.01);
    }

    @Test
    public void testSolver() {
        List<GameState.Operator> liste = Solver.BFS(gameState);
        for(GameState.Operator operator : liste){
            gameState.operation(operator);
        }
        assertEquals(gameState.getCurrent(), gameState.getGoal(),0.01);
    }


}