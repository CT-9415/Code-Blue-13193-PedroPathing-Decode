package org.firstinspires.ftc.teamcode.compCode.SubsystemsAndDriveSetup;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
@Configurable

public class Launcher extends SubsystemBase {
    private final DcMotorEx shooter;
    private double goonRate = 0;

    // Tune these PID values for your motor
    private static final double kP = 14.5;
    private static final double kI = 2.0;
    private static final double kD = 0.0;
    private static final double kF = 12.3;

    public Launcher(HardwareMap hardwareMap) {
        shooter = hardwareMap.get(DcMotorEx.class, "launcher");
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,
                new PIDFCoefficients(kP, kI, kD, kF));
    }

    public void setGoonRate(double velocity) {
        goonRate = velocity;
        shooter.setVelocity(velocity);
    }

    public double getGoonRate() {
        return goonRate;
    }

    public double getVelocity() {
        return shooter.getVelocity();
    }

    public boolean isAtTarget(double tolerance) {
        return Math.abs(getVelocity() - goonRate) < tolerance;
    }

    public void goon(double velocity) {
        setGoonRate(velocity);
    }

    public void off() {
        setGoonRate(0);
    }
}
