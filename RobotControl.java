/**
 * @author Jack Deadman
 * @author Joshua O'Leary
 */

import icommand.nxt.*;
import icommand.nxt.comm.NXTCommand;
import java.util.Scanner;

public class RobotControl {
	private final static Motor MOTOR_LEFT = Motor.B;
	private final static Motor MOTOR_RIGHT = Motor.C;

	// Light threshold values for both sensors
	// TODO: light value range, not just threshold
	private final static int LEFT_LIGHT_THRESHOLD = 520;
	private final static int RIGHT_LIGHT_THESHOLD = 570;

	// Object distance threshold value
	private final static int OBSTACLE_DISTANCE_THRESHOLD = 12;
	private final static int NEAR_OBJECT_DISTANCE_THRESHOLD = 15;
	
	// New SensorPort for Light Sensor
	private final static SensorPort L_SENSOR_PORT_LEFT = SensorPort.S2;
	private final static SensorPort L_SENSOR_PORT_RIGHT = SensorPort.S1;
	
	// New SensorPort for Ultrasonic Sensor
	private final static SensorPort U_SENSOR_PORT = SensorPort.S4;
	
	// Lightsensor object
	private static LightSensor lightSensorLeft;
	private static LightSensor lightSensorRight;
	
	// Ultrasonic disance sensor object
	private static UltrasonicSensor objectSensor;

	// "Base" speed
	private static int baseSpeed = 100;
	private static final int DEFAULT_SPEED_FACTOR = 2;


	/**
	 * Accessor - returns light detection status within a threshold value
	 * @return boolean returns true if black is detected and false otherwise
	 */
	public static boolean blackDetected(LightSensor lsensor, int threshold) {
		return lsensor.getLightValue() < threshold;
	}

	public static boolean blackDetectedLeft() {
		System.out.println("Left sensor: " + lightSensorLeft.getLightValue() + " " + (lightSensorLeft.getLightValue() < LEFT_LIGHT_THRESHOLD));
		return blackDetected(lightSensorLeft, LEFT_LIGHT_THRESHOLD);
	}

	public static boolean blackDetectedRight() {
		System.out.println("Right sensor: " + lightSensorRight.getLightValue() + " " + (lightSensorRight.getLightValue() < RIGHT_LIGHT_THESHOLD));
		return blackDetected(lightSensorRight, RIGHT_LIGHT_THESHOLD);
	}

	public static boolean blackDetectedEither() {
		return blackDetected(lightSensorLeft, LEFT_LIGHT_THRESHOLD) || blackDetected(lightSensorRight, RIGHT_LIGHT_THESHOLD);
	}

	public static boolean blackDetectedBoth() {
		return blackDetected(lightSensorLeft, LEFT_LIGHT_THRESHOLD) && blackDetected(lightSensorRight, RIGHT_LIGHT_THESHOLD);
	}

	public static boolean obstacleDetected() {
		return objectSensor.getDistance() < OBSTACLE_DISTANCE_THRESHOLD;
	}

	public static boolean nearObject() {
		return objectSensor.getDistance() < NEAR_OBJECT_DISTANCE_THRESHOLD;
	}

	/**
	 * Used to make beep sound for a given duration
	 * @param duration int length of tone
	 */
	public static void beep(int duration) {
		Sound.playTone(500, duration);
	}

	/**
	 * Mutator - tell robot to move backward by telling both motors to spin backwards
	 */
	public static void goBackward(){
		MOTOR_LEFT.setSpeed(baseSpeed);
		MOTOR_RIGHT.setSpeed(baseSpeed);		
		MOTOR_LEFT.backward();
		MOTOR_RIGHT.backward();
	}

	/**
	 * Mutator - tell robot to move forward by telling both motors to spin forwards
	 */
	public static void goForward(){
		MOTOR_LEFT.setSpeed(baseSpeed);
		MOTOR_RIGHT.setSpeed(baseSpeed);
		MOTOR_LEFT.forward();
		MOTOR_RIGHT.forward();
	}

	/**
	 * Mutator - tell robot to turn left
	 */

	public static void goLeft(int speedFactor) {
		MOTOR_LEFT.setSpeed(baseSpeed);
		MOTOR_RIGHT.setSpeed(baseSpeed / speedFactor);
		MOTOR_LEFT.forward();
		MOTOR_RIGHT.forward();
	}

	public static void goLeft() {
		goLeft(DEFAULT_SPEED_FACTOR);
	}

	/**
	 * Mutator - tell robot to turn right
	 */
	public static void goRight(int speedFactor) {
		MOTOR_LEFT.setSpeed(baseSpeed / speedFactor);
		MOTOR_RIGHT.setSpeed(baseSpeed);
		MOTOR_LEFT.forward();
		MOTOR_RIGHT.forward();
		// MOTOR_RIGHT.backward();
		//~ MOTOR_RIGHT.stop();
	}

	public static void goRight() {
		goRight(DEFAULT_SPEED_FACTOR);
	}

	public static void goHardLeft() {
		MOTOR_LEFT.setSpeed(baseSpeed / 2);
		MOTOR_RIGHT.setSpeed(baseSpeed / 2);
		MOTOR_LEFT.backward();
		MOTOR_RIGHT.forward();
	}

	public static void goHardRight() {
		MOTOR_LEFT.setSpeed(baseSpeed / 2);
		MOTOR_RIGHT.setSpeed(baseSpeed / 2);
		MOTOR_LEFT.forward();
		MOTOR_RIGHT.backward();
	}

	/**
	 * Mutator - tell both motors to stop spinning
	 */
	public static void stop() {
		MOTOR_LEFT.stop();
		MOTOR_RIGHT.stop();
	}

	/**
	 * Mutator - sets the speed of both motors
	 * @param speed int speed value (0-900)
	 */

	public static void setBaseSpeed(int speed) {
		baseSpeed = speed;
	}

	// private static void setSpeed(int speed) {
	// 	MOTOR_LEFT.setSpeed(speed);
	// 	MOTOR_RIGHT.setSpeed(speed);
	// }

	public static void initialise() {
		NXTCommand.open();
		NXTCommand.setVerify(true);
		lightSensorLeft = new LightSensor(L_SENSOR_PORT_LEFT);
		lightSensorRight = new LightSensor(L_SENSOR_PORT_RIGHT);
		objectSensor = new UltrasonicSensor(U_SENSOR_PORT);
	}		

	public static void debug() throws InterruptedException {
		while( true ) {
			int lSensorValue = lightSensorLeft.getLightValue();
			int rSensorValue = lightSensorRight.getLightValue();

			System.out.println("Left: " + lSensorValue + " Right: " + rSensorValue);

			Thread.sleep(50);
		}
	}

	public static void main(String[] args)  throws InterruptedException {
		RobotControl.initialise();
	
		while(true) {
			System.out.println("Distance: " + objectSensor.getDistance());
			Thread.sleep(20);
		}
	}
}
