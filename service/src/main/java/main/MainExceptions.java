package main;

public class MainExceptions {
    public static void main(String[] args) {
        int a = 10;
        int b = 0;
        try {
            int[] myNumbers = {1, 2, 3};
//            System.out.println(myNumbers[10]); // error!
//            int c = a / b;
            throw new ArithmeticException();
        } catch (ArithmeticException ex) {
            System.out.println(" by by by zerooooooo");
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println(" out of bounds " + ex);
        } finally {
            System.out.println("The 'try catch' is finished.");
        }
        System.out.println("finish");
    }
}
