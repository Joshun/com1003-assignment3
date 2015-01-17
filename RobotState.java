public class RobotState {
	private boolean blackDetected = false;
	private boolean reachedLine = false;
	
	private RobotMovement movementState = RobotMovement.STOPPED;


	public static boolean compareMovement(RobotState s1, RobotState s2) {
		return s1.getMovement() == s2.getMovement();
	}
	
	public RobotState() { }
	
	public boolean compareMovement(RobotState state) {
		return RobotState.compareMovement(this, state);
	}
	
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
	
	public RobotMovement getMovement() {
		return movementState;
	}

	public static void main(String[] args) {
		RobotState testState = new RobotState();
		System.out.println(testState.detectedBlack());
		
		testState.setBackward();
		
		RobotState testState2 = new RobotState();
		testState2.setBackward();
		
		System.out.println(RobotState.compareMovement(testState, testState2));
		System.out.println(testState.compareMovement(testState2));	
		
	}
	
}
