package org.firstinspires.ftc.teamcode.compCode.SubsystemsAndDriveSetup;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Loader extends SubsystemBase {
    private final CRServo loaderright;
    private final CRServo loaderleft;

    public Loader(HardwareMap hardwareMap){
        loaderright = hardwareMap.get(CRServo.class, ("Right Loader"));
        loaderright.setDirection(CRServo.Direction.REVERSE);
        loaderleft = hardwareMap.get(CRServo.class, ("Left Loader"));
    }

    public void setPower(double power){
        loaderleft.setPower(power);
        loaderright.setPower(power);
    }
    public void stop(){setPower(0);}

    public void on() { setPower(1.0); }









}
