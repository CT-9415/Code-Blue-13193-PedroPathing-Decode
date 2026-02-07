package org.firstinspires.ftc.teamcode.compCode.Commands;

import com.arcrobotics.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.compCode.SubsystemsAndDriveSetup.Launcher;

public class ToggleFlywheel extends InstantCommand {
    public ToggleFlywheel(Launcher launcher, double velocity) {
        super(() -> {
            // Check if the launcher is currently running (target velocity > 0)
            // We use a small threshold (0.1) to account for floating point comparisons, roughly equal to 0
            if (launcher.getTargetVelocity() > 0.1) { 
                launcher.off();
            } else {
                launcher.on(velocity);
            }
        }, launcher);
    }
}
