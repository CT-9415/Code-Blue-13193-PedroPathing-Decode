package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.ThreeWheelConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            /*.mass(mass)
            .forwardZeroPowerAcceleration(deceleration)
            .lateralZeroPowerAcceleration(deceleration)
            /* uncomment if using dual PID
            .useSecondaryTranslationalPIDF(true)
            .useSecondaryHeadingPIDF(true)
            .useSecondaryDrivePIDF(true)

            .secondaryTranslationalPIDFCoefficients()
            .secondaryHeadingPIDFCoefficients()
            .secondaryDrivePIDFCoefficients()

            .translationalPIDFCoefficients(new PIDFCoefficients(PIDFValues))
            .headingPIDFCoefficients(new PIDFCoefficients(PIDFvalues))
            .centripetalScaling()*/



            ;

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            //change to motor names in driver hub
            .rightFrontMotorName("rf")
            .rightRearMotorName("rb")
            .leftRearMotorName("lb")
            .leftFrontMotorName("lf")
            .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            /*.xVelocity(velocity)
            .yVelocity(velocity)*/


            ;


    public static ThreeWheelConstants localizerConstants = new ThreeWheelConstants()
            /*.forwardTicksToInches(multiplyer)
            .strafeTicksToInches(multiplyer)
            .turnTicksToInches(multiplyer)*/
            //change for offset from center of robot to center of deadwheels
            .leftPodY(1)
            .rightPodY(-1)
            .strafePodX(-2.5)//perpendicular deadwheel
            //change to name of motor that plugged into same port as deadwheels
            .leftEncoder_HardwareMapName("rb")
            .rightEncoder_HardwareMapName("lb")
            .strafeEncoder_HardwareMapName("rf")
            .leftEncoderDirection(Encoder.FORWARD)
            .rightEncoderDirection(Encoder.FORWARD)
            .strafeEncoderDirection(Encoder.FORWARD)
           /* .xVelocity()*/

    ;



    public static PathConstraints pathConstraints = new PathConstraints(
            0.99,
            100,
            1,
            1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .threeWheelLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .build();
    }
}
