/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.sensors;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import frc.robot.Interfaces.*;
/**
 * Add your docs here.
 */
public class ArmEncoder implements IEncoder {
    private final Encoder encoder;
    private static final double TICKS_PER_ROTATION = 4096;
    private double GEAR_RATIO;
    private final double CIRCUMFERENCE;
    private final int PORT0;
    private final int PORT1;
    private final boolean REVERSED;
    // private final double sign;

    private int adjust = 0;

    public ArmEncoder(int port0, int port1, boolean reversed, double gearRatio, double circumference, EncodingType type) {
        PORT0 = port0;
        PORT1 = port1;
        GEAR_RATIO = gearRatio;
        CIRCUMFERENCE = circumference;
        REVERSED = reversed;
        encoder = new Encoder(PORT0, PORT1, REVERSED, type);
        // encoder.setDistancePerPulse(1 / TICKS_PER_ROTATION);
    }
    public int get() {
        return encoder.get() + adjust;
    }
    public void set(int num) {
        adjust = num - encoder.get();
    }
    // public void set(double degrees) {
    //     this.set((int) (degrees / GEAR_RATIO));
    // }
    public void reset() {
        encoder.reset();
    }
    public double modifier() {
        return CIRCUMFERENCE / 360d;
    }
    public double theta() { // degrees
        return encoder.get() * GEAR_RATIO;
    }
    public double omega() { // degrees / second
        return encoder.getRate() * GEAR_RATIO;
    }
    
}
