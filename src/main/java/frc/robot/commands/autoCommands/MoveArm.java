package frc.robot.commands.autoCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.commands.ArmCommand;

public class MoveArm extends CommandBase{
    ArmCommand armcommand;
    boolean down;

    public MoveArm(ArmCommand armcommand, boolean isBack) {
        this.armcommand = armcommand;
        this.down = isBack;
    }

    @Override
    public void initialize() {
        armcommand.incrementcontroller(down);
    }

    @Override
    public boolean isFinished() {
        return armcommand.atSetpoint();
    }
}