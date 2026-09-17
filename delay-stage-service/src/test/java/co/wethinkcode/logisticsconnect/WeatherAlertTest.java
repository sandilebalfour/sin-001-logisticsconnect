package co.wethinkcode.logisticsconnect;


import org.junit.Test;
import org.junit.jupiter.api.*;

import static org.junit.Assert.*;


public class WeatherAlertTest {

    @Test
    public void testNoArg(){
        WeatherAlert alert = new WeatherAlert();
        alert.setLevel(1);
        assertEquals(Code.GREEN, alert.getCode());
    }
}
