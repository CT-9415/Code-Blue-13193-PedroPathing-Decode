package org.firstinspires.ftc.teamcode.compCode.Commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.compCode.SubsystemsAndDriveSetup.Launcher;

public class SpinUpLauncher extends CommandBase {
    private final Launcher launcher;
    private final double targetVelocity;

    public SpinUpLauncher(Launcher launcher, double targetVelocity) {
        this.launcher = launcher;
        this.targetVelocity = targetVelocity;
        addRequirements(launcher);
    }

    @Override
    public void initialize() {
        launcher.setGoonRate(targetVelocity);
    }

    @Override
    public boolean isFinished() {
        return launcher.isAtTarget(0.5);
    }
}
