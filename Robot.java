import icommand.nxt.*;
import icommand.nxt.comm.NXTCommand;

public class Robot {
	private final static int LIGHT_THESHOLD = 550;
	private final static SensorPort SENSOR_PORT = SensorPort.S2;
	private static LightSensor lineDetect;

	public static boolean blackDetected() {
		//LightSensor lineDetect = new LightSensor(SensorPort.S2);

		return lineDetect.getLightValue() < LIGHT_THESHOLD;
	}

	public static void playTone(int duration) {
		Sound.playTone(500, duration);
	}

	public static void main(String[] args) throws InterruptedException {
		final int INTERVAL = 40;

		NXTCommand.open();
		NXTCommand.setVerify(true);
		lineDetect = new LightSensor(SENSOR_PORT);

		System.out.println("Robot control started.");
			// lineDetect.activate();
		// lineDetect.passivate();

		while( true ) {
			// ColourSensor lineDetect = new ColorSensor(SENSOR_PORT);
			// statements to control the robot
			//
			//Motor.B.backward();
			//Motor.B.setSpeed(900);
			if( blackDetected() ) {
				playTone(INTERVAL);
				System.out.println("Black detected.");
			}
			Thread.sleep(INTERVAL);
		}
		//NXTCommand.close();

	}
}