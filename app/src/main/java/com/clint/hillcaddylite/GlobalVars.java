package com.clint.hillcaddylite;

import android.app.Application;

import java.util.List;

/**
 * Created by Clint on 9/1/2015.
 */
public class GlobalVars extends Application
{
    private Profile currentProfile = new Profile();
    private Boolean backgroundImage = true;
    private Boolean metricUnitsDisplay = false;
    private DatabaseHelper db = new DatabaseHelper(this);

    public void setCurrentProfile(Profile profile)
    {
        this.currentProfile = profile;
    }

    public void setCurrentProfileWithName(String name)
    {
        currentProfile.setName(name);

        List<Club> clubs = db.getClubList(name);
        this.currentProfile.setBag(clubs);

        db.setLastUsed(name);

    }

    public Profile getCurrentProfile()
    {
        if ((currentProfile == null)||(currentProfile.getName().equals("")))
        {
            this.currentProfile = db.getLastUsedProfile();
        }

        return this.currentProfile;

    }

    public DatabaseHelper getDB()
    {
        return db;
    }


    public void setBackgroundSetting(Boolean setting)
    {
        backgroundImage = setting;
        db.saveBackgroundSetting(Conversion.boolToInt(setting));
    }

    public Boolean getBackgroundSetting()
    {
        if(backgroundImage == null)
        {
            this.backgroundImage = Conversion.intToBool(db.getBackgroundFromSettings());
        }

        return this.backgroundImage;
    }

    public void setDisplayUnits(Boolean setting)
    {
        metricUnitsDisplay = setting;
        db.saveUnitSetting(Conversion.boolToInt(setting));
    }

    public Boolean getDisplayUnits()
    {
        if(metricUnitsDisplay == null)
        {
            this.metricUnitsDisplay = Conversion.intToBool(db.getUnitsFromSettings());
        }

        return this.metricUnitsDisplay;
    }

}
