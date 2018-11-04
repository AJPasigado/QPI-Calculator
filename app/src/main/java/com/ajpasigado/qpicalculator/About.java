package com.ajpasigado.qpicalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class About extends AppCompatActivity {

    /* renamed from: com.ajpasigado.qpicalculator.About$1 */
    class C05051 implements OnClickListener {
        C05051() {
        }

        public void onClick(View view) {
            About.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ((Button) findViewById(R.id.back_BTN)).setOnClickListener(new C05051());
    }
}
