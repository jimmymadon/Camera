package com.develogical.camera;

public class Camera implements WriteListener {

    private final Sensor sensor;
    private final MemoryCard memoryCard;
    private boolean powerOn = false;
    private boolean writing = false;

    public Camera(Sensor sensor, MemoryCard memoryCard) {
        this.sensor = sensor;
        this.memoryCard = memoryCard;
    }

    public void pressShutter() {
        if (powerOn) {
            memoryCard.write(sensor.readData());
            writing = true;
        }
    }

    public void powerOn() {
        powerOn = true;
        sensor.powerUp();
    }

    public void powerOff() {
        if (!writing) {
            sensor.powerDown();
        }
        powerOn = false;
    }

    @Override
    public void writeComplete() {
        writing = false;
        if (!powerOn) {
            sensor.powerDown();
        }
    }
}

