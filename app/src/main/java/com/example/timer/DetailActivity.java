package com.example.timer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorShape;

import java.util.ArrayList;


public class DetailActivity extends AppCompatActivity implements ColorPickerDialogListener {
    TextView trainingNameEditText;
    TextView preparingTimeEdit;
    TextView workingTimeEdit;
    TextView restTimeEdit;
    TextView cyclesAmountEdit;
    TextView setsAmountEdit;
    TextView restBetweenSetsEdit;
    TextView cooldownTimeEdit;
    View colorView;
    EditableTimerViewModel editableViewModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final Intent intent = getIntent();

        TimerSequence timer = (TimerSequence) intent.getSerializableExtra("timer");
        editableViewModel = new ViewModelProvider(this, new EditableViewModelFactory(timer)).get(EditableTimerViewModel.class);


        final String type = intent.getStringExtra("type");

        colorView = findViewById(R.id.currentColorView);
        trainingNameEditText = findViewById(R.id.trainingNameEditText);
        preparingTimeEdit = findViewById(R.id.preparingTimeEdit);
        workingTimeEdit = findViewById((R.id.workingEditText));
        restTimeEdit = findViewById(R.id.restEditText);
        cyclesAmountEdit = findViewById(R.id.cyclesEditText);
        setsAmountEdit = findViewById(R.id.setsAmountEdit);
        restBetweenSetsEdit = findViewById(R.id.restBetweenSetsEdit);
        cooldownTimeEdit = findViewById(R.id.cooldownTimeEdit);
        colorView = findViewById(R.id.currentColorView);

        final ArrayList<Button> buttonList = new ArrayList<>();
        final Button changeColorButton = findViewById(R.id.changeColorButton);

        buttonList.add((Button) findViewById(R.id.preparingMinusButton));
        buttonList.add((Button) findViewById(R.id.preparingPlusButton));
        buttonList.add((Button) findViewById(R.id.workingMinusButton));
        buttonList.add((Button) findViewById(R.id.workingPlusButton));
        buttonList.add((Button) findViewById(R.id.restMinusButton));
        buttonList.add((Button) findViewById(R.id.restPlusButton));
        buttonList.add((Button) findViewById(R.id.cyclesMinusButton));
        buttonList.add((Button) findViewById(R.id.cyclesPlusButton));
        buttonList.add((Button) findViewById(R.id.setsAmountMinusButton));
        buttonList.add((Button) findViewById(R.id.setsAmountPlusButton));
        buttonList.add((Button) findViewById(R.id.restBetweenSetsMinusButton));
        buttonList.add((Button) findViewById(R.id.restBetweenSetsPlusButton));
        buttonList.add((Button) findViewById(R.id.cooldownMinusButton));
        buttonList.add((Button) findViewById(R.id.cooldownPlusButton));

        changeColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
            createColorPickerDialog(editableViewModel.id.getValue());
        }
        });

        colorView.setBackgroundColor(editableViewModel.color.getValue());

        trainingNameEditText.setText(editableViewModel.title.getValue());
        preparingTimeEdit.setText(editableViewModel.preparingTime.getValue() + "");
        workingTimeEdit.setText(editableViewModel.workingTime.getValue() + "");
        restTimeEdit.setText(editableViewModel.restTime.getValue() + "");
        cyclesAmountEdit.setText(editableViewModel.cyclesAmount.getValue() + "");
        setsAmountEdit.setText(editableViewModel.setsAmount.getValue() + "");
        restBetweenSetsEdit.setText(editableViewModel.restBetweenSets.getValue() + "");
        cooldownTimeEdit.setText(editableViewModel.cooldownTime.getValue() + "");


        trainingNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editableViewModel.title.setValue(trainingNameEditText.getText().toString());
            }
        });

        editableViewModel.preparingTime.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
              preparingTimeEdit.setText(String.valueOf(Math.max((int)integer, 0)));
            }
        });

        editableViewModel.workingTime.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                workingTimeEdit.setText(String.valueOf(Math.max((int)integer, 0)));
            }
        });

        editableViewModel.restTime.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                restTimeEdit.setText(String.valueOf(Math.max((int)integer, 0)));
            }
        });

        editableViewModel.cyclesAmount.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                cyclesAmountEdit.setText(String.valueOf(Math.max((int)integer, 0)));
            }
        });

        editableViewModel.setsAmount.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                setsAmountEdit.setText(String.valueOf(Math.max((int) integer, 0)));
            }
        });

        editableViewModel.restBetweenSets.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                restBetweenSetsEdit.setText(String.valueOf(Math.max((int)integer, 0)));
            }
        });

        editableViewModel.cooldownTime.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                cooldownTimeEdit.setText(String.valueOf(Math.max((int)integer, 0)));
            }
        });

        editableViewModel.color.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                colorView.setBackgroundColor(integer);
            }
        });

        View.OnClickListener buttonsClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.preparingMinusButton) {
                    editableViewModel.preparingTime.setValue(editableViewModel.preparingTime.getValue() - 1);
                } else if (view.getId() == R.id.preparingPlusButton) {
                    editableViewModel.preparingTime.setValue(editableViewModel.preparingTime.getValue() + 1);
                } else if (view.getId() == R.id.workingMinusButton) {
                    editableViewModel.workingTime.setValue(editableViewModel.workingTime.getValue() - 1);
                } else if (view.getId() == R.id.workingPlusButton) {
                    editableViewModel.workingTime.setValue(editableViewModel.workingTime.getValue() + 1);
                } else if (view.getId() == R.id.restMinusButton) {
                    editableViewModel.restTime.setValue(editableViewModel.restTime.getValue() - 1);
                } else if (view.getId() == R.id.restPlusButton) {
                    editableViewModel.restTime.setValue(editableViewModel.restTime.getValue() + 1);
                } else if (view.getId() == R.id.cyclesMinusButton) {
                    editableViewModel.cyclesAmount.setValue(editableViewModel.cyclesAmount.getValue() - 1);
                } else if (view.getId() == R.id.cyclesPlusButton) {
                    editableViewModel.cyclesAmount.setValue(editableViewModel.cyclesAmount.getValue() + 1);
                } else if (view.getId() == R.id.setsAmountMinusButton) {
                    editableViewModel.setsAmount.setValue(editableViewModel.setsAmount.getValue() - 1);
                } else if (view.getId() == R.id.setsAmountPlusButton) {
                    editableViewModel.setsAmount.setValue(editableViewModel.setsAmount.getValue() + 1);
                } else if (view.getId() == R.id.restBetweenSetsMinusButton) {
                    editableViewModel.restBetweenSets.setValue(editableViewModel.restBetweenSets.getValue() - 1);
                } else if (view.getId() == R.id.restBetweenSetsPlusButton) {
                    editableViewModel.restBetweenSets.setValue(editableViewModel.restBetweenSets.getValue() + 1);
                } else if (view.getId() == R.id.cooldownMinusButton) {
                    editableViewModel.cooldownTime.setValue(editableViewModel.cooldownTime.getValue() - 1);
                } else if (view.getId() == R.id.cooldownPlusButton) {
                    editableViewModel.cooldownTime.setValue(editableViewModel.cooldownTime.getValue() + 1);
                }
            }
        };

        for (Button button:
                buttonList) {
            button.setOnClickListener(buttonsClickListener);
        }

        final View.OnClickListener confirmButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("timer", editableViewModel.saveTimer(type, getApplicationContext()));
                setResult(RESULT_OK, data);
                finish();
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
    }



    @Override
    public void onColorSelected(int _id, int color) {
        editableViewModel.color.setValue(color);
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }
}
