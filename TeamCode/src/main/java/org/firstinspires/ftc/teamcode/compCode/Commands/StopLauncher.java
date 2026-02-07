package org.firstinspires.ftc.teamcode.compCode.Commands;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.compCode.SubsystemsAndDriveSetup.Launcher;

public class StopLauncher extends InstantCommand {
    public StopLauncher(Launcher launcher) {
        super(launcher::off, launcher);
    }
}
