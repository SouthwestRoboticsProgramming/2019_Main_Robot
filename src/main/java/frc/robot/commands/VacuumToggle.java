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
  // private Timer timer = new Timer();
  // private Arm arm;
  private final Vacuum vacuum;

  public VacuumToggle (Vacuum m_vacuum) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    //requires(m_arm);
    requires(m_vacuum);
    // this.arm = m_arm;
    this.vacuum = m_vacuum;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    
    vacuum.set(true);
    // timer.start();
    Log.info("Vacuums -> ON");

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Log.info("Vacuums ON");
    //vacuumSolenoid.setState(true);
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
    Log.info("Vacuums -> OFF");
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    vacuum.set(false);

    // arm.toggleVacuumMotors();
    Log.info("Vacuums -> OFF");
  }
}
