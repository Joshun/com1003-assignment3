import icommand.nxt.*;
import icommand.nxt.comm.NXTCommand;

public class Robot {
	private final static Motor MOTOR_LEFT = Motor.B;
	private final static Motor MOTOR_RIGHT = Motor.C;
	private final static int LIGHT_THESHOLD = 550;
	private final static SensorPort SENSOR_PORT = SensorPort.S2;
	private static LightSensor lineDetect;

	private static RobotState currentState = RobotState.FORWARD;

	private static long currentTime = 0;
	private static long previousTime = 0;

	public static boolean blackDetected() {
		//LightSensor lineDetect = new LightSensor(SensorPort.S2);

		return lineDetect.getLightValue() < LIGHT_THESHOLD;
	}

	public static void beep(int duration) {
		Sound.playTone(500, duration);
	}

	public static void goForward(){
		MOTOR_LEFT.forward();
		MOTOR_RIGHT.forward();
	}

	public static void goBackward(){
		MOTOR_LEFT.backward();
		MOTOR_RIGHT.backward();
	}

	public static void goLeft() {
		MOTOR_LEFT.backward();
		MOTOR_RIGHT.forward();
	}

	public static void goRight() {
		MOTOR_LEFT.forward();
		MOTOR_RIGHT.backward();
	}

	public static void stop() {
		MOTOR_LEFT.stop();
		MOTOR_RIGHT.stop();
	}

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

	public static void start(int interval, int startSpeed) throws InterruptedException {
		boolean ended = false;
		setSpeed(startSpeed);
		goForward();
		while( ! ended ) {
			// ColourSensor lineDetect = new ColorSensor(SENSOR_PORT);
			// statements to control the robot
			//
			//Motor.B.backward();
			//Motor.B.setSpeed(900);
			if( blackDetected() ) {
				beep(interval);
				System.out.println("Black detected.");
				if( currentState == RobotState.FORWARD ) {
					setState(RobotState.LEFT);
				}
				else if( currentState == RobotState.LEFT ) {
					setState(RobotState.STOPPED);
				}

				action();
				// ended = true;
			}
			if( ended ) stop();
			Thread.sleep(interval);
			currentTime += interval;
		}		
	}

	public static void main(String[] args)  throws InterruptedException {
		final int INTERVAL = 60;

		NXTCommand.open();
		NXTCommand.setVerify(true);
		lineDetect = new LightSensor(SENSOR_PORT);

		System.out.println("Robot control started.");
		start(INTERVAL, 50);
	}
}