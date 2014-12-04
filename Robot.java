import icommand.nxt.*;
import icommand.nxt.comm.NXTCommand;

public class Robot {
	public static void main(String[] args) throws InterruptedException {
		final int INTERVAL = 100;
		final int LIGHT_THESHOLD = 250;

		NXTCommand.open();
		NXTCommand.setVerify(true);

		LightSensor lineDetect = new LightSensor(SensorPort.S2	);
		lineDetect.activate();
		// lineDetect.passivate();

		int lightValue = 24;
		while( true ) {


			// ColourSensor lineDetect = new ColorSensor(SENSOR_PORT);
			// statements to control the robot
			//
			//Motor.B.backward();
			//Motor.B.setSpeed(900);
			lightValue = lineDetect.getLightValue();
			System.out.println(lightValue);
			if( lightValue < LIGHT_THESHOLD ) {
				System.out.println("BLACK DETECTED!!!");
				Sound.playTone(500, INTERVAL);
			}
			Thread.sleep(INTERVAL);
		}
		//NXTCommand.close();

	}
}