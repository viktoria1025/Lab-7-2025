import functions.*;
import functions.basic.*;
import threads.*;
import java.io.*;
import java.util.Random;
import java.util.concurrent.Semaphore;


public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Тестирование итераторов");

        double values[] ={2,4,7,14,22};
        System.out.println("ArrayTabulatedFunction:");
        TabulatedFunction arrF = new ArrayTabulatedFunction(0, 15, values);
        for (FunctionPoint p : arrF) {
            System.out.println(p);
        }
        System.out.println("LinkedListTabulatedFunction:");
        TabulatedFunction listF = new LinkedListTabulatedFunction(0,15,values);
        for (FunctionPoint p : listF) {
            System.out.println(p);
        }
        System.out.println("фабрики");

        Function f0 = new Cos();
        TabulatedFunction tf;

        tf = TabulatedFunctions.tabulate(f0, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f0, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f0, 0, Math.PI, 11);
        System.out.println(tf.getClass());

        System.out.println("рефлексия");
        TabulatedFunction f;

        f = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10});
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.createTabulatedFunction(LinkedListTabulatedFunction.class,
                new FunctionPoint[] {
                        new FunctionPoint(0, 0),
                        new FunctionPoint(10, 10)
                }
        );
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.tabulate(LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        System.out.println(f.getClass());
        System.out.println(f);
    }
}