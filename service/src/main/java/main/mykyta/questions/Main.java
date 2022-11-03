package main.mykyta.questions;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        long a = 10;
        long b = 3;
        double result =a / b;
        System.out.println(result);

        System.out.println("Enter something");
        byte[] sss = System.in.readAllBytes();
        System.out.println(" you entered - " + sss);
    }
}
