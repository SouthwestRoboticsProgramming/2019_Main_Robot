/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.enums.Somatotype;
/**
 * Add your docs here.
 */
public class Vacuum extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
    private final int SOLENOID_ID = 7;
    private static final int PCM_ID = 37; // THis can't be set to 0;
    
    private final Solenoid expand;

    public Vacuum() {
        System.out.println("Starting Vacuum()...");
        expand = new Solenoid(PCM_ID, SOLENOID_ID);
        System.out.println("....Vacuum() completed");
    }

    //private final OneSolenoid solenoid = new OneSolenoid(SOLENOID_ID);

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
  //public void setState(Somatotype state) {
  //  solenoid.set(state);
  //
  public void setState(boolean state) {
    expand.set(state);
  }
}
