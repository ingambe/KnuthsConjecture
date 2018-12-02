package com.tassel.ingambe.knuthsconjecture.View;

import com.tassel.ingambe.knuthsconjecture.Model.GameState;

public interface MainView {

    void setCurrentNumber(double current);
    void setGoalNumber(double goal);
    void setOperationsCount(int count);
    void startChronometer();
    void stopChronometer();
    void showSuccess(int steps, long seconds);
    void showFail();
    long getChronometerSeconds();
    void colorOperation(GameState.Operator operator);
    void uncolorButton();

}
