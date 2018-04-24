package frc.team5550.robot.auto.routines;

import frc.team5550.robot.Elevator;
import frc.team5550.robot.auto.AutoDriveRoutine;
import frc.team5550.robot.auto.ForwardStep;
import frc.team5550.robot.auto.ForwardWithElevatorStep;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;

public class Routine2 extends AutoDriveRoutine {

	public Routine2(TalonSRX leftDrive, TalonSRX rightDrive, Spark intakeLeft, Spark intakeRight, AHRS gyro, DigitalInput cubeSwitch, Elevator turboLift) {
		super(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift);

		//this.addStep(new ForwardWithElevatorStep(this, 60,45, Elevator.SWITCH_HEIGHT, 0.85));
		this.addStep(new ForwardStep(this, 100,120));

/*		this.addStep(new ForwardWithElevatorStep(this, 45,60, Elevator.SWITCH_HEIGHT, 0.85));
		this.addStep(new ForwardStep(this, 65,50));
*/	
	}

}
