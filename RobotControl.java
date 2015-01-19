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
	private final static int LIGHT_THESHOLD = 550;
	
	// New SensorPort for Light Sensor
	private final static SensorPort L_SENSOR_PORT_LEFT = SensorPort.S1;
	private final static SensorPort L_SENSOR_PORT_RIGHT = SensorPort.S2;
	
	// New SensorPort for Ultrasonic Sensor
	private final static SensorPort U_SENSOR_PORT = SensorPort.S4;
	
	// Lightsensor object
	private static LightSensor lightSensorLeft;
	private static LightSensor lightSensorRight;
	
	// Ultrasonic disance sensor object
	private static UltrasonicSensor distanceDetect;

	// "Base" speed
	private static int baseSpeed = 100;
	private static int speedFactor = 2;


	/**
	 * Accessor - returns light detection status within a threshold value
	 * @return boolean returns true if black is detected and false otherwise
	 */
	public static boolean blackDetected(LightSensor lsensor) {
		return lsensor.getLightValue() < LIGHT_THESHOLD;
	}

	public static boolean blackDetectedLeft() {
		return blackDetected(lightSensorLeft);
	}

	public static boolean blackDetectedRight() {
		return blackDetected(lightSensorRight);
	}

	public static boolean blackDetectedEither() {
		return blackDetected(lightSensorLeft) || blackDetected(lightSensorRight);
	}

	public static boolean blackDetectedBoth() {
		System.out.println("Left: " + blackDetected(lightSensorLeft) + " Right: " + blackDetected(lightSensorRight));
		System.out.println("Both: " + (blackDetected(lightSensorLeft) && blackDetected(lightSensorRight)));
		return blackDetected(lightSensorLeft) && blackDetected(lightSensorRight);
	}

	public static boolean obstacleDetected() {
		return false;
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
	public static void goLeft() {
		MOTOR_LEFT.setSpeed(baseSpeed);
		MOTOR_RIGHT.setSpeed(baseSpeed / speedFactor);
		MOTOR_LEFT.forward();
		MOTOR_RIGHT.forward();
	}

	/**
	 * Mutator - tell robot to turn right
	 */
	public static void goRight() {
		MOTOR_LEFT.setSpeed(baseSpeed / speedFactor);
		MOTOR_RIGHT.setSpeed(baseSpeed);
		MOTOR_LEFT.forward();
		MOTOR_RIGHT.forward();
		// MOTOR_RIGHT.backward();
		//~ MOTOR_RIGHT.stop();
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
		distanceDetect = new UltrasonicSensor(U_SENSOR_PORT);
	}		


	public static void main(String[] args)  throws InterruptedException {
		RobotControl.initialise();
		RobotControl.goForward();
		Thread.sleep(1000);
		RobotControl.stop();
	}
}
