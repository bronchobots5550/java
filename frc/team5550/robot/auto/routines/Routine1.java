package frc.team5550.robot.auto.routines;

import frc.team5550.robot.Elevator;
import frc.team5550.robot.auto.AutoDriveRoutine;
import frc.team5550.robot.auto.ForwardStep;
import frc.team5550.robot.auto.ForwardWithElevatorStep;
import frc.team5550.robot.auto.ForwardWithIntakeStep;
import frc.team5550.robot.auto.IntakeStep;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;

public class Routine1 extends AutoDriveRoutine{

	public Routine1(TalonSRX leftDrive, TalonSRX rightDrive, Spark intakeLeft, Spark intakeRight, AHRS gyro, DigitalInput cubeSwitch, Elevator turboLift) {
		super(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift);

		this.addStep(new ForwardWithElevatorStep(this, 306,276, Elevator.SCALE_HEIGHT, 0.5));
		this.addStep(new IntakeStep(this, true, 400));
		this.addStep(new ForwardWithElevatorStep(this, -10,-45, Elevator.BASE_HEIGHT, 0.75));
		this.addStep(new ForwardStep(this, 86,60));
		this.addStep(new ForwardWithIntakeStep(this, 20,20, false, 0 ));
		
/*		this.addStep(new ForwardWithElevatorStep(this, 276,306, Elevator.SCALE_HEIGHT, 0.5));
		this.addStep(new IntakeStep(this, true, 400));
		this.addStep(new ForwardWithElevatorStep(this, -45,-10, Elevator.BASE_HEIGHT, 0.75));
		this.addStep(new ForwardStep(this, 60,86));
		this.addStep(new ForwardWithIntakeStep(this, 20,20, false, 0 ));
*/
	}

}
