// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.event.EventLoop;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.IOConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.ArmCommand;
import frc.robot.commands.Autos;
import frc.robot.commands.ElevatorCommand;
import frc.robot.commands.autoCommands.moveArmFancy;
import frc.robot.commands.autoCommands.moveClaw;
import frc.robot.commands.driveCommand;
import frc.robot.commands.clawCommand;
import frc.robot.commands.autoCommands.MoveArmToPositionCommand;
import frc.robot.commands.autoCommands.balance;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Claw;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

  // controllers
  private final XboxController driverController =
      new XboxController(OperatorConstants.DRIVER_CONTROLLER_PORT);
  private final XboxController coDriverController =
      new XboxController(OperatorConstants.CO_DRIVER_CONTROLLER_PORT);

  private final EventLoop dpadLoop = new EventLoop();

  // buttons for commands
  private final JoystickButton driverButtonB = new JoystickButton(driverController, IOConstants.bButtonChannel);
  private final JoystickButton driverButtonY = new JoystickButton(driverController, IOConstants.yButtonChannel);
  private final JoystickButton buttonSelect = new JoystickButton(coDriverController, IOConstants.backButtonChannel);
  private final JoystickButton buttonStart = new JoystickButton(coDriverController, IOConstants.startButtonChannel);
  private final JoystickButton buttonX = new JoystickButton(coDriverController, IOConstants.xButtonChannel);
  private final JoystickButton buttonY = new JoystickButton(coDriverController, IOConstants.yButtonChannel);
  private final JoystickButton buttonA = new JoystickButton(coDriverController, IOConstants.aButtonChannel);
  private final JoystickButton buttonB = new JoystickButton(coDriverController, IOConstants.bButtonChannel);
  private final JoystickButton buttonLeftBumper = new JoystickButton(coDriverController, IOConstants.leftBumperChannel);
  private final JoystickButton buttonRightBumper = new JoystickButton(coDriverController, IOConstants.rightBumperChannel);
  private final Trigger buttonDpadUp = new Trigger(coDriverController.povUp(dpadLoop));
  private final Trigger buttonDpadLeft = new Trigger(coDriverController.povLeft(dpadLoop));

  // subsystems
  private final Drive drive = new Drive();
  private final Claw claw = new Claw();
  public final ArmSubsystem armSub = new ArmSubsystem(); // Public isn't too dangerous here
  // private final vision m_Vision = new vision();

  ///////////
  // autos //
  ///////////

  // A chooser for autonomous commands
  SendableChooser<Command> chooser = new SendableChooser<>();


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // get them cameras
    CameraServer.startAutomaticCapture();

    // Command m_scoreCubeBalance = Autos.scoreCubeBalance(elevator, arm, claw, drive, 0.3, 17.7, 7); FIXME: Replace with new system
    Command m_balance = Autos.balance(drive, 0.3, 5);
    // Command m_scoreCubeLeave = Autos.scoreCubeLeave(drive, elevator, arm, claw, -0.3, 20);
    // Command m_scoreCube = Autos.scoreCubeStay(elevator, arm, claw);
    Command driveOutShort = Autos.drive(drive, 0.3, 6.5);
    Command driveOutMid = Autos.drive(drive, 0.3, 10);
    Command driveOutLong = Autos.drive(drive, 0.3, 15);
    Command m_noAuto = new InstantCommand();
    // chooser.setDefaultOption("score cube balance", m_scoreCubeBalance);
    chooser.addOption("balance", m_balance);
    // chooser.addOption("score cube leave", m_scoreCubeLeave);
    // chooser.addOption("score cube", m_scoreCube);
    chooser.addOption("drive out short", driveOutShort);
    chooser.addOption("drive out mid", driveOutMid);
    chooser.addOption("drive out long", driveOutLong);
    chooser.addOption("No Auto", m_noAuto);

    SmartDashboard.putData(chooser);

    // default commands
    drive.setDefaultCommand(new driveCommand(drive, driverController));
    // arm.setDefaultCommand(new ArmCommand(arm, coDriverController));
    // elevator.setDefaultCommand(new ElevatorCommand(elevator, coDriverController));
    claw.setDefaultCommand(new clawCommand(coDriverController, claw));
    // m_Vision.setDefaultCommand(new visionController(coDriverController, m_Vision, claw));

    // Configure the trigger bindings
    configureBindings();
  }

  private void configureBindings() {
    // drivetrain commands
    driverButtonB.whileTrue(new balance(drive));

    driverButtonB.whileTrue(new MoveArmToPositionCommand(this, () -> new Translation2d(1, 1)));
    driverButtonY.whileTrue(new MoveArmToPositionCommand(this, () -> new Translation2d(0.5, 0.5)));
    
    // end affector commands

    // lock claw joystick movements
    // buttonDpadUp.onTrue(new moveArmFancy(arm, -0.3, 2.5));
    buttonDpadLeft.onTrue(new moveClaw(claw, -0.3, 0));

    buttonLeftBumper.whileTrue(new StartEndCommand(
      () -> claw.runBoth(0.4),
      () -> claw.stopBoth(),
    claw));
    
    buttonRightBumper.whileTrue(new StartEndCommand(
      () -> claw.runBoth(-0.4),
      () -> claw.stopBoth(),
    claw));

    /*
     buttonA.whileTrue(new StartEndCommand(
       () -> claw.runLeft(-0.5),
       () -> claw.stopLeft(),
     claw));
     buttonB.whileTrue(new StartEndCommand(
       () -> claw.runLeft(0.5),
       () -> claw.stopLeft(),
     claw));
     buttonX.whileTrue(new StartEndCommand(
       () -> claw.runRight(-0.5),
       () -> claw.stopRight(),
     claw));
     buttonY.whileTrue(new StartEndCommand(
       () -> claw.runRight(0.5),
       () -> claw.stopRight(),
     claw));
    */

  }

  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return chooser.getSelected();
  }
}
