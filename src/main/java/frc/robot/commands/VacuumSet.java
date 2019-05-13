/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Vacuum;
import frc.robot.enums.Somatotype;
import frc.robot.Log;

public class VacuumSet extends Command {
  // private Timer timer = new Timer();
  private final Vacuum vacuum;
  private boolean isOn;

  public VacuumSet (Vacuum m_vacuum, boolean isOn) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    //requires(m_arm);
    requires(m_vacuum);
    this.vacuum = m_vacuum;
    this.isOn = isOn;
  }
  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    vacuum.set(isOn);
  }
  @Override
  protected boolean isFinished() {
    return false;
  }
  protected void execute() {
    vacuum.setVacuum(isOn);
  }
  @Override
  protected void end() {
    Log.info("Vaccums = " + isOn);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
