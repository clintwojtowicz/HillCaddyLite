package com.clint.hillcaddylite;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Clint on 8/31/2015.
 */
public class Profile
{
    private String name;
    private List<Club> bag = new ArrayList<Club>();

    public Profile()
    {
        name = "";
    }

    public Profile(String _name)
    {
        name = _name;
    }

    public void setName(String _name)
    {
        name = _name;
    }

    public String getName()
    {
        return name;
    }

    public void addClubToBag(Club newClub)
    {
        bag.add(newClub);
        //TODO: also add the club to the DB
    }

    public void removeClubFromBag(String targetClub)
    {
        ListIterator<Club> iterator = bag.listIterator();

        while(iterator.hasNext())
        {
            Club currentClub = iterator.next();
            if(currentClub.getName() == targetClub)
            {
                iterator.remove();
                break;
            }
        }

    }

    public ArrayList<String> getClubNameList()
    {
        ArrayList<String> clubNames = new ArrayList<String>();
        Iterator<Club> iterator = bag.iterator();

        while (iterator.hasNext())
        {
            clubNames.add(iterator.next().getName());
        }

        return clubNames;

    }

    public void setBag(List<Club> clubs)
    {
        bag = clubs;

    }


    public List<ShotResult> getClubDistances()
    {
        Iterator<Club> iterator = bag.iterator();
        List<ShotResult> distanceList = new ArrayList<ShotResult>();

        while(iterator.hasNext())
        {
            Club tempClub = iterator.next();
            ShotResult shot = new ShotResult();

            shot.setClubName(tempClub.getName());
            shot.setDistance(tempClub.getDistance());
            distanceList.add(shot);

        }

        distanceList = sortShotResultList(distanceList);

        return distanceList;
    }

    public List<Club> getClubList()
    {
        sortBagByDist();
        return bag;
    }

    public void sortBagByDist()
    {
        Collections.sort(bag, new Comparator<Club>() {
            @Override
            public int compare(Club c1, Club c2) {
                return c2.getDistance() - c1.getDistance(); //descending order
            }

        });

    }

    public List<ShotResult> sortShotResultList(List<ShotResult> shotResults)
    {
        Collections.sort(shotResults, new Comparator<ShotResult>() {
            @Override
            public int compare(ShotResult s1, ShotResult s2) {
                return s2.getDistance() - s1.getDistance(); //descending order
            }

        });

        return shotResults;
    }

    public List<ShotResult> getAllDistancesFromTarget(Integer Ydist, Integer Zdist)
    {
        Iterator<Club> iterator = bag.iterator();

        List<ShotResult> distanceList = new ArrayList<ShotResult>();


        while(iterator.hasNext())
        {
            Club tempClub = iterator.next();
            ShotResult shot = new ShotResult();
            //show distance
            Integer distance = ShotCalculator.calculateDistance(Zdist, Ydist);
            shot.setClubName(tempClub.getName());
            shot.setDistance(tempClub.getDistance() - distance);
            distanceList.add(shot);

        }

        distanceList = sortShotResultList(distanceList);

        return distanceList;
    }









}
