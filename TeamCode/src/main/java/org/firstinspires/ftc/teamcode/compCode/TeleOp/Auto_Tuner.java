package org.firstinspires.ftc.teamcode.compCode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.compCode.SubsystemsAndDriveSetup.Loader;

@TeleOp(name = "Launcher PIDF Tuner", group = "Tuning")
public class Auto_Tuner extends OpMode {

    private DcMotorEx shooter;
    private Loader loader;

    // Tuning phase
    private enum Phase {
        FIND_MAX_VELOCITY,
        TUNE_PIDF
    }
    private Phase phase = Phase.FIND_MAX_VELOCITY;

    // Phase 1: max velocity detection
    private double maxVelocity = 0;
    private boolean motorRunning = false;
    private ElapsedTime rampTimer = new ElapsedTime();

    // Phase 2: PIDF tuning
    private double kF = 0;
    private double kP = 0;
    private double kI = 0;
    private double kD = 0;
    private double targetVelocity = 0;
    private double targetPercent = 0.75; // start at 75% of max

    // Which coefficient is being adjusted
    private enum SelectedGain { KF, KP, KI, KD, TARGET }
    private SelectedGain selectedGain = SelectedGain.KP;

    // Button edge detection
    private boolean lastA = false;
    private boolean lastB = false;
    private boolean lastX = false;
    private boolean lastY = false;
    private boolean lastDU = false;
    private boolean lastDD = false;
    private boolean lastLB = false;
    private boolean lastRB = false;

    @Override
    public void init() {
        shooter = hardwareMap.get(DcMotorEx.class, "launcher");
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        
        loader = new Loader(hardwareMap);

        telemetry.addLine("=== LAUNCHER PIDF TUNER ===");
        telemetry.addLine("");
        telemetry.addLine("PHASE 1: Find Max Velocity");
        telemetry.addLine("Press A to start/stop the motor at full power.");
        telemetry.addLine("Let it run for a few seconds to find max velocity.");
        telemetry.addLine("Press B when done to move to PIDF tuning.");
        telemetry.update();
    }

    @Override
    public void loop() {
        switch (phase) {
            case FIND_MAX_VELOCITY:
                loopFindMaxVelocity();
                break;
            case TUNE_PIDF:
                loopTunePIDF();
                break;
        }
    }

    // ── PHASE 1: Run motor at full power, record max velocity ──────────
    private void loopFindMaxVelocity() {
        boolean currA = gamepad1.a;
        boolean currB = gamepad1.b;

        // Toggle motor on/off with A
        if (currA && !lastA) {
            motorRunning = !motorRunning;
            if (motorRunning) {
                shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                shooter.setPower(1.0);
                rampTimer.reset();
            } else {
                shooter.setPower(0);
            }
        }
        lastA = currA;

        // Track max velocity
        if (motorRunning) {
            double vel = Math.abs(shooter.getVelocity());
            if (vel > maxVelocity) {
                maxVelocity = vel;
            }
        }

        // Move to phase 2 with B
        if (currB && !lastB && maxVelocity > 0) {
            shooter.setPower(0);
            motorRunning = false;

            // Calculate kF: the REV hub PIDF output range is [-32767, 32767]
            kF = 32767.0 / maxVelocity;

            // Set up for PIDF tuning mode
            shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            targetVelocity = maxVelocity * targetPercent;
            applyPIDF();
            phase = Phase.TUNE_PIDF;
        }
        lastB = currB;

        telemetry.addLine("=== PHASE 1: FIND MAX VELOCITY ===");
        telemetry.addLine("");
        telemetry.addData("Motor", motorRunning ? "RUNNING" : "STOPPED");
        telemetry.addData("Current Velocity", "%.1f ticks/s", shooter.getVelocity());
        telemetry.addData("Max Velocity", "%.1f ticks/s", maxVelocity);
        if (motorRunning) {
            telemetry.addData("Run Time", "%.1f s", rampTimer.seconds());
        }
        telemetry.addLine("");
        telemetry.addLine("[A] Start/Stop motor");
        telemetry.addLine("[B] Done → move to PIDF tuning");
        telemetry.update();
    }

