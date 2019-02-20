/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.Log;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.enums.Somatotype;
import frc.robot.sensors.*;

/**
 * Add your docs here.
 */
public class Arm extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  
  private static final int WRIST_ID = 6;
  private static final int EXTEND_ID = 8;
  private static final int SHOULDER_ID = 7;

  private static final int PCM_ID = 37;
  private static final int BREAK_ID = 4;

  private static final int VACUUM_1 = 0;
  private static final int VACUUM_2 = 1;

  private final WPI_TalonSRX shoulder_motor;
  private final WPI_TalonSRX wrist_motor;
  private final WPI_TalonSRX extention_motor;
  private final WPI_VictorSPX vacuum_motor_1;
  private final WPI_VictorSPX vacuum_motor_2;

  public final ArmEncoder shoulder;
  public final ArmEncoder wrist;

  private final Solenoid break_solenoid;

  public boolean vacuumsOn = false;
  public boolean breaksOn = false;

  
  
  // WPI_VictorSPX vacuum1;
  // WPI_VictorSPX vacuum2;

  private double sum_Shoulder = 0;

  public Arm() {
    extention_motor = new WPI_TalonSRX(EXTEND_ID);
    shoulder_motor = new WPI_TalonSRX(SHOULDER_ID);
    wrist_motor = new WPI_TalonSRX(WRIST_ID);
    
    shoulder = new ArmEncoder(shoulder_motor,false, 1, 32d); // 32 inches
    wrist = new ArmEncoder(wrist_motor, false, 1, 5);
    vacuum_motor_1 = new WPI_VictorSPX(VACUUM_1);
    vacuum_motor_2 = new WPI_VictorSPX(VACUUM_2);
    break_solenoid = new Solenoid(PCM_ID, BREAK_ID);

  }

  public double setShoulder(double x) {
    shoulder_motor.set(x);
    return x;
  }
  public double setWrist(double x) {
    wrist_motor.set(x);
    return x;
  }
  public double setExtention (double x) {
    extention_motor.set(x);
    return x;
  }
  public void brakeShoulder() {
    shoulder_motor.stopMotor();
  }
  public void brakeWrist() {
    wrist_motor.stopMotor();
  }
  public void brakeExtention() {
    extention_motor.stopMotor();
  }
  public void toggleVacuumMotors() {
    if (vacuumsOn) {
      vacuum_motor_1.set(0);
      vacuum_motor_2.set(0);
    }
    else {
      vacuum_motor_1.set(.75);
      vacuum_motor_2.set(.75);
    }
    vacuumsOn = !vacuumsOn;
  }
  public void toggleBreak() {
    if (breaksOn) {
      break_solenoid.set(true);
    }
    else {
      break_solenoid.set(false);
    }
    breaksOn = !breaksOn;
  }
  public void setBreak(boolean state) {
    break_solenoid.set(state);
  }


  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
