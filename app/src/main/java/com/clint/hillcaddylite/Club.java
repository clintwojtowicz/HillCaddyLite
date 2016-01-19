package com.clint.hillcaddylite;

/**
 * Created by Clint on 8/31/2015.
 */
public class Club
{
    private Integer id;
    private String name;
    private Integer distance;  //in meters

    public Club()
    {
        name = "";
    }

    public Club(String _name)
    {
        name = _name;
    }

    public Club(String theName, Integer dist)
    {
        name = theName;
        distance = dist;
    }

    public void setName(String _name)
    {
        name = _name;
    }

    public String getName()
    {
        return name;
    }


    public void setDistance(Integer dist)
    {
        distance = dist;
    }

    public Integer getDistance()
    {
        return distance;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer theId)
    {
        id = theId;
    }

}
