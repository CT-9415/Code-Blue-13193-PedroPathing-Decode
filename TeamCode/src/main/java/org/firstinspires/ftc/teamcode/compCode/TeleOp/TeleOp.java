package org.firstinspires.ftc.teamcode.compCode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.compCode.SubsystemsAndDriveSetup.Launcher;
import org.firstinspires.ftc.teamcode.compCode.SubsystemsAndDriveSetup.Loader;
import org.firstinspires.ftc.teamcode.compCode.SubsystemsAndDriveSetup.MecanumDrive;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends OpMode {
    private MecanumDrive drive;
    private Loader loader;

    private Launcher launcher;
    double forward, strafe, rotate;
    boolean launcherOn = false;
    boolean lastLB = false;

    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap);
        launcher = new Launcher(hardwareMap);
        loader = new Loader(hardwareMap);

    }

    @Override
    public void loop() {
       /* forward = -gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;*/
        boolean currentLB = gamepad1.left_bumper;

        if(gamepad1.a){
            loader.on();
        }
        else {
            loader.();
        }
        if(currentLB && !lastLB){
            launcherOn = !launcherOn;
            if (launcherOn){
                launcher.on();
            }
            else {
                launcher.off();
            }
        }
        lastLB = currentLB;
        drive.drive(- gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        telemetry.addData("Flywheel Velocity", launcher.getVelocity());

        if (gamepad1.dpad_down){
            drive.slow();
        }
        else if (gamepad1.dpad_up) {
            drive.fast();

        }



    }
}
