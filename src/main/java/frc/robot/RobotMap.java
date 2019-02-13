/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
  // Official distance unit of this team is the inch
  
  // physical charictarisitics of robot
  public static final double MASS = 80; // in pounds
	public static final double WHEEL_DISTANCE = 22; //distance between wheels, inches


  // ids
	public static final int LEFT_REAR_MOTOR = 0;
	public static final int RIGHT_REAR_MOTOR = 1;
	public static final int RIGHT_FRONT_MOTOR = 2;
	public static final int LEFT_FRONT_MOTOR = 3;
	public static final int LIFTER_MOTOR = 4;
	
	public static final int GRABBER_SOLENOID_CAN = 5;
	public static final int GRABBER_SOLENOID_PCM = 7;
	
	public static final int LIFTER_UPPER_SWITCH = 3;
	public static final int LIFTER_LOWER_SWITCH = 2;
	
	public static final int GYRO_PORT_NUM = 1;
	

}
