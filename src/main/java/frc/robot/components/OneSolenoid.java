/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.components;
import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.enums.Somatotype;
/**
 * Add your docs here.
 */
public class OneSolenoid {
    private static final int PCM_ID = 37; // THis can't be set to 0;
    
    private final Solenoid expand;

    public OneSolenoid(int EXPAND_ID) {
        expand = new Solenoid(PCM_ID, EXPAND_ID);
    }
    public void set(Somatotype state) {
        boolean val = state.toBoolean();
        expand.set(val);
    }



}
