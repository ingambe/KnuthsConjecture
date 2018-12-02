package com.tassel.ingambe.knuthsconjecture.Presenter;

import com.tassel.ingambe.knuthsconjecture.Model.GameState;
import com.tassel.ingambe.knuthsconjecture.View.MainView;

public class MainPresenter {

    private MainView view;
    private GameState model;

    public MainPresenter(MainView view) {
        this.view = view;
        model = new GameState();
    }

    public void initView(){
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
            view.showSuccess();
        } else if(model.gameLost()){
            view.stopChronometer();
            view.showFail();
        }
    }

}
