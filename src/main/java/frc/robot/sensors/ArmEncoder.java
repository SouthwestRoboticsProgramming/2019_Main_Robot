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
public class ArmEncoder implements IEncoder {
    private final WPI_TalonSRX m_talon;
    private static final double TICKS_PER_ROTATION = 1024;
    private final double sign;
    private final double GEAR_RATIO;
    private final double CIRCUMFERENCE;

    private double previous;

    public ArmEncoder(WPI_TalonSRX talon, boolean reversed, double gearRatio, double circumference) {
        this.m_talon = talon;
        sign = reversed ? -1 : 1;
        this.GEAR_RATIO = gearRatio;
        CIRCUMFERENCE = circumference;
    }
    public int get() {
        return m_talon.getSelectedSensorPosition(0);
    }
    public void set(int num) {
        m_talon.setSelectedSensorPosition(num, 0, 2000);
    }
    public void reset() {
        m_talon.set(0);
    }
    public double modifier() {
        return CIRCUMFERENCE / 360d;
    }
    public double theta() {
        return m_talon.get() / TICKS_PER_ROTATION * sign * GEAR_RATIO * 360d;
    }
    public double omega() {
        return m_talon.getSelectedSensorVelocity(0) / TICKS_PER_ROTATION * sign * GEAR_RATIO * 360d;
    }
    
}
