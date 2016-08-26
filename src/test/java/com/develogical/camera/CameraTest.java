package com.develogical.camera;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class CameraTest {

    private static final byte[] IMAGE = new byte[4];
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    Sensor sensor = context.mock(Sensor.class);
    MemoryCard memoryCard = context.mock(MemoryCard.class);
    Camera myCamera = new Camera(sensor, memoryCard);

    @Test
    public void switchingTheCameraOnPowersUpTheSensor() {
        context.checking(new Expectations() {{
            exactly(1).of(sensor).powerUp();
        }});

        myCamera.powerOn();
    }

    @Test
    public void switchingTheCameraOffPowersDownTheSensor() {
        context.checking(new Expectations() {{
            ignoring(sensor).powerUp();
            exactly(1).of(sensor).powerDown();
        }});

        myCamera.powerOn();
        myCamera.powerOff();
    }

    @Test
    public void pressingShutterWhenPowerOffDoesNothing() {
        context.checking(new Expectations() {{
            never(sensor);
            never(memoryCard);
        }});

        myCamera.pressShutter();
    }

    @Test
    public void pressingShutterWithPowerOnCopiesDataFromSensorToMemoryCard() {
        context.checking(new Expectations() {{
            ignoring(sensor).powerUp();
            exactly(1).of(sensor).readData(); will(returnValue(IMAGE));
            exactly(1).of(memoryCard).write(IMAGE);
        }});

        myCamera.powerOn();
        myCamera.pressShutter();
    }

    @Test
    public void ifDataIsCurrentlyBeingWrittenSwitchingCameraOffDoesNotPowerDownSensor() {

        context.checking(new Expectations() {{
            ignoring(sensor).powerUp();
            exactly(1).of(sensor).readData(); will(returnValue(IMAGE));
            exactly(1).of(memoryCard).write(IMAGE);
        }});

        myCamera.powerOn();
        myCamera.pressShutter();
        myCamera.powerOff();

        context.checking(new Expectations() {{
            exactly(1).of(sensor).powerDown();
        }});
        myCamera.writeComplete();
    }

}
