package com.example.meepmeeptool;

import static com.example.meepmeeptool.DriveConstants.MAX_ACCEL;
import static com.example.meepmeeptool.DriveConstants.MAX_ANG_ACCEL;
import static com.example.meepmeeptool.DriveConstants.MAX_ANG_VEL;
import static com.example.meepmeeptool.DriveConstants.MAX_VEL;
import static com.example.meepmeeptool.DriveConstants.TRACK_WIDTH;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class RightMid {
    public static final Pose2d START_POSE = new Pose2d(31, -63.5, Math.toRadians(-90));
    public static final Pose2d START_POSE_A = new Pose2d(36, -48, Math.toRadians(180));
    public static final Pose2d START_POSE_B = new Pose2d(36, -24, Math.toRadians(-90));
    public static final Pose2d START_POSE_C = new Pose2d(36, -15, Math.toRadians(225));
    public static final Pose2d SCORE_POSE = new Pose2d(28.5, -19, Math.toRadians(225));
    public static final Pose2d CONE_POSE = new Pose2d(57.5, -11.25, Math.toRadians(180));
    public static final Pose2d ZONE_ONE = new Pose2d(59, -12, Math.toRadians(0));
    public static final Pose2d ZONE_TWO = new Pose2d(36, -12, Math.toRadians(90));
    public static final Pose2d ZONE_THREE = new Pose2d(12, -12, Math.toRadians(90));

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setDimensions(13, 13)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(MAX_VEL, MAX_ACCEL, MAX_ANG_VEL, MAX_ANG_ACCEL, TRACK_WIDTH)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(START_POSE)
                                .setReversed(true)
                                .splineToSplineHeading(START_POSE_A, Math.toRadians(90))
                                .lineToSplineHeading(START_POSE_B)
                                .splineToSplineHeading(START_POSE_C, Math.toRadians(90))
                                .splineToConstantHeading(SCORE_POSE.vec(), Math.toRadians(-45))
                                .waitSeconds(.7)
                                .splineTo(CONE_POSE.vec(), Math.toRadians(0))
                                .waitSeconds(.7)
                                .setReversed(false)
                                .splineTo(SCORE_POSE.vec(), SCORE_POSE.getHeading())
                                .waitSeconds(.7)
                                .setReversed(true)
                                .splineTo(CONE_POSE.vec(), Math.toRadians(0))
                                .waitSeconds(.7)
                                .setReversed(false)
                                .splineTo(SCORE_POSE.vec(), SCORE_POSE.getHeading())
                                .waitSeconds(.7)
                                .setReversed(true)
                                .splineTo(CONE_POSE.vec(), Math.toRadians(0))
                                .waitSeconds(.7)
                                .setReversed(false)
                                .splineTo(SCORE_POSE.vec(), SCORE_POSE.getHeading())
                                .waitSeconds(.7)
                                .setReversed(true)
                                .splineTo(CONE_POSE.vec(), Math.toRadians(0))
                                .waitSeconds(.7)
                                .setReversed(false)
                                .splineTo(SCORE_POSE.vec(), SCORE_POSE.getHeading())
                                .waitSeconds(.7)
                                .setReversed(true)
                                .splineTo(CONE_POSE.vec(), Math.toRadians(0))
                                .waitSeconds(.7)
                                .setReversed(false)
                                .splineTo(SCORE_POSE.vec(), SCORE_POSE.getHeading())
                                .waitSeconds(.7)
//						.lineToLinearHeading(ZONE_TWO)
//						.setReversed(true)
//						.splineToSplineHeading(ZONE_ONE, Math.toRadians(180))
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
