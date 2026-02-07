package org.firstinspires.ftc.teamcode.compCode.SubsystemsAndDriveSetup;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

public class MecanumDrive {
    private DcMotorEx leftFront, leftBack, rightFront, rightBack;
    private IMU imu;

    double maxPower = 1;
    double maxSpeed = 1;

    public void init(HardwareMap hardwareMap){
        leftFront=hardwareMap.get(DcMotorEx.class, "lf");
        leftBack=hardwareMap.get(DcMotorEx.class, "lb");
        rightFront=hardwareMap.get(DcMotorEx.class, "rf");
        rightBack=hardwareMap.get(DcMotorEx.class, "rb");

        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        //----------------IMU--------------
        imu= hardwareMap.get(IMU.class,"imu");

        RevHubOrientationOnRobot revHubOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
        );
        imu.initialize(new IMU.Parameters(revHubOrientation));

    }


    public void drive (double forward, double strafe, double rotate){
        double leftFrontPower = forward + strafe + rotate;
        double leftBackPower = forward - strafe + rotate;
        double rightFrontPower = forward - strafe - rotate;
        double rightBackPower = forward + strafe - rotate;



        maxPower = Math.max(maxPower, Math.abs(leftFrontPower));
        maxPower = Math.max(maxPower, Math.abs(leftBackPower));
        maxPower = Math.max(maxPower, Math.abs(rightFrontPower));
        maxPower = Math.max(maxPower, Math.abs(rightBackPower));

        leftFront.setPower(maxSpeed * (leftFrontPower / maxPower));
        leftBack.setPower(maxSpeed * (leftBackPower / maxPower));
        rightFront.setPower(maxSpeed * (rightFrontPower / maxPower));
        rightBack.setPower(maxSpeed * (rightBackPower / maxPower));



    }


    public void slow(){
        maxSpeed = .5;

    }
    public void fast(){
        maxSpeed = 1;
    }








}
