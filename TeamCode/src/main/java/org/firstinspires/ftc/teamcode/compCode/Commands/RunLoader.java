package org.firstinspires.ftc.teamcode.compCode.Commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.compCode.SubsystemsAndDriveSetup.Loader;

public class RunLoader extends CommandBase {
    private final Loader loader;
    private final double power;

    public RunLoader(Loader loader, double power) {
        this.loader = loader;
        this.power = power;
        addRequirements(loader);
    }

    @Override
    public void initialize() {
        loader.setPower(power);
    }

    @Override
    public void end(boolean interrupted) {
        loader.stop();
    }

    @Override
    public boolean isFinished() {
        return false; // runs until cancelled (e.g. button released)
    }
}
