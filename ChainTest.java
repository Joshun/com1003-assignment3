public class ChainTest {
  public static Delay test() {
    System.out.println("Hello...");
    return new Delay();
  }
  public static void main(String[] args) throws InterruptedException {
    test().wait(2000);
    test();
    test();
  }
}