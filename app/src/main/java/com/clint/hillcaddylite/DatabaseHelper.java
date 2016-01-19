package com.clint.hillcaddylite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clint on 9/3/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "hillcaddy";
    private static final String TABLE_PROFILES = "profiles";
    private static final String TABLE_SETTINGS = "settings";

    //profiles table
    private static final String KEY_NAME = "name";
    private static final String KEY_LASTUSED = "lastUsed";

    //clubs table
    private static final String KEY_CLUBID = "clubid";
    private static final String KEY_CLUBNAME = "clubName";
    private static final String KEY_DISTANCE = "distance";

    //settings table
    private static final String KEY_SETTINGS_ROW = "id";
    private static final String KEY_BACKGROUND = "background";
    private static final String KEY_UNITS = "metricUnits";


    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //create profile table if it doesn't already exist
        String command = "CREATE TABLE IF NOT EXISTS "+TABLE_PROFILES+"("+KEY_NAME+" VARCHAR, "+KEY_LASTUSED+" INTEGER);";
        db.execSQL(command);

        //create settings table if it doesn't exist
        createSettingsTable(db);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //put action for upgrading here

    }

    public boolean addProfile(String user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean valid = false;

        //get rid of any spaces
        user = user.replaceAll(" ","");

        if(checkForOriginalProfile(user, db))
        {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, user);
            values.put(KEY_LASTUSED, 0);
            db.insert(TABLE_PROFILES, null, values);

            createClubTable(user, db);

            setLastUsed(user, db);

            valid = true;

        }

        db.close();

        return valid;

    }

    public void createClubTable(String user, SQLiteDatabase db)
    {
        String cmd = "CREATE TABLE IF NOT EXISTS "+user+"_clubs("+KEY_CLUBID+" INTEGER PRIMARY KEY, "+KEY_CLUBNAME+" VARCHAR, "+KEY_DISTANCE+" INTEGER);";
        db.execSQL(cmd);
    }

    public List<Club> loadClubList(String user, SQLiteDatabase db)
    {
        user = user.replaceAll(" ","");

        String command = "SELECT * FROM "+user+"_clubs ;";
        Cursor cursor = db.rawQuery(command, null);

        List<Club> list = new ArrayList<Club>();
        if(cursor.moveToFirst()) {
            do {
                Club club = new Club();
                club.setId(cursor.getInt(0));
                club.setName(cursor.getString(1));
                club.setDistance(cursor.getInt(2));
                list.add(club);

            }while(cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return list;
    }

    public boolean checkForOriginalProfile(String nameToCheck,  SQLiteDatabase db)
    {
        String command = "SELECT "+KEY_NAME+ " FROM "+TABLE_PROFILES+" WHERE "+KEY_NAME+" = '"+nameToCheck+"';";
        Cursor cursor = db.rawQuery(command, null);

        boolean original = true;

        if(cursor.moveToFirst())
        {
            //the name is already there. return false
            original = false;
        }

        cursor.close();

        return original;
    }

    public void removeProfile(String user)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROFILES, KEY_NAME + " = ?", new String[] {user});

        user = user.replaceAll(" ","");
        removeClubTable(user, db);

        db.close();

    }


    public void removeClubTable(String user, SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + user + "_clubs");

    }


    public Profile getLastUsedProfile()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "SELECT "+KEY_NAME+ " FROM "+TABLE_PROFILES+" WHERE "+KEY_LASTUSED+" = 1;";
        Cursor cursor = db.rawQuery(command, null);

        Profile profile = new Profile();
        if(cursor.moveToFirst()) {
            String name = cursor.getString(0);
            profile.setName(name);

            //get all of the clubs for that profile
            List<Club> clubsToLoad = loadClubList(name, db);
            profile.setBag(clubsToLoad);


        }
        else
        {
            //no last used profiles, return profile with name none
            profile.setName("none");

        }

        cursor.close();
        db.close();

        return profile;

    }

    public void setLastUsed(String lastProf, SQLiteDatabase db)
    {
        //set all last used to 0
        String command = "UPDATE "+TABLE_PROFILES+" SET "+KEY_LASTUSED+" = 0;";
        db.execSQL(command);

        //set lastProf to 1
        command = "UPDATE "+TABLE_PROFILES+" SET "+KEY_LASTUSED+" = 1 WHERE "+KEY_NAME+" = '"+lastProf+"';";
        db.execSQL(command);


    }

    public List<String> getAllProfileNames()
    {
        SQLiteDatabase db = getReadableDatabase();
        String command = "SELECT "+KEY_NAME+ " FROM "+TABLE_PROFILES+" ;";
        Cursor cursor = db.rawQuery(command, null);

        List<String> list = new ArrayList<String>();
        if(cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            }while(cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return list;

    }

    public List<Club> getClubList(String user)
    {
        user = user.replaceAll(" ","");

        SQLiteDatabase db = getReadableDatabase();
        String command = "SELECT * FROM "+user+"_clubs ;";
        Cursor cursor = db.rawQuery(command, null);

        List<Club> list = new ArrayList<Club>();
        if(cursor.moveToFirst()) {
            do {
                Club club = new Club();
                club.setId(cursor.getInt(0));
                club.setName(cursor.getString(1));
                club.setDistance(cursor.getInt(2));
                list.add(club);

            }while(cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return list;

    }

    public void setLastUsed(String lastProf)
    {
        SQLiteDatabase db = getWritableDatabase();

        //set all last used to 0
        String command = "UPDATE "+TABLE_PROFILES+" SET "+KEY_LASTUSED+" = 0;";
        db.execSQL(command);

        //set lastProf to 1
        command = "UPDATE "+TABLE_PROFILES+" SET "+KEY_LASTUSED+" = 1 WHERE "+KEY_NAME+" = '"+lastProf+"';";
        db.execSQL(command);

        db.close();

    }

    public void addClub(String user, Club club)
    {
        user = user.replaceAll(" ","");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CLUBNAME, club.getName());
        values.put(KEY_DISTANCE, club.getDistance());

        db.insert(user + "_clubs", null, values);

        db.close();

    }

    public void removeClub(String user, Integer clubID)
    {
        user = user.replaceAll(" ","");

        SQLiteDatabase db = getWritableDatabase();
        String command = "DELETE FROM "+user+"_clubs WHERE "+KEY_CLUBID+"= "+clubID.toString()+";";
        db.execSQL(command);
        db.close();
    }

    public void checkForNullSettings(SQLiteDatabase db)
    {
        String command = "SELECT * FROM "+TABLE_SETTINGS+" ;";
        Cursor cursor = db.rawQuery(command, null);

        if(!cursor.moveToFirst())
        {
            //set default settings
            ContentValues values = new ContentValues();
            values.put(KEY_SETTINGS_ROW, 1);
            values.put(KEY_BACKGROUND, 1);
            values.put(KEY_UNITS, 0);
            db.insert(TABLE_SETTINGS, null, values);

        }

        cursor.close();

    }


    public void saveBackgroundSetting(Integer background)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //this is a check to make sure the settings table has been created
        //createSettingsTable();

        //save last background image to settings
        String command = "UPDATE "+TABLE_SETTINGS+" SET "+KEY_BACKGROUND+" = "+background.toString()+" WHERE "+KEY_SETTINGS_ROW+" = 1 ;";
        db.execSQL(command);
        db.close();

    }

    public Integer getBackgroundFromSettings()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String command = "SELECT "+KEY_BACKGROUND+" FROM "+TABLE_SETTINGS+" WHERE "+KEY_SETTINGS_ROW+" = 1;";
        Cursor cursor = db.rawQuery(command, null);

        Integer rv;

        if(cursor.moveToFirst())
        {
            rv = cursor.getInt(0);
        }
        else
        {
            rv = 1;
        }

        cursor.close();
        db.close();
        return rv;

    }

    public void createSettingsTable(SQLiteDatabase db)
    {
        //create settings table if it doesn't exist
        String command = "CREATE TABLE IF NOT EXISTS "+TABLE_SETTINGS+"("+KEY_SETTINGS_ROW+" INTEGER, "+KEY_BACKGROUND+" INTEGER, "+KEY_UNITS+" INTEGER);";
        db.execSQL(command);
        checkForNullSettings(db);

    }

    public void saveUnitSetting(Integer units)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //this is a check to make sure the settings table has been created
        //createSettingsTable();

        //save last background image to settings
        String command = "UPDATE "+TABLE_SETTINGS+" SET "+KEY_UNITS+" = "+units.toString()+" WHERE "+KEY_SETTINGS_ROW+" = 1 ;";
        db.execSQL(command);
        db.close();

    }

    public Integer getUnitsFromSettings()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String command = "SELECT "+KEY_UNITS+" FROM "+TABLE_SETTINGS+" WHERE "+KEY_SETTINGS_ROW+" = 1;";
        Cursor cursor = db.rawQuery(command, null);

        Integer rv;

        if(cursor.moveToFirst())
        {
            rv = cursor.getInt(0);
        }
        else
        {
            rv = 0;
        }

        cursor.close();
        db.close();
        return rv;

    }

}
