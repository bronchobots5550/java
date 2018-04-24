package frc.team5550.robot.auto;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ForwardStep extends Step{

	int stepID;
	AutoDriveRoutine routine;
	TalonSRX leftDrive;
	TalonSRX rightDrive;
	int inchesLeft;
	int inchesRight;

	public ForwardStep(AutoDriveRoutine routine, int inchesLeft, int inchesRight) {
		super(routine);
		leftDrive = routine.leftDrive;
		rightDrive = routine.rightDrive;
		this.inchesLeft = inchesLeft;
		this.inchesRight = inchesRight;
	}
	
	public ForwardStep(AutoDriveRoutine routine, int inches) {
		super(routine);
		leftDrive = routine.leftDrive;
		rightDrive = routine.rightDrive;
		this.inchesLeft = inches;
		this.inchesRight = inches;
	}
	
	public boolean run() {
		//SmartDashboard.putNumber("Left Pos", leftDrive.getSelectedSensorPosition(0));
		//SmartDashboard.putNumber("Right Pos", rightDrive.getSelectedSensorPosition(0));
		if (resetSensors == false) {
			leftDrive.setSelectedSensorPosition(0, 0, 10);
			rightDrive.setSelectedSensorPosition(0, 0, 10);
			resetSensors = true;
		}
		if (driveForward(inchesLeft, inchesRight)) {
			leftDrive.set(ControlMode.PercentOutput, 0);
			rightDrive.set(ControlMode.PercentOutput, 0);
			return true;
		}
		return false;
	}
	
	public boolean driveForward(double inchesLeft, double inchesRight) {
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
		leftDrive.config_kP(0, .35, 10);
		rightDrive.config_kP(0, .35, 10);
		rightDrive.config_kF(0, .25, 10);
		leftDrive.config_kF(0, .25, 10);
		leftDrive.set(ControlMode.MotionMagic, -posLeft);
		rightDrive.set(ControlMode.MotionMagic, -posRight);
		SmartDashboard.putNumber("Drive Forward pos left", -posLeft);
		SmartDashboard.putNumber("Drive Forward pos right", -posRight);
		if(Math.abs(leftDrive.getSelectedSensorVelocity(0)) < 10 && Math.abs(rightDrive.getSelectedSensorVelocity(0)) < 10 && ((Math.abs(posLeft) * 0.50) - Math.abs(curPosLeft) < 200) && ((Math.abs(posRight) * 0.50) - Math.abs(curPosRight) < 200)) return true;
		return Math.abs(curPosLeft + posLeft) < 200 && Math.abs(curPosRight + posRight) < 200;
	}

}
