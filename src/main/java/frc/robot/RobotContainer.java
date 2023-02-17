// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.IOConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.ArmCommand;
import frc.robot.commands.ElevatorCommand;
import frc.robot.commands.driving;
import frc.robot.commands.endAffectorController;
import frc.robot.commands.autoCommands.PIDBallence;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.drivetrain;
import frc.robot.subsystems.endAffector;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.event.EventLoop;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

  private final XboxController m_driverController =
      new XboxController(OperatorConstants.kDriverControllerPort);
  private final XboxController m_codriverController =
      new XboxController(OperatorConstants.kCoDriverControllerPort);

  EventLoop povLoop = new EventLoop();

  private JoystickButton driverButtonStart = new JoystickButton(m_driverController, IOConstants.startButtonChannel);
  private JoystickButton buttonX = new JoystickButton(m_codriverController, IOConstants.xButtonChannel);
  private JoystickButton buttonY = new JoystickButton(m_codriverController, IOConstants.yButtonChannel);
  private JoystickButton buttonA = new JoystickButton(m_codriverController, IOConstants.aButtonChannel);
  private JoystickButton buttonB = new JoystickButton(m_codriverController, IOConstants.bButtonChannel);
  private JoystickButton buttonStart = new JoystickButton(m_codriverController, IOConstants.startButtonChannel);
  private JoystickButton buttonSelect = new JoystickButton(m_codriverController, IOConstants.startButtonChannel);
  private JoystickButton buttonlb = new JoystickButton(m_codriverController, IOConstants.leftBumperChannel);
  private JoystickButton buttonrb = new JoystickButton(m_codriverController, IOConstants.rightBumperChannel);
  private Trigger buttonDUp = new Trigger(m_codriverController.pov(IOConstants.POVU, povLoop));
  private Trigger buttonDDown = new Trigger(m_codriverController.pov(IOConstants.POVD, povLoop));

  // The robot's subsystems and commands are defined here...
  // will uncomment things later as they get added to the robot
  private final drivetrain drive = new drivetrain();
  private final Arm arm = new Arm();
  private final Elevator elevator = new Elevator();
  private final endAffector claw = new endAffector();
  private final PIDBallence autoballence = new PIDBallence(drive);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // CameraServer.startAutomaticCapture();

    drive.setDefaultCommand(new driving(drive, m_driverController));
    // arm.setDefaultCommand(new ArmCommand(arm));
    // elevator.setDefaultCommand(new ElevatorCommand(elevator));
    claw.setDefaultCommand(new endAffectorController(m_codriverController, claw));

    // Configure the trigger bindings
    configureBindings();
  }

  private void configureBindings() {
    driverButtonStart.onTrue(new InstantCommand(() -> drive.ToggleSlowMode(), drive));
    buttonStart.whileTrue(autoballence);

    // elevator controls

    // buttonY.onTrue(new InstantCommand(
    //   () -> elevator.incrementSetpoint(false),
    // elevator));

    // buttonA.onTrue(new InstantCommand(
    //   () -> elevator.incrementSetpoint(true),
    // elevator));

    buttonY.whileTrue(new StartEndCommand(
      () -> elevator.Run(0.3), 
      () -> elevator.Stop(),
    elevator));

    buttonA.whileTrue(new StartEndCommand(
      () -> elevator.Run(-0.1), 
      () -> elevator.Stop(), 
    elevator));

    // arm controls

    // buttonB.onTrue(new InstantCommand(
    //   () -> arm.incrementSetpoint(false),
    // arm));

    // buttonX.onTrue(new InstantCommand(
    //   () -> arm.incrementSetpoint(true),
    // arm));

    buttonB.whileTrue(new StartEndCommand(
      () -> arm.Run(0.5), 
      () -> arm.Run(0), 
    arm));

    buttonX.whileTrue(new StartEndCommand(
      () -> arm.Run(-0.5), 
      () -> arm.Run(0), 
    arm));
    
    // end affector controls

    buttonSelect.onTrue(new InstantCommand(() -> claw.toggleEndAffectorLock(), claw));

    buttonlb.whileTrue(new StartEndCommand(
      () -> claw.runBothLeft(0.3),
      () -> claw.stopBoth(),
    claw));
    
    buttonrb.whileTrue(new StartEndCommand(
      () -> claw.runBothRight(0.3),
      () -> claw.stopBoth(),
    claw));



  }

  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return null;
  }
}
