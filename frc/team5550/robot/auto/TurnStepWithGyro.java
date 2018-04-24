package frc.team5550.robot.auto;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.GenericHID.Hand;

public class TurnStepWithGyro extends Step {

	double dest_angle;
	Hand hand;
	int stepID;
	TalonSRX leftDrive;
	TalonSRX rightDrive;
	AHRS gyro;
	double angleDistance;
	
	public TurnStepWithGyro(AutoDriveRoutine routine, double dest_angle, Hand hand) {
		super(routine);
		this.dest_angle = dest_angle;
		this.hand = hand;
		this.leftDrive = routine.leftDrive;
		this.rightDrive = routine.rightDrive;
		this.gyro = routine.gyro;
		this.angleDistance = distanceFromAngle(gyro.getAngle(), dest_angle);
	}

	@Override
	public boolean run() {
		//SmartDashboard.putNumber("Left Pos", leftDrive.getSelectedSensorPosition(0));
				//SmartDashboard.putNumber("Right Pos", rightDrive.getSelectedSensorPosition(0));
				if (resetSensors == false) {
					leftDrive.setSelectedSensorPosition(0, 0, 10);
					rightDrive.setSelectedSensorPosition(0, 0, 10);
					resetSensors = true;
				}
				if (turn(angleDistance)) {
					leftDrive.set(ControlMode.PercentOutput, 0);
					rightDrive.set(ControlMode.PercentOutput, 0);
					
					return true;
				}
				return false;
	}
	
	public boolean turn(double angleDistance) {
		TalonSRX turnDrive = (Hand.kLeft == hand) ? leftDrive : rightDrive;
		double marginError = 3;
		double inches = getInchesFromAngle(angleDistance);
		double gearRatio = 30.00 / 24.00;
		double circumf = (3.25 * Math.PI);
		double wheelRotate = inches / circumf;
		double motorRotate = wheelRotate / gearRatio;
		double pos = motorRotate * 4096;
		turnDrive.config_kP(0, .35, 10);
		turnDrive.config_kF(0, .25, 10);
		turnDrive.set(ControlMode.MotionMagic, pos);
		return (distanceFromAngle(gyro.getAngle(), dest_angle) < marginError);
	}
	
	public double getInchesFromAngle(double angle) {
		return angle * (88/180);
	}
	
	public double distanceFromAngle(double startAngle, double destAngle) {
		return -Math.abs(startAngle-destAngle);
	}

}
