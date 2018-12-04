package com.tassel.ingambe.knuthsconjecture.Presenter;

import com.tassel.ingambe.knuthsconjecture.MainActivity;
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
        view.setOperationsCount(model.getOperationCount());
        view.setGoalNumber(model.getGoal());
        view.updateButtonText(model.getCurrent());
        view.startChronometer();
        if(view.testFirstLaunch()){
            view.alertDialogHelp();
            view.setFirstLaunch();
        }
    }

    public void applyOperator(GameState.Operator operator){
        if((operator == GameState.Operator.FACTORIAL && model.getCurrent() > 30)
                || (operator == GameState.Operator.SQUARE && model.getCurrent() > 1E50)){
            view.showBigNumber();
        } else {
            model.operation(operator);
            view.setOperationsCount(model.getOperationCount());
            view.setCurrentNumber(model.getCurrent());
            if (model.gameEnded()) {
                view.stopChronometer();
                view.showSuccess(model.getOperationCount(), view.getChronometerSeconds());
            } else if (model.gameLost()) {
                view.stopChronometer();
                view.showFail();
            }
            view.updateButtonText(model.getCurrent());
        }
    }

    public void restart(MainView view){
        model = new GameState();
        view.stopChronometer();
        view.restartChronometer();
        view.uncolorButton();
        initView(view);
    }

    public GameState.Operator hint(MainActivity.HintTask task) {
        List<GameState.Operator> solution = Solver.BFS(model, task);
        return solution.get(0);
    }

    public void colorHint(GameState.Operator operator){
        view.colorOperation(operator);
    }

    public void undoMove(){
        model.undoLastMove();
        view.uncolorButton();
        view.updateButtonText(model.getCurrent());
        view.setCurrentNumber(model.getCurrent());
        view.setOperationsCount(model.getOperationCount());
    }

}
