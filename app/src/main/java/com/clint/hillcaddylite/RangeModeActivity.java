package com.clint.hillcaddylite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.Iterator;
import java.util.List;

public class RangeModeActivity extends AppCompatActivity {

    GlobalVars globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range_mode);
        globals = ((GlobalVars)getApplicationContext());
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_range_mode, menu);
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

    @Override
    protected void onResume()
    {
        setBackgroundImage();
        showClubs();
        super.onResume();
    }

    public void addClub(View view)
    {
        EditText clubName = (EditText)findViewById(R.id.clubName_range_editText);
        String cName = "";
        EditText dist = (EditText)findViewById(R.id.distance_range_editText);
        Integer distance = 0;
        Boolean valid = true;

        try {
            cName = clubName.getText().toString();
            distance = Integer.parseInt(dist.getText().toString());
        }
        catch (Exception e) {
            this.showMessage("The club name and distance can not be empty");
            valid = false;
        }

        if (valid && !cName.isEmpty()) {

            Club club = new Club(cName, distance);

            //get the current profile and add the club to their bag. Also add to DB
            Profile currentUser = globals.getCurrentProfile();
            currentUser.addClubToBag(club);

            DatabaseHelper db = globals.getDB();
            db.addClub(currentUser.getName(), club);

            clearTextEdits();

            //update the list of clubs and distances
            showClubs();
        }


    }

    public void showMessage(String m)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Error");
        builder.setMessage(m);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.show();
    }

    public void clearTextEdits()
    {
        EditText distEditText = (EditText) findViewById(R.id.distance_range_editText);
        distEditText.getText().clear();

        EditText clubEditText = (EditText) findViewById(R.id.clubName_range_editText);
        clubEditText.getText().clear();


    }

    public void showSettingsView(MenuItem view)
    {
        //show Settings view
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);

    }

    private void setBackgroundImage()
    {
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.rangeMode_background);

        if(globals.getBackgroundSetting()) {
            layout.setBackgroundResource(R.drawable.colorado_cropped_opaque);
        }
        else{
            layout.setBackgroundColor(Color.WHITE);
        }

    }

    public void showClubs()
    {
        DatabaseHelper db = globals.getDB();
        Profile profile = globals.getCurrentProfile();
        String currentUser = profile.getName();

        List<Club> shots = db.getClubList(currentUser);
        profile.setBag(shots);

        shots = profile.getClubList();

        TableLayout shotTable = (TableLayout) findViewById(R.id.clubs_table);
        shotTable.removeAllViews();

        TableRow header = new TableRow(this);

        TextView tv0 = new TextView(this);
        tv0.setText("Club Name     ");
        tv0.setTextColor(Color.BLACK);
        tv0.setTextSize(20);
        header.addView(tv0);

        TextView tv1 = new TextView(this);
        tv1.setText("Distance    ");
        tv1.setTextColor(Color.BLACK);
        tv1.setTextSize(20);
        header.addView(tv1);

        shotTable.addView(header);

        Iterator<Club> iterator = shots.iterator();

        while (iterator.hasNext()) {
            Club club = iterator.next();

            TableRow tbrow = new TableRow(this);

            TextView t1v = new TextView(this);
            t1v.setText(club.getName());
            t1v.setTextColor(Color.BLACK);
            t1v.setGravity(Gravity.LEFT);
            t1v.setTextSize(12);
            tbrow.addView(t1v);

            TextView t2v = new TextView(this);
            t2v.setText(club.getDistance().toString());
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.LEFT);
            t2v.setTextSize(12);
            tbrow.addView(t2v);

            //set button id to club id so we can use it to remove the shot
            //this is okay because android assigned ids start at 0x00FFFFFF, there will never be that many shots
            Button button = new Button(this);
            button.setText("Remove");
            button.setId(club.getId());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeSelectedClub(v);
                }
            });
            tbrow.addView(button);

            shotTable.addView(tbrow);
        }

    }

    public void removeSelectedClub(View view)
    {
        DatabaseHelper db = globals.getDB();
        Profile profile = globals.getCurrentProfile();
        String currentUser = profile.getName();

        db.removeClub(currentUser, view.getId());

        showClubs();

    }


}
