/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;
import frc.robot.subsystems.Arm;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;
import frc.robot.Interfaces.IController.Button;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.subsystems.Logger;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;
import frc.robot.Log;

import frc.utilities.Calc;

public class ArmCtrl extends Command {
  private final OI oi;
  private final Arm arm;

  private double previousWrist = 0;
  private double previousShoulder = 0;
  private double previousExtention = 0;
  private double extensionPastLimit = 0;
  boolean isPastLimit = false;
  boolean breaking = false;
  boolean previouslyBreaking = false;
  boolean inProcessOfBreaking = false;
  boolean isBreakOn = false;
  int breakCounter = 0;
  private JoystickButton extendArm;

  private final double MAX_CHNG_W = .2;
  private final double MAX_CHNG_S = .2;
  private final double MAX_CHNG_E = .2;
  private final double MAX_EXTENSION = 1;

  DigitalInput LowerShoulderLimitSwitch = new DigitalInput(RobotMap.LOWER_SHOULDER_LIMIT_SWITCH);
  DigitalInput UpperShoulderLimitSwitch = new DigitalInput(RobotMap.UPPER_SHOULDER_LIMIT_SWITCH);
  DigitalInput LowerWristLimitSwitch = new DigitalInput(RobotMap.LOWER_WRIST_LIMIT_SWITCH);
  DigitalInput UpperWristLimitSwitch = new DigitalInput(RobotMap.UPPER_WRIST_LIMIT_SWITCH);
  DigitalInput LowerExtendLimitSwitch = new DigitalInput(RobotMap.LOWER_EXTEND_LIMIT_SWITCH);
  DigitalInput UpperExtendLimitSwitch = new DigitalInput(RobotMap.UPPER_EXTEND_LIMIT_SWITCH);



  public ArmCtrl(Arm m_arm, OI m_oi) {
    // Use requires() here to declare subsystem dependencies
    requires(m_arm);
    this.arm = m_arm;
    this.oi = m_oi;
    extendArm = oi.arm_pilot.getButton(Button.ExtendClaw);
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

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    //Log.info("Lower Shoulder Limit: " + isShoulderTooLow());
    //Log.info("Upper Shoulder Limit: " + isShoulderTooHigh());
    //Log.info("Lower Wrist Limit: " + isWristTooLow());
    //Log.info("Upper Wrist Limit: " + isWristTooHigh());
    
    //Log.info("Lower Extension Limit: " + isExtendTooLow());
    //Log.info("Upper Extension Limit: " + isExtendTooHigh());


    double shoulder_val = oi.arm_pilot.getAnalog(Hand.kLeft); //But its the left side???
    double wrist_val = oi.arm_pilot.getAnalog(Hand.kRight);

    if (inProcessOfBreaking) {
      if (breakCounter < 10) {
        breakCounter++;
        Log.info("Waiting!");
      }
      else {
        inProcessOfBreaking = false;
        breakCounter = 0;
        Log.info("Done waiting!");
      }
    }
    else {
      //if (((((Math.abs(shoulder_val) < .1) && (Math.abs(previousShoulder)>.1))||((Math.abs(shoulder_val) > .1) && (Math.abs(previousShoulder)<.1))))) {
      if ((Math.abs(shoulder_val) < .1)) {
        arm.setBreak(true);
        if (Math.abs(previousShoulder)>.1) {
          inProcessOfBreaking = true;
          Log.info("Changing break states!");
        }
      }
      else if ((Math.abs(shoulder_val) > .1)) {
        arm.setBreak(false);
        if (Math.abs(previousShoulder)<.1) {
          inProcessOfBreaking = true;
          Log.info("Changing break states!");
        }
      }
      else if (((isShoulderTooHigh() && (shoulder_val >= 0))||(isShoulderTooLow() && (shoulder_val <= 0)))&&!isBreakOn) {
        Log.info("shoulder too high/low");
        shoulder_val = 0;
        arm.setShoulder(shoulder_val);
      }
      else {
        //shoulder_val = (shoulder_val - previousShoulder) * MAX_CHNG_S + previousShoulder;
        shoulder_val = Math.min(Math.abs(shoulder_val), .6) * Math.signum(shoulder_val) ;
        if (Math.abs(shoulder_val) < .01) {
          shoulder_val = 0;
          arm.brakeShoulder();
        }
      }
    }

    arm.setShoulder(shoulder_val * .3);
    

    if ((isWristTooHigh() && (wrist_val >= 0)) || (isWristTooLow() && (wrist_val <= 0))) {
      wrist_val = 0;
      arm.setWrist(0);
    }
    else {
      wrist_val    = (wrist_val    - previousWrist   ) * MAX_CHNG_W + previousWrist   ;
      if (Math.abs(wrist_val) < .01) {
        wrist_val = 0;
        arm.brakeWrist();
      }
      else {
        arm.setWrist(   wrist_val   );
      }
    }
    
    

    double lft_t = oi.arm_pilot.getTrigger(Hand.kLeft); // left and right triggers
    double rht_t = oi.arm_pilot.getTrigger(Hand.kRight);
    //dou       gnble trigger = Math.max(lft_t, rht_t); // No Math.abs because triggers are always between 0 and 1
    //trigger = -Calc.eq(lft_t, trigger) * lft_t + // lft is negative because it indicates contracting while rht is expanding
    //           Calc.eq(rht_t, trigger) * rht_t ;
    double trigger;
    if (lft_t >= rht_t) {
      trigger = -lft_t;
    }
    else {
      trigger = rht_t;
    }

    if (isExtendTooHigh()) {
      if (trigger >= 0) {
        isPastLimit = true;
      }
      else {
        isPastLimit = false;
      }
      extensionPastLimit = 0;
    }
    if (isExtendTooLow() && (trigger >= 0)) {
      trigger = 0;
    }
    else if (isExtendTooHigh() && (trigger <= 0)) {
      trigger = 0;
    }
    else {
      //trigger = (trigger - previousExtention) * MAX_CHNG_E + previousExtention;
      if (Math.abs(trigger) < .01) {
        trigger = 0;
        arm.brakeExtention();
      }
    }
    
    arm.setExtention(trigger * .3);

    if (isPastLimit) {
      extensionPastLimit += trigger;
    }
    previousExtention = trigger; // used for smoothing
    previousWrist = wrist_val;
    previousShoulder = shoulder_val;
    previouslyBreaking = breaking;
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {

  }
}
