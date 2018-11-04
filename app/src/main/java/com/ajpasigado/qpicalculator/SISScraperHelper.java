package com.ajpasigado.qpicalculator;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SISScraperHelper {
    String[] excluded_subjects;
    ArrayList<Grade> grades = new ArrayList();
    Handler login;
    private String pass;
    public Integer status = Integer.valueOf(0);
    public Double totalQPI = Double.valueOf(0.0d);
    public Double totalUnits = Double.valueOf(0.0d);
    private String username;

    /* renamed from: com.ajpasigado.qpicalculator.SISScraperHelper$1 */
    class C05281 implements Runnable {
        C05281() {
        }

        public void run() {
            SISScraperHelper.this.totalUnits = Double.valueOf(0.0d);
            SISScraperHelper.this.totalQPI = Double.valueOf(0.0d);
            try {
                Iterator it = Jsoup.connect("https://sis2.addu.edu.ph/curriculum").data("form_id", "user_login_block").data("form_build_id", "form-8Gy3iES_AG7-O7thvZbAQvKkhqjfcH2q2NsmTKFGyA0").data("name", SISScraperHelper.this.username).data("pass", SISScraperHelper.this.pass).method(Method.POST).execute().parse().select("tr").iterator();
                while (true) {
                    Boolean included = true;
                    if (!it.hasNext()) {
                        break;
                    }
                    Elements td = ((Element) it.next()).select("td");
                    if (td.size() > 0) {
                        Double units = Double.valueOf(Double.parseDouble(((Element) td.get(4)).text()));
                        String letter = ((Element) td.get(5)).text();
                        if (Arrays.asList(SISScraperHelper.this.excluded_subjects).indexOf(((Element) td.get(3)).text()) != -1) {
                            included = false;
                        }
                        included = Boolean.valueOf(included);
                        if (MainActivity.getEquivalent(letter).doubleValue() != 0.0d && included.booleanValue()) {
                            SISScraperHelper.this.totalUnits = Double.valueOf(SISScraperHelper.this.totalUnits.doubleValue() + units.doubleValue());
                            SISScraperHelper.this.totalQPI = Double.valueOf(SISScraperHelper.this.totalQPI.doubleValue() + (MainActivity.getEquivalent(letter).doubleValue() * units.doubleValue()));
                            SISScraperHelper.this.grades.add(new Grade(letter, units.intValue()));
                        }
                    }
                }
                SISScraperHelper.this.status = Integer.valueOf(1);
            } catch (Exception e) {
                SISScraperHelper.this.status = Integer.valueOf(2);
                Log.i("Links", e.toString());
            }
            SISScraperHelper.this.login.sendEmptyMessage(0);
        }
    }

    public SISScraperHelper(String[] excluded, String username, String pass, Handler login) {
        this.username = username;
        this.pass = pass;
        this.excluded_subjects = excluded;
        this.login = login;
    }

    public void getWebsite() {
        new Thread(new C05281()).start();
    }
}
