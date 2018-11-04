package com.ajpasigado.qpicalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class SISLogin extends AppCompatActivity {
    Button back;
    ArrayList<Grade> grades = new ArrayList();
    public Handler handler;
    Button login;
    ProgressBar progress;
    SISScraperHelper sis;
    public Double totalQPI = Double.valueOf(0.0d);
    public Double totalUnits = Double.valueOf(0.0d);

    /* renamed from: com.ajpasigado.qpicalculator.SISLogin$2 */
    class C05242 implements OnClickListener {
        C05242() {
        }

        public void onClick(View view) {
            SISLogin.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sislogin);
        this.login = (Button) findViewById(R.id.sis_login_btn);
        this.progress = (ProgressBar) findViewById(R.id.progressBar);
        final EditText username = (EditText) findViewById(R.id.sis_username);
        final EditText password = (EditText) findViewById(R.id.sis_password);
        this.login.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ((InputMethodManager) SISLogin.this.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(password.getWindowToken(), 0);
                SISLogin.this.loadFromSIS(username.getText().toString(), password.getText().toString());
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (!(bundle.getString("username") == null || bundle.getString("password") == null)) {
            username.setText(bundle.getString("username"));
            password.setText(bundle.getString("password"));
        }
        this.back = (Button) findViewById(R.id.back_BTN);
        this.back.setOnClickListener(new C05242());
    }

    private void loadFromSIS(final String username, final String pass) {
        this.login.animate().alpha(0.0f);
        this.login.setEnabled(false);
        this.back.animate().alpha(0.0f);
        this.back.setEnabled(false);
        this.progress.animate().alpha(1.0f);
        this.handler = new Handler() {

            /* renamed from: com.ajpasigado.qpicalculator.SISLogin$3$1 */
            class C05251 implements DialogInterface.OnClickListener {
                C05251() {
                }

                public void onClick(DialogInterface dialog, int id) {
                    SISLogin.this.grades = SISLogin.this.sis.grades;
                    SISLogin.this.totalUnits = SISLogin.this.sis.totalUnits;
                    SISLogin.this.totalQPI = SISLogin.this.sis.totalQPI;
                    Intent intent = new Intent();
                    intent.putExtra("total_QPI", SISLogin.this.totalQPI);
                    intent.putExtra("total_units", SISLogin.this.totalUnits);
                    intent.putExtra("grades", SISLogin.this.grades);
                    intent.putExtra("username", username);
                    intent.putExtra("password", pass);
                    SISLogin.this.setResult(-1, intent);
                    SISLogin.this.finish();
                    SISLogin.this.finish();
                }
            }

            /* renamed from: com.ajpasigado.qpicalculator.SISLogin$3$2 */
            class C05262 implements DialogInterface.OnClickListener {
                C05262() {
                }

                public void onClick(DialogInterface dialog, int id) {
                }
            }

            public void handleMessage(Message msg) {
                SISLogin.this.login.animate().alpha(1.0f);
                SISLogin.this.login.setEnabled(true);
                SISLogin.this.back.animate().alpha(1.0f);
                SISLogin.this.back.setEnabled(true);
                SISLogin.this.progress.animate().alpha(0.0f);

                Builder builder = new Builder(SISLogin.this);
                if (SISLogin.this.sis.status.intValue() == 1) {
                    builder.setMessage("Your grades were successfully loaded. ").setCancelable(false).setPositiveButton("OK", new C05251());
                } else {
                    builder.setMessage("Failed to connect to server. Please check your internet connection or your credentials.").setCancelable(false).setPositiveButton("OK", new C05262());
                }
                builder.create().show();
                super.handleMessage(msg);
            }
        };
        this.sis = new SISScraperHelper(getResources().getStringArray(R.array.excluded_subjects), username, pass, this.handler);
        this.sis.getWebsite();
    }

    public void onBackPressed() {
        if (this.back.isEnabled()) {
            finish();
        }
    }
}
