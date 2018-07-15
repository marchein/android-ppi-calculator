package de.marc_hein.ppicalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.xml.datatype.Duration;

public class MainActivity extends AppCompatActivity {

    public static final String DEBUG_TAG = "HEIN_DEBUG";
    private String resultDefaultText;
    private double resultValue;
    private long heightValue, widthValue;
    private float sizeValue;
    private ArrayList<EditText> textFields;
    private TextView resultField;
    private boolean roundResult;
    private boolean hasResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
    }

    public void onCheckBoxClicked(View v) {
        roundResult = ((CheckBox) v).isChecked();
        if (hasResult) {
            resultField.setText(formatResult(getResult(resultValue)));
        }
    }

    public void onCalculateClick(View v) {
        Log.d(DEBUG_TAG, "Calculating...");
        String[] currentValues = new String[3];

        for (int i = 0; i < textFields.size(); i++) {
            currentValues[i] = textFields.get(i).getText().toString();
        }

        try {
            if (processData(currentValues)) {
                resultField.setText(formatResult(getResult(calc())));
            }
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onResetClick(View v) {
        for (int i = 0; i < textFields.size(); i++) {
            textFields.get(i).setText(new String());
        }
        resultField.setText(resultDefaultText);
        widthValue = 0;
        heightValue = 0;
        sizeValue = 0;
        resultValue = 0;
        hasResult = false;
        Toast.makeText(this, "Resetted fields...", Toast.LENGTH_SHORT).show();
    }

    private void setupViews() {
        textFields = new ArrayList<>();
        textFields.add((EditText) findViewById(R.id.widthField));
        textFields.add((EditText) findViewById(R.id.heightField));
        textFields.add((EditText) findViewById(R.id.sizeField));
        resultField = findViewById(R.id.resultField);
        resultDefaultText = resultField.getText().toString();
    }

    private boolean processData(String[] currentValues) throws IllegalArgumentException {
        try {
            widthValue = Long.parseLong(currentValues[0]);
            heightValue = Long.parseLong(currentValues[1]);
            sizeValue = Float.parseFloat(currentValues[2]);
            return widthValue > 0 && heightValue > 0 && sizeValue > 0;
        } catch (Exception e) {
            throw new IllegalArgumentException("One or more parameters are no numbers");
        }

    }

    private double calc() throws IllegalArgumentException {
        if (widthValue < 1) {
            throw new IllegalArgumentException("The width can not be smaller than 1.");
        } else if (heightValue < 1) {
            throw new IllegalArgumentException("The height can not be smaller than 1.");
        } else if (sizeValue <= 0) {
            throw new ArithmeticException("The size can not be smaller than 1.");
        } else {
            resultValue = Math.sqrt((widthValue * widthValue) + (heightValue * heightValue)) / sizeValue;
            hasResult = true;
            return resultValue;
        }
    }

    private double getResult(double result) {
        return roundResult ? Math.round(resultValue) : resultValue;
    }

    private String formatResult(double result) {
        DecimalFormat format = new DecimalFormat("0.#########");
        return format.format(result);
    }
 }
