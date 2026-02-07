package org.firstinspires.ftc.teamcode.compCode.Autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.Timer;

@Autonomous
public class RedObeliskAuto extends OpMode{
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;



    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();

        follower = Constants.createFollower(hardwareMap);




    }


    @Override
    public void loop() {
        follower.update();
        autonomousPathUpdate();

        class Paths {
            public PathChain Path1;
            public PathChain Path2;



    }

    private void autonomousPathUpdate() {
            public Paths(Follower follower) {
                Path1 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(123.538, 122.780),

                                        new Pose(83.995, 83.067)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(37), Math.toRadians(45))

                        .build();

                Path2 = follower.pathBuilder().addPath(
                                new BezierCurve(
                                        new Pose(83.995, 83.067),
                                        new Pose(99.857, 101.228),
                                        new Pose(120.267, 97.044)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(45), Math.toRadians(-90))

                        .build();
            }
        }
        }
    }
}
