import java.util.Scanner;
import java.util.regex.Pattern;

import static java.lang.System.out;

/*
Троян М.В.    9.12.2022
Консольный калькулятор

1) Реализовать класс Main с методом public static String calc(String input)

Вопрос по 3му пукнту
3) Калькулятор должен принимать на вход числа от 1 до 10 включительно , не более.
я так понял что поступает одна цифра от 1 до 10 а не в длину до 10 символов.

Реализация
1) Прогоним принятую строку через регулярное вырожение (В нескольких вариантах) с выдачей ошибок
2) Выделим флаг - Арабские или римские
3) в цикле пробежися по цифрам текста (Преоброзуя их из текстовых в int)
4) С римскими цифрами немного сложнее - нету жостко фиксированного написания
есть только правила что левая сторона отнимается от правой или добавляется к левой
Согласно этому, я могу написать = VII -> 7 или IIIX -> 7 (это не должно вызвать ошибку)
Хотя онлайн калькулятор выдает такие данные  IIIX => 11 (разбивая ее на II и IX) 
5) Нужно написать приватный метод конвертации цифры результата в Римские цифры для вывода результата
 */
public class Main {
    public static void main(String[] args) throws ErrorException {
        //примем строку от пользователя
       Scanner userScan = new Scanner(System.in);
       System.out.println("Введите данные.");;
       String userStr = userScan.nextLine();
       String result = calc(userStr);
       System.out.println(result);
    }

    public static String calc(String input) throws ErrorException {
        int mnojitel=0,number1=0,number2=0,opredelitel=0;
        boolean arabika = false;
        OperationE operE = OperationE.non;

        //input = input.trim();
        input = input.replaceAll("\\s+","");

        if(Pattern.matches("^\\d{1,2}[-+*/]\\d{1,2}",input))
        {
            arabika = true;
        }else if(Pattern.matches("^[ivxIVX]{1,4}[-+*/][ivxIVX]{1,4}",input)){
            arabika = false;
        }else {
            if(Pattern.matches("^\\D{1,4}[-+*/]\\D{1,4}",input)) {
                    throw new ErrorException("строка не является математической операцией");
            }else if(Pattern.matches("^[ivxIVX]{1,4}[-+*/]\\d{1,}",input) || Pattern.matches("^\\d{1,2}[-+*/][ivxIVX]{1,4}",input))
            {
                    throw new ErrorException("используются одновременно разные системы счисления");
            }else{
                    throw new ErrorException("формат математической операции не удовлетворяет заданию - два операнда и один оператор(+,-,/,*)");
            }

        }
    if (arabika) {

        for (int i = 0; i < input.length(); i++) {
            if (input.codePointAt(i) >= "0".codePointAt(0) && input.codePointAt(i) <= "9".codePointAt(0) && opredelitel == 0) {
                if (mnojitel == 0) {
                    number1 = input.codePointAt(i) - "0".codePointAt(0);
                    mnojitel++;
                } else {
                    number1 = (number1 * 10) + (input.codePointAt(i) - "0".codePointAt(0));
                }
            } else if (opredelitel == 0) {
                opredelitel++;
                mnojitel = 0;
                i--; //вот это поворот 0_0
            } else if (opredelitel == 1) {
                switch (input.charAt(i)) {
                    case '-':
                        operE = OperationE.minus;
                        opredelitel++;
                        break;
                    case '+':
                        operE = OperationE.plus;
                        opredelitel++;
                        break;
                    case '*':
                        operE = OperationE.multiply;
                        opredelitel++;
                        break;
                    case '/':
                        operE = OperationE.divide;
                        opredelitel++;
                        break;
                }

            } else if (input.codePointAt(i) >= "0".codePointAt(0) && input.codePointAt(i) <= "9".codePointAt(0) && opredelitel == 2) {
                if (mnojitel == 0) {
                    number2 = input.codePointAt(i) - "0".codePointAt(0);
                    mnojitel++;
                } else {
                    number2 = (number2 * 10) + (input.codePointAt(i) - "0".codePointAt(0));
                }
            }
        }// End for()
    }else {//end Arabika
        input = input.toUpperCase();
        int num;
        for (int i = 0; i < input.length(); i++)
        {
            num=0;
            if(input.charAt(i)=='I')
            {
                for (int q=0;q<3;q++){
                    if(i+q < input.length())
                    {
                        if(input.charAt(i+q)=='I')
                        {
                            num++;
                        }else{break;}
                    }
                }
                if(num !=0){i=i+(num-1);}

            }else if(input.charAt(i)=='V')
            {
                num=5;
            }else if(input.charAt(i)=='X')
            {
                num=10;
            }else if(input.charAt(i)=='-' || input.charAt(i)=='+' || input.charAt(i)=='*' || input.charAt(i)=='/')
            {
                opredelitel++;
                switch (input.charAt(i)) {
                    case '-':
                        operE = OperationE.minus;
                        break;
                    case '+':
                        operE = OperationE.plus;
                        break;
                    case '*':
                        operE = OperationE.multiply;
                        break;
                    case '/':
                        operE = OperationE.divide;
                        break;
                }

            }

            if(opredelitel==0)
            {
                if(number1 == 0){number1 = num;}else{number1 = number1 < num ?  num - number1 : number1 + num ;}
            } else {
                if(number2 == 0){number2 = num;}else{number2 = number2 < num ?  num - number2 : number2 + num ;}
            }


        }//End for
    }
        if(number1 > 10 || number2 > 10){
            throw new ErrorException("введенное значение превышает лимит");
        }

        int result = 0;
        switch (operE){
            case non :
                out.println("throw Exception //т.к. не задана арифметическая операция");
                System.exit(0);
                break;
            case minus :
                result = number1 - number2;
                break;
            case plus :
                result = number1 + number2;
                break;
            case multiply :
                result = number1==0 || number2==0 ? 0 : number1 * number2;
                break;
            case divide:
                result = ((number1 == 0) || (number2 == 0)) ? 0 : Math.round (number1 / number2);
                break;
        }

        if(!arabika && result < 0)
        {
            System.out.println("throw Exception //т.к. в римской системе нет отрицательных чисел");
            System.exit(0);
        } else if (!arabika) {
           return codeRim(result);
        }
        return String.valueOf(result);
    }

    private static String codeRim (int num)
    {
        String result = "";
        while (num!=0) {
            if (num - 100 >= 0) {result += "C";num -= 100;}
            if (num - 90 >= 0) {result += "XC";num -= 90;}
            if (num - 50 >= 0) {result += "L";num -= 50;}
            if (num - 40 >= 0) {result += "XL";num -= 40;}
            if (num - 10 >= 0) {result += "X";num -= 10;}
            if (num - 9 == 0) {result += "IX";num -= 9;}
            if (num - 8 == 0) {result += "VIII";num -= 8;}
            if (num - 7 == 0) {result += "VII";num -= 7;}
            if (num - 6 == 0) {result += "VI";num -= 6;}
            if (num - 5 == 0) {result += "V";num -= 5;}
            if (num - 4 == 0) {result += "IV";num -= 4;}
            if (num - 3 == 0) {result += "III";num -= 3;}
            if (num - 2 == 0) {result += "II";num -= 2;}
            if (num - 1 == 0) {result += "I";num -= 1;}
        }
        if(result=="") {
            try {
                throw new ErrorException("отсутствует ноль в римских числах");
            } catch (ErrorException e) {
                out.println("Пасхальное яичко !!!");
            }
        }
        return result;
    }

}