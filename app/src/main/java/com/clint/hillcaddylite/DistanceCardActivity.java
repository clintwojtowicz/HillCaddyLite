package com.clint.hillcaddylite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

public class DistanceCardActivity extends AppCompatActivity {

    GlobalVars globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_card);
        globals = ((GlobalVars) getApplicationContext());
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume()
    {
        setBackgroundImage();
        this.showDistanceCard();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_distance_card, menu);
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

    public void showDistanceCard() {
        Profile profile = globals.getCurrentProfile();
        List<ShotResult> distanceList = profile.getClubDistances();

        TableLayout distanceTable = (TableLayout) findViewById(R.id.distanceCard_table);
        distanceTable.removeAllViews();

        TableRow header = new TableRow(this);

        TextView tv0 = new TextView(this);
        tv0.setText("Club       ");
        tv0.setTextColor(Color.BLACK);
        tv0.setTypeface(null, Typeface.BOLD);
        tv0.setGravity(Gravity.LEFT);
        tv0.setTextSize(22);
        header.addView(tv0);

        TextView tv1 = new TextView(this);
        //determine whether to use metrics or yards for text
        if(!globals.getDisplayUnits()) tv1.setText(R.string.distance_yds_table_range_text);
        else tv1.setText(R.string.distance_m_table_range_text);
        tv1.setTextColor(Color.BLACK);
        tv1.setTypeface(null, Typeface.BOLD);
        tv1.setGravity(Gravity.LEFT);
        tv1.setTextSize(22);
        header.addView(tv1);

        distanceTable.addView(header);

        Iterator<ShotResult> iterator = distanceList.iterator();

        while (iterator.hasNext()) {
            ShotResult shot = iterator.next();

            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setText(shot.getClubName());
            t1v.setTextColor(Color.BLACK);
            t1v.setGravity(Gravity.LEFT);
            t1v.setTextSize(18);
            tbrow.addView(t1v);

            TextView t2v = new TextView(this);
            //check to see whether distance should be in yards or meters
            if(!globals.getDisplayUnits()) t2v.setText(shot.getDistance().toString());
            else {
                t2v.setText(Conversion.yardToMeterRnd(shot.getDistance()).toString());
            }
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.LEFT);
            t2v.setTextSize(18);
            tbrow.addView(t2v);

            distanceTable.addView(tbrow);
        }

    }

    public void showSettingsView(MenuItem view)
    {
        //show Settings view
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);

    }

    private void setBackgroundImage()
    {
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.distanceCard_background);

        if(globals.getBackgroundSetting()) {
            layout.setBackgroundResource(R.drawable.fallbrook_cropped_opaque);
        }
        else{
            layout.setBackgroundColor(Color.WHITE);
        }

    }


}
