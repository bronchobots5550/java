package frc.team5550.robot.auto.routines;

import frc.team5550.robot.Elevator;
//import frc.team5550.robot.auto.ForwardStep;
import frc.team5550.robot.auto.AutoDriveRoutine;
import frc.team5550.robot.auto.TurnStep;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;

public class RoutineTurn90 extends AutoDriveRoutine {

	public RoutineTurn90(TalonSRX leftDrive, TalonSRX rightDrive, Spark intakeLeft, Spark intakeRight, AHRS gyro, DigitalInput cubeSwitch, Elevator turboLift) {
		super(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift);
		this.addStep(new TurnStep(this, 90));
	}

}
