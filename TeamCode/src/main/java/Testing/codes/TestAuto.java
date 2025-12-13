package Testing.codes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.pedropathing.util.Timer;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;


@Autonomous
public class TestAuto extends OpMode{

    private Follower follower;


    private Timer pathTimer, opModeTimer;

    public enum PathState {
        //START POSITION_END POSITION
        //DRIVE > MOVEMENT STATE
        //SHOOT > ATTEMPT TO SCORE THE ARTIFACT
        DRIVE_STARTPOS_SHOOTPOS,
        SHOOT_PRELOAD,

        DRIVE_SHOOTPOS_ENDPOS

    }

    PathState pathState;

    private final Pose startPose = new Pose(20.386,122.398,Math.toRadians(138));
    private final Pose shootPose = new Pose(48,96,Math.toRadians(138));
    private final Pose endPose = new Pose(62.480102695763804,110.72657252888318,Math.toRadians(90));

    private PathChain driveStartPosShootPos, driveShootPosEndPos;

    public void buildPaths(){
        //put coordinates for starting pose > ending pose
        driveStartPosShootPos = follower.pathBuilder()
                .addPath(new BezierLine(startPose,shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();
        driveShootPosEndPos = follower.pathBuilder()
                .addPath(new BezierLine(shootPose,endPose))
                .setLinearHeadingInterpolation(shootPose.getHeading(),endPose.getHeading())
                .build();






    }
    public void statePathUpdate() {
        switch (pathState) {
            case DRIVE_STARTPOS_SHOOTPOS:
                follower.followPath(driveStartPosShootPos, true);
                setPathState(PathState.SHOOT_PRELOAD); //reset timer & make new state
                break;
            case SHOOT_PRELOAD:
                // check is follower done it's path
                // and check that 5 seconds has elapsed

                if (!follower.isBusy()&& pathTimer.getElapsedTimeSeconds() > 5){
                    follower.followPath(driveShootPosEndPos,true);
                    setPathState(PathState.DRIVE_SHOOTPOS_ENDPOS);


                }

                break;
            case DRIVE_SHOOTPOS_ENDPOS:
                // all done!
                if (!follower.isBusy()){
                    telemetry.addLine("Done all Paths");
                }
            default:
                telemetry.addLine("No State Commanded");
                break;

        }

    }

    public void setPathState(PathState newState){
        pathState=newState;
        pathTimer.resetTimer();
    }


    @Override
    public void init(){
        pathState = PathState.DRIVE_STARTPOS_SHOOTPOS;
        pathTimer = new Timer();
        opModeTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);
        // TODO add in any other init mechanisms

        buildPaths();
        follower.setPose(startPose);

    }
    public void start() {
        opModeTimer.resetTimer();
        setPathState(pathState);
    }




    @Override
    public void loop(){
        follower.update();
        statePathUpdate();

        telemetry.addData("path state", pathState.toString());
        telemetry.addData("x",follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.addData("Path time", pathTimer.getElapsedTimeSeconds());

    }






}
