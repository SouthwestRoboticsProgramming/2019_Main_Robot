/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.subsystems.DriveTrain;
import frc.robot.Log;

public class AutonomousChain extends CommandGroup {

  /**
   * Add your docs here.
   */
  public AutonomousChain(DriveTrain m_sub) {
    requires(m_sub);
    // Add Commands here:
    // e.g. (new Command1());
    // addSequential(new Command2());
    // these will run in addSequentialorder.
    //addSequential(new AutonomousGyroDriveCommand(m_sub, m_sub.getLeftEncoder(), 160, .4, -90d));
    addSequential(new AutonomousGyroDrive(m_sub, m_sub.getLeftEncoder(), 100, .4, 0d));
    // To run multiple commands at the same time,
    // use addParallel()
    // e.g. addParallel(new Command1());
    // addSequential(new Command2());
    // Command1 and Command2 will run in parallel.

    // A command group will require all of the subsystems that each member
    // would require.
    // e.g. if Command1 requires chassis, and Command2 requires arm,
    // a CommandGroup containing them would require both the chassis and the
    // arm.
  }
}
