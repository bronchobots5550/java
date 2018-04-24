package frc.team5550.robot.auto;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurnStep extends Step {

	int stepID;
	AutoDriveRoutine routine;
	TalonSRX leftDrive;
	TalonSRX rightDrive;
	AHRS gyro;
	double angle;

	public TurnStep(AutoDriveRoutine routine, double angle) {
		super(routine);
		this.leftDrive = routine.leftDrive;
		this.rightDrive = routine.rightDrive;
		this.gyro = routine.gyro;
		this.angle = angle;

	}

	@Override
	public boolean run() {
		/*SmartDashboard.putNumber("Left Pos", leftDrive.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Right Pos", rightDrive.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Left Velocity", leftDrive.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("Right Velocity", rightDrive.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("Gyro", gyro.getAngle());*/
		if (resetSensors == false) {
			leftDrive.setSelectedSensorPosition(0, 0, 10);
			rightDrive.setSelectedSensorPosition(0, 0, 10);
			resetSensors = true;
		}
		if (driveTurnWheel(angle)) {
			leftDrive.set(ControlMode.PercentOutput, 0);
			rightDrive.set(ControlMode.PercentOutput, 0);
			return true;
		}
		return false;
	}

	public boolean driveTurnWheel(double degrees) {
		double pos = (degrees / 90.00) * 8425.00;

		if (pos > 0) {
			leftDrive.config_kP(0, .95, 10);
			leftDrive.config_kF(0, 0.15, 10);
			rightDrive.config_kP(0, .6, 10);
			rightDrive.config_kF(0, 0.15, 10);

		} else {
			leftDrive.config_kP(0, .6, 10);
			leftDrive.config_kF(0, 0.15, 10);
			rightDrive.config_kP(0, .95, 10);
			rightDrive.config_kF(0, 0.15, 10);
		}

		//SmartDashboard.putNumber("Drive Forward pos", pos);
		if (Math.abs(leftDrive.getSelectedSensorPosition(0) + pos) < 50) {
			leftDrive.set(ControlMode.Current, 0);
		} else {
			leftDrive.set(ControlMode.MotionMagic, -pos);
		}

		if (Math.abs(rightDrive.getSelectedSensorPosition(0) - pos) < 50) {
			rightDrive.set(ControlMode.Current, 0);
		} else {
			rightDrive.set(ControlMode.MotionMagic, pos);
		}
		
		if(Math.abs(leftDrive.getSelectedSensorPosition(0)) > (Math.abs(pos) * 0.50) && Math.abs(leftDrive.getSelectedSensorVelocity(0)) < 10 && Math.abs(rightDrive.getSelectedSensorVelocity(0)) < 10) {
			return true;
		}
		
		return false;
	}

}
