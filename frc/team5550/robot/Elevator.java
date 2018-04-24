package frc.team5550.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.IterativeRobot;

//import com.ctre.CANTalon.FeedbackDevice;
//import com.ctre.CANTalon.TalonControlMode;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Elevator extends IterativeRobot {

	private TalonSRX elevObj;
	public static final int SCALE_HEIGHT = 26000;
	public static final int SWITCH_HEIGHT = 8000;
	public static final int BASE_HEIGHT = 10;
	public static final int EXCHANGE_HEIGHT = 2000;
	private int elevLast;

	public Elevator(int canPos, boolean eInvert, boolean eSensorPhase) {
		int elevVelocity = 1200;
		int elevAccel = 1200;
		elevObj = new TalonSRX(canPos);
		elevObj.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		elevObj.setNeutralMode(NeutralMode.Coast);// enableBrakeMode(false);
		elevObj.setInverted(eInvert);
		elevObj.setSensorPhase(eSensorPhase);
		elevLast = 1;
		this.elevSetPosition(0);
		this.elevSetMotion(elevVelocity, elevAccel);
	}

	public double getPosition() {
		return elevObj.getSelectedSensorPosition(0);
	}

	public void elevMove(double posCnt) {

		elevObj.set(ControlMode.PercentOutput, posCnt);

	}

	public void elevSetMotion(int cruiseSpeed, int accelRate) {
		elevObj.configNominalOutputForward(0, 10);
		elevObj.configNominalOutputReverse(0, 10);
		elevObj.configPeakOutputForward(1, 10);
		elevObj.configPeakOutputReverse(-1, 10);
		elevObj.selectProfileSlot(0, 0);
		elevObj.config_kP(0, 3, 10);
		elevObj.config_kI(0, 0, 10);
		elevObj.config_kD(0, 0, 10);
		elevObj.config_kF(0, 0.15, 10);
		elevObj.configMotionCruiseVelocity(cruiseSpeed, 10);
		elevObj.configMotionAcceleration(accelRate, 10);
		elevObj.configForwardSoftLimitEnable(true, 10);
		elevObj.configForwardSoftLimitThreshold(30000, 10);
		elevObj.configReverseSoftLimitEnable(true, 10);
		elevObj.configReverseSoftLimitThreshold(0, 10);

	}

	public void elevMovePosition(double posCnt) {

		elevObj.set(ControlMode.MotionMagic, posCnt);

	}

	public void elevSetPosition(int posCnt) {

		elevObj.setSelectedSensorPosition(posCnt, 0, 10);

	}

	public void elevNextPosition(int posCnt) {
		double newPosition;
		newPosition = elevObj.getSelectedSensorPosition(0) + (posCnt * 350);
		if (newPosition < 8)
			newPosition = 10;
		if (newPosition > 30000)
			newPosition = 30000;
		elevMovePosition(newPosition);
	}
	
	public void setNeutralMode(NeutralMode nm) {
		elevObj.setNeutralMode(nm);
	}
}
