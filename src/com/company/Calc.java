package com.company;

import java.io.IOException;
import java.util.Scanner;

import static java.lang.Double.parseDouble;

public class Calc {

    public static void main(String[] args) {
        System.out.print("\nWelcome to the calculator!\nEnter two arguments and the operator (+, -, *, /) separated by a space. For example: 2.2 * 3\n"); //Инструкция пользователя
        Scanner inputValue = new Scanner(System.in); //Ввод данных пользователем
        String fullExpressionUnity = inputValue.nextLine();
        inputValue.close();
        String[] fullExpressionSplit = fullExpressionUnity.split(" "); //Разделение строчки на строчный массив при встрече пробела
        int ex = 0;
        if (fullExpressionSplit.length >= 4) { //Если колличество элементов массива больше 4, то выводится сообщение об ошибке
            System.out.println("Exceeded the number of arguments or invalid entry");
            ex = 1;
        }
        char operation = fullExpressionSplit[1].charAt(0);
        if (operation != '+' && operation != '-' && operation != '*' && operation != '/') //Если введённый оператор не соответствует четырём требуемым, то выводится сообщение об ошибке
        {
            System.out.println("Invalid expression operator entered");
            ex = 1;
        }
        double x = parseDouble(fullExpressionSplit[0].replace(",", ".")), y = parseDouble(fullExpressionSplit[2].replace(",", "."));   //Если пользователь введёт по ошибке ",", вместо ".",
                                                                                                                                                                        // то запятая заменится и данные будут корректны без перезапуска программы
        if (ex == 1){ //Во время выполнения программы вывелась ошибка, то также переменной ex присвоилась 1 и программа завершила работу.
            System.exit(1);
        }
        if (y != 0 && operation != '/') { //Если не происходит деление на 0, то программа продолжает работу, иначе выводится NaN
            System.out.print("=" + calculation(x, y, operation));
        }
        else {
            System.out.print("= NaN");
        }
    }
    public static double calculation(double x, double y, char operation){ //Выбирается оператор, выражение вычисляется и возвращается результат
        switch (operation){
            case ('+'):
                return x + y;
            case ('-'):
                return x - y;
            case ('*'):
                return x * y;
            case ('/'):
                return x / y;
            default: return 0;
        }
    }
}