    // ── PHASE 2: Adjust PIDF values live ───────────────────────────────
    private void loopTunePIDF() {
        boolean currA = gamepad1.a;
        boolean currB = gamepad1.b;
        boolean currX = gamepad1.x;
        boolean currY = gamepad1.y;
        boolean currDU = gamepad1.dpad_up;
        boolean currDD = gamepad1.dpad_down;
        boolean currLB = gamepad1.left_bumper;
        boolean currRB = gamepad1.right_bumper;

        // Cycle selected gain with X
        if (currX && !lastX) {
            switch (selectedGain) {
                case KF:     selectedGain = SelectedGain.KP; break;
                case KP:     selectedGain = SelectedGain.KI; break;
                case KI:     selectedGain = SelectedGain.KD; break;
                case KD:     selectedGain = SelectedGain.TARGET; break;
                case TARGET: selectedGain = SelectedGain.KF; break;
            }
        }
        lastX = currX;

        // Get step size: dpad = coarse, bumper = fine
        double step = getStep();

        // Increase with dpad_up or right_bumper
        if (currDU && !lastDU) { adjustSelected(step); applyPIDF(); }
        if (currRB && !lastRB) { adjustSelected(step * 0.1); applyPIDF(); }
        lastDU = currDU;
        lastRB = currRB;

        // Decrease with dpad_down or left_bumper
        if (currDD && !lastDD) { adjustSelected(-step); applyPIDF(); }
        if (currLB && !lastLB) { adjustSelected(-step * 0.1); applyPIDF(); }
        lastDD = currDD;
        lastLB = currLB;

        // Toggle motor on/off with A
        if (currA && !lastA) {
            motorRunning = !motorRunning;
            if (motorRunning) {
                shooter.setVelocity(targetVelocity);
            } else {
                shooter.setVelocity(0);
            }
        }
        lastA = currA;

        // Toggle loader on/off with Y
        if (currY && !lastY) {

        }
        
        if (currY) {
            loader.on();
        } else {
            loader.stop();
        }
        lastY = currY;

        // Display
        double currentVel = shooter.getVelocity();
        double error = targetVelocity - currentVel;

        telemetry.addLine("=== PHASE 2: PIDF TUNING ===");
        telemetry.addLine("");
        telemetry.addData("Motor", motorRunning ? "RUNNING" : "STOPPED");
        telemetry.addData("Target Velocity", "%.1f ticks/s", targetVelocity);
        telemetry.addData("Current Velocity", "%.1f ticks/s", currentVel);
        telemetry.addData("Error", "%.1f ticks/s", error);
        telemetry.addData("Max Velocity", "%.1f ticks/s", maxVelocity);
        telemetry.addLine("");
        telemetry.addData(selectedGain == SelectedGain.KF ? ">> kF" : "   kF", "%.4f", kF);
        telemetry.addData(selectedGain == SelectedGain.KP ? ">> kP" : "   kP", "%.4f", kP);
        telemetry.addData(selectedGain == SelectedGain.KI ? ">> kI" : "   kI", "%.4f", kI);
        telemetry.addData(selectedGain == SelectedGain.KD ? ">> kD" : "   kD", "%.4f", kD);
        telemetry.addData(selectedGain == SelectedGain.TARGET ? ">> Target%" : "   Target%",
                "%.0f%%", targetPercent * 100);
        telemetry.addLine("");
        telemetry.addLine("[X] Cycle selected gain");
        telemetry.addLine("[DPad Up/Down] Coarse adjust");
        telemetry.addLine("[RB/LB] Fine adjust (0.1x)");
        telemetry.addLine("[A] Start/Stop motor");
        telemetry.addLine("[Y] Hold to run Loader");
        telemetry.update();
    }

    private double getStep() {
        switch (selectedGain) {
            case KF:     return 1.0;
            case KP:     return 5.0;
            case KI:     return 1.0;
            case KD:     return 0.5;
            case TARGET: return 0.05;
            default:     return 1.0;
        }
    }

    private void adjustSelected(double amount) {
        switch (selectedGain) {
            case KF:     kF = Math.max(0, kF + amount); break;
            case KP:     kP = Math.max(0, kP + amount); break;
            case KI:     kI = Math.max(0, kI + amount); break;
            case KD:     kD = Math.max(0, kD + amount); break;
            case TARGET:
                targetPercent = Math.max(0.1, Math.min(1.0, targetPercent + amount));
                targetVelocity = maxVelocity * targetPercent;
                if (motorRunning) {
                    shooter.setVelocity(targetVelocity);
                }
                break;
        }
    }

    private void applyPIDF() {
        shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,
                new PIDFCoefficients(kP, kI, kD, kF));
        if (motorRunning) {
            shooter.setVelocity(targetVelocity);
        }
    }

    @Override
    public void stop() {
        shooter.setPower(0);
    }
}
