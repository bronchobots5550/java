/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team5550.robot;

import java.util.ArrayList;

import frc.team5550.robot.auto.AutoDriveRoutine;
//import frc.team5550.robot.auto.routines.Routine2;
import frc.team5550.robot.auto.routines.Routine2;
import frc.team5550.robot.auto.routines.RoutineLeftRScale;
import frc.team5550.robot.auto.routines.RoutineLeftRScaleStop;
import frc.team5550.robot.auto.routines.RoutineLeftScale;
import frc.team5550.robot.auto.routines.RoutineLeftSwitch;
import frc.team5550.robot.auto.routines.RoutineMiddleLSwitch;
import frc.team5550.robot.auto.routines.RoutineMiddleRSwitch;
import frc.team5550.robot.auto.routines.RoutineRightLScale;
import frc.team5550.robot.auto.routines.RoutineRightLScaleStop;
import frc.team5550.robot.auto.routines.RoutineRightScale;
import frc.team5550.robot.auto.routines.RoutineRightSwitch;
import frc.team5550.robot.auto.routines.RoutineTest;
import frc.team5550.robot.auto.routines.RoutineTurn180;
import frc.team5550.robot.auto.routines.RoutineTurn45;
import frc.team5550.robot.auto.routines.RoutineTurn90;
import frc.team5550.robot.auto.routines.RoutineTurnN180;
import frc.team5550.robot.auto.routines.RoutineTurnN45;
import frc.team5550.robot.auto.routines.RoutineTurnN90;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionThread;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	private int curStep = 1;
	private boolean resetSensors = false;
	private String m_optionSelected, m_positionSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	private SendableChooser<String> m_position = new SendableChooser<>();
	TalonSRX leftDrive, rightDrive;
	VictorSPX leftDriveFollow, rightDriveFollow;
	// DigitalInput cubeSwitch;
	// AnalogInput magSensor;
	XboxController controller;
	XboxController controller2;
	Spark intakeLeft, intakeRight;
	public static Spark intakeTop;
	DigitalInput cubeSwitch;
	boolean resetTriggered = false;
	Elevator turboLift;
	// UsbCamera camera;
	public static ArrayList<AutoDriveRoutine> routines = new ArrayList<AutoDriveRoutine>();

	private boolean stopAuto = false;

	private static final int IMG_WIDTH = 320;
	private static final int IMG_HEIGHT = 240;

	private VisionThread visionThread;
	// private double centerR1 = 0.0;
	private double centerX = 0.0;
	private double numImg = 0;
	private final Object imgLock = new Object();
	public AHRS gyro; // REAL BOT
	// public AnalogGyro gyro; // PRACTICE BOT
	int selected_Routine = 9;

	public String gameData;

	long endTime = 0;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_position.addDefault("Left", "Left");
		m_position.addObject("Right", "Right");
		m_position.addObject("Middle", "Middle");
		SmartDashboard.putData("Position", m_position);
		m_chooser.addDefault("Deliver Opposite", "Deliver");
		m_chooser.addObject("Stop Opposite", "Stop");
		SmartDashboard.putData("Auto Options", m_chooser);
		gyro = new AHRS(Port.kUSB1); // REAL ROBOT
		// gyro = new AnalogGyro(0); // PRACTICE BOT
		// gyro.setSensitivity(.0011); // PRACTICE BOT
		gyro.reset();

		// leftDrive = new TalonSRX(1); // Practice Robot
		// rightDrive = new TalonSRX(0); //Practice Robot

		cubeSwitch = new DigitalInput(0);
		// THIS IS THE REAL ROBOT SETUP
		leftDrive = new TalonSRX(0);
		rightDrive = new TalonSRX(2);
		leftDriveFollow = new VictorSPX(0);
		rightDriveFollow = new VictorSPX(1);
		leftDriveFollow.follow(leftDrive);
		leftDriveFollow.setInverted(true);
		// rightDriveFollow.setInverted(true);
		rightDriveFollow.follow(rightDrive);
		//

		turboLift = new Elevator(1, false, true);
		initDriveMotors();
		controller = new XboxController(0);
		controller2 = new XboxController(1);
		// intakeLeft = new Spark(8);
		// intakeRight = new Spark(9);

		// REAL ROBOT
		intakeLeft = new Spark(9);
		intakeRight = new Spark(8);
		intakeTop = new Spark(0);
		//

		/** INITIALIZE CAMERA **/
		// UsbCamera cam = new UsbCamera("cam0", 0);
		// CameraServer.getInstance().addCamera(cam);
		// camera = CameraServer.getInstance().startAutomaticCapture();

		// camera.setBrightness(Preferences.getInstance().getInt("camBrightness", 50));
		// // GET SETTINGS FROM DASHBOARD
		// camera.setExposureManual(Preferences.getInstance().getInt("camExposure",
		// 50)); // GET SETTINGS FROM DASHBOARD
		// camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
		// camera.setFPS(10);

		/** START VISION PROCESSING **/
		// startVision();
		// visionThread.setPriority(1);

		turboLift.elevSetPosition(0);
		// turboLift.elevSetMotion(100,100);

		// magSensor = new AnalogInput(0);
		// cubeSwitch = new DigitalInput(0);
		// m_chooser.addObject("Auto1", "Auto1");
		// routines.add(new Routine1(leftDrive, rightDrive, intakeLeft, intakeRight,
		// gyro)); //0
		routines.add(new RoutineLeftScale(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift)); // 0
		routines.add(new RoutineTurn45(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift)); // 1
		routines.add(new RoutineTurnN45(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift)); // 2
		routines.add(new RoutineTurn90(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift)); // 3
		routines.add(new RoutineTurnN90(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift)); // 4
		routines.add(new RoutineTurn180(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift)); // 5
		routines.add(new RoutineTurnN180(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift)); // 6
		routines.add(
				new RoutineRightScale(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift)); // 7
		routines.add(
				new RoutineMiddleRSwitch(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift)); // 8
		routines.add(
				new RoutineMiddleLSwitch(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift)); // 9
		routines.add(
				new RoutineLeftRScale(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift)); // 10
		routines.add(
				new RoutineRightLScale(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift)); // 11
		routines.add(
				new RoutineLeftRScaleStop(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift)); // 12
		routines.add(new RoutineRightLScaleStop(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch,
				turboLift)); // 13
		routines.add(new Routine2(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift)); // 14
		routines.add(new RoutineTest(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift)); // 15
		routines.add(
				new RoutineRightSwitch(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift)); // 16
		routines.add(
				new RoutineLeftSwitch(leftDrive, rightDrive, intakeLeft, intakeRight, gyro, cubeSwitch, turboLift)); // 17

	}

	@Override
	public void disabledPeriodic() {

		SmartDashboard.putString("Pos Read", m_position.getName());
		SmartDashboard.putString("Opt Read", m_chooser.getName());

	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable chooser
	 * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
	 * remove all of the chooser code and uncomment the getString line to get the
	 * auto name from the text box below the Gyro
	 *
	 * <p>
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the SendableChooser
	 * make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {

		gameData = DriverStation.getInstance().getGameSpecificMessage();
		gyro.reset();
		leftDrive.setSelectedSensorPosition(0, 0, 10);
		rightDrive.setSelectedSensorPosition(0, 0, 10);
		leftDrive.set(ControlMode.MotionMagic, 0);
		rightDrive.set(ControlMode.MotionMagic, 0);
		leftDrive.set(ControlMode.PercentOutput, 0);
		rightDrive.set(ControlMode.PercentOutput, 0);
		curStep = 0;
		resetSensors = false;
		m_optionSelected = m_chooser.getSelected();
		m_positionSelected = m_position.getSelected();
		initDriveMotors();

		m_positionSelected = "Middle"; // Use this to force position
		m_optionSelected = "Deliver"; // Use this to force option

		if (m_positionSelected == "Middle" && gameData.charAt(0) == 'L')
			selected_Routine = 9;
		else if (m_positionSelected == "Middle" && gameData.charAt(0) == 'R')
			selected_Routine = 8;
		else if (m_positionSelected == "Left" && gameData.charAt(1) == 'L')
			selected_Routine = 0;
		else if (m_positionSelected == "Right" && gameData.charAt(1) == 'R')
			selected_Routine = 7;
		else if (m_positionSelected == "Left" && gameData.charAt(1) == 'R' && gameData.charAt(0) == 'L')
			selected_Routine = 10;
		else if (m_positionSelected == "Right" && gameData.charAt(1) == 'L' && gameData.charAt(0) == 'R')
			selected_Routine = 11;
		else if (m_positionSelected == "Left" && gameData.charAt(1) == 'R' && m_optionSelected == "Deliver")
			selected_Routine = 10;
		else if (m_positionSelected == "Right" && gameData.charAt(1) == 'L' && m_optionSelected == "Deliver")
			selected_Routine = 11;
		else if (m_positionSelected == "Left" && gameData.charAt(1) == 'R' && m_optionSelected == "Stop")
			selected_Routine = 12;
		else if (m_positionSelected == "Right" && gameData.charAt(1) == 'L' && m_optionSelected == "Stop")
			selected_Routine = 13;
		else
			selected_Routine = 14;
		// 7 Right Scale
		// selected_Routine = 9; // Use this line when testing

		routines.get(selected_Routine).reset();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto Pos selected: " + m_positionSelected + "\n\n");
		System.out.println("Auto Opt selected: " + m_optionSelected + "\n\n");
		System.out.println("Game Data: " + gameData + "\n\n");
		leftDrive.setSelectedSensorPosition(0, 0, 10);
		rightDrive.setSelectedSensorPosition(0, 0, 10);

	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		if (!stopAuto) {
			routines.get(selected_Routine).run();
		} else {
			double inchesLeft = -10;
			double inchesRight = -10;
			double gearRatio = 30.00 / 24.00;
			double circumf = (3.25 * 3.14159);
			double wheelRotateLeft = inchesLeft / circumf;
			double motorRotateLeft = wheelRotateLeft / gearRatio;
			double posLeft = motorRotateLeft * 4096;
			double wheelRotateRight = inchesRight / circumf;
			double motorRotateRight = wheelRotateRight / gearRatio;
			double posRight = motorRotateRight * 4096;
			leftDrive.config_kP(0, .35, 10);
			rightDrive.config_kP(0, .35, 10);
			rightDrive.config_kF(0, .25, 10);
			leftDrive.config_kF(0, .25, 10);
			leftDrive.set(ControlMode.MotionMagic, -posLeft);
			rightDrive.set(ControlMode.MotionMagic, -posRight);
		}
		if (gyro.getPitch() > 15) {
			stopAuto = true;
			leftDrive.set(ControlMode.PercentOutput, 0);
			rightDrive.set(ControlMode.PercentOutput, 0);
			leftDrive.setSelectedSensorPosition(0, 0, 10);
			rightDrive.setSelectedSensorPosition(0, 0, 10);
		}
	}

	@Override
	public void teleopInit() {
		initDriveMotors();
		gyro.reset();
		leftDrive.setSelectedSensorPosition(0, 0, 10);
		rightDrive.setSelectedSensorPosition(0, 0, 10);

	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		double forwardSpeed = controller.getRawAxis(1);
		double turnSpeed = -controller.getRawAxis(4);
		int itUp = controller2.getPOV();
		double centerX = 0;
		double numImg = 0;

		if (cubeSwitch.get()) {
			controller.setRumble(RumbleType.kLeftRumble, 1);
			controller.setRumble(RumbleType.kRightRumble, 1);
			controller2.setRumble(RumbleType.kLeftRumble, 1);
			controller2.setRumble(RumbleType.kRightRumble, 1);
		} else {
			controller.setRumble(RumbleType.kLeftRumble, 0);
			controller.setRumble(RumbleType.kRightRumble, 0);
			controller2.setRumble(RumbleType.kLeftRumble, 0);
			controller2.setRumble(RumbleType.kRightRumble, 0);
		}

		synchronized (imgLock) {
			centerX = this.centerX;
			numImg = this.numImg;
		}
		SmartDashboard.putNumber("Vision Center", centerX);
		SmartDashboard.putNumber("Vision Images", numImg);
		SmartDashboard.putNumber("Left Pos", leftDrive.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Right Pos", rightDrive.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Gyro Angle Final", gyro.getAngle());
		SmartDashboard.putNumber("Gyro Pitch", gyro.getPitch());
		SmartDashboard.putNumber("Gyro Roll", gyro.getRoll());
		SmartDashboard.putBoolean("Cube Aquired", cubeSwitch.get());

		if (controller2.getAButton()) {
			turboLift.elevMovePosition(Elevator.EXCHANGE_HEIGHT);
		}
		if (controller2.getBButton()) {
			turboLift.elevMovePosition(Elevator.BASE_HEIGHT);
		}
		if (controller2.getYButton()) {
			turboLift.elevMovePosition(Elevator.SCALE_HEIGHT);
		}
		if (controller2.getXButton()) {
			turboLift.elevMovePosition(Elevator.SWITCH_HEIGHT);
		}
		/*
		 * if (controller2.getBumper(Hand.kLeft)) { turboLift.elevNextPosition(-1); } if
		 * (controller2.getBumper(Hand.kRight)) { turboLift.elevNextPosition(1); }
		 */
		if (itUp == -1) {
			intakeLeft.set(0);
			intakeRight.set(0);
			intakeTop.set(0);
		} else {
			// Out-take
			if (itUp <= 45 || itUp >= 315) {
				intakeLeft.set(-.5);
				intakeRight.set(.5);
				intakeTop.set(-.3);
				// In-take after switch
			} else if (cubeSwitch.get() && itUp >= 90 && itUp <= 270) {
				intakeLeft.set(.4);
				intakeRight.set(-.4);
				intakeTop.set(0);
				// In-take
			} else if (itUp <= 180) {
				intakeLeft.set(-.3);
				intakeRight.set(-.6);
				intakeTop.set(.5);
				// In-take
			} else {
				intakeLeft.set(.6);
				intakeRight.set(.3);
				intakeTop.set(.5);
			}
		}

		if (forwardSpeed < .2 && forwardSpeed > -.2)
			forwardSpeed = 0;
		if (turnSpeed < .2 && turnSpeed > -.2)
			turnSpeed = 0;
		/*
		 * if(controller.getBumper(Hand.kRight)) { leftDrive.configOpenloopRamp(0, 10);
		 * rightDrive.configOpenloopRamp(0, 10); } else {
		 * leftDrive.configOpenloopRamp(0.25, 10); rightDrive.configOpenloopRamp(0.25,
		 * 10); }
		 */
		curvatureDrive(forwardSpeed, turnSpeed, true);

		// SmartDashboard.putNumber("Hall Effect", magSensor.getAverageValue());
		// SmartDashboard.putBoolean("Cube Switch", cubeSwitch.get());
		if (controller2.getBumper(Hand.kLeft) && controller2.getBumper(Hand.kRight)) {
			resetTriggered = true;
			turboLift.setNeutralMode(NeutralMode.Coast);
			turboLift.elevMove(0);
		} else {
			if (resetTriggered) {
				turboLift.elevSetPosition(0);
				turboLift.setNeutralMode(NeutralMode.Brake);
				turboLift.elevMovePosition(0);
			}
			resetTriggered = false;
		}
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}

	public void initDriveMotors() {

		/*
		 * Practice Bot leftDrive.configSelectedFeedbackSensor(FeedbackDevice.
		 * CTRE_MagEncoder_Relative, 0, 10); //
		 * leftDrive.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10,
		 * // 10); //
		 * leftDrive.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10,
		 * // 10);
		 * 
		 * leftDrive.configNominalOutputForward(0, 10);
		 * leftDrive.configNominalOutputReverse(0, 10);
		 * leftDrive.configPeakOutputForward(1, 10);
		 * leftDrive.configPeakOutputReverse(-1, 10); leftDrive.setInverted(true);
		 * leftDrive.setSensorPhase(true); leftDrive.selectProfileSlot(0, 0);
		 * leftDrive.config_kP(0, .45, 10); leftDrive.config_kI(0, 0, 10);
		 * leftDrive.config_kD(0, 0, 10); leftDrive.config_kF(0, .15, 10);
		 * leftDrive.configMotionAcceleration(3000, 10);
		 * leftDrive.configMotionCruiseVelocity(3000, 10);
		 * leftDrive.configOpenloopRamp(0, 10);
		 * leftDrive.setNeutralMode(NeutralMode.Brake);
		 * 
		 * rightDrive.configSelectedFeedbackSensor(FeedbackDevice.
		 * CTRE_MagEncoder_Relative, 0, 10); //
		 * rightDrive.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10,
		 * // 10); //
		 * rightDrive.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, //
		 * 10, 10);
		 * 
		 * rightDrive.configNominalOutputForward(0, 10);
		 * rightDrive.configNominalOutputReverse(0, 10);
		 * rightDrive.configPeakOutputForward(1, 10);
		 * rightDrive.configPeakOutputReverse(-1, 10); rightDrive.setInverted(false);
		 * rightDrive.setSensorPhase(true); rightDrive.selectProfileSlot(0, 0);
		 * rightDrive.config_kP(0, .45, 10); rightDrive.config_kI(0, 0, 10);
		 * rightDrive.config_kD(0, 0, 10); rightDrive.config_kF(0, .15, 10);
		 * rightDrive.configMotionAcceleration(3000, 10);
		 * rightDrive.configMotionCruiseVelocity(3000, 10);
		 * rightDrive.configOpenloopRamp(0, 10);
		 * rightDrive.setNeutralMode(NeutralMode.Brake);
		 */
		// REAL ROBOT
		leftDrive.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		// leftDrive.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10,
		// 10);
		// leftDrive.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10,
		// 10);

		leftDrive.configNominalOutputForward(0, 10);
		leftDrive.configNominalOutputReverse(0, 10);
		leftDrive.configPeakOutputForward(1, 10);
		leftDrive.configPeakOutputReverse(-1, 10);
		leftDrive.setInverted(true);
		leftDrive.setSensorPhase(true);
		leftDrive.selectProfileSlot(0, 0);
		leftDrive.config_kP(0, .35, 10);
		leftDrive.config_kI(0, 0, 10);
		leftDrive.config_kD(0, 0, 10);
		leftDrive.config_kF(0, .15, 10);
		leftDrive.configMotionAcceleration(3000, 10);
		leftDrive.configMotionCruiseVelocity(3000, 10);
		leftDrive.configOpenloopRamp(.5, 10);
		leftDrive.setNeutralMode(NeutralMode.Brake);

		rightDrive.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		// rightDrive.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10,
		// 10);
		// rightDrive.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic,
		// 10, 10);

		rightDrive.configNominalOutputForward(0, 10);
		rightDrive.configNominalOutputReverse(0, 10);
		rightDrive.configPeakOutputForward(1, 10);
		rightDrive.configPeakOutputReverse(-1, 10);
		rightDrive.setInverted(false);
		rightDrive.setSensorPhase(true);
		rightDrive.selectProfileSlot(0, 0);
		rightDrive.config_kP(0, .35, 10);
		rightDrive.config_kI(0, 0, 10);
		rightDrive.config_kD(0, 0, 10);
		rightDrive.config_kF(0, .15, 10);
		
		
		
		//CHANGE THE SPEED BACK
		
		
		
		rightDrive.configMotionAcceleration(3000, 10);
		rightDrive.configMotionCruiseVelocity(3000, 10);
		rightDrive.configOpenloopRamp(.5, 10);
		rightDrive.setNeutralMode(NeutralMode.Brake);

	}

	public void initTeleopMotors() {

		// USED FOR PRACTICE BOT ONLY
		leftDrive.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		// leftDrive.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10,
		// 10);
		// leftDrive.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10,
		// 10);

		leftDrive.configNominalOutputForward(0, 10);
		leftDrive.configNominalOutputReverse(0, 10);
		leftDrive.configPeakOutputForward(1, 10);
		leftDrive.configPeakOutputReverse(-1, 10);
		leftDrive.setInverted(true);
		leftDrive.setSensorPhase(true);
		leftDrive.selectProfileSlot(0, 0);
		leftDrive.config_kP(0, .25, 10);
		leftDrive.config_kI(0, 0, 10);
		leftDrive.config_kD(0, 0, 10);
		leftDrive.config_kF(0, .15, 10);
		leftDrive.configMotionAcceleration(750, 10);
		leftDrive.configMotionCruiseVelocity(2000, 10);
		leftDrive.configOpenloopRamp(0.25, 10);
		leftDrive.setNeutralMode(NeutralMode.Brake);

		rightDrive.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		// rightDrive.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10,
		// 10);
		// rightDrive.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic,
		// 10, 10);

		rightDrive.configNominalOutputForward(0, 10);
		rightDrive.configNominalOutputReverse(0, 10);
		rightDrive.configPeakOutputForward(1, 10);
		rightDrive.configPeakOutputReverse(-1, 10);
		rightDrive.setInverted(false);
		rightDrive.setSensorPhase(true);
		rightDrive.selectProfileSlot(0, 0);
		rightDrive.config_kP(0, .25, 10);
		rightDrive.config_kI(0, 0, 10);
		rightDrive.config_kD(0, 0, 10);
		rightDrive.config_kF(0, .15, 10);
		rightDrive.configMotionAcceleration(750, 10);
		rightDrive.configMotionCruiseVelocity(2000, 10);
		rightDrive.configOpenloopRamp(0.25, 10);
		rightDrive.setNeutralMode(NeutralMode.Brake);
	}

	public void curvatureDrive(double xSpeed, double zRotation, boolean isQuickTurn) {

		xSpeed = limit(xSpeed);

		zRotation = limit(zRotation);
		double m_quickStopAccumulator = 0;
		double m_quickStopAlpha = 0.1;
		double m_maxOutput = 0.8;
		rightDrive.configOpenloopRamp(0.25, 10);
		leftDrive.configOpenloopRamp(0.25, 10);
		if (turboLift.getPosition() > (Elevator.SWITCH_HEIGHT + 300)) {
			m_maxOutput = 0.5;
			rightDrive.configOpenloopRamp(0.5, 10);
			leftDrive.configOpenloopRamp(0.5, 10);
		}

		if (controller.getBumper(Hand.kRight)) {
			m_maxOutput -= 0.2;
		}

		double m_turnSpeed = 0.6;

		double angularPower;
		boolean overPower;

		if (isQuickTurn) {
			if (Math.abs(xSpeed) < 0.2) {
				m_quickStopAccumulator = (1 - m_quickStopAlpha) * m_quickStopAccumulator
						+ m_quickStopAlpha * limit(zRotation) * 2;
			}
			overPower = true;
			angularPower = zRotation;
		} else {
			overPower = false;
			angularPower = Math.abs(xSpeed) * zRotation - m_quickStopAccumulator;

			if (m_quickStopAccumulator > 1) {
				m_quickStopAccumulator -= 1;
			} else if (m_quickStopAccumulator < -1) {
				m_quickStopAccumulator += 1;
			} else {
				m_quickStopAccumulator = 0.0;
			}
		}
		angularPower *= m_turnSpeed;

		double leftMotorOutput = xSpeed + angularPower;
		double rightMotorOutput = xSpeed - angularPower;

		// If rotation is overpowered, reduce both outputs to within acceptable range
		if (overPower) {
			if (leftMotorOutput > 1.0) {
				rightMotorOutput -= leftMotorOutput - 1.0;
				leftMotorOutput = 1.0;
			} else if (rightMotorOutput > 1.0) {
				leftMotorOutput -= rightMotorOutput - 1.0;
				rightMotorOutput = 1.0;
			} else if (leftMotorOutput < -1.0) {
				rightMotorOutput -= leftMotorOutput + 1.0;
				leftMotorOutput = -1.0;
			} else if (rightMotorOutput < -1.0) {
				leftMotorOutput -= rightMotorOutput + 1.0;
				rightMotorOutput = -1.0;
			}
		}
		double maxVelocity = 3000;

		SmartDashboard.putNumber("Left Motor", leftMotorOutput);
		SmartDashboard.putNumber("Right Motor", rightMotorOutput);
		// SmartDashboard.putNumber("Left Velocity",
		// leftDrive.getSelectedSensorVelocity(0));
		// SmartDashboard. putNumber("Right Velocity",
		// rightDrive.getSelectedSensorVelocity(0));
		// SmartDashboard.putNumber("Elevator", turboLift.getPosition());
		// leftDrive.set(ControlMode.Velocity , leftMotorOutput * maxVelocity);
		// rightDrive.set(ControlMode.Velocity, rightMotorOutput * maxVelocity);
		leftDrive.set(ControlMode.PercentOutput, leftMotorOutput * m_maxOutput);
		rightDrive.set(ControlMode.PercentOutput, rightMotorOutput * m_maxOutput);

	}

	public double limit(double value) {
		if (value > 1.0) {
			return 1.0;
		}
		if (value < -1.0) {
			return -1.0;
		}
		return value;
	}

	/*
	 * public void startVision() {
	 * 
	 * visionThread = new VisionThread(camera, new GripPipeline(), pipeline -> {
	 * double s; double centerR1; if (!pipeline.filterContoursOutput().isEmpty()) {
	 * Rect r1; Rect r2; s = pipeline.filterContoursOutput().size(); if (s == 1) {
	 * r1 = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0)); centerR1 =
	 * r1.x + (r1.width / 2); } else if (s == 2) { r1 =
	 * Imgproc.boundingRect(pipeline.filterContoursOutput().get(0)); r2 =
	 * Imgproc.boundingRect(pipeline.filterContoursOutput().get(1)); centerR1 =
	 * ((r1.x + (r1.width / 2)) + (r2.x + (r2.width / 2))) / 2; } else centerR1 =
	 * 0.00;
	 * 
	 * } else { s = 0; centerR1 = 0; } synchronized (imgLock) { centerX = centerR1;
	 * numImg = s; } });
	 * 
	 * visionThread.start();
	 * 
	 * }
	 */

}
