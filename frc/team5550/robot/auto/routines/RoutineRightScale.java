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

public class RoutineRightScale extends AutoDriveRoutine{

	public RoutineRightScale(TalonSRX leftDrive, TalonSRX rightDrive, Spark intakeLeft, Spark intakeRight, AHRS gyro, DigitalInput cubeSwitch, Elevator turboLift) {
		super(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift);

		this.addStep(new ForwardWithElevatorStep(this, 264,294, Elevator.SCALE_HEIGHT, 0.2));
		this.addStep(new IntakeStep(this, true, 400));
		this.addStep(new ForwardWithElevatorStepNC(this, -75,-50, Elevator.BASE_HEIGHT, 0.75));
		this.addStep(new ForwardStep(this,20, 55));
		this.addStep(new ForwardWithIntakeStep(this, 30,50, false, 0 ));
		this.addStep(new ForwardWithElevatorStepNC(this,-22, -92,Elevator.SCALE_HEIGHT, .025 ));
		//this.addStep(new ForwardStep(this,40, 76));
		//this.addStep(new ForwardWithIntakeStep(this, 20,20, false, 0 ));
		//this.addStep(new ForwardWithElevatorStepNC(this, 0,-85,Elevator.SCALE_HEIGHT, .025 ));
		//this.addStep(new ForwardStep(this, 50,55));		
		//this.addStep(new IntakeStep(this, true, 400));
		
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
