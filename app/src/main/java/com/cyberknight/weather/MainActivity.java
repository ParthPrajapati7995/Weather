package com.cyberknight.weather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

import com.cyberknight.weather.bluetooth.Select;
import com.cyberknight.weather.database.AlarmReceiver;
import com.cyberknight.weather.database.BtpDbSource;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    private boolean flag = false;
    BtpDbSource database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String check = null;
        Intent i = getIntent();
        check = i.getStringExtra("Check");
        if (check != null)
            flag = true;
        ArrayList<OverviewValues> list = new ArrayList<>();
        list.add(new OverviewValues("Temperature",44.5,37.3,47.4));
        list.add(new OverviewValues("Pressure",115.2,110.1,123.1));
        list.add(new OverviewValues("Humidity",52.1,31.3,55.7));
        list.add(new OverviewValues("Wind Speed",12.3,5.2,16.3));
        list.add(new OverviewValues("Temperature",36.4,39.8,26.1));
        list.add(new OverviewValues("Pressure",115.2,110.1,123.1));
        list.add(new OverviewValues("Humidity",52.1,31.3,55.7));
        list.add(new OverviewValues("Wind Speed",12.3,5.2,16.3));

        gridView = (GridView) findViewById(R.id.activity_main_grid_view);
        GridViewCardAdapter adapter = new GridViewCardAdapter(this, list);
        gridView.setAdapter(adapter);

        database = new BtpDbSource(getApplicationContext());
        scheduleAlarm();
    }

    private void scheduleAlarm(){
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMills = System.currentTimeMillis();
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMills, AlarmManager.INTERVAL_HOUR*2, pendingIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_charts:
                Intent i = new Intent(MainActivity.this, LineChartActivity.class);
                startActivity(i);
                return true;
            case R.id.action_connect:
                if (!flag)
                    startActivity(new Intent(MainActivity.this, Select.class));
                else {
                    super.onBackPressed();
                }
                finish();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}