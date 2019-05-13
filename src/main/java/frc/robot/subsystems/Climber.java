/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.components.*;
import frc.robot.enums.Somatotype;
/**
 * Add your docs here.
 */
public class Climber extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private static final int CONTRACT_ID = 0;
  private static final int EXPAND_ID = 1;
  private static final TwoWaySolenoid solenoid = new TwoWaySolenoid(CONTRACT_ID, EXPAND_ID);

  public Climber() {}

  public void set(Somatotype state) {
    solenoid.set(state);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
