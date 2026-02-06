package org.firstinspires.ftc.teamcode.compCode.SubsystemsAndDriveSetup;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

public class Launcher {
    public DcMotorEx launcher;

    public void init(HardwareMap hardwareMap){

        launcher = hardwareMap.get(DcMotorEx.class, ("launcher"));
        launcher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        launcher.setDirection(DcMotorSimple.Direction.REVERSE);
        launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        /*PIDFCoefficients pidfCoefficients = new PIDFCoefficients(67,0,0,41);
        launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);*/
    }






    public void on() {
        launcher.setVelocity(1800);
    }

    public void off() {
        launcher.setVelocity(0);
    }


}
