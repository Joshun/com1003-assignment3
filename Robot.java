/**
 * @author Jack Deadman
 * @author Joshua O'Leary
 */

public class Robot {
	private static final int INTERVAL = 30;
	private final static int OBSTACLE_DETECTION_RANGE = 12;

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
		// Value of '4' ensures robot turns tightly enough to move onto line correctly
		RobotControl.goRight(4);
		blockExecutionUntilOnLine();
		debugLog(">> Lined up.");
	}

	/**
	 * Prevent executing anything until a robot leaves and re-enters the line
	 */
	public static void blockExecutionUntilOnLine() throws InterruptedException {
		boolean hasComeOffLine = false;

		while (!hasComeOffLine || !RobotControl.blackDetectedEither()) {
			if (!RobotControl.blackDetectedEither()) {
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

		if (leftSensorDetect && ! rightSensorDetect) {
			RobotControl.goLeft();
		}
		else if (!leftSensorDetect && rightSensorDetect) {
			RobotControl.goRight();
		}
		else if (!leftSensorDetect && !rightSensorDetect) {
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
			RobotControl.beep(noteLength, beepValue);
			Thread.sleep(noteLength);
		}
	}

	/**
	 * Makes robot spin and play tune
	 */
	public static void celebrate() throws InterruptedException {
		debugLog("> Starting victory sequence...");

		RobotControl.setBaseSpeed(900);
		RobotControl.goHardRight();
		Thread.sleep(2500);

		RobotControl.stop();
		Thread.sleep(500);

		fanfare(100);
	}
	/**
	 * Determines whether the robot has reached the spot (end goal) or not
	 * @return Returns true if robot has reached spot
	 */
	public static boolean reachedSpot() {
		// Need to ensure that there are no objects in range, preventing a false positive that can occur when turning at corners.
		// + 5 is used because this needs to be checked before the robot gets to the turning point
		return RobotControl.blackDetectedBoth() && !RobotControl.obstacleDetected(OBSTACLE_DETECTION_RANGE + 5);
	}

	/**
	 * Main algorithm for guiding the robot from start position to the spot
	 */
	public static void navigateToSpot() throws InterruptedException {
		debugLog("> Navigating to spot...");
		// Keep navigating to spot until both sensors detect black and no obstacles are near it. 'Near' means in range of an object as opposed to right next to it.
		// Near is used to prevent false positive of spot being detected when turning at corners
		while (!reachedSpot()) {

			if (RobotControl.obstacleDetected(OBSTACLE_DETECTION_RANGE)) {
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

	/**
	 * Set up program based on input from command-line flags (for debug and stopping)
	 * @param args String Command-line arguments (passed in from main)
	 */
	public static void setUpFlags(String[] args) {
		if (args.length == 1) {
			if (args[0].equals("-d")) {
				debugMode = true;
				debugLog("Debug Mode");
			}
			else if (args[0].equals("-s")) {
				System.out.println("Stopping robot...");
				RobotControl.stop();
				System.exit(0);
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		RobotControl.initialise();
		setUpFlags(args);

		// Stop robot in case already moving from running program previously
		RobotControl.stop();
		RobotControl.setBaseSpeed(150);

		navigateToStartLine();
		RobotControl.stop();
		Thread.sleep(2000);

		lineUpStart();
		navigateToSpot();

		// Wait for 2 seconds to give robot time to move onto spot before calling ending sequence
		Thread.sleep(2000);
		RobotControl.stop();		
		celebrate();

		debugLog("> Finished!");
	}
}

