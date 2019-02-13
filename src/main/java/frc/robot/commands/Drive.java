/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Log;
import frc.robot.Robot;
import frc.robot.sensors.*;
import frc.robot.Interfaces.IGyro.Axis;

/**
 * An example command.  You can replace me with your own command.
 */
import frc.robot.subsystems.DriveTrain;

public class Drive extends Command {
  DriveTrain m_sub;
  public Drive(DriveTrain m_train) {
    // Use requires() here to declare subsystem dependencies
    m_sub = m_train;
    requires(m_sub);
  }
  @Override
  protected void initialize() {
    Log.info("initializing drive command");
  }
  @Override
  protected void execute() {

    Log.info("Driving");
    m_sub.driveCommandPeriodic();
  }
  @Override
  protected boolean isFinished() {
    return false;
  }
  @Override
  protected void end() {
  }
  @Override
  protected void interrupted() {
  }
}
