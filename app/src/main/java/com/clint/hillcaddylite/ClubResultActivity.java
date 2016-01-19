package com.clint.hillcaddylite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

public class ClubResultActivity extends AppCompatActivity {

    GlobalVars globals;
    private Integer Ydist;
    private Integer Zdist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_result);

        Intent intent = getIntent();
        Zdist = intent.getIntExtra(CourseModeActivity.EXTRA_ZDIST, 0);
        Ydist = intent.getIntExtra(CourseModeActivity.EXTRA_YDIST, 0);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        globals = ((GlobalVars)getApplicationContext());

    }

    @Override
    protected void onResume()
    {
        setBackgroundImage();
        showShotResults(Ydist, Zdist);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_club_result, menu);
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

    private void showShotResults(Integer ydist, Integer zdist)
    {
        Profile profile = globals.getCurrentProfile();
        List<ShotResult> distanceList = profile.getAllDistancesFromTarget(ydist, zdist);

        TableLayout distanceTable = (TableLayout) findViewById(R.id.resultCard_table);
        //make sure the table is clear before making changes
        distanceTable.removeAllViews();

        TableRow header = new TableRow(this);

        TextView tv0 = new TextView(this);
        tv0.setText("Club       ");
        tv0.setTextColor(Color.BLACK);
        tv0.setTypeface(null, Typeface.BOLD);
        tv0.setGravity(Gravity.LEFT);
        tv0.setTextSize(20);
        header.addView(tv0);

        TextView tv1 = new TextView(this);
        //determine whether to use metrics or yards for text
        if(!globals.getDisplayUnits()) tv1.setText(R.string.distance_yds_table_result_text);
        else tv1.setText(R.string.distance_m_table_result_text);
        tv1.setTextColor(Color.BLACK);
        tv1.setTypeface(null, Typeface.BOLD);
        tv1.setGravity(Gravity.LEFT);
        tv1.setTextSize(20);
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

        ShotResult closestShot = findClosestShot(distanceList);
        //decide whether the closest club dialog should show yards or meters for results
        if(!globals.getDisplayUnits()) showClosestClubDialogYards(closestShot, ydist, zdist);
        else showClosestClubDialogMeters(closestShot, ydist, zdist);

    }

    public void showClosestClubDialogYards(ShotResult shot, Integer yDist, Integer zDist)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("HillCaddy Says...");

        Integer dist = ShotCalculator.calculateDistance(yDist, zDist);

        String shotDist = "The shot will play "+ dist.toString() +" yards.\n";

        if (shot.getDistance() < 0) {
            Integer shotAbs = Math.abs(shot.getDistance());
            builder.setMessage(shotDist + "The closest club is your " + shot.getClubName() + ". It will land " + shotAbs.toString() + " yds short of the target");
        }
        else if (shot.getDistance() < 1000)
        {
            builder.setMessage(shotDist + "The closest club is your " + shot.getClubName() + ". It will land " + shot.getDistance().toString() + " yds past the target");
        }
        else
        {
            builder.setMessage(shotDist + "No clubs available, go to range mode and check your clubs and distances for a club recommendation.");
        }

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){}
        });

        builder.show();
    }

    public void showClosestClubDialogMeters(ShotResult shot, Integer yDist, Integer zDist)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("HillCaddy Says...");

        Integer dist = ShotCalculator.calculateDistance(yDist, zDist);
        //convert distance to meters
        dist = Conversion.yardToMeterRnd(dist);

        String shotDist = "The shot will play "+ dist.toString() +" meters.\n";

        if (shot.getDistance() < 0) {
            Integer shotAbs = Math.abs(Conversion.yardToMeterRnd(shot.getDistance()));
            builder.setMessage(shotDist + "The closest club is your " + shot.getClubName() + ". It will land " + shotAbs.toString() + " m short of the target");
        }
        else if (shot.getDistance() < 1000)
        {
            builder.setMessage(shotDist + "The closest club is your " + shot.getClubName() + ". It will land " + shot.getDistance().toString() + " m past the target");
        }
        else
        {
            builder.setMessage(shotDist + "No clubs available, go to range mode and check your clubs and distances for a club recommendation.");
        }

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){}
        });

        builder.show();
    }

    private ShotResult findClosestShot(List<ShotResult> shots)
    {
        ShotResult closest = new ShotResult();
        closest.setDistance(1000);

        Iterator<ShotResult> iterator = shots.iterator();

        while (iterator.hasNext())
        {
            ShotResult shotToCheck = iterator.next();
            if(Math.abs(shotToCheck.getDistance()) < Math.abs(closest.getDistance()))
            {
                closest = shotToCheck;
            }

        }

        return closest;

    }

    public void showSettingsView(MenuItem view)
    {
        //show Settings view
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);

    }

    private void setBackgroundImage()
    {
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.clubResult_background);

        if(globals.getBackgroundSetting()) {
            layout.setBackgroundResource(R.drawable.fallbrook_cropped_opaque);
        }
        else{
            layout.setBackgroundColor(Color.WHITE);
        }

    }

}
