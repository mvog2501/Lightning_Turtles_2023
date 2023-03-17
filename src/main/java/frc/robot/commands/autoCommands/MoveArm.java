package frc.robot.commands.autoCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;

public class MoveArm extends CommandBase{

    // var setup
    Arm arm;
    double distance, speed;

    public MoveArm(Arm arm, double speed, double distance) {
        // typical stuff
        this.arm = arm;
        this.distance = distance;
        this.speed = speed;
        addRequirements(this.arm);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        arm.Run(speed);
    }

    @Override
    public void end(boolean interupted) {
        arm.Stop();
    }

    @Override
    public boolean isFinished() {
        if (speed < 0) {
            if (arm.GetEncoderRotation() <= -distance) {
                return true;
            }
        }
        else {
            if (arm.GetEncoderRotation() >= distance) {
                return true;
            }
        }
        return false;
    }
}
