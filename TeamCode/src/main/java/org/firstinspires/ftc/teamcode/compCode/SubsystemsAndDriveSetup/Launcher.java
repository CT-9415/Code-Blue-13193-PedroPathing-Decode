package org.firstinspires.ftc.teamcode.compCode.SubsystemsAndDriveSetup;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class Launcher extends SubsystemBase {
    private final DcMotorEx shooter;
    private double targetVelocity = 0;

    // Tune these PID values for your motor
    private static final double kP = 10.0;
    private static final double kI = 3.0;
    private static final double kD = 0.0;
    private static final double kF = 12.0;

    public Launcher(HardwareMap hardwareMap) {
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,
                new PIDFCoefficients(kP, kI, kD, kF));
    }

    public void setTargetVelocity(double velocity) {
        targetVelocity = velocity;
        shooter.setVelocity(velocity);
    }

    public double getTargetVelocity() {
        return targetVelocity;
    }

    public double getVelocity() {
        return shooter.getVelocity();
    }

    public boolean isAtTarget(double tolerance) {
        return Math.abs(getVelocity() - targetVelocity) < tolerance;
    }

    public void on(double velocity) {
        setTargetVelocity(velocity);
    }

    public void off() {
        setTargetVelocity(0);
    }
}
