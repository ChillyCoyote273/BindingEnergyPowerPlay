package org.firstinspires.ftc.teamcode.Framework.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Framework.Utilities.SlideConstants;
import org.firstinspires.ftc.teamcode.Framework.Utilities.SlideController;
import org.firstinspires.ftc.teamcode.Framework.Utilities.SlideState;

public class LinearSlide extends SubsystemBase {
    private HardwareMap hw;
    private String nameMain;
    private MotorEx slideMotor;
    private String nameAux;
    private MotorEx auxillaryMotor;
    private SlideController controller;
    private Telemetry t;

    private boolean usingPID;

    public LinearSlide(final HardwareMap hw, String nameMain, String nameAux, Telemetry t){
        this.hw = hw;
        this.nameMain = nameMain;
        this.nameAux = nameAux;
        this.controller = new SlideController();
        slideMotor = new MotorEx(hw, nameMain);
        slideMotor.setInverted(true);
        slideMotor.encoder.setDirection(Motor.Direction.REVERSE);
        slideMotor.resetEncoder();
        auxillaryMotor = new MotorEx(hw, nameAux);
        this.t = t;
        usingPID = true;
    }

    public int getEncoderCount(){
        return slideMotor.getCurrentPosition();
    }

    public void setPower(double power){
        double position = slideMotor.getCurrentPosition();

        if (position < 0 && power < 0 || position > 3000 && power > 0) {
            power = 0;
        }

        slideMotor.set(power);
        auxillaryMotor.set(power);
    }

    public int getEncoder(){
        return slideMotor.getCurrentPosition();
    }

    @Override
    public void periodic() {
        if (usingPID) {
            int position = slideMotor.getCurrentPosition();
            double power = controller.getPower(position);
            slideMotor.set(power);
            auxillaryMotor.set(power);
        }
    }
}
