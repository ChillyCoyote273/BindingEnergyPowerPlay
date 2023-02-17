package org.firstinspires.ftc.teamcode.Autonomous.Competition;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SelectCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.checkerframework.checker.units.qual.A;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Framework.Commands.AsyncDelay;
import org.firstinspires.ftc.teamcode.Framework.Commands.Claw.CloseClaw;
import org.firstinspires.ftc.teamcode.Framework.Commands.Claw.OpenClaw;
import org.firstinspires.ftc.teamcode.Framework.Commands.Drive.TrajectoryCommand;
import org.firstinspires.ftc.teamcode.Framework.Commands.Flipper.FlipIn;
import org.firstinspires.ftc.teamcode.Framework.Commands.Flipper.FlipOut;
import org.firstinspires.ftc.teamcode.Framework.Commands.SavePosition;
import org.firstinspires.ftc.teamcode.Framework.Commands.Slide.SetSlidePosition;
import org.firstinspires.ftc.teamcode.Framework.Utilities.AutoEndPose;
import org.firstinspires.ftc.teamcode.Framework.subsystems.AutoDrive;
import org.firstinspires.ftc.teamcode.Framework.subsystems.Camera;
import org.firstinspires.ftc.teamcode.Framework.subsystems.Claw;
import org.firstinspires.ftc.teamcode.Framework.subsystems.Flipper;
import org.firstinspires.ftc.teamcode.Framework.subsystems.LinearSlide;

import java.util.HashMap;

@Config
@Autonomous(preselectTeleOp = "MainTeleop")
public class RightMid extends CommandOpMode {
    public static final Pose2d START_POSE = new Pose2d(31, -63.5, Math.toRadians(-90));
    public static final Pose2d START_POSE_A = new Pose2d(35, -48, Math.toRadians(180));
    public static final Pose2d START_POSE_B = new Pose2d(36, -24, Math.toRadians(-90));
    public static final Pose2d START_POSE_C = new Pose2d(37, -14, Math.toRadians(225));

    public static Pose2d SCORE_POSE_ZERO = new Pose2d(28.5, -15, Math.toRadians(225));
    public static Pose2d SCORE_POSE_ONE = new Pose2d(28.5, -14.75, Math.toRadians(225));
    public static Pose2d SCORE_POSE_TWO = new Pose2d(28.5, -14.5, Math.toRadians(225));
    public static Pose2d SCORE_POSE_THREE = new Pose2d(28.5, -14.25, Math.toRadians(225));
    public static Pose2d SCORE_POSE_FOUR = new Pose2d(28.5, -14.2, Math.toRadians(225));
    public static Pose2d SCORE_POSE_FIVE = new Pose2d(28.5, -14, Math.toRadians(225));

    public static Pose2d CONE_POSE_ONE = new Pose2d(60, -10.3, Math.toRadians(0));
    public static Pose2d CONE_POSE_TWO = new Pose2d(60, -10.3, Math.toRadians(0));
    public static Pose2d CONE_POSE_THREE = new Pose2d(60, -10.2, Math.toRadians(0));
    public static Pose2d CONE_POSE_FOUR = new Pose2d(60, -10.1, Math.toRadians(0));
    public static Pose2d CONE_POSE_FIVE = new Pose2d(60, -10, Math.toRadians(0));

    public static Pose2d ZONE_ONE = new Pose2d(60, -11, Math.toRadians(0));
    public static Pose2d ZONE_TWO = new Pose2d(36, -11, Math.toRadians(90));

    public static Pose2d ZONE_THREE = new Pose2d(9, -11, Math.toRadians(90));

    Telemetry telemetry;

    AutoDrive drive;
    LinearSlide slide;
    Flipper flipper;
    Claw claw;
    Camera camera;

    public static int signalSide = 1;

