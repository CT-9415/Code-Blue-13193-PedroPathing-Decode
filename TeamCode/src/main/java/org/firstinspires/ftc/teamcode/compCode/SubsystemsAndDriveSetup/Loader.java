package org.firstinspires.ftc.teamcode.compCode.SubsystemsAndDriveSetup;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Loader {
    public CRServo loaderright;
    public CRServo loaderleft;

    public void init(HardwareMap hardwareMap){
        loaderright = hardwareMap.get(CRServo.class, ("Right Loader"));
        loaderright.setDirection(CRServo.Direction.REVERSE);
        loaderleft = hardwareMap.get(CRServo.class, ("Left Loader"));
    }


    public void on(){
        loaderleft.setPower(1);
        loaderright.setPower(1);
    }
    public void off(){
        loaderleft.setPower(0);
        loaderright.setPower(0);
    }




}
