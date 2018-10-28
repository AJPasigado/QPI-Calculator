package com.ajpasigado.qpicalculator;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;
import android.animation.ValueAnimator;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public Integer count = 100;
    CalculatorMain cm = new CalculatorMain();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Make to run your application only in portrait mode



        TextView desired_qpi_tx = findViewById(R.id.desired_QPI_TXBX);
        TextView units_tx = findViewById(R.id.units_left_TXBX);

        desired_qpi_tx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                refreshCumulative();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        units_tx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                refreshCumulative();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void refreshCumulative(){
        TextView desired_qpi_tx = findViewById(R.id.desired_QPI_TXBX);
        TextView units_tx = findViewById(R.id.units_left_TXBX);
        TextView minimum_lbl = findViewById(R.id.minimum_required_LBL);
        Double temp;

        try
        {
            temp = cm.getMinRequired(Double.parseDouble(desired_qpi_tx.getText().toString()), Double.parseDouble(units_tx.getText().toString()));
        }
        catch(NumberFormatException e)
        {
            temp = -1.0;
        }

        if (temp > 4 || temp < 0){
            minimum_lbl.setText("=(");
        } else if ( minimum_lbl.getText().toString() != "=("){
            animate(minimum_lbl, minimum_lbl.getText().toString(), String.format("%.2f", temp));
        } else {
            animate(minimum_lbl, "0.0", String.format("%.2f", temp));
        }
    }

    public void addRowClick (View v){
        addRows();
    }

    public void addRows(){
        cm.getMinRequired(2.0,2.0);
        LinearLayout list = findViewById(R.id.main_list);
        LinearLayout vertList = new LinearLayout(this);
        vertList.setVerticalGravity(1);
        vertList.setId(count);

        int width1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        int width2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65, getResources().getDisplayMetrics());
        int width3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());

        Space sp1 = new Space(this);
        sp1.setLayoutParams(new LinearLayout.LayoutParams(width1, height));
        Space sp2 = new Space(this);
        sp2.setLayoutParams(new LinearLayout.LayoutParams(width2, height));
        Space sp3 = new Space(this);
        sp3.setLayoutParams(new LinearLayout.LayoutParams(width3, height));

        int width4 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());

        ArrayList<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("A");
        spinnerArray.add("B+");
        spinnerArray.add("B");
        spinnerArray.add("C+");
        spinnerArray.add("C");
        spinnerArray.add("D");
        spinnerArray.add("F/FD");
        Spinner spinner1 = new Spinner(this);
        spinner1.setLayoutParams(new Spinner.LayoutParams(width4, height));
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner1.setAdapter(spinnerArrayAdapter1);
        spinner1.setId(count+1);

        ArrayList<String> spinnerArray2 = new ArrayList<>();
        spinnerArray2.add("9");
        spinnerArray2.add("6");
        spinnerArray2.add("5");
        spinnerArray2.add("3");
        spinnerArray2.add("2");
        spinnerArray2.add("1");
        Spinner spinner2 = new Spinner(this);
        spinner2.setLayoutParams(new Spinner.LayoutParams(width4, height));
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray2);
        spinner2.setAdapter(spinnerArrayAdapter2);
        spinner2.setId(count+2);
        spinner2.setSelection(3);

        spinner1.setOnItemSelectedListener(getOnSpinnerClick(spinner1, spinner2));
        spinner2.setOnItemSelectedListener(getOnSpinnerClick(spinner1, spinner2));

        Button btn = new Button(this);
        btn.setOnClickListener(getOnButtonClick(count, vertList, list));
        btn.setId(count+3);
        btn.setBackground(getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel));
        btn.setMaxHeight(10);

        count += 100;

        vertList.addView(sp1);
        vertList.addView(spinner1);
        vertList.addView(sp2);
        vertList.addView(spinner2);
        vertList.addView(sp3);
        vertList.addView(btn);
        list.addView(vertList);

        refreshData();
    }

    View.OnClickListener getOnButtonClick(final int count, final LinearLayout vert, final LinearLayout list)  {
        return new View.OnClickListener() {
            public void onClick(View v) {
                list.removeView(vert);
                cm.removeData(count);
                refreshData();
                refreshCumulative();
            }
        };
    }

    AdapterView.OnItemSelectedListener getOnSpinnerClick(final Spinner spin1, final Spinner spin2)  {
        return new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int a = spin1.getId();
                if (spin1.getSelectedItem().toString() != "-" && spin2.getSelectedItem().toString() != "-") {
                    cm.addData(a - 1, getEquivalent(spin1.getSelectedItem().toString()), Double.parseDouble(spin2.getSelectedItem().toString()));
                }
                refreshData();
                refreshCumulative();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }

        };
    }

    void refreshData(){
        cm.calculateData();
        TextView tv = findViewById(R.id.your_qpi_LBL);
        Double ans = cm.getTotalUnits() != 0 ?  (cm.getTotalQPI()/cm.getTotalUnits()) : 0.0;
        animate(tv, tv.getText().toString(), String.format("%.2f", ans));
    }

    Double getEquivalent(String a){
        switch (a) {
            case "A":
                return 4.0;
            case "B+":
                return 3.5;
            case "B":
                return 3.0;
            case "C+":
                return 2.5;
            case "C":
                return 2.0;
            case "D":
                return 1.0;
            default: return 0.0;
        }
    }

    private void animate(final TextView textview, String start, String end){
        ValueAnimator animator = ValueAnimator.ofFloat(Float.parseFloat(start), Float.parseFloat(end));
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textview.setText( String.format("%.2f", animation.getAnimatedValue()));
            }
        });
        animator.start();
    }
}
