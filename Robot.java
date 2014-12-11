import icommand.nxt.*;
import icommand.nxt.comm.NXTCommand;

public class Robot {
	private final static Motor MOTOR_LEFT = Motor.B;
	private final static Motor MOTOR_RIGHT = Motor.C;
	private final static int LIGHT_THESHOLD = 550;
	private final static SensorPort SENSOR_PORT = SensorPort.S2;
	private static LightSensor lineDetect;

	RobotState currentState = RobotState.FORWARD;

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

	public static void stop() {
		MOTOR_LEFT.stop();
		MOTOR_RIGHT.stop();
	}

	public static void setSpeed(int speed) {
		MOTOR_LEFT.setSpeed(speed);
		MOTOR_RIGHT.setSpeed(speed);
	}

	public static void goLeft() {
		MOTOR_LEFT.backward();
		MOTOR_RIGHT.forward();
	}

	public static void goRight() {
		MOTOR_LEFT.forward();
		MOTOR_RIGHT.backward();
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
				ended = true;
			}
			if( ended ) stop();
			Thread.sleep(interval);
		}		
	}

	public static void main(String[] args)  throws InterruptedException {
		final int INTERVAL = 40;

		NXTCommand.open();
		NXTCommand.setVerify(true);
		lineDetect = new LightSensor(SENSOR_PORT);

		System.out.println("Robot control started.");
		start(40, 50);
	}
}