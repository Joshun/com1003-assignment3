public class Delayer {
  public int duration = 0;

  public Delayer() { }

  public Delayer(int duration) {
    this.duration = duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  // useful for chaining
  public void waitFor(int duration) throws InterruptedException {
    Thread.sleep(duration);
  }

  public void apply() throws InterruptedException {
    Thread.sleep(duration);
  }

  public static void main(String[] args) throws InterruptedException {
    System.out.println("Message should print in 2 seconds");
    Delayer d = new Delayer();
    d.waitFor(2000);
    System.out.println("Hello World!");
  }
}