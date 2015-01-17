public class RobotState {
	private boolean blackDetected = false;
	private boolean reachedLine = false;
	
	private RobotMovement movementState = RobotMovement.STOPPED;
	
	public RobotState() { }
	
	public boolean detectedBlack() {
		return blackDetected;
	}
	
	public boolean reachedLine() {
		return reachedLine;
	}
	
	public void setReachedLine() {
		reachedLine = true;
	}
	
	public void setForward() {
		movementState = RobotMovement.FORWARD;
	}
	
	public void setBackward() {
		movementState = RobotMovement.BACKWARD;
	}
	
	public void setLeft() {
		movementState = RobotMovement.LEFT;
	}
	
	public void setRight() {
		movementState = RobotMovement.RIGHT;
	}
	
	public void setDetectedBlack(boolean blackDetected) {
		this.blackDetected = blackDetected;
	}
	
	public static void main(String[] args) {
		RobotState testState = new RobotState();
		System.out.println(testState.detectedBlack());
	}
	
}
