package frc.robot.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.endAffectorConstants;

public class endAffector extends SubsystemBase{
    private CANSparkMax leftAffector = new CANSparkMax(endAffectorConstants.leftMotor, MotorType.kBrushless);
    private CANSparkMax rightAffector = new CANSparkMax(endAffectorConstants.rightMotor, MotorType.kBrushless);

    private AbsoluteEncoder LeftEncoder = leftAffector.getAbsoluteEncoder(Type.kDutyCycle);
    private AbsoluteEncoder righEncoder = rightAffector.getAbsoluteEncoder(Type.kDutyCycle);

    public endAffector() {}

    public void runLeft(double speed) {
        leftAffector.set(speed);
    }

    public void runRight(double speed) {
        rightAffector.set(speed);
    }

    public void stopLeft() {
        leftAffector.stopMotor();
    }

    public void stopRight() {
        rightAffector.stopMotor();
    }

    public void runBothLeft(double speed) {
        leftAffector.set(speed);
        rightAffector.set(-speed);
    }

    public void runBothRight(double speed) {
        leftAffector.set(-speed);
        rightAffector.set(speed);
    }

    public void runBothOppicite(double speed) {
        leftAffector.set(speed);
        rightAffector.set(speed);
    }

    public void stopBoth() {
        leftAffector.stopMotor();
        rightAffector.stopMotor();
    }

    public double getLeftEncoder() {
        return LeftEncoder.getPosition();
    }

    public double getRightEncoder() {
        return righEncoder.getPosition();
    }
}
