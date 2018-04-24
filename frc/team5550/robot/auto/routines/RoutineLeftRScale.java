package frc.team5550.robot.auto.routines;

import frc.team5550.robot.Elevator;
import frc.team5550.robot.auto.AutoDriveRoutine;
import frc.team5550.robot.auto.ForwardStep;
import frc.team5550.robot.auto.ForwardWithElevatorStep;
import frc.team5550.robot.auto.IntakeStep;
import frc.team5550.robot.auto.TurnStep;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;

public class RoutineLeftRScale extends AutoDriveRoutine{

	public RoutineLeftRScale(TalonSRX leftDrive, TalonSRX rightDrive, Spark intakeLeft, Spark intakeRight, AHRS gyro, DigitalInput cubeSwitch, Elevator turboLift) {
		super(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift);
		
		this.addStep(new ForwardStep(this,230,230));
		this.addStep(new TurnStep(this, 90));
		this.addStep(new ForwardWithElevatorStep(this,195,195,Elevator.SCALE_HEIGHT, .80));
		//this.addStep(new TurnStep(this, -90));
		//this.addStep(new ForwardStep(this,25, 35));
		//this.addStep(new IntakeStep(this, true, 400));
		
/*		this.addStep(new ForwardStep(this,235,235));
		this.addStep(new TurnStep(this, 90));
		this.addStep(new ForwardWithElevatorStep(this,195,195,Elevator.SCALE_HEIGHT, .80));
		this.addStep(new TurnStep(this, -90));
		this.addStep(new ForwardStep(this, 40,30));
		this.addStep(new IntakeStep(this, true, 400));
*/
	}

}
