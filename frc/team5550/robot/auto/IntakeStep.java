package frc.team5550.robot.auto;

import frc.team5550.robot.Robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;

public class IntakeStep extends Step {

	Spark intakeLeft;
	Spark intakeRight;
	DigitalInput cubeSwitch;
	long actionTime;
	long endTime = 0;
	boolean switchPresent;
	Spark intakeTop;

	public IntakeStep(AutoDriveRoutine routine, boolean out, long actionTime) {
		super(routine);
		this.intakeLeft = this.routine.intakeLeft;
		this.intakeRight = this.routine.intakeRight;
		this.actionTime = actionTime;
		this.switchPresent = true;
		//this.intakeTop = new Spark(0);
	}

	public IntakeStep(AutoDriveRoutine routine, boolean out) {
		super(routine);
		this.intakeLeft = this.routine.intakeLeft;
		this.intakeRight = this.routine.intakeRight;
		this.switchPresent = true;
		this.cubeSwitch = this.routine.cubeSwitch;
		this.actionTime = 100;
	}

	@Override
	public boolean run() {
		if (resetSensors == false) {
			resetSensors = true;
			endTime = System.currentTimeMillis() + actionTime;
		}
		if (autoIntake(true, endTime)) {
			intakeLeft.set(0);
			intakeRight.set(0);
			Robot.intakeTop.set(0);
			//intakeTop.free();
			return true;
		}
		return false;
	}

	public boolean autoIntake(boolean out, long endTime) {
		if (out) {
			intakeLeft.set(-.6);
			intakeRight.set(.6);
			Robot.intakeTop.set(-.3);
		} else {
			if (!switchPresent) {
				if (System.currentTimeMillis() + actionTime + 300 < endTime) {
					intakeLeft.set(.3);
					intakeRight.set(-.6);
					Robot.intakeTop.set(.5);
				} else {
					intakeLeft.set(.6);
					intakeRight.set(-.6);
					Robot.intakeTop.set(.5);
				}
			} else {
				if (!cubeSwitch.get()) {
					intakeLeft.set(-.3);
					intakeRight.set(-.6);
					Robot.intakeTop.set(.5);
				} else {
					intakeLeft.set(.4);
					intakeRight.set(-.4);
					Robot.intakeTop.set(.5);
				}
			}
		}

		return (endTime - System.currentTimeMillis() < 0 && (switchPresent && (cubeSwitch.get() && true))) || (endTime - System.currentTimeMillis() < 0 && !switchPresent && true);

	}

}
