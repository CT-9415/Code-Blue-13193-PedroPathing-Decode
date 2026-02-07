package org.firstinspires.ftc.teamcode.compCode.TeleOp;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.compCode.Commands.Fire;
import org.firstinspires.ftc.teamcode.compCode.SubsystemsAndDriveSetup.Launcher;
import org.firstinspires.ftc.teamcode.compCode.SubsystemsAndDriveSetup.Loader;
import org.firstinspires.ftc.teamcode.compCode.SubsystemsAndDriveSetup.MecanumDrive;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends OpMode {
    private MecanumDrive drive;
    private Launcher launcher;
    private Loader loader;

    private boolean lastRB = false;
    private boolean lastLB = false;
    private boolean launcherOn = false;
    private boolean firing = false;

    // Target velocity for the launcher (ticks/sec) — tune this
    private static final double LAUNCH_VELOCITY = 1600;
    // How long the loader runs to feed the ball (ms)
    private static final long LOAD_TIME_MS = 1000;

    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap);
        launcher = new Launcher(hardwareMap);
        loader = new Loader(hardwareMap);

        CommandScheduler.getInstance().reset();
    }

    @Override
    public void loop() {
        // --- Fire command: right bumper triggers the full fire sequence ---
        boolean currentRB = gamepad1.left_bumper;
        if (currentRB && !lastRB && !firing) {
            firing = true;
            new Fire(launcher, loader, LAUNCH_VELOCITY, LOAD_TIME_MS).whenFinished(() -> firing = false).schedule();
        }
        lastRB = currentRB;

        // --- Loader manual: hold A to run loader ---
        if (gamepad1.a) {
            loader.on();
        } else if (!firing) {
            loader.stop();
        }

        // --- Drive ---
        drive.drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

        if (gamepad1.dpad_down) {
            drive.slow();
        } else if (gamepad1.dpad_up) {
            drive.fast();
        }

        // --- Telemetry ---
        telemetry.addData("Launcher Velocity", launcher.getVelocity());
        telemetry.addData("Launcher Target", launcher.getGoonRate());
        telemetry.addData("Firing", firing);

        // Run the command scheduler
        CommandScheduler.getInstance().run();
    }

    @Override
    public void stop() {
        CommandScheduler.getInstance().reset();
    }
}
