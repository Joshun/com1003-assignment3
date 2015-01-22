/**
 * Object handle blocking the program from executing for a given time
 * 
 * @author Jack Deadman
 * @author Joshua O'Leary
 */

public class Delayer {
	private int duration;

	/**
	 * Constructs a new Delayer with a given duration
	 * @param  duration int Duration of delay (in milliseconds)
	 */
	public Delayer(int duration) {
		this.duration = duration;
	}

	/**
	 * Constructs a new delay with a default duration
	 */
	public Delayer() {
		this(0);
	}

	/**
	 * Changes duration of delay
	 * @param duration int Duration of delay (in milliseconds)
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * Returns duration Delayer has been set to
	 * @return int Delay
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Used for a one-off delay
	 * @param duration int Duration of delay (in milliseconds)
	 */
	public void waitFor(int duration) throws InterruptedException {
		Thread.sleep(duration);
	}

	/**
	 * Used to apply the duration that has been previously set
	 */
	public void apply() throws InterruptedException {
		Thread.sleep(duration);
	}

	public static void main(String[] args) throws InterruptedException {
		// Test harness
		System.out.println("Message should print in 2 seconds");

		Delayer d = new Delayer();
		d.waitFor(2000);
		System.out.println("Hello World!");

		System.out.println("Message should print in 3 seconds");
		Delayer d2 = new Delayer(3000);
		d2.apply();
		System.out.println("Hello World!");
	}
}