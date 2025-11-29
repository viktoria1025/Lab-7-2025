package functions;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TabulatedFunctions {

    private TabulatedFunctions() {}

    // Табулирование функции
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы выходят за область определения");
        }
        if (pointsCount < 2) throw new IllegalArgumentException("Нужно минимум 2 точки");
        if (leftX >= rightX - 1e-10) throw new IllegalArgumentException("Левая граница должна быть меньше правой");

        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }

        return createTabulatedFunction(leftX, rightX, values);
    }

    // Запись в байтовый поток
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);

        dataOut.writeInt(function.getPointsCount());

        for (int i = 0; i < function.getPointsCount(); i++) {
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }
    }

    // Чтение из байтового потока
    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);

        int pointsCount = dataIn.readInt();

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        return createTabulatedFunction(points);
    }
    // Чтение из байтового потока (перегруженный)
    public static TabulatedFunction inputTabulatedFunction(Class<? extends TabulatedFunction> functionClass, InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);

        int pointsCount = dataIn.readInt();

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        return createTabulatedFunction(functionClass, points);
    }

    // Запись в текстовый поток
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        PrintWriter write = new PrintWriter(out);

        write.print(function.getPointsCount() + " ");

        for (int i = 0; i < function.getPointsCount(); i++) {
            write.print(function.getPointX(i) + " " + function.getPointY(i));
            if (i < function.getPointsCount() - 1) write.print(" ");
        }
    }

    // Чтение из текстового потока
    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tok = new StreamTokenizer(in);
        // Читаем количество точек
        tok.nextToken();
        int pointsCount = (int) tok.nval;
        // Читаем координаты точек
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            tok.nextToken();
            double x = tok.nval;
            tok.nextToken();
            double y = tok.nval;
            points[i] = new FunctionPoint(x, y);
        }

        return createTabulatedFunction(points);
    }
    // Чтение из текстового потока (перегруженный)
    public static TabulatedFunction readTabulatedFunction(Class<? extends TabulatedFunction> functionClass, Reader in) throws IOException {
        StreamTokenizer tok = new StreamTokenizer(in);
        // Читаем количество точек
        tok.nextToken();
        int pointsCount = (int) tok.nval;
        // Читаем координаты точек
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            tok.nextToken();
            double x = tok.nval;
            tok.nextToken();
            double y = tok.nval;
            points[i] = new FunctionPoint(x, y);
        }

        return createTabulatedFunction(functionClass, points);
    }
    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory(); //Приватное статическое поле фабрики

    // Метод для замены фабрики
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory factory) {
        TabulatedFunctions.factory = factory;
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);
    }
    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> functionClass, double leftX, double rightX, int pointsCount) {
        try {
            Constructor<?> constructor = functionClass.getConstructor(double.class, double.class, int.class);
            return (TabulatedFunction) constructor.newInstance(leftX, rightX, pointsCount);

        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Не найден конструктор", e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Ошибка создания", e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("Ошибка в конструкторе", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Нет доступа к конструктору", e);
        }
    }
    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> functionClass, double leftX, double rightX, double[] values) {
        try {
            Constructor<?> constructor = functionClass.getConstructor(double.class, double.class, double[].class);
            return (TabulatedFunction) constructor.newInstance(leftX, rightX, values);

        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Не найден конструктор", e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Ошибка создания", e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("Ошибка в конструкторе", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Нет доступа к конструктору", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> functionClass, FunctionPoint[] points) {
        try {
            Constructor<?> constructor = functionClass.getConstructor(FunctionPoint[].class);
            return (TabulatedFunction) constructor.newInstance((Object) points);

        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Не найден конструктор", e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Ошибка создания", e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("Ошибка в конструкторе", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Нет доступа к конструктору", e);
        }
    }
    // с рефлексикй  tabulate
    public static TabulatedFunction tabulate(Class<? extends TabulatedFunction> functionClass, Function function, double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы выходят за область определения");
        }
        if (pointsCount < 2) throw new IllegalArgumentException("Нужно минимум 2 точки");
        if (leftX >= rightX - 1e-10) throw new IllegalArgumentException("Левая граница должна быть меньше правой");

        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }
        // Используем рефлексию
        return createTabulatedFunction(functionClass, leftX, rightX, values);
    }
}
