package org.firstinspires.ftc.teamcode.compCode.Commands;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.compCode.SubsystemsAndDriveSetup.Launcher;
import org.firstinspires.ftc.teamcode.compCode.SubsystemsAndDriveSetup.Loader;

public class Fire extends SequentialCommandGroup {
    /**
     * Spins up the launcher to target velocity, runs the loader to feed
     * the ball, then stops the launcher. Call from TeleOp or Auto.
     *
     * @param launcher   the launcher subsystem
     * @param loader     the loader subsystem
     * @param velocity   target launcher velocity (ticks/sec)
     * @param loadTimeMs how long to run the loader (milliseconds)
     */
    public Fire(Launcher launcher, Loader loader, double velocity, long loadTimeMs) {
        addCommands(
                new SpinUpLauncher(launcher, velocity),
                new RunLoader(loader, 1.0).withTimeout(loadTimeMs),
                new StopLauncher(launcher)
        );
    }
}
