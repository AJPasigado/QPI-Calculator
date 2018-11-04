package com.ajpasigado.qpicalculator;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import java.util.Arrays;
import java.util.List;

public class GradesAdapter extends Adapter<GradesAdapter.GradesViewHolder> {
    List<Grade> grades;
    private LayoutInflater inflator;
    String[] letter_grades_array;
    MainActivity main;
    String[] units_array;

    class GradesViewHolder extends ViewHolder {
        View background;
        View foreground;
        Spinner letterGrade;
        Spinner numberOfunits;

        public GradesViewHolder(View itemView) {
            super(itemView);
            this.letterGrade = (Spinner) itemView.findViewById(R.id.letter_grade_spinner);
            this.numberOfunits = (Spinner) itemView.findViewById(R.id.units_spinner);
            this.foreground = itemView.findViewById(R.id.foreground);
            this.background = itemView.findViewById(R.id.background);
        }
    }

    public GradesAdapter(Context context, List<Grade> grades, MainActivity main) {
        this.inflator = LayoutInflater.from(context);
        this.grades = grades;
        this.letter_grades_array = context.getResources().getStringArray(R.array.letter_grades);
        this.units_array = context.getResources().getStringArray(R.array.number_of_units);

        this.main = main;
    }

    public GradesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GradesViewHolder(this.inflator.inflate(R.layout.grades_row, parent, false));
    }

    public void onBindViewHolder(final GradesViewHolder holder, int position) {
        final Grade current = (Grade) this.grades.get(position);
        holder.letterGrade.setSelection(Arrays.asList(this.letter_grades_array).indexOf(current.letterGrade));
        holder.numberOfunits.setSelection(Arrays.asList(units_array).indexOf(Integer.toString(current.numberOfunits)));
        holder.letterGrade.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity mainActivity = GradesAdapter.this.main;
                double doubleValue = mainActivity.totalQPI.doubleValue();
                MainActivity mainActivity2 = GradesAdapter.this.main;
                double doubleValue2 = MainActivity.getEquivalent(holder.letterGrade.getSelectedItem().toString()).doubleValue() * ((double) current.numberOfunits);
                MainActivity mainActivity3 = GradesAdapter.this.main;
                mainActivity.totalQPI = Double.valueOf(doubleValue + (doubleValue2 - (MainActivity.getEquivalent(current.letterGrade).doubleValue() * ((double) current.numberOfunits))));
                GradesAdapter.this.main.refreshData();
                current.letterGrade = holder.letterGrade.getSelectedItem().toString();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        holder.numberOfunits.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity mainActivity = GradesAdapter.this.main;
                mainActivity.totalUnits = Double.valueOf(mainActivity.totalUnits.doubleValue() + ((double) (Integer.parseInt(holder.numberOfunits.getSelectedItem().toString()) - current.numberOfunits)));
                mainActivity = GradesAdapter.this.main;
                double doubleValue = mainActivity.totalQPI.doubleValue();
                MainActivity mainActivity2 = GradesAdapter.this.main;
                double doubleValue2 = MainActivity.getEquivalent(holder.letterGrade.getSelectedItem().toString()).doubleValue() * ((double) Integer.parseInt(holder.numberOfunits.getSelectedItem().toString()));
                MainActivity mainActivity3 = GradesAdapter.this.main;
                mainActivity.totalQPI = Double.valueOf(doubleValue + (doubleValue2 - (MainActivity.getEquivalent(current.letterGrade).doubleValue() * ((double) current.numberOfunits))));
                GradesAdapter.this.main.refreshData();
                current.numberOfunits = Integer.parseInt(holder.numberOfunits.getSelectedItem().toString());
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public int getItemCount() {
        return this.grades.size();
    }

    public void removeGrade(int position) {
        this.grades.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreGrade(Grade grade, int position) {
        this.grades.add(position, grade);
        notifyItemInserted(position);
    }

    public void clear() {
        int size = this.grades.size();
        this.grades.clear();
        notifyItemRangeRemoved(0, size);
    }
}
