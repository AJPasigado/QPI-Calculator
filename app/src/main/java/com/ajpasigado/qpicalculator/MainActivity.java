package com.ajpasigado.qpicalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.animation.ValueAnimator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GradesRecyclerItemTouchHelper {
    private GradesAdapter grades_adapter;
    private RecyclerView grades_recycler_view;

    ArrayList<Grade> grades = new ArrayList<>();
    public Double totalQPI = 0.0;
    public Double totalUnits = 0.0;

    private final String KEY_TOTAL_QPI = "qpi_key";
    private final String KEY_TOTAL_UNITS = "units_key";
    private final String KEY_GRADES = "grades_key";

    private DatabaseHelper db;
    public String username;
    public String password;

    AESCrypt encrypt = new AESCrypt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Make the application only in portrait mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Initialize the database helper
        db = new DatabaseHelper(this);

        // If there is no saved instance, and its not the first session, load from database
        Cursor data = db.getData();
        data.moveToFirst();
        if (data.getInt(1) != 1 && savedInstanceState == null) {
            //If it is not the first session of the app, load from database
            username = data.getString(2);
            password = data.getString(3);

            try {
                password = encrypt.decrypt(password);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Grade>>() {}.getType();
            ArrayList<Grade> finalOutputString = gson.fromJson(data.getString(4), type);
            grades = finalOutputString;

            totalQPI = data.getDouble(5);
            totalUnits = data.getDouble(6);
            refreshData();
        } else if (savedInstanceState == null){
            //else start a tutorial
            startTutorial();
        }


        // Check if an instance was saved (for configuration changes e.g. screen size change)
        if (savedInstanceState != null){
            grades = savedInstanceState.getParcelableArrayList(KEY_GRADES);
            totalQPI = savedInstanceState.getDouble(KEY_TOTAL_QPI);
            totalUnits = savedInstanceState.getDouble(KEY_TOTAL_UNITS);
            refreshData();
        }

        // Refresh the recycler view if an instance was saved
        refreshRecyclerView();


        //Add a functionality for the floating add button
        FloatingActionButton act = findViewById(R.id.addRow);
        act.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View v){
                Grade temp = new Grade("A", 3);
                grades.add(temp);

                totalQPI += 12;
                totalUnits += 3;

                refreshRecyclerView();
            //    refreshData();
            }
        });

        //Add functionality for clear button
        FloatingActionButton clear = findViewById(R.id.clear_all_BTN);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Double tempQPI = totalQPI;
                final Double tempUnits = totalUnits;
                final ArrayList<Grade> tempgrades = (ArrayList<Grade>)grades.clone();
                grades_adapter.clear();
                totalUnits = 0.0;
                totalQPI = 0.0;
                refreshData();

                ConstraintLayout layout = findViewById(R.id.main_layout);
                Snackbar snackbarClear = Snackbar.make(layout, "All grades removed", Snackbar.LENGTH_LONG);
                snackbarClear.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        grades = tempgrades;
                        totalUnits = tempUnits;
                        totalQPI = tempQPI;
                        refreshData();
                        refreshRecyclerView();
                    }
                });
                snackbarClear.setActionTextColor(Color.WHITE);
                snackbarClear.show();


            }
        });


        // Get the textViews for the target QPI calculator
        TextView desired_qpi_tx = findViewById(R.id.desired_QPI_TXBX);
        TextView units_tx = findViewById(R.id.units_left_TXBX);

        //Add text change functionality for the textboxes
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

        //Add functionality to tutorial button
        Button tutorial = findViewById(R.id.tutorialBTN);
        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTutorial();
            }
        });

        Button load_from_sis = findViewById(R.id.LoadFromSISBTN);
        load_from_sis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadSIS();
            }
        });

        Button about = findViewById(R.id.aboutBTN);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAbout();
            }
        });
    }

    public void startTutorial(){
        Intent intent = new Intent(this, Intro.class);
        startActivity(intent);
    }

    public void loadAbout(){
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
    }

    public void loadSIS(){
        Intent intent = new Intent(this, SISLogin.class);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                grades = data.getParcelableArrayListExtra("grades");
                totalQPI = data.getDoubleExtra("total_QPI", 0.0);
                totalUnits = data.getDoubleExtra("total_units", 0.0);
                username = data.getStringExtra("username");
                password = data.getStringExtra("password");
            }
        }
        refreshRecyclerView();
        refreshData();
    }

    public void refreshRecyclerView(){
        grades_recycler_view = findViewById(R.id.grades_rview);
        grades_adapter = new GradesAdapter(this, this.grades, this);
        grades_recycler_view.setAdapter(grades_adapter);

        grades_recycler_view.setLayoutManager(new LinearLayoutManager(this));

        grades_recycler_view.setItemAnimator(new DefaultItemAnimator());

        //Go to the last entry
        grades_recycler_view.scrollToPosition(grades.size()-1);

        //Add a swipe to left touch helper
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
            minimum_lbl.setText("n/a");
        } else if ( minimum_lbl.getText().toString() != "n/a"){
            animate(minimum_lbl, minimum_lbl.getText().toString(), String.format("%.2f", temp));
        } else {
            animate(minimum_lbl, "0.00", String.format("%.2f", temp));
        }
    }

    void refreshData(){
        TextView tv = findViewById(R.id.your_qpi_LBL);
        Double ans = totalUnits != 0 ?  (totalQPI/totalUnits) : 0.0;
        animate(tv, tv.getText().toString(), String.format("%.2f", ans));

        ConstraintLayout empty = findViewById(R.id.emptyView);

        ConstraintSet cons = new ConstraintSet();
        empty.setConstraintSet(cons);
        SlidingUpPanelLayout panel = findViewById(R.id.sliding_layout);

        Button load = findViewById(R.id.LoadFromSISBTN);
        Button tutorial = findViewById(R.id.tutorialBTN);
        Button about = findViewById(R.id.aboutBTN);
        FloatingActionButton clear = findViewById(R.id.clear_all_BTN);

        if (grades.isEmpty()){
            empty.animate().alpha(1.0f);

            panel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

            load.setEnabled(true);
            tutorial.setEnabled(true);
            about.setEnabled(true);

            clear.animate().alpha(0f);
            clear.setEnabled(false);
        } else {
            empty.animate().alpha(0.0f);
            panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

            load.setEnabled(false);
            tutorial.setEnabled(false);
            about.setEnabled(false);

            clear.animate().alpha(1.0f);
            clear.setEnabled(true);
        }

        updateDatabase();

        refreshCumulative();
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

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof GradesAdapter.GradesViewHolder ){
            final int deleteIndex = viewHolder.getAdapterPosition();
            final Grade grade = grades.get(deleteIndex);

            grades_adapter.removeGrade(deleteIndex);
            totalUnits -= grade.numberOfunits;
            totalQPI -= grade.numberOfunits * getEquivalent(grade.letterGrade);
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
        }
    }

    @Override
    public void onBackPressed() {
        SlidingUpPanelLayout mLayout = findViewById(R.id.sliding_layout);
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    protected void onDestroy() {
        updateDatabase();
        super.onDestroy();
    }

    public void updateDatabase(){
        Gson gson = new Gson();
        String grades_json = gson.toJson(grades);

        String temp = password;

        try {
            temp = encrypt.encrypt(password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.updateData(username, temp , grades_json, totalQPI, totalUnits.intValue());
    }

    public double getMinRequired(Double desired, Double unitsLeft){
        Double tempUnits = totalUnits;
        Double tempQPI = totalQPI;

        return ((desired * (tempUnits + unitsLeft)) - tempQPI) / unitsLeft;
    }

    public static Double getEquivalent(String a){
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putDouble(KEY_TOTAL_QPI, totalQPI);
        outState.putDouble(KEY_TOTAL_UNITS, totalUnits);
        outState.putParcelableArrayList(KEY_GRADES, grades);

        super.onSaveInstanceState(outState);
    }
}