package frc.team5550.robot.auto;

import java.util.ArrayList;

import frc.team5550.robot.Elevator;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;

public class AutoDriveRoutine {
	
	TalonSRX leftDrive;
	TalonSRX rightDrive;
	Spark intakeLeft;
	Spark intakeRight;
	DigitalInput cubeSwitch;
	Elevator turboLift;
	boolean resetSensors = false;
	AHRS gyro;
	int curStep = 0;
	ArrayList<Step> steps = new ArrayList<Step>();
	
	public AutoDriveRoutine(TalonSRX leftDrive, TalonSRX rightDrive, Spark intakeLeft, Spark intakeRight, AHRS gyro, DigitalInput cubeSwitch, Elevator turboLift) {
		this.gyro = gyro;
		this.leftDrive = leftDrive;
		this.rightDrive = rightDrive;
		this.intakeLeft = intakeLeft;
		this.intakeRight = intakeRight;
		this.turboLift = turboLift;
		this.cubeSwitch = cubeSwitch;
		
	}
	
	public void addStep(Step s) {
		steps.add(s);
	}
	
	public void run() {
		if(curStep != steps.size()) {
			if(steps.get(curStep).run()) {
				curStep++;
				System.out.println(System.currentTimeMillis()+" After Step "+ Integer.toString(curStep) +" Gyro: " + Double.toString(this.gyro.getAngle()) +"\n");
				System.out.println("Left Encoder: "+ Integer.toString(leftDrive.getSelectedSensorPosition(0)) +" Right Encoder: " + Integer.toString(rightDrive.getSelectedSensorPosition(0)) +"\n");
				
			}
		}
	}
	
	public void reset() {
		curStep = 0;
	}
	
}
