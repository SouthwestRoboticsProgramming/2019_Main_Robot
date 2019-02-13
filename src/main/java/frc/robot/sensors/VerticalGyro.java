/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot.sensors;
import frc.robot.Interfaces.IGyro;
import java.util.function.*;
/**
 * Add your docs here.
 */
public class VerticalGyro implements IGyro {
    @Override
   public Axis align(Axis axis) {
       Axis a;
       switch(axis) {
           case Z : a = Axis.X; break;
           case X : a = Axis.Y; break;
           case Y : a = Axis.Z; break;
           default: a = Axis.Z; break;
       }
       return a;
   }

   @Override
   public double modifier(Axis axis) {
       double mod;
       switch(axis) {
           case X : mod = -1 ; break;
           case Y : mod = 1; break;
           case Z : mod = -1; break;
           default: mod = 1 ; break;
       }
       return mod;
   }
}