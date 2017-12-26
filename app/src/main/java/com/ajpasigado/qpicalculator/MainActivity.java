package com.ajpasigado.qpicalculator;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public Integer count = 100;
    CalculatorMain cm = new CalculatorMain();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void rowClick (View v){
        //
    }

    public void addRowClick (View v){
        addRows();
    }

    public void addRows(){
        LinearLayout list = findViewById(R.id.main_list);
        LinearLayout vertList = new LinearLayout(this);
        vertList.setVerticalGravity(1);
        vertList.setId(count);

        int width1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        int width2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65, getResources().getDisplayMetrics());
        int width3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
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

        spinner1.setOnItemSelectedListener(getOnSpinnerClick(spinner1, spinner2));
        spinner2.setOnItemSelectedListener(getOnSpinnerClick(spinner1, spinner2));

        Button btn = new Button(this);
        btn.setText("-");
        btn.setOnClickListener(getOnButtonClick(count, vertList, list));
        btn.setId(count+3);

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
            }
        };
    }

    AdapterView.OnItemSelectedListener getOnSpinnerClick(final Spinner spin1, final Spinner spin2)  {
        return new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int a = spin1.getId();
                cm.addData(a - 1, getEquivalent(spin1.getSelectedItem().toString()), Double.parseDouble(spin2.getSelectedItem().toString()));
                refreshData();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }

        };
    }

    void refreshData(){
        cm.calculateData();
        TextView tv = findViewById(R.id.total_label);
        Double ans = cm.getTotalUnits() != 0 ?  (cm.getTotalQPI()/cm.getTotalUnits()) : 0.0;
        tv.setText(String.format("%.2f", ans));
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
}
