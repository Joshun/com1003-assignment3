public class Robot {
	private static RobotState previousState;
	private static RobotState currentState = new RobotState();
	private static boolean reachedLine = false;
	private static final int INTERVAL = 50;

	public static void untilLine() throws InterruptedException {
		RobotControl.goForward();
		while(!RobotControl.blackDetectedEither()) {
			Thread.sleep(INTERVAL);
		}
		System.out.println("Reached line.");
		RobotControl.beep(500);
		// Stops
		// Waits for a few secs
		// Turns right
			RobotControl.stop();
			Thread.sleep(2000);

			RobotControl.goRight();

			boolean offLine = false;

			while(!RobotControl.blackDetectedEither() || !offLine) {
				if( ! offLine && ! RobotControl.blackDetectedEither() ) {
					offLine = true;
				}
				Thread.sleep(INTERVAL);
			}
			System.out.println("Lined up.");
			RobotControl.beep(500);
			RobotControl.stop();
	}
	
	public static void updateState() {
		previousState = currentState.copy();

		boolean onLine = RobotControl.blackDetectedBoth();
		
	// Update current state based on environment
		currentState.setDetectedBlack(onLine);
		

		currentState.setDetectedObstacle(RobotControl.obstacleDetected());

	}
	
	// public static boolean blackDetected() {
	// 	return Math.random() > 0.5;
	// }
	
	// public static boolean obstacleDetected() {
	// 	return Math.random() > 0.8;
	// }	
	
	public static void processMovement() {
		boolean leftSensorDetect = RobotControl.blackDetectedLeft();
		boolean rightSensorDetect = RobotControl.blackDetectedRight();

		if( leftSensorDetect && ! rightSensorDetect) {
			RobotControl.goRight();
		}
		if( !leftSensorDetect && rightSensorDetect ) {
			RobotControl.goLeft();
		}
		if( leftSensorDetect && rightSensorDetect ) {
			RobotControl.goForward();
		}
		if( ! leftSensorDetect && ! rightSensorDetect ) {
			RobotControl.goLeft();
		}
	}

	public static void loop() throws InterruptedException {
		while(true) {
			updateState();
			processMovement();
			// System.out.println("State changed? " + !RobotState.compareMovement(previousState, currentState));
			// if (!RobotState.compareMovement(previousState, currentState)) {
			// 	RobotMovement direction = currentState.getMovement();
				
			// 	System.out.println(direction);
			// 	switch(direction) {
			// 		case LEFT:
			// 			RobotControl.goLeft();
			// 			break;
			// 		case RIGHT:
			// 			RobotControl.goRight();
			// 			break;
			// 		case FORWARD:
			// 			RobotControl.goForward();
			// 			break;
			// 		case BACKWARD:
			// 			RobotControl.goBackward();
			// 			break;
			// 	}
			// 	// if (direction == RobotMovement.LEFT || direction == RobotMovement.RIGHT) {
			// 	// 	currentState.swapTurnDirection();
			// 	// }

			// }
			Thread.sleep(INTERVAL);
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		RobotControl.initialise();
		RobotControl.stop();
		Thread.sleep(1000);
		RobotControl.setBaseSpeed(150);
		untilLine();
		loop();


		// RobotControl.goForward();
		// Thread.sleep(4000);
		// RobotControl.goRight();
		// Thread.sleep(4000);
		// RobotControl.goLeft();
		// Thread.sleep(4000);
		// RobotControl.stop();
	}
}

