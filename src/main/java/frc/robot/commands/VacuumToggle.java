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

public class VacuumToggle extends Command {
  private Timer timer = new Timer();
  private Arm arm;
  private final Vacuum vacuum;
  private boolean solenoidIsOn = false; 
  public VacuumToggle (Arm m_arm, Vacuum m_vacuum) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    //requires(m_arm);
    requires(m_vacuum);
    this.arm = m_arm;
    this.vacuum = m_vacuum;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    arm.toggleVacuumMotors();
    solenoidIsOn = true;
    vacuum.setState(solenoidIsOn);
    timer.start();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
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
    vacuum.setState(false);
    solenoidIsOn = false;
    arm.toggleVacuumMotors();
  }
}
