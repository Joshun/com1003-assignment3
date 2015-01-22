import icommand.nxt.*;
import icommand.nxt.comm.NXTCommand;

/**
 * Wrapper class for abstracting from NXT icommand library, configured specifically for our robot's
 * sensors, object position and callibration
 * 
 * @author Jack Deadman
 * @author Joshua O'Leary
 */

public class RobotControl {

	// Default ratio between wheels when turning (e.g. with a factor of 2, outer wheel is 2x faster than pivot wheel)
	private static final int DEFAULT_SPEED_FACTOR = 2;

	// Default frequency used for beeping
	private static final int DEFAULT_BEEP_FREQ = 500;

	private static final Motor MOTOR_LEFT = Motor.C;
	private static final Motor MOTOR_RIGHT = Motor.B;

	// Light threshold values for both sensors (maximum brightness by which black is detected),
	// callibrated specifically for our robot's sensors
	private static final int LEFT_LIGHT_THRESHOLD = 520;
	private static final int RIGHT_LIGHT_THESHOLD = 570;
	
	// SensorPort for Light Sensor
	private static final SensorPort L_SENSOR_PORT_LEFT = SensorPort.S2;
	private static final SensorPort L_SENSOR_PORT_RIGHT = SensorPort.S1;
	
	// SensorPort for Ultrasonic Sensor
	private static final SensorPort U_SENSOR_PORT = SensorPort.S4;
	
	// Lightsensor objects
	private static LightSensor lightSensorLeft;
	private static LightSensor lightSensorRight;
	
	// Ultrasonic disance sensor object
	private static UltrasonicSensor objectSensor;

	// "Base" speed (used by most of the movement functions - all speed is relative to this)
	private static int baseSpeed = 100;

	/**
	 * Establishes connection with robot and sets up sensors
	 */
	public static void initialise() {
		NXTCommand.open();
		NXTCommand.setVerify(true);
		
		lightSensorLeft = new LightSensor(L_SENSOR_PORT_LEFT);
		lightSensorRight = new LightSensor(L_SENSOR_PORT_RIGHT);
		objectSensor = new UltrasonicSensor(U_SENSOR_PORT);
	}

	/**
	 * Ends connection with robot
	 */
	public static void closeConnection() {
		NXTCommand.close();
	}

	/**
	 * Set the robot's base speed for all movement
	 * @param speed int Robot's base speed
	 */
	public static void setBaseSpeed(int speed) {
		baseSpeed = speed;
	}

	/**
	 * Get the robot's base speed for all movement
	 * @retuns int Returns the Robot's base speed
	 */
	public static int getBaseSpeed() {
		return baseSpeed;
	}

	/**
	 * Base method for determining whether a light sensor detects black within a given threshold
	 * @param lsensor LightSensor NXT LightSensor
	 * @param threshold int Highest value for which black is still detected
	 * @return boolean Returns true if given light sensor detects black
	 */
	private static boolean blackDetected(LightSensor lsensor, int threshold) {
		return lsensor.getLightValue() <= threshold;
	}

	/**
	 * Determines whether the left light sensor detects black
	 * @return boolean Returns <code>true</code> if left sensor detects black
	 */
	public static boolean blackDetectedLeft() {
		return blackDetected(lightSensorLeft, LEFT_LIGHT_THRESHOLD);
	}

	/**
	 * Determines whether the right light sensor detects black
	 * @return boolean Returns <code>true</code> if right sensor detects black
	 */
	public static boolean blackDetectedRight() {
		return blackDetected(lightSensorRight, RIGHT_LIGHT_THESHOLD);
	}

	/**
	 * Helper method determining whether either sensor detects black
	 * @return boolean Returns <code>true</code> if either sensors detects black
	 */
	public static boolean blackDetectedEither() {
		return blackDetected(lightSensorLeft, LEFT_LIGHT_THRESHOLD) || blackDetected(lightSensorRight, RIGHT_LIGHT_THESHOLD);
	}

	/**
	 * Helper method determining whether both sensors detects black
	 * @return boolean Returns true if both sensors detects black
	 */
	public static boolean blackDetectedBoth() {
		return blackDetected(lightSensorLeft, LEFT_LIGHT_THRESHOLD) && blackDetected(lightSensorRight, RIGHT_LIGHT_THESHOLD);
	}

	/**
	 * Determines whether an obstacle is detected in a given range
	 * @param range int Furthest distance until object is still detected (in cm)
	 * @return boolean Returns true 
	 */
	public static boolean obstacleDetected(int range) {
		return objectSensor.getDistance() <= range;
	}

