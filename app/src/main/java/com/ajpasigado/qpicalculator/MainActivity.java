package com.ajpasigado.qpicalculator;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.animation.ValueAnimator;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GradesRecyclerItemTouchHelper {
    private GradesAdapter grades_adapter;
    private RecyclerView grades_recycler_view;

    List<Grade> grades = new ArrayList<>();
    public Double totalQPI = 0.0;
    public Double totalUnits = 0.0;

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

        FloatingActionButton act = findViewById(R.id.addRow);
        act.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View v){
                Grade temp = new Grade();
                temp.numberOfunits = 3;
                temp.letterGrade = "A";
                grades.add(temp);

                totalQPI += 12;
                totalUnits += 3;

                refreshRecyclerView();
                refreshData();
            }
        });

        refreshRecyclerView();
    }

    public void refreshRecyclerView(){
        grades_recycler_view = findViewById(R.id.grades_rview);
        grades_adapter = new GradesAdapter(this, grades, this);
        grades_recycler_view.setAdapter(grades_adapter);
        grades_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        grades_recycler_view.setItemAnimator(new DefaultItemAnimator());
        ItemTouchHelper.SimpleCallback callback = new GradesAdapterHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(callback).attachToRecyclerView(grades_recycler_view);
    }

    public void refreshCumulative(){
        TextView desired_qpi_tx = findViewById(R.id.desired_QPI_TXBX);
        TextView units_tx = findViewById(R.id.units_left_TXBX);
        TextView minimum_lbl = findViewById(R.id.minimum_required_LBL);
        Double temp;

        try
        {
            temp = getMinRequired(Double.parseDouble(desired_qpi_tx.getText().toString()), Double.parseDouble(units_tx.getText().toString()));
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

    void refreshData(){
        TextView tv = findViewById(R.id.your_qpi_LBL);
        Double ans = totalUnits != 0 ?  (totalQPI/totalUnits) : 0.0;
        animate(tv, tv.getText().toString(), String.format("%.2f", ans));
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

    private int counter;

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof GradesAdapter.GradesViewHolder ){
            final int deleteIndex = viewHolder.getAdapterPosition();
            final Grade grade = grades.get(deleteIndex);

            grades_adapter.removeGrade(deleteIndex);
            totalUnits -= grade.numberOfunits;
            totalQPI -= grade.numberOfunits * getEquivalent(grade.letterGrade);
            counter ++;
            refreshData();

            ConstraintLayout layout = findViewById(R.id.main_layout);

            Snackbar snackbar = Snackbar.make(layout, "Grade removed", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    grades_adapter.restoreGrade(grade, deleteIndex);
                    totalUnits += grade.numberOfunits;
                    totalQPI += grade.numberOfunits * getEquivalent(grade.letterGrade);

                    refreshData();
                }
            });
            snackbar.setActionTextColor(Color.WHITE);
            snackbar.show();

            if (counter > 3){
                Snackbar snackbarClear = Snackbar.make(layout, "Would you like to clear all grades?", Snackbar.LENGTH_LONG);
                snackbarClear.setAction("CLEAR ALL", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        grades_adapter.clear();
                        totalUnits = 0.0;
                        totalQPI = 0.0;
                        refreshData();
                        counter = 0;
                    }
                });
                snackbarClear.setActionTextColor(Color.WHITE);
                snackbarClear.show();
            }
        }
    }

    public double getMinRequired(Double desired, Double unitsLeft){
        Double tempUnits = totalUnits;
        Double tempQPI = totalQPI;
        Double answer = ((desired * (tempUnits + unitsLeft)) - tempQPI) / unitsLeft;

        return answer;
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
