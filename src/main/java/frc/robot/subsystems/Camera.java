/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Servo;
/**
 * Add your docs here.
 */
public class Camera extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.\
  private static final int MOUNT_ID = 9;

  private static final double MIN_ANGLE = -90;
  private static final double MAX_ANGLE = 90;

  private Servo mount = new Servo(MOUNT_ID);

  public void setAngle(double degrees) {
    mount.setAngle( Math.min(Math.max(degrees, MIN_ANGLE),MAX_ANGLE) );
  }
  public void set(double speed) {
    mount.setSpeed(speed);
  }
  

  @Override
  public void initDefaultCommand() {}
}
