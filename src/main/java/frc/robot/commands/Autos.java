// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.commands.autoCommands.*;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Claw;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;

public final class Autos {

  // drive forward
  public static CommandBase drive(Drive subsystem, double speed, double distance) {
    return Commands.sequence(new moveForward(subsystem, speed, distance));
  }


  public static CommandBase balance(Drive drive, double speed, double backDistance) {
    return Commands.sequence(
      drive(drive, speed, backDistance), 
      new balance(drive));
  }


  public static CommandBase scoreCubeStay(Elevator elevator, Arm arm, Claw end) {
    return Commands.sequence(
      new moveElevator(elevator, 0.4, 83),
      new moveArm(arm, 0.3, 56), 
      new moveClaw(end, 0.3, 0.075));
  }


  public static CommandBase moveArmIn(Elevator elevator, Arm arm, Claw end) {
    return Commands.sequence(
      new moveArm(arm, -0.3, 1),
      new moveElevator(elevator, -0.4, 1));
  }
  

  public static CommandBase scoreCubeBalance(Elevator elevator, Arm arm, Claw end, Drive drive, 
      double driveSpeed, double backDistance, double forwardDistance) {
    return Commands.sequence(
      scoreCubeStay(elevator, arm, end), 
      moveArmIn(elevator, arm, end), 
      drive(drive, -driveSpeed, backDistance), 
      balance(drive, driveSpeed, forwardDistance));
  }


  public static CommandBase scoreCubeLeave(Drive drive, Elevator elevator, Arm arm, Claw end,
        double speed, double distance) {
      return Commands.sequence(
        scoreCubeStay(elevator, arm, end),
        moveArmIn(elevator, arm, end),
        drive(drive, speed, distance));
  }

  public static CommandBase score2Cubes(Drive drive, Elevator elevator, Arm arm, Claw end,
                                        double driveSpeed, double driveDistance, double elevatorSpeed,
                                        double armSpeed, double clawSpeed, double turnSpeed) {
    return Commands.sequence(scoreCubeStay(elevator, arm, end), moveArmIn(elevator, arm, end),
            drive(drive, -driveSpeed, driveDistance), new moveTurn(drive, turnSpeed, 180),
            new moveElevator(elevator, elevatorSpeed, 20), new moveArm(arm, armSpeed, 20),
            new moveElevator(elevator, -elevatorSpeed, -2), new moveClaw(end, -clawSpeed, 0.075),
            new moveElevator(elevator, elevatorSpeed, 20), new moveArm(arm, -clawSpeed, 1));
  }


  private Autos() {
    // not meant to be defiend
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
