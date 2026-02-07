package org.firstinspires.ftc.teamcode.compCode.SubsystemsAndDriveSetup;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

public class Launcher extends SubsystemBase {
    private final DcMotorEx launcher;


    public Launcher(HardwareMap hardwareMap){

        launcher = hardwareMap.get(DcMotorEx.class, ("launcher"));
        launcher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


       /* PIDFCoefficients pidfCoefficients = new PIDFCoefficients(170,0,0,12.9);
        launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);*/
    }
    public void setVelocity(double velocity){launcher.setVelocity(velocity);}
    public void stop(){setVelocity(0);}






    public void on() {
        setVelocity(8);
    }

    public void off() {
        launcher.setVelocity(0);
    }


    public double getVelocity(){
        double velocity = launcher.getVelocity();
        return velocity;
    }

}
