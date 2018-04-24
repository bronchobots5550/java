package frc.team5550.robot.auto;

public abstract class Step {

	AutoDriveRoutine routine;
	boolean resetSensors = false;
	
	public Step(AutoDriveRoutine routine) {
		this.routine = routine;
	}
	
	public abstract boolean run();
	
	
}
