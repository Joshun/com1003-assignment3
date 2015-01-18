public class Robot {
	private static RobotState previousState;
	private static RobotState currentState = new RobotState();
	private static final int INTERVAL = 50;
	
	public static void updateState() {
		
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		while(true) {
			
			Thread.sleep(INTERVAL);
		}
		
	}
}
