package org.firstinspires.ftc.teamcode.Framework.CommandGroups;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.Framework.Commands.AsyncDelay;
import org.firstinspires.ftc.teamcode.Framework.Commands.Claw.OpenClaw;
import org.firstinspires.ftc.teamcode.Framework.Commands.Flipper.FlipIn;
import org.firstinspires.ftc.teamcode.Framework.Commands.Slide.SetSlidePosition;
import org.firstinspires.ftc.teamcode.Framework.subsystems.Claw;
import org.firstinspires.ftc.teamcode.Framework.subsystems.Flipper;
import org.firstinspires.ftc.teamcode.Framework.subsystems.LinearSlide;

public class DropCone extends ParallelCommandGroup {
	public DropCone(LinearSlide slide, Flipper flipper, Claw claw) {
		addCommands(
				new SetSlidePosition(slide, LinearSlide.ONE_CONE),
				new SequentialCommandGroup(
						new AsyncDelay(0.2),
						new OpenClaw(claw),
						new AsyncDelay(0.15),
						new FlipIn(flipper)
				)
		);
	}
}
