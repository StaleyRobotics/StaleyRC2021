package frc.robot.commands.auto;

import static frc.robot.Constants.IntakeConstants.defaultIntakePower;

import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.intake.RunIntake;
import frc.robot.commands.intake.ToggleJoint;
import frc.robot.commands.shooter.ShootBallsOpenLoop;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Vision;

/**
 * Assumes someone else took our spot. Shoots 3 balls while off centered. Moves to trench to gather
 * 3 more balls. Turns and moves back to face generator and shoots collected balls.
 */
public class LeftSixBall extends LowGearAuto {

  private DriveTrain drive;
  private Vision vision;

  public LeftSixBall() {

    drive = DriveTrain.getInstance();
    vision = Vision.getInstance();

    Trajectory trajectoryForward = TrajectoryGenerator.generateTrajectory(
        drive.getPoseListFromPathWeaverJson("SpotJackedStart"),
        drive.getTrajectoryConfig(false));

    Trajectory trajectoryForwardContinue = TrajectoryGenerator.generateTrajectory(
        drive.getPoseListFromPathWeaverJson("SpotJackedEnd"),
        drive.getTrajectoryConfig(false));

    addCommands(
        new InstantCommand(drive::resetOdometry, drive),
        new InstantCommand(drive::zeroEncoder, drive),
        new ShootBallsOpenLoop(vision.calculateDistance(vision.getPitch())),
        drive.getAutonomousCommandFromTrajectory(trajectoryForward),
        new ToggleJoint(0.5),
        new RunIntake(defaultIntakePower),
        drive.getAutonomousCommandFromTrajectory(trajectoryForwardContinue),
        new ShootBallsOpenLoop(vision.calculateDistance(vision.getPitch()))
    );
  }
}
