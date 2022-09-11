package main.mykyta;

public class MainRunner {
    public static void main(String[] args) {
        LearnLambdaClass learnLambdaClass = new LearnLambdaClass() {
            @Override
            public void doSomething() {
                System.out.println("from anonymouse child");;
            }
        };
        learnLambdaClass.doSomething();

        int k = 10;

        MyInterface myInterface = (mm, bb) -> {
            System.out.println("from anon class" + mm);
            System.out.println("from anon class2" + bb);
        };
        if (k > 7) {
            myInterface.method2("dddd", 4);
        } else {
            System.out.println("fdfdfdf");
        }

//        MyInterface myInterface = new MyInterface() {
//
//            @Override
//            public void method1() {
//                System.out.println("from anon class");
//            }
//        };

    }
}
