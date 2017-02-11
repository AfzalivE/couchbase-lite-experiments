package com.afzaln.cblexperiments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private CouchStore couchStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        couchStore = CouchStore.getInstance(this);
        Calendar todayCal = Calendar.getInstance();
        Calendar tomorrowCal = Calendar.getInstance();
        tomorrowCal.add(Calendar.DAY_OF_MONTH, 1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {

            for (int i = 0, iSize = 10; i < iSize; i++) {
                Calendar calendar = todayCal;
                if (i > 4) {
                    calendar = tomorrowCal;
                }
                Alpha alpha = AlphaBuilder.alpha()
                        .text("Hello " + i)
                        .build();

                alpha.created_at = Utils.sDateFormatter.format(calendar.getTime());

                couchStore.createDoc(alpha);

                Beta beta = BetaBuilder.beta()
                        .num(i)
                        .build();

                beta.created_at = Utils.sDateFormatter.format(calendar.getTime());

                couchStore.createDoc(beta);
            }

            Snackbar.make(view, "Created alpha and betas", Snackbar.LENGTH_SHORT).show();
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(v -> {
            couchStore.getBetas();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
