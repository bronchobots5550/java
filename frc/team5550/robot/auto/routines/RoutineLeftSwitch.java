package frc.team5550.robot.auto.routines;

import frc.team5550.robot.Elevator;
import frc.team5550.robot.auto.AutoDriveRoutine;
import frc.team5550.robot.auto.ForwardStep;
import frc.team5550.robot.auto.ForwardStepNC;
import frc.team5550.robot.auto.ForwardWithElevatorStep;
import frc.team5550.robot.auto.ForwardWithIntakeStep;
import frc.team5550.robot.auto.IntakeStep;
import frc.team5550.robot.auto.TurnStep;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;

public class RoutineLeftSwitch extends AutoDriveRoutine{

	public RoutineLeftSwitch(TalonSRX leftDrive, TalonSRX rightDrive, Spark intakeLeft, Spark intakeRight, AHRS gyro, DigitalInput cubeSwitch, Elevator turboLift) {
		super(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift);

		this.addStep(new ForwardWithElevatorStep(this, 190,190, Elevator.SWITCH_HEIGHT, 0.0));
		this.addStep(new ForwardStepNC(this,0, -44));
		this.addStep(new ForwardStep(this,54, 54));
		this.addStep(new IntakeStep(this, true, 400));
		this.addStep(new ForwardWithElevatorStep(this, -45,-45, Elevator.BASE_HEIGHT, 0.75));
	//	this.addStep(new ForwardStep(this,40, 76));
	//	this.addStep(new ForwardWithIntakeStep(this, 20,20, false, 0 ));
	//	this.addStep(new ForwardWithElevatorStep(this, 0,-85,Elevator.SCALE_HEIGHT, .025 ));
	//	this.addStep(new ForwardStep(this, 50,55));		
	//	this.addStep(new IntakeStep(this, true, 400));
		
/*		this.addStep(new ForwardWithElevatorStep(this, 306,276, Elevator.SCALE_HEIGHT, 0.5));
		this.addStep(new IntakeStep(this, true, 400));
		this.addStep(new ForwardWithElevatorStep(this, -10,-45, Elevator.BASE_HEIGHT, 0.75));
		this.addStep(new ForwardStep(this, 86,60));
		this.addStep(new ForwardWithIntakeStep(this, 20,20, false, 0 ));
		this.addStep(new ForwardWithElevatorStep(this, -80,0,Elevator.SCALE_HEIGHT, .025 ));
		this.addStep(new ForwardStep(this, 55,55));		
		this.addStep(new IntakeStep(this, true, 400));
*/		
	}

}
