package frc.team5550.robot.auto;

import frc.team5550.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ForwardWithIntakeStep extends Step {

	int stepID;
	AutoDriveRoutine routine;
	TalonSRX leftDrive;
	TalonSRX rightDrive;
	Spark intakeLeft;
	Spark intakeRight;
	int inchesLeft;
	int inchesRight;
	boolean outtake;
	double activationPct;
	long endTime;
	DigitalInput cubeSwitch;
	boolean cubed = true;
	long actionTime = 300;

	public ForwardWithIntakeStep(AutoDriveRoutine routine, int inchesLeft, int inchesRight, boolean outtake,
			double activationPct) {
		super(routine);
		leftDrive = routine.leftDrive;
		rightDrive = routine.rightDrive;
		intakeLeft = routine.intakeLeft;
		intakeRight = routine.intakeRight;
		this.inchesLeft = inchesLeft;
		this.inchesRight = inchesRight;
		this.outtake = outtake;
		this.activationPct = activationPct;
		this.cubeSwitch = routine.cubeSwitch;
	}

	public ForwardWithIntakeStep(AutoDriveRoutine routine, int inches, boolean outtake, double activationPct) {
		super(routine);
		leftDrive = routine.leftDrive;
		rightDrive = routine.rightDrive;
		intakeLeft = routine.intakeLeft;
		intakeRight = routine.intakeRight;
		this.inchesLeft = inches;
		this.inchesRight = inches;
		this.outtake = outtake;
		this.activationPct = activationPct;
		this.cubeSwitch = routine.cubeSwitch;
	}

	public boolean run() {
		// SmartDashboard.putNumber("Left Pos", leftDrive.getSelectedSensorPosition(0));
		// SmartDashboard.putNumber("Right Pos",
		// rightDrive.getSelectedSensorPosition(0));
		// Reset Sensors Block
		if (resetSensors == false) {
			leftDrive.setSelectedSensorPosition(0, 0, 10);
			rightDrive.setSelectedSensorPosition(0, 0, 10);
			resetSensors = true;
		}
		// If function returns true, you've completed step
		if (driveForward(inchesLeft, inchesRight, outtake, activationPct)) {
			leftDrive.set(ControlMode.PercentOutput, 0);
			rightDrive.set(ControlMode.PercentOutput, 0);
			intakeLeft.set(0);
			intakeRight.set(0);
			Robot.intakeTop.set(0);
			return true;
		}
		if (cubed == false) {
			endTime = System.currentTimeMillis() + actionTime;
			cubed = true;
		}
		return false;
	}

	public boolean driveForward(int inchesLeft, int inchesRight, boolean outtake, double activationPct) {
		double gearRatio = 30.00 / 24.00;
		double circumf = (3.25 * 3.14159);
		double wheelRotateLeft = inchesLeft / circumf;
		double motorRotateLeft = wheelRotateLeft / gearRatio;
		double posLeft = motorRotateLeft * 4096;
		double wheelRotateRight = inchesRight / circumf;
		double motorRotateRight = wheelRotateRight / gearRatio;
		double posRight = motorRotateRight * 4096;
		double curPosLeft = leftDrive.getSelectedSensorPosition(0);
		double curPosRight = rightDrive.getSelectedSensorPosition(0);
		boolean hasCube = cubeSwitch.get();
		leftDrive.config_kP(0, .45, 10);
		rightDrive.config_kP(0, .45, 10);
		rightDrive.config_kP(0, 1, 10);
		leftDrive.config_kP(0, 1, 10);
		leftDrive.set(ControlMode.MotionMagic, -posLeft);
		rightDrive.set(ControlMode.MotionMagic, -posRight);
		// SmartDashboard.putNumber("Drive Forward pos left", -posLeft);
		if (posLeft * activationPct + curPosLeft < 200) {
			if (outtake) {
				intakeLeft.set(-.6);
				intakeRight.set(.6);
				Robot.intakeTop.set(-.3);
			} else {

				if (!cubeSwitch.get()) {
					intakeLeft.set(.3);
					intakeRight.set(-.6);
					Robot.intakeTop.set(.5);
				} else {
					intakeLeft.set(.4);
					intakeRight.set(-.4);
					Robot.intakeTop.set(.5);
				}

			}

		}
		return (Math.abs(curPosLeft + posLeft) < 200 && Math.abs(curPosRight + posRight) < 200)
				|| (hasCube && !outtake);
		// && System.currentTimeMillis() > endTime;
	}

}
