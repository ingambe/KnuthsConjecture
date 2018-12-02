package com.tassel.ingambe.knuthsconjecture;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.tassel.ingambe.knuthsconjecture.Model.GameState;
import com.tassel.ingambe.knuthsconjecture.Presenter.MainPresenter;
import com.tassel.ingambe.knuthsconjecture.Solver.Solver;
import com.tassel.ingambe.knuthsconjecture.View.MainView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainView {

    @BindView(R.id.tv_operation_count)
    TextView tvOperationCount;
    @BindView(R.id.chrono_time)
    Chronometer chronometer;
    @BindView(R.id.tv_current_number)
    TextView tvCurrentNumber;
    @BindView(R.id.tv_goal)
    TextView tvGoal;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.bt_square_root)
    Button btSquareRoot;
    @BindView(R.id.bt_factorial)
    Button btFactorial;
    @BindView(R.id.bt_floor)
    Button btFloor;
    @BindView(R.id.bt_square)
    Button btSquare;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = (MainPresenter) getLastCustomNonConfigurationInstance();
        if (presenter == null) {
            presenter = new MainPresenter();
        }
        presenter.initView(this);
    }

    @OnClick({R.id.bt_square_root, R.id.bt_factorial, R.id.bt_floor, R.id.bt_square, R.id.tv_hint})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_square_root:
                presenter.applyOperator(GameState.Operator.SQUARE_ROOT);
                break;
            case R.id.bt_factorial:
                presenter.applyOperator(GameState.Operator.FACTORIAL);
                break;
            case R.id.bt_floor:
                presenter.applyOperator(GameState.Operator.FLOOR);
                break;
            case R.id.bt_square:
                presenter.applyOperator(GameState.Operator.SQUARE);
                break;
            case R.id.tv_hint:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setMessage(getString(R.string.solution_generation_text))
                        .setCancelable(true);
                HintTask task = new HintTask(presenter, builder);
                task.execute();
                break;
        }
        uncolorButton();
    }

    @Override
    public void setCurrentNumber(double current) {
        tvCurrentNumber.setText(getString(R.string.number_text, current));
    }

    @Override
    public void setGoalNumber(double goal) {
        tvGoal.setText(getString(R.string.goal_text, goal));
    }

    @Override
    public void setOperationsCount(int count) {
        tvGoal.setText(getString(R.string.op_count_text, count));
    }

    @Override
    public void startChronometer() {
        chronometer.start();
    }

    @Override
    public void stopChronometer() {
        chronometer.stop();
    }

    @Override
    public void showSuccess(int steps, long seconds) {
        String text = getString(R.string.success_text, steps, seconds);
        createDialog(text);
    }

    @Override
    public void showFail() {
        String text = getString(R.string.fail_text);
        createDialog(text);
    }

    private void createDialog(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setMessage(text)
                .setNeutralButton(getString(R.string.retry_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.restart(MainActivity.this);
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    @Override
    public long getChronometerSeconds() {
        return (SystemClock.elapsedRealtime() - chronometer.getBase());
    }

    @Override
    public void colorOperation(GameState.Operator operator) {
        switch (operator) {
            case SQUARE_ROOT:
                btSquareRoot.setBackground(getDrawable(R.color.colorPrimary));
                break;
            case FACTORIAL:
                btFactorial.setBackground(getDrawable(R.color.colorPrimary));
                break;
            case SQUARE:
                btSquare.setBackground(getDrawable(R.color.colorPrimary));
                break;
            case FLOOR:
                btFloor.setBackground(getDrawable(R.color.colorPrimary));
                break;
        }
    }

    @Override
    public void uncolorButton() {
        btSquareRoot.setBackgroundResource(0);
        btFactorial.setBackgroundResource(0);
        btSquare.setBackgroundResource(0);
        btFloor.setBackgroundResource(0);
    }

    private static class HintTask extends AsyncTask<Void, Void, GameState.Operator>{

        private MainPresenter presenter;
        private AlertDialog.Builder builder;
        private AlertDialog dialog;

        HintTask(MainPresenter presenter, AlertDialog.Builder builder) {
            this.presenter = presenter;
            this.builder = builder;
        }

        @Override
        protected void onPreExecute(){
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel(true);
                }
            });
            dialog = builder.create();
            dialog.show();
        }

        @Override
        protected GameState.Operator doInBackground (Void...voids){
            return presenter.hint();
        }

        @Override
        protected void onPostExecute(GameState.Operator operator) {
            presenter.colorHint(operator);
            dialog.dismiss();
        }
    }
}
