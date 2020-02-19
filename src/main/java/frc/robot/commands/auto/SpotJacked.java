package frc.robot.commands.auto;

import static frc.robot.Constants.IntakeConstants.defaultIntakePower;

import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.intake.RunIntake;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Intake;

public class SpotJacked extends SequentialCommandGroup {

  private DriveTrain drive;
  private Intake intake;

public SpotJacked() {

  drive = DriveTrain.getInstance();
  intake = Intake.getInstance();

  Trajectory trajectoryForward = TrajectoryGenerator.generateTrajectory(
      drive.getPoseListFromPathWeaverJson("SpotJackedStart"),
      drive.getTrajectoryConfig(false));

  Trajectory trajectoryForwardContinue = TrajectoryGenerator.generateTrajectory(
      drive.getPoseListFromPathWeaverJson("SpotJackedEnd"),
      drive.getTrajectoryConfig(false));

  addCommands(
      new InstantCommand(drive::zeroEncoder, drive),
      //shooter command
      drive.getAutonomousCommandFromTrajectory(trajectoryForward),
      new RunIntake(defaultIntakePower),
      drive.getAutonomousCommandFromTrajectory(trajectoryForwardContinue)
      //shooter command
  );
  }
}