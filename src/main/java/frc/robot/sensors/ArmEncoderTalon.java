/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.sensors;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import frc.robot.Interfaces.*;
/**
 * Add your docs here.
 */
public class ArmEncoderTalon implements IEncoder {
    private final WPI_TalonSRX m_talon;
    private static final double TICKS_PER_ROTATION = 4096;
    private final int sign;
    private double GEAR_RATIO;
    private final double CIRCUMFERENCE;
    private int adjust = 0;

    public ArmEncoderTalon(WPI_TalonSRX talon, boolean reversed, double gearRatio, double circumference) {
        this.m_talon = talon;
        sign = reversed ? -1 : 1;
        this.GEAR_RATIO = gearRatio;
        CIRCUMFERENCE = circumference;
    }
    public int get() {
        return sign * m_talon.getSelectedSensorPosition(0) + adjust;
    }
    public int raw() {
        return m_talon.getSelectedSensorPosition(0);
    }
    public void set(int num) {
        adjust = num - sign * raw();
    }
    // public void set(double angle) {
    //     this.set((int) (this.theta() - angle) * TICKS_PER_ROTATION / sign /GEAR_RATIO / 360d);
    // }
    public void reset() {
        m_talon.set(0);
    }
    public double modifier() {
        return CIRCUMFERENCE / 360d;
    }
    public double theta() { // degrees
        return (double) get() / (double) TICKS_PER_ROTATION  * GEAR_RATIO * 360d;
    }
    public double omega() { // degrees / second
        return m_talon.getSelectedSensorVelocity(0) / TICKS_PER_ROTATION  * GEAR_RATIO * 360d;
    }
    
}
