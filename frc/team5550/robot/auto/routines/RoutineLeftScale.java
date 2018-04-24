package frc.team5550.robot.auto.routines;

import frc.team5550.robot.Elevator;
import frc.team5550.robot.auto.AutoDriveRoutine;
import frc.team5550.robot.auto.ForwardStep;
import frc.team5550.robot.auto.ForwardWithElevatorStep;
import frc.team5550.robot.auto.ForwardWithElevatorStepNC;
import frc.team5550.robot.auto.ForwardWithIntakeStep;
import frc.team5550.robot.auto.IntakeStep;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;

public class RoutineLeftScale extends AutoDriveRoutine{

	public RoutineLeftScale(TalonSRX leftDrive, TalonSRX rightDrive, Spark intakeLeft, Spark intakeRight, AHRS gyro, DigitalInput cubeSwitch, Elevator turboLift) {
		super(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift);

		this.addStep(new ForwardWithElevatorStep(this, 294,264, Elevator.SCALE_HEIGHT, 0.2));
		this.addStep(new IntakeStep(this, true, 400));
		this.addStep(new ForwardWithElevatorStepNC(this, -50,-75, Elevator.BASE_HEIGHT, 0.75));
		this.addStep(new ForwardStep(this,55, 20));
		this.addStep(new ForwardWithIntakeStep(this, 50,30, false, 0 ));
		this.addStep(new ForwardWithElevatorStepNC(this,-92, -22,Elevator.SCALE_HEIGHT, .025 ));
		//this.addStep(new ForwardStep(this, 22,22));
		//this.addStep(new IntakeStep(this, true, 400));
		//this.addStep(new ForwardWithElevatorStepNC(this, -20, -20, Elevator.SCALE_HEIGHT, 0.5));
		//this.addStep(new ForwardWithElevatorStep(this, 0, Elevator.BASE_HEIGHT, 0));
		
		
		
/*		this.addStep(new ForwardWithElevatorStep(this, 264,284, Elevator.SCALE_HEIGHT, 0.5));
		this.addStep(new IntakeStep(this, true, 400));
		this.addStep(new ForwardWithElevatorStep(this, -45,-10, Elevator.BASE_HEIGHT, 0.75));
		this.addStep(new ForwardStep(this, 60,86));
		this.addStep(new ForwardWithIntakeStep(this, 20,20, false, 0 ));
		this.addStep(new ForwardWithElevatorStep(this, 0,-80,Elevator.SCALE_HEIGHT, .025 ));
		this.addStep(new ForwardStep(this, 55,55));		
		this.addStep(new IntakeStep(this, true, 400));
*/		
	}

}
