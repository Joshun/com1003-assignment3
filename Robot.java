/**
 * @author Jack Deadman
 * @author Joshua O'Leary
 */

import icommand.nxt.*;
import icommand.nxt.comm.NXTCommand;
import java.util.Scanner;

public class Robot {
	private final static Motor MOTOR_LEFT = Motor.B;
	private final static Motor MOTOR_RIGHT = Motor.C;
	private final static int LIGHT_THESHOLD = 550;
	
	// New SensorPort for Light Sensor
	private final static SensorPort L_SENSOR_PORT_1 = SensorPort.S1;
	private final static SensorPort L_SENSOR_PORT_2 = SensorPort.S2;
	
	// New SensorPort for Ultrasonic Sensor
	private final static SensorPort U_SENSOR_PORT = SensorPort.S4;
	
	// Lightsensor object
	private static LightSensor lineDetect1;
	private static LightSensor lineDetect2;
	
	// Ultrasonic disance sensor object
	private static UltrasonicSensor distanceDetect;

	private static RobotState currentState = RobotState.FORWARD;

	private static long currentTime = 0;
	private static long previousTime = 0;

	/**
	 * Accessor - returns light detection status within a threshold value
	 * @return boolean returns true if black is detected and false otherwise
	 */
	public static boolean blackDetected(LightSensor lsensor) {
		return lsensor.getLightValue() < LIGHT_THESHOLD;
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
		MOTOR_LEFT.backward();
		MOTOR_RIGHT.backward();
	}

	/**
	 * Mutator - tell robot to move forward by telling both motors to spin forwards
	 */
	public static void goForward(){
		MOTOR_LEFT.forward();
		MOTOR_RIGHT.forward();
	}

	/**
	 * Mutator - tell robot to turn left
	 */
	public static void goLeft() {
		MOTOR_LEFT.backward();
		//~ MOTOR_LEFT.stop();
		MOTOR_RIGHT.forward();
	}

	/**
	 * Mutator - tell robot to turn right
	 */
	public static void goRight() {
		MOTOR_LEFT.forward();
		MOTOR_RIGHT.backward();
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
	public static void setSpeed(int speed) {
		MOTOR_LEFT.setSpeed(speed);
		MOTOR_RIGHT.setSpeed(speed);
	}

	public static void action() {
		switch( currentState ) {
			case FORWARD:
				goForward();
				break;
			case BACKWARD:
				goBackward();
				break;
			case LEFT:
				goLeft();
				break;
			case RIGHT:
				goRight();
				break;
			case STOPPED:
				stop();
				break;
			default:
				stop();
		}
	}

	public static void setState(RobotState state) {
		currentState = state;
	}

	public static void updateLastTime() {
		previousTime = currentTime;
	}

	public static boolean timeExceeded(int amount) {
		return (previousTime + amount) > currentTime;
	}
	
	/**
	 * Start method - starts the robot's main loop
	 * @param interval int interval by which the main loop sleeps at the end of each iteration
	 * @param startSpeed int the initial speed the robot is placed in
	 */
	public static void start(int interval, int startSpeed) throws InterruptedException {
		boolean ended = false;
		boolean reachedLine = false;
		boolean leftTurn = false;
		boolean turnedLast = false;
		setSpeed(startSpeed);
		goForward();
		while( ! ended ) {
			System.out.println("distanceDetect.getDistance():" + distanceDetect.getDistance());
			if ( distanceDetect.getDistance() < 15 ) {
				goRight();
				turnedLast = false;
				beep(500);
				Thread.sleep(300);
			}
			if ( blackDetected() ) {
	
				// Go forward
				reachedLine = true;
				System.out.println("Going forward");
				goForward();
				
				turnedLast = false;
			}
			else if (reachedLine){
				// Stop
				// Turn right
				
				// Only change direction of turning if last movement was not a turn (i.e. going forward)
				// This prevents constantly turning backwards and forwards while black hasn't been detected
				// (keep same direction until black is next detected)
				if( ! turnedLast ) {
					turnedLast = true;
					leftTurn = !leftTurn;
				}
				
				if( leftTurn ) {
					System.out.println("Going left");
					goLeft();
				}
				else {
					System.out.println("Going right");	
					goRight();
				}			
			}
			if( ended ) stop();
			Thread.sleep(interval);
			currentTime += interval;
		}		
	}

	public static void main(String[] args)  throws InterruptedException {
		final int INTERVAL = 1;
		final int SPEED = 100;
		NXTCommand.open();
		NXTCommand.setVerify(true);
		lineDetect1 = new LightSensor(L_SENSOR_PORT_1);
		lineDetect2 = new LightSensor(L_SENSOR_PORT_2);
		distanceDetect = new UltrasonicSensor(U_SENSOR_PORT);
		

		stop();
		Scanner keyboardInput = new Scanner(System.in);
		keyboardInput.nextLine();
		
		System.out.println("Robot control started.");
		start(INTERVAL, SPEED);
	}
}
