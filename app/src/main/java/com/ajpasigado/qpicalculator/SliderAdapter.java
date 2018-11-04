package com.ajpasigado.qpicalculator;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    public String[] slide_details = new String[]{"This is a simple QPI Calculator with a lot of handy features. Click the next button to start your tutorial.", "Simply click the '+' floating button to add a new grade entry.", "Swipe a grade entry to the left to remove it. When you are removing too many grades, you will be notified when you want to reset the entries.", "Tap on a grade entry to change either its number of units or letter grade.", "The cumulative QPI will automatically update itself when a grade is added or removed.", "Swipe up the cumulative QPI and you can see a target QPI calculator. Enter your desired QPI and the number of units left and the minimum QPI needed will be automatically calculated.", "Press Load from SIS to get to the login page. Enter your credentials and wait for the process to finish.", "Click start to get into the app"};
    public int[] slide_images = new int[]{R.drawable.home, R.drawable.add, R.drawable.remove, R.drawable.change, R.drawable.calculate, R.drawable.target, R.drawable.sis, R.drawable.home};
    public String[] slide_titles = new String[]{"WELCOME", "ADDING A GRADE", "REMOVING A GRADE", "CHANGING A GRADE", "CALCULATING QPI", "TARGET QPI", "LOAD FROM SIS", "START ADDING"};

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public int getCount() {
        return this.slide_titles.length;
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == ((ConstraintLayout) object);
    }

    public Object instantiateItem(ViewGroup container, int position) {
        Context context = this.context;
        Context context2 = this.context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = this.layoutInflater.inflate(R.layout.slide, container, false);
        TextView details = (TextView) view.findViewById(R.id.slide_details);
        ImageView image = (ImageView) view.findViewById(R.id.slide_image);
        ((TextView) view.findViewById(R.id.slide_title)).setText(this.slide_titles[position]);
        details.setText(this.slide_details[position]);
        image.setImageResource(this.slide_images[position]);
        container.addView(view);
        return view;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
