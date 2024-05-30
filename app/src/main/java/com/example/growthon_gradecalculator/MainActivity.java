package com.example.growthon_gradecalculator;

import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private Button addSubjectButton, calculateButton;
    private int subjectCount = 1;
    private HashMap<Integer, Spinner> gradeSpinners = new HashMap<>();
    private HashMap<Integer, CheckBox> majorCheckBoxes = new HashMap<>();
    private HashMap<Integer, EditText> subjectNames = new HashMap<>();
    private HashMap<Integer, EditText> subjectCredits = new HashMap<>();
    private static final String[] grades = {"A+", "A0", "B+", "B0", "C+", "C0", "D+", "D0", "F"};
    private static final double[] gradeValues = {4.5, 4.0, 3.5, 3.0, 2.5, 2.0, 1.5, 1.0, 0.0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableLayout = findViewById(R.id.tableLayout);
        addSubjectButton = findViewById(R.id.add_subject_button);
        calculateButton = findViewById(R.id.calculate_button);

        addSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubjectRow();
            }
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateGrades();
            }
        });

        // 첫 번째 과목의 스피너와 체크박스를 초기화
        initializeSpinnerAndCheckBox(1);
    }

    private void addSubjectRow() {
        subjectCount++;
        TableRow newRow = new TableRow(this);
        newRow.setGravity(Gravity.CENTER);

        EditText subjectText = new EditText(this);
        subjectText.setHint("과목명 입력");
        subjectText.setPadding(8, 8, 8, 8);
        subjectText.setGravity(Gravity.CENTER);
        subjectText.setInputType(InputType.TYPE_CLASS_TEXT);

        EditText creditText = new EditText(this);
        creditText.setHint("학점 입력");
        creditText.setPadding(8, 8, 8, 8);
        creditText.setGravity(Gravity.CENTER);
        creditText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        Spinner gradeSpinner = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, grades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradeSpinner.setAdapter(adapter);

        CheckBox majorCheckBox = new CheckBox(this);
        majorCheckBox.setGravity(Gravity.CENTER);

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        params.gravity = Gravity.CENTER;

        majorCheckBox.setLayoutParams(params);

        newRow.addView(subjectText);
        newRow.addView(creditText);
        newRow.addView(gradeSpinner);
        newRow.addView(majorCheckBox);

        tableLayout.addView(newRow);
        subjectNames.put(subjectCount, subjectText);
        subjectCredits.put(subjectCount, creditText);
        gradeSpinners.put(subjectCount, gradeSpinner);
        majorCheckBoxes.put(subjectCount, majorCheckBox);
    }

    private void initializeSpinnerAndCheckBox(int index) {
        Spinner gradeSpinner = findViewById(getResources().getIdentifier("grade_spinner" + index, "id", getPackageName()));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, grades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradeSpinner.setAdapter(adapter);

        CheckBox majorCheckBox = findViewById(getResources().getIdentifier("major_checkbox" + index, "id", getPackageName()));
        gradeSpinners.put(index, gradeSpinner);
        majorCheckBoxes.put(index, majorCheckBox);

        EditText subjectName = findViewById(getResources().getIdentifier("subject_name" + index, "id", getPackageName()));
        EditText subjectCredit = findViewById(getResources().getIdentifier("subject_credit" + index, "id", getPackageName()));
        subjectNames.put(index, subjectName);
        subjectCredits.put(index, subjectCredit);
    }

    private void calculateGrades() {
        double totalPoints = 0;
        double majorPoints = 0;
        int totalCredits = 0;
        int majorCredits = 0;

        for (int i = 1; i <= subjectCount; i++) {
            Spinner gradeSpinner = gradeSpinners.get(i);
            CheckBox majorCheckBox = majorCheckBoxes.get(i);
            EditText subjectName = subjectNames.get(i);
            EditText subjectCredit = subjectCredits.get(i);

            if (gradeSpinner != null && majorCheckBox != null && subjectName != null && subjectCredit != null) {
                String grade = gradeSpinner.getSelectedItem().toString();
                double gradeValue = getGradeValue(grade);

                int credits = Integer.parseInt(subjectCredit.getText().toString());

                totalPoints += gradeValue * credits;
                totalCredits += credits;

                if (majorCheckBox.isChecked()) {
                    majorPoints += gradeValue * credits;
                    majorCredits += credits;
                }
            }
        }

        if (totalCredits > 0) {
            double gpa = totalPoints / totalCredits;
            TextView overallGradeTextView = findViewById(R.id.overall_grade);
            overallGradeTextView.setText(String.format("%.2f", gpa));
        } else {
            Toast.makeText(this, "학점을 입력하세요.", Toast.LENGTH_LONG).show();
        }

        if (majorCredits > 0) {
            double majorGpa = majorPoints / majorCredits;
            TextView majorGradeTextView = findViewById(R.id.major_grade);
            majorGradeTextView.setText(String.format("%.2f", majorGpa));
        }

        TextView creditsTextView = findViewById(R.id.credits);
        creditsTextView.setText(String.valueOf(totalCredits));
    }


    private double getGradeValue(String grade) {
        for (int i = 0; i < grades.length; i++) {
            if (grades[i].equals(grade)) {
                return gradeValues[i];
            }
        }
        return 0;
    }
}
//좆까
