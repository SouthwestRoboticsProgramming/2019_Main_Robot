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
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import frc.robot.Log;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.enums.Somatotype;
import frc.robot.sensors.*;
import frc.utilities.Calc;
import frc.robot.commands.ArmCtrl;

import frc.utilities.*;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;


/**
 * Add your docs here.
 */
public class Arm extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  
  private static final int WRIST_ID = 6; // used to be 6
  private static final int EXTEND_ID = 8 ;// before that 47
  private static final int SHOULDER_ID = 7; // 7

  private static final int PCM_ID = 37;
  private static final int BREAK_ID = 4; // used to be 4

  private final WPI_TalonSRX shoulder_motor;
  private final WPI_TalonSRX wrist_motor;
  private final WPI_TalonSRX extention_motor;


  // private final double R = 40 * 48 / 15 ; // # teeth output gear / # teeth input
  // private final double maxTorque = 1.41;
  // private final double I = .27;

  private static final double minimumBrakeTime = .1;
  private static final Timer timeWhileInRange = new Timer();

  public final ArmEncoderTalon shoulder;
  // public final ArmEncoder wrist;
  public final ArmEncoder extend;

  private final Solenoid brake_solenoid;

  public boolean vacuumsOn = false;
  public boolean brakesOn = false;
  public boolean brakeTiming = false;
  public boolean unbrakeTiming = false;

  private final double minimumMotorInput = .05;
  private final double minimumShoulderMotorInput = .07;

  private static boolean isLowerShoulderSwitchEnabled = true;
  private static boolean isUpperShoulderSwitchEnabled = true;
  private static boolean isLowerExtendSwitchEnabled = true;
  private static boolean isUpperExtendSwitchEnabled = true;
  private static boolean isLowerWristSwitchEnabled = true;
  private static boolean isUpperWristSwitchEnabled = true;

  // private double alpha = 0;
  // private double omega = 0;
  // private double theta = 0;

  final DigitalInput LowerShoulderLimitSwitch = new DigitalInput(RobotMap.LOWER_SHOULDER_LIMIT_SWITCH);
  final DigitalInput UpperShoulderLimitSwitch = new DigitalInput(RobotMap.UPPER_SHOULDER_LIMIT_SWITCH);
  final DigitalInput LowerWristLimitSwitch =    new DigitalInput(RobotMap.LOWER_WRIST_LIMIT_SWITCH);
  final DigitalInput UpperWristLimitSwitch =    new DigitalInput(RobotMap.UPPER_WRIST_LIMIT_SWITCH);
  final DigitalInput LowerExtendLimitSwitch =   new DigitalInput(RobotMap.LOWER_EXTEND_LIMIT_SWITCH);
  final DigitalInput UpperExtendLimitSwitch =   new DigitalInput(RobotMap.UPPER_EXTEND_LIMIT_SWITCH);
  
  // WPI_VictorSPX vacuum1;
  // WPI_VictorSPX vacuum2;

  public final int SHOULDER_MIN_COUNT = -152;
  public final int SHOULDER_MAX_COUNT = 1412;
  public final int SHOULDER_START_COUNT = 769;
  // final int WRIST_MAX_ANG = ;
  // final int WRIST_MIN_ANG = ;
  public final int EXTEND_MIN_COUNT = 0;
  public final int EXTEND_MAX_COUNT = 126879;

  private double previousShoulder;

  

  // public static boolean isManualControl = true;

  public Arm() {
    extention_motor = new WPI_TalonSRX(EXTEND_ID);
    extention_motor.setInverted(true);
    shoulder_motor = new WPI_TalonSRX(SHOULDER_ID);
    wrist_motor = new WPI_TalonSRX(WRIST_ID);
    wrist_motor.setInverted(false);
    shoulder = new ArmEncoderTalon(shoulder_motor,true, /*5267d*/ 1d,/*32d*/ 1d); // 32 inches
    // wrist = new ArmEncoder(5,6,true,(1/188d) * 52.5,1, EncodingType.k4X);
    extend = new ArmEncoder(8,9,false,(1/36d),1, EncodingType.k4X);



    brake_solenoid = new Solenoid(PCM_ID, BREAK_ID);

  }

  public void calibrate() {
    // setExtention(1);
    // setShoulder(-.2);
    // while(!isShoulderTooHigh()) {
      //Log.info("In process of calibrating shoulder");
      // setShoulder(.2);
    // }
    // shoulder.set(SHOULDER_MAX_COUNT * (int) Calc.val(isShoulderTooHigh()) + SHOULDER_MIN_COUNT * (int) Calc.val(isShoulderTooLow()));
    if (isShoulderTooHigh()) {
      shoulder.set(SHOULDER_MAX_COUNT);
      // Log.info("Calibrating shoulder to max");
    }
    else if (isShoulderTooLow()) {
      shoulder.set(SHOULDER_MIN_COUNT);
      // Log.info("Calibrating shoulder to min");
    }
    if (isExtendTooHigh()) {
      extend.set(EXTEND_MAX_COUNT);
      // Log.info("Calibrating extend to max");
    }
    else if (isExtendTooLow()) {
      extend.set(EXTEND_MIN_COUNT);
      // Log.info("Calibrating extend to min");
    }
    // if (isShoulderTooHigh() || isShoulderTooLow() || isExtendTooHigh() || isExtendTooLow()) {
    //   Log.info("Calibrating");
    // }
    // Log.info("Shoulder calibrated");
    // for (int i=0; i<5; i++) {
    //   setExtention(.2);
    // }
    // Log.info("Arm extended");
    // while (!isExtendTooLow()) {
    //   //Log.info("In process of calibrating extension");
    //   setExtention(-.2);
    // }
    // if (!isExtendTooLow()) {
    //   Log.info("Extend is too low");
    // }
    // extend.set(EXTEND_MIN_COUNT);
    //setExtendAng(20000);
    
  }

  public double setShoulder(double x) {
    // x = -x; //I don't know whether we need to do this
    x = Math.min(1 - Calc.val(isShoulderTooHigh()), x);
    x = Math.max(Calc.val(isShoulderTooLow()) - 1, x);
    // Log.info("isShoulderTooHigh: "+isShoulderTooHigh());
    // Log.info("isShoulderTooLow: " + isShoulderTooLow());
    //  if (Math.abs(x) < minimumMotorInput) {
    //     if (unbrakeTiming) {
    //       unbrakeTiming = false;
    //       timeWhileInRange.reset();
    //     }
    //     if (timeWhileInRange.get() == 0 && !brakeTiming) {
    //       timeWhileInRange.start();
    //       brakeTiming = true;
    //     }
    //     else if(timeWhileInRange.get()>=minimumBrakeTime + 100 && !brakesOn && brakeTiming) {
    //       brakeShoulder();
    //       //timeWhileInRange.reset();
    //     }
    //   }
    //   else  {
    //     if (brakeTiming) {
    //       brakeTiming = false;
    //       timeWhileInRange.reset();
    //    }
    //    if (timeWhileInRange.get() == 0 && !unbrakeTiming) {
    //      timeWhileInRange.start();
    //      unbrakeTiming = true;
    //    }
    //    else if(timeWhileInRange.get()>=minimumBrakeTime && brakesOn && unbrakeTiming) {
    //      unbrakeShoulder();
    //      //timeWhileInRange.reset();
    //    }
    //    if (!brakesOn) {
    //     this.shoulder_motor.set(x);
    //    }
    //  }
//    if (Math.abs(x) < minimumShoulderMotorInput && Math.abs(previousShoulder) < minimumShoulderMotorInput
     if (Math.abs(x) < minimumShoulderMotorInput ) {
       brakeShoulder();
       previousShoulder = 0;
       return 0;
     }
     else {
       unbrakeShoulder();
       shoulder_motor.set(x);
       previousShoulder = x;
       return x;
     }
  }
  public double setWrist(double x) { // Again I'm uncertain
    x = Math.min(1 - Calc.val(isWristTooHigh()), x);
    x = Math.max(Calc.val(isWristTooLow()) -1, x);
    // Log.info("WRIST " + x);
    wrist_motor.set(x);
    return x;
    // Log.info("IS WRIST TO LOW" + isWristTooLow());
    // Log.info("IS WRIST TO HIGH" + isWristTooHigh());
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
  public double setShoulderAng(double degrees) {
    double chngAng = degrees - shoulder.theta();
    // double t = Math.abs(chngAng / shoulder.omega() / 10d) * Math.signum(chngAng);
    double t = chngAng / 40d;
    return setShoulder(Math.min(Math.abs(t), 0.5d) * Math.signum(t));
  }
  // public double setWristAng(double degrees) {
  //   double chngAng = degrees - wrist.theta();
  //   double t = chngAng * chngAng / 20d / 20d;
  //   return setWrist(Math.min(Math.abs(t),0.5) * Math.signum(t)) ;
  // }
  public double setExtendCount(int counts) {
    double chngAng = counts - extend.get();
    double t = chngAng / 20000d;
    return setExtention(Math.min(Math.abs(t),0.4) * Math.signum(t)) ;
  }
  public void checkEncoders() {
    if (isShoulderTooHigh()) {
      shoulder.set(SHOULDER_MAX_COUNT);
    }
    if (isShoulderTooLow()) {
      shoulder.set(SHOULDER_MIN_COUNT);
    }
    // if (isWristTooLow()) {
    //   wrist.set(WRIST_MIN_ANG);
    // }
    // if (isWristTooHigh()) {
    //   wrist.set(WRIST_MAX_ANG);
    // }
    if (isExtendTooLow()) {
      extend.set(EXTEND_MIN_COUNT);
    }
    if (isExtendTooHigh()) {
      extend.set(EXTEND_MAX_COUNT);
    }
  }

  public double getShoulder() {
    return shoulder_motor.get();
  }

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




  public void toggleBrake() {
    // Cal changed this bit by adding the line below this commented block and commenting the 5 lines below
    // if (breaksOn) {
    //   break_solenoid.set(true);
    // }
    // else {
    //   break_solenoid.set(false);
    // }
    brake_solenoid.set(brakesOn);
  }
  public void setBrake(boolean state) {
    if (brakesOn != state) {
      brakesOn = state;
      brake_solenoid.set(state);
    }
  }
  public boolean isShoulderTooHigh() {
    return !UpperShoulderLimitSwitch.get() &&   isUpperShoulderSwitchEnabled;
	}
	public boolean isShoulderTooLow() {
    return (!LowerShoulderLimitSwitch.get()) && isLowerShoulderSwitchEnabled;
  }
  public boolean isWristTooHigh() {
    return !UpperWristLimitSwitch.get() && isUpperWristSwitchEnabled;
	}
	public boolean isWristTooLow() {
    return !LowerWristLimitSwitch.get() && isLowerWristSwitchEnabled;
  }
  // This one is used differently!
  public boolean isExtendTooHigh() {
    return !UpperExtendLimitSwitch.get() && isUpperExtendSwitchEnabled;
	}
	public boolean isExtendTooLow() {
    return !LowerExtendLimitSwitch.get() && isLowerExtendSwitchEnabled;
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
    setDefaultCommand(new ArmCtrl(this));
  }
  public static void limitSwitchesEnabled(boolean val) {
    isLowerShoulderSwitchEnabled = val;
    isUpperShoulderSwitchEnabled = val;
    isLowerExtendSwitchEnabled = val;
    isUpperExtendSwitchEnabled = val;
    isLowerWristSwitchEnabled = val;
    isUpperWristSwitchEnabled = val;
  }
  // public double[] setPosition(double x, double y) {
  //   double r = Math.hypot(x,y);
  //   double theta = Calc.atrig(y,x);
  //   setShoulderAng(theta);
  //   // setExtendCount((int) r); // there should be some constant here
  //   setWristAng(-theta);
  //   return new double[] {r,theta,-theta};
  // } 
}
