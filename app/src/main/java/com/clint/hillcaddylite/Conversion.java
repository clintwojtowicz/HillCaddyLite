package com.clint.hillcaddylite;

/**
 * Created by Clint on 9/1/2015.
 */
public class Conversion
{
    public static Double mphToMs(Double mph)
    {
        return mph * .44704;
    }

    public static Double msToMph(Double ms)
    {
        return ms / .44704;
    }

    public static Double yardToMeter(Double yd)
    {
        return yd * .9144;
    }

    public static Integer yardToMeterRnd(Integer yd)
    {
        Double meters = yd * .9144;
        return meters.intValue();
    }

    public static Double meterToYard(Double m)
    {
        return m / .9144;
    }

    public static Integer meterToYardRnd(Integer m)
    {
        Double yards = m / .9144;
        return yards.intValue();
    }

    public static Double degreesToRadians(Double deg)
    {
        return deg * Constants.PI / 180.0;
    }

    public static Double radiansToDegrees(Double rad)
    {
        return rad * 180.0 / Constants.PI;
    }

    public static Double rpmToRadps(Double rpm)
    {
        return rpm * Constants.PI / 30.0;
    }

    public static Double radpsToRpm(Double radps)
    {
        return radps * 30.0 / Constants.PI;
    }

    public static Integer celsiusToFahrenheit(Float tempC)
    {
        return (int)Math.round(tempC * 1.8 + 32);

    }

    public static Float fahrenheitToCelsius(Integer tempF)
    {
        return (float)((tempF - 32) / 1.8);
    }

    public static Integer boolToInt(Boolean b)
    {
        return b ? 1 : 0;
    }

    public static Boolean intToBool(Integer i)
    {
        return i != 0;
    }


}
