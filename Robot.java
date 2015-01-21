/**
 * @author Jack Deadman
 * @author Joshua O'Leary
 */

public class Robot {
	private static final int INTERVAL = 30;
	private final static int OBSTACLE_DISTANCE_THRESHOLD = 12;

	private static boolean debugMode = false;
	/**
	 * Output to console if debug mode is on
	 * @param message String Message to display
	 */
	public static void debugLog(String message) {
		if (debugMode) {
			System.out.println(message);
		}
	}

	/**
	 * Move robot forward until it reaches the black line
	 */
	public static void navigateToStartLine() throws InterruptedException {
		debugLog("> Moving to start line...");
		RobotControl.goForward();
		blockExecutionUntilOnLine();
		debugLog(">> Reached line.");
	}

	/**
	 * Turns robot until the line is in between its sensors
	 */
	public static void lineUpStart() throws InterruptedException {
		debugLog("> Lining up...");
		RobotControl.goRight(4);
		blockExecutionUntilOnLine();
		debugLog(">> Lined up.");
	}

	/**
	 * Prevent executing anything until a robot leaves and re-enters the line
	 */
	public static void blockExecutionUntilOnLine() throws InterruptedException {
		boolean hasComeOffLine = false;

		while( !hasComeOffLine || !RobotControl.blackDetectedEither() ) {
			if(! RobotControl.blackDetectedEither() ) {
				hasComeOffLine = true;
			}
			Thread.sleep(INTERVAL);
		}
	}
	
	/**
	 * Ensures the robot remains on the line by moving it left or right accordingly
	 */
	public static void alignWithLine() throws InterruptedException {
		boolean leftSensorDetect = RobotControl.blackDetectedLeft();
		boolean rightSensorDetect = RobotControl.blackDetectedRight();

		if( leftSensorDetect && ! rightSensorDetect) {
			RobotControl.goLeft();
		}
		if( !leftSensorDetect && rightSensorDetect ) {
			RobotControl.goRight();
		}
		if( !leftSensorDetect && !rightSensorDetect ) {
			RobotControl.goForward();
		}
	}

	/**
	 * Plays a musical fanfare tune in the ending sequence
	 * @param noteLength int Duration of each note in milliseconds
	 */
	public static void fanfare(int noteLength) throws InterruptedException {
		int[] beepValues = {261, 261, 261, 329, 261, 329, 783};
		for (int beepValue : beepValues) {
			RobotControl.beep(noteLength, beepValues[i]);
			Thread.sleep(noteLength);
		}
	}

	/**
	 * Makes robot spin and play tune
	 */
	public static void celebrate() throws InterruptedException {
		RobotControl.stop();
		System.out.println("> Starting victory sequence...");
		RobotControl.setBaseSpeed(900);
		RobotControl.goHardRight();
		Thread.sleep(2500);
		RobotControl.stop();
		Thread.sleep(500);
		fanfare(100);			
	}

	/**
	 * Main algorithm for guiding the robot from start position to the spot
	 */
	public static void navigateToSpot() throws InterruptedException {
		debugLog("> Navigating to spot...");
		while(true) {

			if(RobotControl.blackDetectedBoth() && !RobotControl.obstacleDetected(OBSTACLE_DISTANCE_THRESHOLD + 5)) {
				break;
			}

			if( RobotControl.obstacleDetected(OBSTACLE_DISTANCE_THRESHOLD) ) {
				debugLog(">> Detected obstacle!");
				RobotControl.goHardLeft();
				blockExecutionUntilOnLine();
			}
			else {
				alignWithLine();
			}

			Thread.sleep(INTERVAL);
		}
		debugLog(">> Found spot.");
		RobotControl.goForward();
	}
	
	public static void main(String[] args) throws InterruptedException {

		RobotControl.initialise();
		if( args.length == 1) {
			if( args[0].equals("-d") ) {
				debugMode = true;
				debugLog("Debug Mode");
			}
			else if( args[0].equals("-s") ) {
				System.out.println("Stopping robot...");
				RobotControl.stop();
				return;
			}
		}

		RobotControl.stop();
		RobotControl.setBaseSpeed(150);

		navigateToStartLine();
		RobotControl.stop();
		Thread.sleep(2000);

		lineUpStart();
		navigateToSpot();

		Thread.sleep(2000);
		celebrate();

		debugLog("> Finished!");
	}
}

