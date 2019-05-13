/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Log;
import frc.robot.sensors.LimeLight;
import frc.robot.subsystems.DriveTrain;
import frc.robot.commands.Drive;

public class LineFollowToggle extends Command {
  // private DriveTrain drive;
  public LineFollowToggle () {
    // this.drive = drive;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Drive.isManualDrive = false;
    Log.info("Initializing LineFollowToggle");
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    //Log.info("Target Present"+lime.getMatch()+"Target X"+lime.getX()+"Target Y"+lime.getY());
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    // return timer.get() > 1d;
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
    Drive.isManualDrive = true;
  }
}
