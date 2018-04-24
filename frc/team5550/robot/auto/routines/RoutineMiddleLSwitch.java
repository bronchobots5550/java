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

public class RoutineMiddleLSwitch extends AutoDriveRoutine{

	public RoutineMiddleLSwitch(TalonSRX leftDrive, TalonSRX rightDrive, Spark intakeLeft, Spark intakeRight, AHRS gyro, DigitalInput cubeSwitch, Elevator turboLift) {
		super(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift);

		this.addStep(new ForwardWithElevatorStep(this, 10,40, Elevator.SWITCH_HEIGHT, 0.85));
		this.addStep(new ForwardStep(this,70));
		this.addStep(new ForwardStep(this,40,10));
		this.addStep(new ForwardStep(this,23));
		this.addStep(new IntakeStep (this, true, 400));
		
/*		this.addStep(new ForwardWithElevatorStep(this, 40,10, Elevator.SWITCH_HEIGHT, 0.85));
		this.addStep(new ForwardStep(this,70,70));
		this.addStep(new ForwardStep(this,10,40));
		this.addStep(new ForwardWithIntakeStep(this, 23,23, true, .80 ));
*/		
	}

}
