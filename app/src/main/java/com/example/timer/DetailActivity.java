package com.example.timer;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorShape;

import java.util.Objects;
import java.util.Random;

public class DetailActivity extends AppCompatActivity implements ColorPickerDialogListener {
    TextView trainingNameEditText;
    TextView preparingTimeEdit;
    TextView workingTimeEdit;
    TextView restTimeEdit;
    TextView cyclesAmountEdit;
    TextView setsAmountEdit;
    TextView restBetweenSetsEdit;
    TextView cooldownTime;
    View colorView;
    SQLiteHelper dbHelper;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        dbHelper = new SQLiteHelper(getApplicationContext());
        final Intent intent = getIntent();

        final String type = intent.getStringExtra("type");
        colorView = findViewById(R.id.currentColorView);
        trainingNameEditText = findViewById(R.id.trainingNameEditText);
        preparingTimeEdit = findViewById(R.id.preparingTimeEdit);
        workingTimeEdit = findViewById((R.id.workingEditText));
        restTimeEdit = findViewById(R.id.restEditText);
        cyclesAmountEdit = findViewById(R.id.cyclesEditText);
        setsAmountEdit = findViewById(R.id.setsAmountEdit);
        restBetweenSetsEdit = findViewById(R.id.restBetweenSetsEdit);
        cooldownTime = findViewById(R.id.cooldownTimeEdit);


        final Button changeColorButton = findViewById(R.id.changeColorButton);
        final Button preparingMinusButton = findViewById(R.id.preparingMinusButton);
        final Button preparingPlusButton = findViewById(R.id.preparingPlusButton);
        final Button workingMinusButton = findViewById(R.id.workingMinusButton);
        final Button workingPlusButton = findViewById(R.id.workingPlusButton);
        final Button restMinusButton = findViewById(R.id.restMinusButton);
        final Button restPlusButton = findViewById(R.id.restPlusButton);
        final Button cyclesMinusButton = findViewById(R.id.cyclesMinusButton);
        final Button cyclesPlusButton = findViewById(R.id.cyclesPlusButton);
        final Button setsAmountMinusButton = findViewById(R.id.setsAmountMinusButton);
        final Button setsAmountPlusButton = findViewById(R.id.setsAmountPlusButton);
        final Button restBetweenSetsMinusButton = findViewById(R.id.restBetweenSetsMinusButton);
        final Button restBetweenSetsPlusButton = findViewById(R.id.restBetweenSetsPlusButton);
        final Button cooldownMinusButton = findViewById(R.id.cooldownMinusButton);
        final Button cooldownPlusButton = findViewById(R.id.cooldownPlusButton);
        final TimerSequence timer;


        if (type != null && type.equals("edit")) {
            timer = (TimerSequence) Objects.requireNonNull(getIntent().getExtras()).getSerializable("timer");
        } else {
            timer = new TimerSequence(0, "", 0,
                    0, 0, 0, 0, 0, 0);

        }
        assert timer != null;


        changeColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createColorPickerDialog(timer.getId());
            }
        });

        int color = dbHelper.getColorByTimerID(timer.getId());
        colorView.setBackgroundColor(color);


        trainingNameEditText.setText(timer.getTitle());
        preparingTimeEdit.setText(timer.getPreparationTime() + "");
        workingTimeEdit.setText(timer.getWorkingTime() + "");
        restTimeEdit.setText(timer.getRestTime() + "");
        cyclesAmountEdit.setText(timer.getCyclesAmount() + "");
        setsAmountEdit.setText(timer.getSetsAmount() + "");
        restBetweenSetsEdit.setText(timer.getBetweenSetsRest() + "");
        cooldownTime.setText(timer.getCooldownTime() + "");


        trainingNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                timer.setTitle(trainingNameEditText.getText().toString());
            }
        });

        View.OnClickListener buttonsClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.equals(preparingMinusButton)) {
                    timer.setPreparationTime(timer.getPreparationTime() - 1);
                    preparingTimeEdit.setText(timer.getPreparationTime() + "");
                } else if (view.equals(preparingPlusButton)) {
                    timer.setPreparationTime(timer.getPreparationTime() + 1);
                    preparingTimeEdit.setText(timer.getPreparationTime() + "");
                } else if (view.equals(workingMinusButton)) {
                    timer.setWorkingTime(timer.getWorkingTime() - 1);
                    workingTimeEdit.setText(timer.getWorkingTime() + "");
                } else if (view.equals(workingPlusButton)) {
                    timer.setWorkingTime(timer.getWorkingTime() + 1);
                    workingTimeEdit.setText(timer.getWorkingTime() + "");
                } else if (view.equals(restMinusButton)) {
                    timer.setRestTime(timer.getRestTime() - 1);
                    restTimeEdit.setText(timer.getRestTime() + "");
                } else if (view.equals(restPlusButton)) {
                    timer.setRestTime(timer.getRestTime() + 1);
                    restTimeEdit.setText(timer.getRestTime() + "");
                } else if (view.equals(cyclesMinusButton)) {
                    timer.setCyclesAmount(timer.getCyclesAmount() - 1);
                    cyclesAmountEdit.setText(timer.getCyclesAmount() + "");
                } else if (view.equals(cyclesPlusButton)) {
                    timer.setCyclesAmount(timer.getCyclesAmount() + 1);
                    cyclesAmountEdit.setText(timer.getCyclesAmount() + "");
                } else if (view.equals(setsAmountMinusButton)) {
                    timer.setSetsAmount(timer.getSetsAmount() - 1);
                    setsAmountEdit.setText(timer.getSetsAmount() + "");
                } else if (view.equals(setsAmountPlusButton)) {
                    timer.setSetsAmount(timer.getSetsAmount() + 1);
                    setsAmountEdit.setText(timer.getSetsAmount() + "");
                } else if (view.equals(restBetweenSetsMinusButton)) {
                    timer.setBetweenSetsRest(timer.getBetweenSetsRest() - 1);
                    restBetweenSetsEdit.setText(timer.getBetweenSetsRest() + "");
                } else if (view.equals(restBetweenSetsPlusButton)) {
                    timer.setBetweenSetsRest(timer.getBetweenSetsRest() + 1);
                    restBetweenSetsEdit.setText(timer.getBetweenSetsRest() + "");
                } else if (view.equals(cooldownMinusButton)) {
                    timer.setCooldownTime(timer.getCooldownTime() - 1);
                    cooldownTime.setText(timer.getCooldownTime() + "");
                } else if (view.equals(cooldownPlusButton)) {
                    timer.setCooldownTime(timer.getCooldownTime() + 1);
                    cooldownTime.setText(timer.getCooldownTime() + "");
                }
            }
        };

        preparingMinusButton.setOnClickListener(buttonsClickListener);
        preparingPlusButton.setOnClickListener(buttonsClickListener);
        preparingMinusButton.setOnClickListener(buttonsClickListener);
        preparingPlusButton.setOnClickListener(buttonsClickListener);
        workingMinusButton.setOnClickListener(buttonsClickListener);
        workingPlusButton.setOnClickListener(buttonsClickListener);
        restMinusButton.setOnClickListener(buttonsClickListener);
        restPlusButton.setOnClickListener(buttonsClickListener);
        cyclesMinusButton.setOnClickListener(buttonsClickListener);
        cyclesPlusButton.setOnClickListener(buttonsClickListener);
        setsAmountMinusButton.setOnClickListener(buttonsClickListener);
        setsAmountPlusButton.setOnClickListener(buttonsClickListener);
        restBetweenSetsMinusButton.setOnClickListener(buttonsClickListener);
        restBetweenSetsPlusButton.setOnClickListener(buttonsClickListener);
        cooldownMinusButton.setOnClickListener(buttonsClickListener);
        cooldownPlusButton.setOnClickListener(buttonsClickListener);


        final View.OnClickListener confirmButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(SQLiteHelper.TRAINING_TITLE, trainingNameEditText.getText().toString());
                contentValues.put(SQLiteHelper.PREPARATION_TIME, String.valueOf(preparingTimeEdit.getText()));
                contentValues.put(SQLiteHelper.WORKING_TIME, String.valueOf(workingTimeEdit.getText()));
                contentValues.put(SQLiteHelper.REST_TIME, String.valueOf(restTimeEdit.getText()));
                contentValues.put(SQLiteHelper.CYCLES_AMOUNT, String.valueOf(cyclesAmountEdit.getText()));
                contentValues.put(SQLiteHelper.SETS_AMOUNT, String.valueOf(setsAmountEdit.getText()));
                contentValues.put(SQLiteHelper.BETWEEN_SETS_REST, String.valueOf(restBetweenSetsEdit.getText()));
                contentValues.put(SQLiteHelper.COOLDOWN_TIME, String.valueOf(cooldownTime.getText()));

                if (type != null) {
                    if (type.equals("edit")) {
                        int _id = timer.getId();
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.update(SQLiteHelper.TABLE_TIMER_NAME, contentValues, SQLiteHelper.COLUMN_ID + "=" + _id, null);
                        Intent data = new Intent();
                        data.putExtra("timer", timer);
                        setResult(RESULT_OK, data);
                    } else {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        int _id = (int)db.insert(SQLiteHelper.TABLE_TIMER_NAME, null, contentValues);
                        contentValues.clear();
                        Random rnd = new Random();
                        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                        contentValues.put(SQLiteHelper.TIMER_COLOR, color);
                        contentValues.put(SQLiteHelper.TIMER_ID, _id);
                        db.insert(SQLiteHelper.TABLE_SETTINGS_NAME, null, contentValues);
                        Intent data = new Intent();
                        data.putExtra("list", getDataFromViews());
                        setResult(RESULT_OK, data);
                    }
                    finish();
                }

            }
        };

        Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(confirmButtonListener);
    }

    private void createColorPickerDialog(int id) {
        ColorPickerDialog.newBuilder()
                .setColor(Color.RED)
                .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setAllowCustom(true)
                .setAllowPresets(true)
                .setColorShape(ColorShape.SQUARE)
                .setDialogId(id)
                .show(this);
// полный список атрибутов класса ColorPickerDialog смотрите ниже
    }

    private TimerSequence getDataFromViews() {
        return new TimerSequence(0, trainingNameEditText.getText().toString(),
                Integer.parseInt(preparingTimeEdit.getText().toString()),
                Integer.parseInt(workingTimeEdit.getText().toString()),
                Integer.parseInt(restTimeEdit.getText().toString()),
                Integer.parseInt(cyclesAmountEdit.getText().toString()),
                Integer.parseInt(setsAmountEdit.getText().toString()),
                Integer.parseInt(restBetweenSetsEdit.getText().toString()),
                Integer.parseInt(cooldownTime.getText().toString()));
    }


    @Override
    public void onColorSelected(int _id, int color) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.TIMER_COLOR, color);
        db.update(SQLiteHelper.TABLE_SETTINGS_NAME, contentValues, SQLiteHelper.TIMER_ID+ "=" + _id, null);
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }
}