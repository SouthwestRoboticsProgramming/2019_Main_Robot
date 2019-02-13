/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.sensors;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.util.WPILibVersion;
import frc.robot.Interfaces.IEncoder;
import frc.robot.Log;
/**
 * Add your docs here.
 */
public class WheelEncoder implements IEncoder {
    private WPI_TalonSRX m_talon;
	private static final double WHEEL_DIAMETER = 6;
    private static final double WHEEL_CIRCUMFRENCE = WHEEL_DIAMETER * Math.PI;
    private static final double GEAR_RATIO = Math.PI;
    private static final int TICKS_PER_ROTATION = 1024;
    private double sign;

    public WheelEncoder(WPI_TalonSRX talon, boolean reverse) {
        this.m_talon = talon;
        sign = reverse ? -1 : 1;

    }
    public void reset() {
        int startingDistance = 0;
        startingDistance = m_talon.getSelectedSensorPosition(0);
        String msg ="Wheel circumference " + String.valueOf(WHEEL_CIRCUMFRENCE); 
        Log.info(msg);
        m_talon.setSelectedSensorPosition(0, 0, 2000);
        startingDistance = m_talon.getSelectedSensorPosition(0);
        msg = "Verifying encoder reset. Starting distance " + String.valueOf(startingDistance); 
        Log.info(msg);
    }
    public double getDistance() {
        //int tickDifference = m_talon.getSelectedSensorPosition(0) - startingDistance;
        //return sign * tickDifference/TICKS_PER_ROTATION * WHEEL_CIRCUMFRENCE / 10.71; // returns position in inches
        return sign * (m_talon.getSelectedSensorPosition(0) / TICKS_PER_ROTATION) * WHEEL_CIRCUMFRENCE * GEAR_RATIO; 
    }
}
