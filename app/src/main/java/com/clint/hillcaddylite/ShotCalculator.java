package com.clint.hillcaddylite;

/**
 * Created by Clint on 9/12/2015.
 */

public class ShotCalculator
{

    /*
        x refers to left to right
        y refers to distance down range
        z refers to height

     */

    //This function calculates the distance that the shot will play based on
    //the elevation and distance from the target
    public static Integer calculateDistance(Integer z, Integer y)
    {
        //(10/6) represents adding or subtracting 10 yards per every 6 yards of height
        Float rv = y.floatValue() + z.floatValue() * (10/6);
        return rv.intValue();
    }

    public static Integer getLandingHeight(Double angleToTargDeg, Integer distanceToTarg)
    {
        //note: distanceToTarg is the hypotenuse of the triangle, it should be the straight line distance given by laser range finder
        Long result = Math.round(distanceToTarg * Math.sin(Conversion.degreesToRadians(angleToTargDeg)));
        return result.intValue();

    }

    public static Integer getDistance(Double angleToTargDeg, Integer distanceToTarg)
    {
        //note: distanceToTarg is the hypotenuse of the triangle, it should be the straight line distance given by laser range finder
        Long result = Math.round(distanceToTarg * Math.cos(Conversion.degreesToRadians(angleToTargDeg)));
        return result.intValue();

    }

}
