/**
 * Main program: class containing algorithms to line up and navigate robot to spot
 * 
 * @author Jack Deadman
 * @author Joshua O'Leary
 */

public class Robot {
	// Delay used by continuous loops to prevent intense CPU usage
	private static final int DELAY_BETWEEN_CYCLES = 30;

	// Maximum distance of object detected before robot turns
	private static final int OBSTACLE_DETECTION_RANGE = 14;

	private static final Delayer CPU_REST = new Delayer(DELAY_BETWEEN_CYCLES);

	private static Direction START_DIRECTION;

	// Enables display of debug messages on console output
	private static boolean debugMode = false;

	/**
	 * Output to console if debug mode is on
	 * @param object Object Object to display
	 */
	public static void debugLog(Object object) {
		if (debugMode) {
			System.out.println(object);
		}
	}

	/**
	 * Move robot forward until it reaches the black line. The program execution is
	 * blocked until the robot reaches destination
	 */
	public static void navigateToStartLine() throws InterruptedException {
		debugLog("> Moving to start line...");
		RobotControl.goForward();
		blockExecutionUntilOnLine();
		debugLog(">> Reached line.");
	}

	/**
	 * Turns robot until the line is in between its sensors. The program execution is
	 * blocked until the robot is lined up on the line
	 */
	public static void lineUpStart() throws InterruptedException {
		debugLog("> Lining up...");
		// Value of '4' ensures robot turns tightly enough to move onto line correctly
		if (START_DIRECTION == Direction.LEFT) {
			RobotControl.goLeft(4);
		}
		else {
			RobotControl.goRight(4);
		}
		blockExecutionUntilOnLine();
		debugLog(">> Lined up.");
	}

	/**
	 * Prevent executing anything until a robot leaves and re-enters the line
	 */
	public static void blockExecutionUntilOnLine() throws InterruptedException {
		boolean hasComeOffLine = false;

		while (!(hasComeOffLine && RobotControl.blackDetectedEither())) {
			if (!RobotControl.blackDetectedEither()) {
				hasComeOffLine = true;
			}
			CPU_REST.apply();
		}
	}
	
	/**
	 * Ensures the robot remains on the line by moving it left or right accordingly
	 */
	public static void moveAlongLine() throws InterruptedException {
		boolean leftSensorDetect = RobotControl.blackDetectedLeft();
		boolean rightSensorDetect = RobotControl.blackDetectedRight();

		if (leftSensorDetect && !rightSensorDetect) {
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
	 * Plays a musical fanfare tune (blocking - holds up further execution)
	 * @param noteLength int Duration of each note in milliseconds
	 */
	public static void fanfare(int noteLength) throws InterruptedException {
		int[] beepValues = {261, 261, 261, 329, 261, 329, 261, 261, 261, 329, 261, 329, 392,
							440, 392, 349, 329, 293, 261};

		for (int beepValue : beepValues) {
			// Beep is non-blocking so a wait is added so each note has time to be played
			RobotControl.beep(noteLength, beepValue).waitFor(noteLength);
		}
	}

	/**
	 * Makes robot spin and play tune
	 */
	public static void celebrate() throws InterruptedException {
		debugLog("> Starting victory sequence...");

		RobotControl.goRightTurnOnSpot(900).waitFor(2500);
		RobotControl.stop().waitFor(500);

		fanfare(100);
	}

	/**
	 * Determines whether the robot has reached the spot (end goal) or not
	 * @return Returns true if robot has reached spot
	 */
	public static boolean reachedSpot() {
		// Need to ensure that there are no objects in range, preventing a false positive that can occur when
		// turning at corners.
		// + 5 is used because this needs to be checked before the robot gets to the turning point
		return RobotControl.blackDetectedBoth() && !RobotControl.obstacleDetected(OBSTACLE_DETECTION_RANGE + 10);
	}

	/**
	 * Main algorithm for guiding the robot from start position to the spot
	 */
	public static void navigateToSpot() throws InterruptedException {
		debugLog("> Navigating to spot...");
		// Keep navigating to spot until both sensors detect black and no obstacles are near it. 'Near' means in range
		// of an object as opposed to right next to it.
		// Near is used to prevent false positive of spot being detected when turning at corners
		while (!reachedSpot()) {
			if (RobotControl.obstacleDetected(OBSTACLE_DETECTION_RANGE)) {
				debugLog(">> Detected obstacle!");
				if (START_DIRECTION == Direction.RIGHT) {
					RobotControl.goLeftTurnOnSpot();
				}
				else {
					RobotControl.goRightTurnOnSpot();
				}
				blockExecutionUntilOnLine();
			}
			else {
				moveAlongLine();
			}
			CPU_REST.apply();
		}
		debugLog(">> Found spot.");
	}

	/**
	 * Determine direction of robot's first turn
	 */
	public static void setRandomDirection() {
		if (Math.random() >= 0.5) {
			START_DIRECTION = Direction.LEFT;
		}
		else {
			START_DIRECTION = Direction.RIGHT;
		}
		debugLog("> Start direction: " + START_DIRECTION);
	}

	/**
	 * Set up program based on input from command-line flags (for debug and stopping)
	 * @param args String Command-line arguments (passed in from main)
	 */
	public static void setUpFlags(String[] args) {
		if (args.length == 1) {
			String flag = args[0];
			// Outputs debugging information if -d flag is used when calling the program
			if (flag.equals("-d")) {
				debugMode = true;
				debugLog("Debug Mode");
			}
			else if (flag.equals("-s")) {
				System.out.println("Stopping robot...");
				RobotControl.stop();
				System.exit(0);
			}
			else if (flag.equals("-h")) {
				System.out.println("-d\tDebug Mode");
				System.out.println("-s\tStop the robot");
				System.exit(0);
			}
			else {
				System.out.println("Invalid flag: " + flag);
				System.exit(0);
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		RobotControl.initialise();
		setUpFlags(args);

		setRandomDirection();

		// Stop robot in case already moving from running program previously
		RobotControl.stop();
		RobotControl.setBaseSpeed(150);

		navigateToStartLine();
		RobotControl.stop().waitFor(2000);

		lineUpStart();
		navigateToSpot();

		// Wait for 1.85 seconds to give robot time to move onto spot before calling ending sequence
		RobotControl.goForward().waitFor(1850);
		RobotControl.stop();

		celebrate();
		RobotControl.closeConnection();
		debugLog("> Finished!");
	}
}

