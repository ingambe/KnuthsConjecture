package com.tassel.ingambe.knuthsconjecture;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.tassel.ingambe.knuthsconjecture.Model.GameState;
import com.tassel.ingambe.knuthsconjecture.Presenter.MainPresenter;
import com.tassel.ingambe.knuthsconjecture.View.MainView;

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
    @BindView(R.id.tv_undo)
    TextView tvUndo;
    @BindView(R.id.tv_retry)
    TextView tvRetry;

    private static final String PREFRENCE_NAME = "KNUTH_PREFERENCES";
    private static final String PREFERENCE_FIRST_LAUNCH_KEY = "FIRST_LAUNCH_KEY";

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

    @OnClick({R.id.bt_square_root, R.id.bt_factorial, R.id.bt_floor, R.id.bt_square, R.id.tv_hint, R.id.tv_retry, R.id.tv_undo})
    public void onClick(View view) {
        uncolorButton();
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
            case R.id.tv_undo:
                presenter.undoMove();
                break;
            case R.id.tv_retry:
                presenter.restart(MainActivity.this);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help_menu:
                alertDialogHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setCurrentNumber(double current) {
        current = Math.floor(current * 1e2) / 1e2;
        if (current < 10000) {
            tvCurrentNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.current_number_size));
        } else if (current > 1E15) {
            tvCurrentNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.current_really_big_number_size));
        } else {
            tvCurrentNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.current_big_number_size));
        }
        tvCurrentNumber.setText(getString(R.string.number_text, current));
    }

    @Override
    public void setGoalNumber(double goal) {
        tvGoal.setText(getString(R.string.goal_text, goal));
    }

    @Override
    public void setOperationsCount(int count) {
        tvOperationCount.setText(getString(R.string.op_count_text, count));
    }

    @Override
    public boolean testFirstLaunch(){
        SharedPreferences preferences = getSharedPreferences(PREFRENCE_NAME, MODE_PRIVATE);
        return preferences.getBoolean(PREFERENCE_FIRST_LAUNCH_KEY, true);
    }

    @Override
    public void setFirstLaunch(){
        SharedPreferences preferences = getSharedPreferences(PREFRENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREFERENCE_FIRST_LAUNCH_KEY, false).apply();
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
    public void restartChronometer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
    }

    @Override
    public void showSuccess(int steps, double seconds) {
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
    public double getChronometerSeconds() {
        return (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000.0;
    }

    @Override
    public void colorOperation(GameState.Operator operator) {
        switch (operator) {
            case SQUARE_ROOT:
                btSquareRoot.setBackground(getDrawable(R.color.colorAccent));
                break;
            case FACTORIAL:
                btFactorial.setBackground(getDrawable(R.color.colorAccent));
                break;
            case SQUARE:
                btSquare.setBackground(getDrawable(R.color.colorAccent));
                break;
            case FLOOR:
                btFloor.setBackground(getDrawable(R.color.colorAccent));
                break;
        }
    }

    @Override
    public void uncolorButton() {
        btSquareRoot.setBackground(getDrawable(R.color.colorPrimary));
        btFactorial.setBackground(getDrawable(R.color.colorPrimary));
        btSquare.setBackground(getDrawable(R.color.colorPrimary));
        btFloor.setBackground(getDrawable(R.color.colorPrimary));
    }

    @Override
    public void updateButtonText(double current) {
        if (current < 15) {
            btFactorial.setText(getString(R.string.factorial_text, GameState.factorial(Math.floor(current))));
        } else {
            btFactorial.setText(getString(R.string.big_factorial_text));
        }
        if (current < 20000) {
            btSquare.setText(getString(R.string.square_text, current * current));
        } else {
            btSquare.setText(getString(R.string.big_square_text));
        }
        if (current < 1E15) {
            btSquareRoot.setText(getString(R.string.square_root_text, Math.sqrt(current)));
            btFloor.setText(getString(R.string.floor_text, Math.floor(current)));
        } else {
            btSquareRoot.setText(getString(R.string.big_square_root_text));
            btFloor.setText(getString(R.string.big_floor_text));
        }
    }

    @Override
    public void showBigNumber() {
        Toast.makeText(this, getString(R.string.too_big_dialog), Toast.LENGTH_LONG).show();
    }

    @Override
    public void alertDialogHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setMessage(getString(R.string.game_explanation))
                .setNeutralButton(R.string.explanation_understood, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(true);
        builder.create().show();
    }

    public static class HintTask extends AsyncTask<Void, Void, GameState.Operator> {

        private final MainPresenter presenter;
        private final AlertDialog.Builder builder;
        private AlertDialog dialog;

        HintTask(MainPresenter presenter, AlertDialog.Builder builder) {
            this.presenter = presenter;
            this.builder = builder;
        }

        @Override
        protected void onPreExecute() {
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel(true);
                    dialog.dismiss();
                }
            });
            builder.setCancelable(true);
            builder.setNegativeButton(R.string.stop_hint_task_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cancel(true);
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.show();
        }

        @Override
        protected GameState.Operator doInBackground(Void... voids) {
            return presenter.hint(this);
        }

        @Override
        protected void onPostExecute(GameState.Operator operator) {
            if(operator != null) {
                presenter.colorHint(operator);
                dialog.dismiss();
            }
        }
    }
}
