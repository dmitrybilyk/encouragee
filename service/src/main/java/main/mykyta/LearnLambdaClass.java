package main.mykyta;

public class LearnLambdaClass {
    private String someVar;
    class InnerClass {
        public void doInInner() {
            System.out.println("dfdf" + someVar);
        }
    }
    static class InnerClass2 {
        public void doInInner() {
            System.out.println("dfdf");
        }
    }
    private class InnerClass3 {
        public void doInInner() {
            System.out.println("dfdf");
        }
    }
    public void doSomething() {
        System.out.println("I'm doing something");
    }
}
