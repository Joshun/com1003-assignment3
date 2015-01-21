/**
 * Wrapper class for abstracting the NXT icommand api
 * @author Jack Deadman
 * @author Joshua O'Leary
 */

import icommand.nxt.*;
import icommand.nxt.comm.NXTCommand;

public class RobotControl {

	// Default ratio between wheels when turning (e.g. with a factor of 2, outer wheel is 2x faster than pivot wheel)
	private static final int DEFAULT_SPEED_FACTOR = 2;

	// Default frequency used for beeping
	private static final int DEFAULT_BEEP_FREQ = 500;

	private final static Motor MOTOR_LEFT = Motor.C;
	private final static Motor MOTOR_RIGHT = Motor.B;

	// Light threshold values for both sensors (maximum brightness by which black is detected),
	// callibrated specifically for our robot's sensors
	private final static int LEFT_LIGHT_THRESHOLD = 520;
	private final static int RIGHT_LIGHT_THESHOLD = 570;
	
	// SensorPort for Light Sensor
	private final static SensorPort L_SENSOR_PORT_LEFT = SensorPort.S2;
	private final static SensorPort L_SENSOR_PORT_RIGHT = SensorPort.S1;
	
	// SensorPort for Ultrasonic Sensor
	private final static SensorPort U_SENSOR_PORT = SensorPort.S4;
	
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
	 * Set the robot's base speed for all movement
	 * @param speed int Robot's base speed
	 */
	public static void setBaseSpeed(int speed) {
		baseSpeed = speed;
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
	 * @return boolean Returns true if left sensor detects black
	 */
	public static boolean blackDetectedLeft() {
		// System.out.println("Left sensor: " + lightSensorLeft.getLightValue() + " " + (lightSensorLeft.getLightValue() < LEFT_LIGHT_THRESHOLD));
		return blackDetected(lightSensorLeft, LEFT_LIGHT_THRESHOLD);
	}

	/**
	 * Determines whether the right light sensor detects black
	 * @return boolean Returns true if right sensor detects black
	 */
	public static boolean blackDetectedRight() {
		return blackDetected(lightSensorRight, RIGHT_LIGHT_THESHOLD);
	}

	/**
	 * Helper method determining whether either sensor detects black
	 * @return boolean Returns true if either sensors detects black
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
	 */

	public static void beep(int duration, int hz)
	{
		Sound.playTone(hz, duration);
	}

	/**
	 * Helper method for a default beep sound
	 * @param duration Length of beep in milliseconds
	 */
	public static void beep(int duration) {
		beep(duration, 500);
	}

	/**
	 * Instruct robot to move forward
	 */
	public static void goForward(){
		MOTOR_LEFT.setSpeed(baseSpeed);
		MOTOR_RIGHT.setSpeed(baseSpeed);
		MOTOR_LEFT.forward();
		MOTOR_RIGHT.forward();
	}

	/**
	 * Instruct robot to move backward
	 */
	public static void goBackward(){
		MOTOR_LEFT.setSpeed(baseSpeed);
		MOTOR_RIGHT.setSpeed(baseSpeed);		
		MOTOR_LEFT.backward();
		MOTOR_RIGHT.backward();
	}

	/**
	 * Instruct the robot to stop all movement
	 */
	public static void stop() {
		MOTOR_LEFT.stop();
		MOTOR_RIGHT.stop();
	}

	/**
	 * Instruct robot to turn left
	 * @param speedFactor Ratio between the speeds of the two wheels: higher the ratio, the sharper the turn
	 */
	public static void goLeft(int speedFactor) {
		// 
		MOTOR_LEFT.setSpeed(baseSpeed / speedFactor == 0 ? 1 : speedFactor);
		MOTOR_RIGHT.setSpeed(baseSpeed);
		MOTOR_LEFT.forward();
		MOTOR_RIGHT.forward();
	}

	/**
	 * Instruct robot to turn left using the default speed ratio
	 */
	public static void goLeft() {
		goLeft(DEFAULT_SPEED_FACTOR);
	}

	/**
	 * Instruct robot to turn right
	 * @param speedFactor Ratio between the speeds of the two wheels: higher the ratio, the sharper the turn
	 */
	public static void goRight(int speedFactor) {
		MOTOR_LEFT.setSpeed(baseSpeed);
		MOTOR_RIGHT.setSpeed(baseSpeed / speedFactor == 0 ? 1 : speedFactor);
		MOTOR_LEFT.forward();
		MOTOR_RIGHT.forward();
	}

	/**
	 * Instruct robot to turn right using the default speed ratio
	 */
	public static void goRight() {
		goRight(DEFAULT_SPEED_FACTOR);
	}

	/**
	 * Instruct the robot to spin left on the spot
	 * @param speed int Speed for turning left
	 */
	public static void goHardLeft(int speed) {
		MOTOR_LEFT.setSpeed(speed / 2);
		MOTOR_RIGHT.setSpeed(speed / 2);
		MOTOR_LEFT.backward();
		MOTOR_RIGHT.forward();
	}

	/**
	 * Helper method to instruct the robot to spin left on the spot (using the base speed)
	 */
	public static void goHardLeft() {
		goHardLeft(baseSpeed);
	}

	/**
	 * Instruct the robot to spin right on the spot
	 * @param speed int Speed for turning right
	 */
	public static void goHardRight(int speed) {
		MOTOR_LEFT.setSpeed(speed / 2);
		MOTOR_RIGHT.setSpeed(speed / 2);
		MOTOR_LEFT.forward();
		MOTOR_RIGHT.backward();
	}

	/**
	 * Helper method to instruct the robot to spin right on the spot (using the base speed)
	 */
	public static void goHardRight() {
		goHardRight(baseSpeed);
	}

	public static void main(String[] args)  throws InterruptedException {
		// Test harness 
		RobotControl.initialise();
		int delayBetweenTests = 2000;

		System.out.println("Robot is turning hard right");
		RobotControl.goHardRight();
		Thread.sleep(delayBetweenTests);

		System.out.println("Robot is turning hard left");
		RobotControl.goHardLeft();
		Thread.sleep(delayBetweenTests);

		System.out.println("Robot is turning right");
		RobotControl.goRight();
		Thread.sleep(delayBetweenTests);

		System.out.println("Robot is turning left");
		RobotControl.goLeft();
		Thread.sleep(delayBetweenTests);

		System.out.println("Robot is turning right slowly");
		RobotControl.goRight(4);
		Thread.sleep(delayBetweenTests);

		System.out.println("Robot is turning right slowly");
		RobotControl.goLeft(4);
		Thread.sleep(delayBetweenTests);

		System.out.println("Robot is going forwards");
		RobotControl.goForward();
		Thread.sleep(delayBetweenTests);

		System.out.println("Robot is going backwards");
		RobotControl.goBackward();
		Thread.sleep(delayBetweenTests);

		System.out.println("Base speed changed to 300");
		RobotControl.setBaseSpeed(300);

		System.out.println("Robot is going forwards");
		RobotControl.goForward();
		Thread.sleep(delayBetweenTests);

		System.out.println("Robot has stopped");
		RobotControl.stop();

		System.out.println("Robot is beeping");
		RobotControl.beep(delayBetweenTests);
		Thread.sleep(delayBetweenTests);

		System.out.println("Testing obstacle detection in 2 seconds");
		Thread.sleep(delayBetweenTests);
		System.out.println(RobotControl.obstacleDetected(10) ? "Detected obstacle" : "No obstacle detected");
		Thread.sleep(delayBetweenTests);

		System.out.println("Testing obstacle detection again in 2 seconds");
		Thread.sleep(delayBetweenTests);
		System.out.println(RobotControl.obstacleDetected(10) ? "Detected obstacle" : "No obstacle detected");
		Thread.sleep(delayBetweenTests);

		System.out.println("Testing left sensor black detection in 2 seconds");
		Thread.sleep(delayBetweenTests);
		System.out.println(RobotControl.blackDetectedLeft() ? "Detected black" : "No black detected");
		Thread.sleep(delayBetweenTests);

		System.out.println("Testing left sensor black detection in 2 seconds");
		Thread.sleep(delayBetweenTests);
		System.out.println(RobotControl.blackDetectedLeft() ? "Detected black" : "No black detected");
		Thread.sleep(delayBetweenTests);		

		System.out.println("Testing right sensor black detection in 2 seconds");
		Thread.sleep(delayBetweenTests);
		System.out.println(RobotControl.blackDetectedRight() ? "Detected black" : "No black detected");
		Thread.sleep(delayBetweenTests);

		System.out.println("Testing right sensor black detection in 2 seconds");
		Thread.sleep(delayBetweenTests);
		System.out.println(RobotControl.blackDetectedRight() ? "Detected black" : "No black detected");
		Thread.sleep(delayBetweenTests);

		System.out.println("Testing both sensors black detection in 2 seconds");
		Thread.sleep(delayBetweenTests);
		System.out.println(RobotControl.blackDetectedBoth() ? "Detected black" : "No black detected");
		Thread.sleep(delayBetweenTests);

		System.out.println("Testing both sensors black detection in 2 seconds");
		Thread.sleep(delayBetweenTests);
		System.out.println(RobotControl.blackDetectedBoth() ? "Detected black" : "No black detected");
		Thread.sleep(delayBetweenTests);

		System.out.println("Testing either sensors black detection in 2 seconds");
		Thread.sleep(delayBetweenTests);
		System.out.println(RobotControl.blackDetectedEither() ? "Detected black" : "No black detected");
		Thread.sleep(delayBetweenTests);

		System.out.println("Testing either sensors black detection in 2 seconds");
		Thread.sleep(delayBetweenTests);
		System.out.println(RobotControl.blackDetectedEither() ? "Detected black" : "No black detected");
		Thread.sleep(delayBetweenTests);
	}
}