	/**
	 * Makes a beep sound for a given duration
	 * @param duration int Length of beep in milliseconds
	 * @param hz int Beep frequency in hertz
	 * @return Delayer Used to chain waitFor() to prevent further program execution until beep has fully sounded, used for playing a sequence of notes.
	 */

	public static Delayer beep(int duration, int hz) {
		Sound.playTone(hz, duration);
		return new Delayer();
	}

	/**
	 * Helper method for a default beep sound
	 * @param duration Length of beep in milliseconds
	 * @return Delayer Used to chain waitFor() to prevent further program execution until beep has fully sounded, used for playing a sequence of notes.
	 */
	public static Delayer beep(int duration) {
		return beep(duration, DEFAULT_BEEP_FREQ);
	}

	// TODO: Add javadoc here
	public static Delayer goForward(speed) {
		MOTOR_LEFT.setSpeed(speed);
		MOTOR_RIGHT.setSpeed(speed);
		MOTOR_LEFT.forward();
		MOTOR_RIGHT.forward();
		return new Delayer();
	}

	/**
	 * Instruct robot to move forward at the base speed
	 * @return Delayer Used to chain waitFor() to provide a minimum duration of movement is achieved
	 */
	public static Delayer goForward() {
		return goForward(baseSpeed);
	}

	public static Delayer goBackward(speed) {
		MOTOR_LEFT.setSpeed(speed);
		MOTOR_RIGHT.setSpeed(speed);
		MOTOR_LEFT.backward();
		MOTOR_RIGHT.backward();
		return new Delayer();
	}

	/**
	 * Instruct robot to move backward at the baseSpeed
	 * @return Delayer Used to chain waitFor() to provide a minimum duration of movement is achieved
	 */
	public static Delayer goBackward() {
		return goBackward(baseSpeed);
	}

	/**
	 * Instruct the robot to stop all movement
	 * @return Delayer Used to chain waitFor() to provide a minimum duration of movement is achieved
	 */
	public static Delayer stop() {
		MOTOR_LEFT.stop();
		MOTOR_RIGHT.stop();
		return new Delayer();
	}

	/**
	 * Instruct robot to turn left
	 * @param speedFactor Ratio between the speeds of the two wheels: higher the ratio, the sharper the turn
	 * @return Delayer Used to chain waitFor() to provide a minimum duration of movement is achieved
	 */
	public static Delayer goLeft(int speedFactor) {
		// Change zero and negative values to a default, preventing unwanted / dangerous behaviour		
		if(speedFactor > 0) {
			MOTOR_LEFT.setSpeed(baseSpeed / speedFactor);
			MOTOR_RIGHT.setSpeed(baseSpeed);
			MOTOR_LEFT.forward();
			MOTOR_RIGHT.forward();
		}
		else {
			goLeft();
		}
		return new Delayer();
	}

	/**
	 * Instruct robot to turn left using the default speed ratio
	 * @return Delayer Used to chain waitFor() to provide a minimum duration of movement is achieved
	 */
	public static Delayer goLeft() {
		return goLeft(DEFAULT_SPEED_FACTOR);
	}

	/**
	 * Instruct robot to turn right
	 * @param speedFactor Ratio between the speeds of the two wheels: higher the ratio, the sharper the turn
	 * @return Delayer Used to chain waitFor() to provide a minimum duration of movement is achieved
	 */
	public static Delayer goRight(int speedFactor) {
		// Change zero and negative values to a default, preventing unwanted / dangerous behaviour
		if(speedFactor > 0) {
			MOTOR_LEFT.setSpeed(baseSpeed);
			MOTOR_RIGHT.setSpeed(baseSpeed / speedFactor);
			MOTOR_LEFT.forward();
			MOTOR_RIGHT.forward();
		}
		else {
			goRight();
		}
		return new Delayer();
	}

	/**
	 * Instruct robot to turn right using the default speed ratio
	 * @return Delayer Used to chain waitFor() to provide a minimum duration of movement is achieved
	 */
	public static Delayer goRight() {
		return goRight(DEFAULT_SPEED_FACTOR);
	}

	/**
	 * Instruct the robot to spin left on the spot
	 * @param speed int Speed for turning left
	 * @return Delayer Used to chain waitFor() to provide a minimum duration of movement is achieved
	 */
	public static Delayer goLeftTurnOnSpot(int speed) {
		// Division by two gives a more desirable turning speed relative to the base speed
		MOTOR_LEFT.setSpeed(speed / 2);
		MOTOR_RIGHT.setSpeed(speed / 2);
		MOTOR_LEFT.backward();
		MOTOR_RIGHT.forward();
		return new Delayer();
	}

