package frc.robot.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ElevatorConstants;

public class Elevator extends SubsystemBase{
    private CANSparkMax em1 = new CANSparkMax(ElevatorConstants.elevatorMotor1, MotorType.kBrushless);
    private CANSparkMax em2 = new CANSparkMax(ElevatorConstants.elevatorMotor2, MotorType.kBrushless);

    private AbsoluteEncoder encoder = em1.getAbsoluteEncoder(Type.kDutyCycle);


    public Elevator(){
        em2.follow(em1);
    }

    public void Run(double speed) {
        em1.set(speed);
    }

    public void Stop(){
        em1.stopMotor();
    }

    public double GetEncoderRotation(){
        return encoder.getPosition();
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("motor 1 voltage", em1.getBusVoltage());
        SmartDashboard.putNumber("motor 2 voltage", em1.getBusVoltage());
        SmartDashboard.putNumber("motor speeds", encoder.getVelocity());
    }
}

