public class Robot {
	private static final int INTERVAL = 50;

	public static void untilLine() throws InterruptedException {
		RobotControl.goForward();

		while(!RobotControl.blackDetectedEither()) {
			Thread.sleep(INTERVAL);
		}

		System.out.println("Reached line.");
		// Stops
		// Waits for a few secs
		// Turns right
		RobotControl.stop();
		Thread.sleep(2000);

		RobotControl.goRight(4);

		turnUntilLine();

		System.out.println("Lined up.");
		RobotControl.stop();
	}

	public static void turnUntilLine() throws InterruptedException {
		boolean offLine = false;

		while(!RobotControl.blackDetectedEither() || !offLine) {
			if( ! offLine && ! RobotControl.blackDetectedEither() ) {
				offLine = true;
			}
			Thread.sleep(INTERVAL);
		}
	}
	
	public static void processMovement() throws InterruptedException {
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

		// On spot (only if black detected from both left and right sensors, and no object is detected)
		if( leftSensorDetect && rightSensorDetect && !RobotControl.nearObject()) {
			RobotControl.goForward();
			Thread.sleep(1000);
			RobotControl.setBaseSpeed(900);
			RobotControl.goHardRight();
			for(int i=0; i<200; i++) {
				RobotControl.beep(200, (int)(Math.random() * 1000.0));
			}
			Thread.sleep(1000);
			RobotControl.stop();
			System.exit(0);
		}
	}

	public static void loop() throws InterruptedException {
		while(true) {
			if( RobotControl.obstacleDetected() ) {
				boolean onLine = false;
				RobotControl.goHardLeft();
				turnUntilLine();			
			}
			
			processMovement();

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
	}
}

