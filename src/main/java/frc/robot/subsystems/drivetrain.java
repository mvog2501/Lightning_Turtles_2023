package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.driveTrainConstants;

public class drivetrain extends SubsystemBase{

    private static WPI_TalonFX frontleftMotor = 
        new WPI_TalonFX(driveTrainConstants.frontLeftPort);
    private static WPI_TalonFX frontRightMotor = 
        new WPI_TalonFX(driveTrainConstants.frontRightPort);
    private static WPI_TalonFX backLeftMotor = 
        new WPI_TalonFX(driveTrainConstants.backLeftPort);
    private static WPI_TalonFX backRightMotor = 
        new WPI_TalonFX(driveTrainConstants.backRightPort);

    private static DifferentialDrive drive = new DifferentialDrive(frontleftMotor, frontRightMotor);

    SlewRateLimiter speedLimiter = new SlewRateLimiter(10);
    SlewRateLimiter turnLimiter = new SlewRateLimiter(10);

    private static AHRS gyro = new AHRS(SPI.Port.kMXP);
    
    public Boolean slowmode = false;

    public drivetrain() {
        drive.setDeadband(0.09);
        configureMotors();
        resetEncoders();
    }

    public void ToggleSlowMode() {
        slowmode = !slowmode;
    }

    public void arcadeDrive(double speed, double rotation) {
        drive.arcadeDrive(speedLimiter.calculate(speed), turnLimiter.calculate(rotation), true);
    }

    public double getAverageEncoderRotation() {
        return (frontleftMotor.getSelectedSensorPosition(0) + frontRightMotor.getSelectedSensorPosition(0))/2;
    }

    public void resetEncoders() {
        frontleftMotor.getSensorCollection().setIntegratedSensorPosition(0, 0);
        frontRightMotor.getSensorCollection().setIntegratedSensorPosition(0, 0);
    }

    public double getgyroz() {
        return gyro.getPitch();
    }

    public double getgyrox() {
        return gyro.getYaw();
    }

    public double getgyroy() {
        return gyro.getRoll();
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("left speed", frontleftMotor.getSensorCollection().getIntegratedSensorVelocity());
        SmartDashboard.putNumber("right speed", frontRightMotor.getSensorCollection().getIntegratedSensorVelocity());
    }

    // public double getAverageEncoderVelocity() {} to be added later?

    private void configureMotors() {
        TalonFXConfiguration configs = new TalonFXConfiguration();

        frontleftMotor.configFactoryDefault();
        backLeftMotor.configFactoryDefault();
        frontRightMotor.configFactoryDefault();
        backRightMotor.configFactoryDefault();

        frontleftMotor.setSafetyEnabled(false);
        backLeftMotor.setSafetyEnabled(false);
        frontRightMotor.setSafetyEnabled(false);
        backRightMotor.setSafetyEnabled(false);

        // Determines which motors will be inverted

        frontleftMotor.setInverted(false);
        backLeftMotor.setInverted(false);
        frontRightMotor.setInverted(true);
        backRightMotor.setInverted(true);

        // Sets the motors to brake mode
        frontleftMotor.setNeutralMode(NeutralMode.Brake);
        backLeftMotor.setNeutralMode(NeutralMode.Brake);
        frontRightMotor.setNeutralMode(NeutralMode.Brake);
        backRightMotor.setNeutralMode(NeutralMode.Brake);

        backLeftMotor.follow(frontleftMotor);
        backRightMotor.follow(frontRightMotor);

        frontleftMotor.configPeakOutputForward(1.0);
        frontleftMotor.configPeakOutputReverse(-1.0);

        frontRightMotor.configPeakOutputForward(1.0);
        frontRightMotor.configPeakOutputReverse(-1.0);

        frontleftMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        frontRightMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    }
}
