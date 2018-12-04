package com.tassel.ingambe.knuthsconjecture.View;

import com.tassel.ingambe.knuthsconjecture.Model.GameState;

public interface MainView {

    void setCurrentNumber(double current);
    void setGoalNumber(double goal);
    void setOperationsCount(int count);
    void startChronometer();
    void stopChronometer();
    void restartChronometer();
    void showSuccess(int steps, double seconds);
    void showFail();
    double getChronometerSeconds();
    void colorOperation(GameState.Operator operator);
    void uncolorButton();
    void updateButtonText(double current);
    void showBigNumber();
    void alertDialogHelp();
    boolean testFirstLaunch();
    void setFirstLaunch();

}