    private Command cycle(int coneHeight, Trajectory junctionToCones, Trajectory conesToJunction) {
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new SetSlidePosition(slide, LinearSlide.CONE_STACK[coneHeight]),
                        new SequentialCommandGroup(
                                new AsyncDelay(0.07),
                                new OpenClaw(claw),
                                new AsyncDelay(0.07),
                                new FlipIn(flipper)
                        ),
                        new SequentialCommandGroup(
                                new AsyncDelay(0.07),
                                new TrajectoryCommand(drive, junctionToCones)
                        )
                ),
                new CloseClaw(claw),
                new AsyncDelay(0.07),
                new ParallelCommandGroup(
                        new SetSlidePosition(slide, LinearSlide.LOW),
                        new SequentialCommandGroup(
                                new AsyncDelay(0.07),
                                new ParallelCommandGroup(
                                        new TrajectoryCommand(drive, conesToJunction),
                                        new SequentialCommandGroup(
                                                new AsyncDelay(0.07),
                                                new ParallelCommandGroup(
                                                        new SetSlidePosition(slide, LinearSlide.TWO_CONE),
                                                        new SequentialCommandGroup(
                                                                new AsyncDelay(0.3),
                                                                new ParallelCommandGroup(
                                                                        new SetSlidePosition(slide, LinearSlide.MEDIUM),
                                                                        new SequentialCommandGroup(
                                                                                new AsyncDelay(0.17),
                                                                                new FlipOut(flipper)
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    @Override
    public void initialize() {
        telemetry = new MultipleTelemetry(super.telemetry,
                FtcDashboard.getInstance().getTelemetry());

        drive = new AutoDrive(hardwareMap, START_POSE);
        slide = new LinearSlide(hardwareMap, telemetry);
        flipper = new Flipper(hardwareMap, telemetry);
        claw = new Claw(hardwareMap);
        claw.open();
        camera = new Camera(hardwareMap);


        Trajectory junctionToConesOne = drive.trajectoryBuilder(SCORE_POSE_ZERO, true)
                .splineTo(CONE_POSE_ONE.vec(), Math.toRadians(0))
                .build();
        Trajectory conesToJunctionOne = drive.trajectoryBuilder(
                        junctionToConesOne.end(), Math.toRadians(180))
                .splineTo(SCORE_POSE_ONE.vec(), SCORE_POSE_ONE.getHeading())
                .build();

        Trajectory junctionToConesTwo = drive.trajectoryBuilder(conesToJunctionOne.end(), true)
                .splineTo(CONE_POSE_TWO.vec(), Math.toRadians(0))
                .build();
        Trajectory conesToJunctionTwo = drive.trajectoryBuilder(
                        junctionToConesTwo.end(), Math.toRadians(180))
                .splineTo(SCORE_POSE_TWO.vec(), SCORE_POSE_TWO.getHeading())
                .build();

        Trajectory junctionToConesThree = drive.trajectoryBuilder(conesToJunctionTwo.end(), true)
                .splineTo(CONE_POSE_THREE.vec(), Math.toRadians(0))
                .build();
        Trajectory conesToJunctionThree = drive.trajectoryBuilder(
                        junctionToConesThree.end(), Math.toRadians(180))
                .splineTo(SCORE_POSE_THREE.vec(), SCORE_POSE_THREE.getHeading())
                .build();

        Trajectory junctionToConesFour = drive.trajectoryBuilder(conesToJunctionThree.end(), true)
                .splineTo(CONE_POSE_FOUR.vec(), Math.toRadians(0))
                .build();
        Trajectory conesToJunctionFour = drive.trajectoryBuilder(
                        junctionToConesFour.end(), Math.toRadians(180))
                .splineTo(SCORE_POSE_FOUR.vec(), SCORE_POSE_FOUR.getHeading())
                .build();

        Trajectory junctionToConesFive = drive.trajectoryBuilder(conesToJunctionFour.end(), true)
                .splineTo(CONE_POSE_FIVE.vec(), Math.toRadians(0))
                .build();
        Trajectory conesToJunctionFive = drive.trajectoryBuilder(
                        junctionToConesFive.end(), Math.toRadians(180))
                .splineTo(SCORE_POSE_FIVE.vec(), SCORE_POSE_FIVE.getHeading())
                .build();


        TrajectoryCommand startToJunction = new TrajectoryCommand(drive,
                drive.trajectoryBuilder(START_POSE, Math.toRadians(90))
                        .splineToSplineHeading(START_POSE_A, Math.toRadians(90))
                        .splineToSplineHeading(START_POSE_B, Math.toRadians(90))
                        .splineToSplineHeading(START_POSE_C, Math.toRadians(90))
                        .splineToConstantHeading(SCORE_POSE_ZERO.vec(), Math.toRadians(-45))
                        .build()
        );
        TrajectoryCommand parkOne = new TrajectoryCommand(drive,
                drive.trajectoryBuilder(SCORE_POSE_FIVE, true)
                        .splineToSplineHeading(ZONE_ONE, Math.toRadians(180))
                        .build()
        );
        TrajectoryCommand parkTwo = new TrajectoryCommand(drive,
                drive.trajectoryBuilder(SCORE_POSE_FIVE)
                        .lineToLinearHeading(ZONE_TWO)
                        .build()
        );
        TrajectoryCommand parkThree = new TrajectoryCommand(drive,
                drive.trajectoryBuilder(SCORE_POSE_FIVE, Math.toRadians(0))
                        .splineToSplineHeading(ZONE_TWO, Math.toRadians(0))
                        .splineToSplineHeading(ZONE_THREE, Math.toRadians(90))
                        .build()
        );


        ParallelCommandGroup setUpScoring = new ParallelCommandGroup(
                startToJunction,
                new CloseClaw(claw),
                new SequentialCommandGroup(
                        new AsyncDelay(.15),
                        new ParallelCommandGroup(
                                new SetSlidePosition(slide, LinearSlide.MEDIUM),
                                new FlipOut(flipper)
                        )
                )
        );
        SelectCommand park = new SelectCommand(
                new HashMap<Object, Command>() {{
                    put(1, parkOne);
                    put(2, parkTwo);
                    put(3, parkThree);
                }},
                () -> signalSide
        );
        ParallelCommandGroup dropForPark = new ParallelCommandGroup(
                new SetSlidePosition(slide, LinearSlide.ONE_CONE),
                new SequentialCommandGroup(
                        new AsyncDelay(0.15),
                        new OpenClaw(claw),
                        new ParallelCommandGroup(
                                park,
                                new SequentialCommandGroup(
                                        new AsyncDelay(.2),
                                        new FlipIn(flipper)
                                )
                        )
                )
        );


        SequentialCommandGroup runAuto = new SequentialCommandGroup(
                setUpScoring,
                cycle(5, junctionToConesOne, conesToJunctionOne),
                cycle(4, junctionToConesTwo, conesToJunctionTwo),
                cycle(3, junctionToConesThree, conesToJunctionThree),
                cycle(2, junctionToConesFour, conesToJunctionFour),
                cycle(1, junctionToConesFive, conesToJunctionFive),
                dropForPark,
                new SavePosition(drive::getPoseEstimate)
        );

        register(drive, slide, flipper, claw);

        ElapsedTime time = new ElapsedTime();
        while (!isStarted()) {
            camera.periodic();
            if (time.time() > 0.5) {
                telemetry.addData("Camera", camera.getConfidences());
                telemetry.update();
                time.reset();
            }
        }

        AutoEndPose.setTimer();
        signalSide = camera.getSide();
        camera.stop();

        schedule(runAuto);
    }
}