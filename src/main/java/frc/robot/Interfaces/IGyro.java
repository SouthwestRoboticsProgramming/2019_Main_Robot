/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Interfaces;

import frc.robot.sensors.ADIS16448_IMU;
import java.util.function.Function;
/*
 * Add your docs here.
 */
public interface IGyro {
    final ADIS16448_IMU gyro = new ADIS16448_IMU();

    default double modifier(Axis axis) {
        return 1;
    }
    default void calibrate() {
        gyro.calibrate();
    }

    default double getAng() {
        return getAng(align(Axis.Z));
    }
    default double getAng(Axis axis) {
        double ang;
        switch(align(axis)) {
            case X : ang = gyro.getAngleX(); break;
            case Y : ang = gyro.getAngleY(); break;
            case Z : ang = gyro.getAngleZ(); break;
            default: ang = gyro.getAngle(); break; 
        }
        return modifier(align(axis)) * ang;
    }

    default double getRate() {
        return getRate(align(Axis.Z));
    }
    default double getRate(Axis axis) {
        double rate;
        switch(align(axis)) {
            case X : rate = gyro.getRateX(); break;
            case Y : rate = gyro.getRateY(); break;
            case Z : rate = gyro.getRateZ(); break;
            default: rate = gyro.getRate() ; break;
        }
        return modifier(align(axis)) * rate;
    }

    default double getAccel() {
        return getAccel(align(Axis.Z));
    }
    default double getAccel(Axis axis) {
        double accel;
        switch(align(axis)) {
            case X : accel = gyro.getAccelX(); break;
            case Y : accel = gyro.getAccelY(); break;
            case Z : accel = gyro.getAccelZ(); break;
            default: accel = gyro.getAccelZ(); break;
        }
        return modifier(align(axis)) * accel;
    }

    default Axis align(Axis axis) {
        return axis;
    }
    public enum Axis {
        X, // roll
        Y, // pitch
        Z  // yaw
    }
}
