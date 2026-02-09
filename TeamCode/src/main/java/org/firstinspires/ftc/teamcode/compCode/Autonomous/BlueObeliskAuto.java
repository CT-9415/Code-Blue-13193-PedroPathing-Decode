package org.firstinspires.ftc.teamcode.compCode.Autonomous;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.compCode.Commands.Fire;
import org.firstinspires.ftc.teamcode.compCode.SubsystemsAndDriveSetup.Launcher;
import org.firstinspires.ftc.teamcode.compCode.SubsystemsAndDriveSetup.Loader;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous
public class BlueObeliskAuto extends OpMode {


    private TelemetryManager panelsTelemetry;
    public Follower follower;
    private int pathState;
    private BlueObeliskAuto.Paths paths;
    private Timer pathTimer;

    private Launcher launcher;
    private Loader loader;
    private boolean fireFinished;


    private static final double LAUNCH_VELOCITY = 1800;
    private static final long LOAD_TIME_MS = 2000;

    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(16.5, 114.875, Math.toRadians(180)));

        launcher = new Launcher(hardwareMap);
        loader = new Loader(hardwareMap);

        paths = new BlueObeliskAuto.Paths(follower);
        pathTimer = new Timer();
        pathState = 0;

        CommandScheduler.getInstance().reset();

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void start() {
        pathTimer.resetTimer();
        setPathState(0);
    }

    @Override
    public void loop() {
        follower.update();
        autonomousPathUpdate();
        CommandScheduler.getInstance().run();

        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void stop() {
        CommandScheduler.getInstance().reset();
    }

    public static class Paths {
        public PathChain Path1;
        public PathChain Path2;

        public Paths(Follower follower) {
            Path1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(16.5, 114.875 ),
                                    new Pose(60.005, 83.067)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(135))
                    .build();

            Path2 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(60.005, 83.067),
                                    new Pose(44.143, 101.228),
                                    new Pose(23.733, 97.044)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(45), Math.toRadians(-90))
                    .build();
        }
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // Drive to scoring position
                follower.followPath(paths.Path1, true);
                setPathState(1);
                break;
            case 1: // Wait to arrive, then fire
                if (!follower.isBusy()) {
                    fireFinished = false;
                    new Fire(launcher, loader, LAUNCH_VELOCITY, LOAD_TIME_MS)
                            .whenFinished(() -> fireFinished = true)
                            .schedule();
                    setPathState(2);
                }
                break;
            case 2: // Wait for fire to finish, then drive to next position
                if (fireFinished) {
                    follower.followPath(paths.Path2, true);
                    setPathState(3);
                }
                break;
            case 3: // Done
                if (!follower.isBusy()) {
                    setPathState(-1);
                }
                break;
            default:
                break;
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }
}
