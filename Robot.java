public class Robot {
	private static final int INTERVAL = 50;
	private static boolean debugMode = false;

	public static void debugLog(String message) {
		if (debugMode) {
			System.out.println(message);
		}
	}

	public static void navigateToStartLine() throws InterruptedException {
		debugLog("Moving to start line...");
		RobotControl.goForward();
		blockExecutionUntilOnLine();
		debugLog("\tReached line.");
	}

	public static void lineUpStart() throws InterruptedException {
		debugLog("Lining up...");
		RobotControl.goRight(4);
		blockExecutionUntilOnLine();
		debugLog("\tLined up.");
	}

	public static void blockExecutionUntilOnLine() throws InterruptedException {
		boolean hasComeOffLine = false;

		while( !hasComeOffLine || !RobotControl.blackDetectedEither() ) {
			if(! RobotControl.blackDetectedEither() ) {
				hasComeOffLine = true;
			}
			Thread.sleep(INTERVAL);
		}
	}
	

	public static void fanfare(int noteLength) throws InterruptedException {
		int[] beepValues = {261, 261, 261, 329, 261, 329, 783};
		for(int i=0; i<beepValues.length; i++) {
			RobotControl.beep(noteLength, beepValues[i]);
			Thread.sleep(noteLength);
		}
	}

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

	public static void celebrate() throws InterruptedException {
		debugLog("Reached goal!");
		RobotControl.stop();
		RobotControl.setBaseSpeed(900);
		RobotControl.goHardRight();
		Thread.sleep(2500);
		RobotControl.stop();
		Thread.sleep(500);
		fanfare(100);			
	}

	public static void navigateToSpot() throws InterruptedException {
		while(true) {

			if(RobotControl.blackDetectedBoth() && !RobotControl.nearObject()) {
				break;
			}

			if( RobotControl.obstacleDetected() ) {
				debugLog("Detected obstacle!");
				RobotControl.goHardLeft();
				blockExecutionUntilOnLine();		
			}
			else {
				alignWithLine();
			}

			Thread.sleep(INTERVAL);
		}
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

		debugLog("Finished!");
	}
}

