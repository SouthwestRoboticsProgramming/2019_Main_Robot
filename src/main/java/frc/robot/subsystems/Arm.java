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
import edu.wpi.first.wpilibj.Timer;

import frc.robot.Log;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.enums.Somatotype;
import frc.robot.sensors.*;

import frc.utilities.*;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;


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

  private final double R = 40 * 48 / 15 ; // # teeth output gear / # teeth input
  private final double maxTorque = 1.41;
  private final double I = .27;

  private static final double minimumBrakeTime = .1;
  private static final Timer timeWhileInRange = new Timer();

  public final ArmEncoder shoulder;
  public final ArmEncoder wrist;

  private final Solenoid brake_solenoid;

  public boolean vacuumsOn = false;
  public boolean brakesOn = false;
  public boolean brakeTiming = false;
  public boolean unbrakeTiming = false;

  private final double minimumMotorInput = .1;

  private double alpha = 0;
  private double omega = 0;
  private double theat = 0;

  final DigitalInput LowerShoulderLimitSwitch = new DigitalInput(RobotMap.LOWER_SHOULDER_LIMIT_SWITCH);
  final DigitalInput UpperShoulderLimitSwitch = new DigitalInput(RobotMap.UPPER_SHOULDER_LIMIT_SWITCH);
  final DigitalInput LowerWristLimitSwitch =    new DigitalInput(RobotMap.LOWER_WRIST_LIMIT_SWITCH);
  final DigitalInput UpperWristLimitSwitch =    new DigitalInput(RobotMap.UPPER_WRIST_LIMIT_SWITCH);
  final DigitalInput LowerExtendLimitSwitch =   new DigitalInput(RobotMap.LOWER_EXTEND_LIMIT_SWITCH);
  final DigitalInput UpperExtendLimitSwitch =   new DigitalInput(RobotMap.UPPER_EXTEND_LIMIT_SWITCH);
  
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
    brake_solenoid = new Solenoid(PCM_ID, BREAK_ID);

  }

  public void setShoulder(double x) {
    x = -x; //I don't know whether we need to do this
    x = Math.min(1 - Calc.val(isShoulderTooHigh()), x);
    x = Math.max(Calc.val(isShoulderTooLow()) - 1, x);
    if (Math.abs(x) < minimumMotorInput) {
      if (unbrakeTiming) {
        unbrakeTiming = false;
        timeWhileInRange.reset();
      }
      if (timeWhileInRange.get() == 0 && !brakeTiming) {
        timeWhileInRange.start();
        brakeTiming = true;
      }
      else if(timeWhileInRange.get()>=minimumBrakeTime && !brakesOn && brakeTiming) {
        brakeShoulder();
        brakesOn = true;
        timeWhileInRange.reset();
      }
    }
    else if (Math.abs(x) > minimumMotorInput) {
      if (brakeTiming) {
        brakeTiming = false;
        timeWhileInRange.reset();
      }
      if (timeWhileInRange.get() == 0 && !unbrakeTiming) {
        timeWhileInRange.start();
        unbrakeTiming = true;
      }
      else if(timeWhileInRange.get()>=minimumBrakeTime && brakesOn && unbrakeTiming) {
        unbrakeShoulder();
        brakesOn = false;
        timeWhileInRange.reset();
      }
      else {
        setShoulder(x);
      }
    }
  }
  public double setWrist(double x) {
    x = -x; // Again I'm uncertain
    x = Math.min(1 - Calc.val(isWristTooHigh()), x);
    x = Math.max(Calc.val(isWristTooLow()) -1, x);
    if (Math.abs(x) < minimumMotorInput) {
      brakeWrist();
      return 0;
    }
    wrist_motor.set(x);
    return x;
  }
  public double setExtention (double x) {
    x = Math.min(1 - Calc.val(isExtendTooHigh()), x);
    x = Math.max(Calc.val(isExtendTooLow()) - 1, x);
    if (Math.abs(x) < minimumMotorInput) {
      brakeExtention();
      return 0;
    }
    extention_motor.set(x);
    return x;
  }
  // public double setShoulderAng(double degrees) {
  //   double m = shoulder.omega();
  //   double b = shoulder.theta();
  //   double t = (degrees - b) / m;
  //   setShoulder(t);
  //   return t;
  // }


  public void brakeShoulder() {
    shoulder_motor.set(0);
    setBrake(true);
  }
  public void unbrakeShoulder() {
    setBrake(false);
  }
  public void brakeWrist() {
    // wrist_motor.stopMotor();
    wrist_motor.set(0);
  }
  public void unBrakeWrist() {}

  public void brakeExtention() {
    extention_motor.set(0);
  }
  public void unBrakeExtention() {}

  public void toggleVacuumMotors() {
    // if (vacuumsOn) {
    //   vacuum_motor_1.set(0);
    //   vacuum_motor_2.set(0);
    // }
    // else {
    //   vacuum_motor_1.set(.75);
    //   vacuum_motor_2.set(.75);
    // }
    // vacuumsOn = !vacuumsOn;
    vacuumsOn = ! vacuumsOn;
    double v = Calc.val(vacuumsOn) * .75;
    vacuum_motor_1.set(v);
    vacuum_motor_2.set(v);
  }
  public void toggleBrake() {
    // Cal changed this bit by adding the line below this commented block and commenting the 5 lines below
    // if (breaksOn) {
    //   break_solenoid.set(true);
    // }
    // else {
    //   break_solenoid.set(false);
    // }
    brakesOn = !brakesOn;
    brake_solenoid.set(brakesOn);
  }
  public void setBrake(boolean state) {
    brake_solenoid.set(state);
  }
  public boolean isShoulderTooHigh() {
    return !UpperShoulderLimitSwitch.get();
	}
	public boolean isShoulderTooLow() {
    return !LowerShoulderLimitSwitch.get();
  }
  public boolean isWristTooHigh() {
    
    return !UpperWristLimitSwitch.get();
	}
	public boolean isWristTooLow() {
    
    return !LowerWristLimitSwitch.get();
  }
  // This one is used differently!
  public boolean isExtendTooHigh() {
    
    return !UpperExtendLimitSwitch.get();
	}
	public boolean isExtendTooLow() {
    
    return !LowerExtendLimitSwitch.get();
  }
  public Duo position() {
    double x;
    double y;
    y = Math.sin(shoulder.theta()); // * extend.theta();
    x = Math.cos(shoulder.theta()); // * extend.theta();
    return new Duo(x,y);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
