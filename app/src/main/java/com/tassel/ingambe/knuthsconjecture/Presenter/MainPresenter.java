package com.tassel.ingambe.knuthsconjecture.Presenter;

import com.tassel.ingambe.knuthsconjecture.Model.GameState;
import com.tassel.ingambe.knuthsconjecture.Solver.Solver;
import com.tassel.ingambe.knuthsconjecture.View.MainView;

import java.util.List;

public class MainPresenter {

    private MainView view;
    private GameState model;

    public MainPresenter() {
        model = new GameState();
    }

    public void initView(MainView view){
        this.view = view;
        view.setCurrentNumber(model.getCurrent());
        view.setGoalNumber(model.getGoal());
        view.startChronometer();
    }

    public void applyOperator(GameState.Operator operator){
        model.operation(operator);
        view.setOperationsCount(model.getOperationCount());
        view.setCurrentNumber(model.getCurrent());
        if(model.gameEnded()){
            view.stopChronometer();
            view.showSuccess(model.getOperationCount(), view.getChronometerSeconds());
        } else if(model.gameLost()){
            view.stopChronometer();
            view.showFail();
        }
    }

    public void restart(MainView view){
        model = new GameState();
        view.stopChronometer();
        view.restartChronometer();
        initView(view);
    }

    public GameState.Operator hint() {
        List<GameState.Operator> solution = Solver.BFS(model);
        return solution.get(0);
    }

    public void colorHint(GameState.Operator operator){
        view.colorOperation(operator);
    }

}
