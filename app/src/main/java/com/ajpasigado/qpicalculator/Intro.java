package com.ajpasigado.qpicalculator;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Intro extends AppCompatActivity {
    private LinearLayout dots;
    private TextView[] mdots;
    OnPageChangeListener pageChangeListener = new C05113();
    private int previousPage;
    private Button skipprev;
    private ViewPager slider;
    SliderAdapter sliderAdapter;
    private Button startnext;

    /* renamed from: com.ajpasigado.qpicalculator.Intro$1 */
    class C05091 implements OnClickListener {
        C05091() {
        }

        public void onClick(View view) {
            if (Intro.this.previousPage != 0) {
                Intro.this.slider.setCurrentItem(Intro.this.previousPage - 1);
            } else {
                Intro.this.finish();
            }
        }
    }

    /* renamed from: com.ajpasigado.qpicalculator.Intro$2 */
    class C05102 implements OnClickListener {
        C05102() {
        }

        public void onClick(View view) {
            if (Intro.this.previousPage != Intro.this.mdots.length - 1) {
                Intro.this.slider.setCurrentItem(Intro.this.previousPage + 1);
            } else {
                Intro.this.finish();
            }
        }
    }

    /* renamed from: com.ajpasigado.qpicalculator.Intro$3 */
    class C05113 implements OnPageChangeListener {
        C05113() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
            Intro.this.mdots[Intro.this.previousPage].setTextColor(Intro.this.getResources().getColor(R.color.colorPrimary));
            Intro.this.mdots[position].setTextColor(-1);
            if (position == 0) {
                Intro.this.skipprev.setText("  SKIP");
                Intro.this.startnext.setText("NEXT  ");
            } else if (position == Intro.this.mdots.length - 1) {
                Intro.this.skipprev.setText("  PREV");
                Intro.this.startnext.setText("START  ");
            } else {
                Intro.this.skipprev.setText("  PREV");
                Intro.this.startnext.setText("NEXT  ");
            }
            Intro.this.previousPage = position;
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.slider = (ViewPager) findViewById(R.id.slidesPGR);
        this.dots = (LinearLayout) findViewById(R.id.dotCL);
        this.skipprev = (Button) findViewById(R.id.skipprevBTN);
        this.startnext = (Button) findViewById(R.id.startnextBTN);
        this.sliderAdapter = new SliderAdapter(this);
        this.slider.setAdapter(this.sliderAdapter);
        this.slider.addOnPageChangeListener(this.pageChangeListener);
        this.mdots = new TextView[8];
        for (int i = 0; i < this.mdots.length; i++) {
            this.mdots[i] = new TextView(this);
            this.mdots[i].setText(Html.fromHtml("&#8226;"));
            this.mdots[i].setTextSize(35.0f);
            this.mdots[i].setTextColor(getResources().getColor(R.color.colorPrimary));
            this.dots.addView(this.mdots[i]);
        }
        this.mdots[0].setTextColor(-1);
        this.skipprev.setOnClickListener(new C05091());
        this.startnext.setOnClickListener(new C05102());
    }
}
