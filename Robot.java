public class Robot {
	private static RobotState previousState;
	private static RobotState currentState = new RobotState();
	private static boolean reachedLine = false;
	private static final int INTERVAL = 50;

	public static void untilLine() throws InterruptedException {
		while(!blackDetected()) {
			Thread.sleep(INTERVAL);
		}
		// Stops
		// Waits for a few secs
		// Turns right
			currentState.setRight();
	}
	
	public static void updateState() {
		previousState = currentState.copy();
		
		boolean onLine = blackDetected();
		System.out.println("onLine? " + onLine);
		
		// Called once line has been reached, only once
		if(!reachedLine && onLine) {
			reachedLine = true;
			currentState.setRight();
		}

	// Update current state based on environment
		currentState.setDetectedBlack(onLine);
		currentState.setDetectedObstacle(obstacleDetected());
	//	obstacleDetected()

	}
	
	public static boolean blackDetected() {
		return Math.random() > 0.5;
	}
	
	public static boolean obstacleDetected() {
		return Math.random() > 0.8;
	}
	
	public static void goLeft() {
		// Go left
			System.out.println("Going left...");
	}
	
	public static void goRight() {
		// Go right
			System.out.println("Going right...");		
	}

	public static void goForward() {
		// Go forward
			System.out.println("Going forward...");
	}
	
	public static void goBackward() {
		// Go backward
			System.out.println("Going backward...");
	}	
	
	public static void loop() throws InterruptedException {
		while(true) {
			updateState();
			System.out.println("State changed? " + !RobotState.compareMovement(previousState, currentState));
			if (!RobotState.compareMovement(previousState, currentState)) {
				RobotMovement direction = currentState.getMovement();
				
				switch(direction) {
					case LEFT:
						goLeft();
						break;
					case RIGHT:
						goRight();
						break;
					case FORWARD:
						goForward();
						break;
					case BACKWARD:
						goBackward();
						break;
				}
				if (direction == RobotMovement.LEFT || direction == RobotMovement.RIGHT) {
					currentState.swapTurnDirection();
				}
			}
			Thread.sleep(INTERVAL);
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		untilLine();
		loop();
	}
}

