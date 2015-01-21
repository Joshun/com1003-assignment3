public class Delay {
  public void wait(int duration) throws InterruptedException {
    Thread.sleep(duration);
  }

  public static void main(String[] args) throws InterruptedException {
    System.out.println("Message should print in 2 seconds");
    Delay d = new Delay();
    d.wait(2000);
    System.out.println("Hello World!");
  }
}