	/**
	 * Helper method to instruct the robot to spin left on the spot (using the base speed)
	 * @return Delayer Used to chain waitFor() to provide a minimum duration of movement is achieved
	 */
	public static Delayer goLeftTurnOnSpot() {
		return goLeftTurnOnSpot(baseSpeed);
	}

	/**
	 * Instruct the robot to spin right on the spot
	 * @param speed int Speed for turning right
	 * @return Delayer Used to chain waitFor() to provide a minimum duration of movement is achieved
	 */
	public static Delayer goRightTurnOnSpot(int speed) {
		MOTOR_LEFT.setSpeed(speed / 2);
		MOTOR_RIGHT.setSpeed(speed / 2);
		MOTOR_LEFT.forward();
		MOTOR_RIGHT.backward();
		return new Delayer();
	}

	/**
	 * Helper method to instruct the robot to spin right on the spot (using the base speed)
	 * @return Delayer Used to chain waitFor() to provide a minimum duration of movement is achieved
	 */
	public static Delayer goRightTurnOnSpot() {
		return goRightTurnOnSpot(baseSpeed);
	}

	public static void main(String[] args) throws InterruptedException {
		// Test harness 
		RobotControl.initialise();
		int delayBetweenTests = 2000;

		System.out.println("Robot is turning right on spot");
		RobotControl.goRightTurnOnSpot().waitFor(delayBetweenTests);

		System.out.println("Robot is turning left on spot");
		RobotControl.goLeftTurnOnSpot().waitFor(delayBetweenTests);

		System.out.println("Robot is turning right");
		RobotControl.goRight().waitFor(delayBetweenTests);

		System.out.println("Robot is turning left");
		RobotControl.goLeft().waitFor(delayBetweenTests);

		System.out.println("Robot is turning right slowly");
		RobotControl.goRight(4).waitFor(delayBetweenTests);

		System.out.println("Robot is turning left slowly");
		RobotControl.goLeft(4).waitFor(delayBetweenTests);

		System.out.println("Robot is going forwards");
		RobotControl.goForward().waitFor(delayBetweenTests);

		System.out.println("Robot is going backwards");
		RobotControl.goBackward().waitFor(delayBetweenTests);

		System.out.println("Base speed changed to 300");
		RobotControl.setBaseSpeed(300);

		System.out.println("Robot is going forwards");
		RobotControl.goForward().waitFor(delayBetweenTests);

		System.out.println("Robot has stopped");
		RobotControl.stop();

		System.out.println("Robot is beeping");
		RobotControl.beep(delayBetweenTests).waitFor(delayBetweenTests);

		System.out.println("Testing obstacle detection in 2 seconds");
		Thread.sleep(delayBetweenTests);
		System.out.println(RobotControl.obstacleDetected(10) ? "Detected obstacle" : "No obstacle detected");

		System.out.println("Testing obstacle detection again in 2 seconds");
		Thread.sleep(delayBetweenTests);
		System.out.println(RobotControl.obstacleDetected(10) ? "Detected obstacle" : "No obstacle detected");

		System.out.println("Testing left sensor black detection in 2 seconds");
		Thread.sleep(delayBetweenTests);
		System.out.println(RobotControl.blackDetectedLeft() ? "Detected black" : "No black detected");

		System.out.println("Testing left sensor black detection in 2 seconds");
		Thread.sleep(delayBetweenTests);
		System.out.println(RobotControl.blackDetectedLeft() ? "Detected black" : "No black detected");

		System.out.println("Testing right sensor black detection in 2 seconds");
		Thread.sleep(delayBetweenTests);
		System.out.println(RobotControl.blackDetectedRight() ? "Detected black" : "No black detected");

		System.out.println("Testing right sensor black detection in 2 seconds");
		Thread.sleep(delayBetweenTests);
		System.out.println(RobotControl.blackDetectedRight() ? "Detected black" : "No black detected");

		System.out.println("Testing both sensors black detection in 2 seconds");
		Thread.sleep(delayBetweenTests);
		System.out.println(RobotControl.blackDetectedBoth() ? "Detected black" : "No black detected");

		System.out.println("Testing both sensors black detection in 2 seconds");
		Thread.sleep(delayBetweenTests);
		System.out.println(RobotControl.blackDetectedBoth() ? "Detected black" : "No black detected");

		System.out.println("Testing either sensors black detection in 2 seconds");
		Thread.sleep(delayBetweenTests);
		System.out.println(RobotControl.blackDetectedEither() ? "Detected black" : "No black detected");

		System.out.println("Testing either sensors black detection in 2 seconds");
		Thread.sleep(delayBetweenTests);
		System.out.println(RobotControl.blackDetectedEither() ? "Detected black" : "No black detected");

		RobotControl.closeConnection();
	}
}
