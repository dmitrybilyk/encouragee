package main.mykyta;

public class ImplementorOfMyInterface implements MyInterface {
    @Override
    public void method2(String s, int a) {
        System.out.println("from implementor of myinteface");
    }
}
