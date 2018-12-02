package com.tassel.ingambe.knuthsconjecture.View;

public interface MainView {

    void setCurrentNumber(double current);
    void setGoalNumber(double goal);
    void setOperationsCount(int count);
    void startChronometer();
    void stopChronometer();
    void showSuccess();
    void showFail();

}
