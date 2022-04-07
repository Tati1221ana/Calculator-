package Calculator;

import java.text.DecimalFormat;
import java.util.Scanner;

public class Choices {

    public static void main(String args[]) throws Exception {
        beginningChoice();
    }


    public static void beginningChoice() throws Exception {
        System.out.println("Здравствуйте! Вы хотите продолжить ваш расчет или создать новый?\n Продолжить: нажмите 1 \n Создать новый: нажмите 2 ");
        Scanner scan = new Scanner(System.in);
        String choice = scan.nextLine();

        if ("1".equals(choice)) {
            changingPays();
        } else if ("2".equals(choice)) {
            choiceTimeOrPay();
        } else {
            System.out.println("повторите ввод");
            beginningChoice();
        }
        HelperWithMortage.printPays(false);
    }

    public static void choiceTimeOrPay() throws Exception {
        System.out.println("Выберите: \n  рассчитать срок ипотеки: нажмите 1 \n  рассчитать минимальный платеж: нажмите 2");
        Scanner scan = new Scanner(System.in);
        String ch = scan.nextLine();
        DecimalFormat df = new DecimalFormat("###.##");
        String result = ch.toLowerCase();

        switch (result) {

            case "1": {
                System.out.println("Пожалуйста, введиите сумму ипотеки");
                int summa = scan.nextInt();
                System.out.println("Пожалуйста, введите сумму первоначального взноса");
                double firstPay = scan.nextDouble();
                System.out.println(" Пожалуйста, введите желаемый размер ежемесячного платежа");
                double monthPay = scan.nextDouble();
                System.out.println("Введите процентную ставку (в процентах)");
                double procent = scan.nextDouble();

                double sum = summa - firstPay;
                double monthProcent = procent * 0.01 / 12;

                Mortgage.findTime(sum, monthPay, monthProcent);
                HelperWithMortage.printPays(false);
                System.out.println("Вам потребуется " + Mortgage.time + " месяца (" + Mortgage.time/12 + " лет и " + Mortgage.time%12 + " месяца), чтобы выплатить ипотеку");
                break;
            }

            case "2": {
                System.out.println("введите сумму ипотеки");
                Double sum = scan.nextDouble();
                System.out.println("введите  сумму первоначального платежа");
                double firstPay = scan.nextDouble();
                System.out.println("введите срок ипотеки в годах");
                int creditTime = scan.nextInt();
                System.out.println("введите процентную ставку в процентах");
                double procentYear = scan.nextDouble();

                double creditSum = sum - firstPay;
                double procent = procentYear * 0.01 / 12;

                Mortgage.findPay(creditSum, creditTime, procent);
                HelperWithMortage.printPays(false);
                System.out.println("Ежемесячный платеж : " + df.format(Mortgage.monthPay));
                break;
            }

            default: {
                System.out.println("вы ввели другие символы, повторите ввод");
                choiceTimeOrPay();
            }
        }
        System.out.println("Вы хотите рассчитать досрочное погашение? \n да: нажмите 1 \n нет: нажмите 2");
        int choice = scan.nextInt();
        if (choice == 1){
            changingPays();
        }
    }

    public static void changingPays() {
        Scanner scan = new Scanner(System.in);
        String choice;
        HelperWithMortage.printPays(false);

        first:
        while (true) {
            System.out.println("Работа с досрочным погашением. Выберите:\n  изменение срока: нажмите 1\n  изменение платежа: нажмите 2 " +
                    "\n изменение платежа по схеме 'вноси ДП, но плати так же': нажмите 3");
            choice = scan.nextLine();
            if ("1".equals(choice)) {  // изменение срока
                second:

                while (true) {
                    System.out.println(" Для добавления или изменения платежа нажмите 1 \n " +
                            "Для удаления платежа нажмите 2 \n " +
                            "Для возможности выбора изменения платежа нажмите 3 \n " +
                            "Для завершения работы нажмите 4");
                    switch (scan.nextInt()) {
                        case 1: {
                            System.out.println("Введите номер месяца");
                            int month = scan.nextInt();
                            System.out.println("Введите сумму ");
                            double pay = scan.nextDouble();
                            Mortgage.addTime(month, pay);
                            break;
                        }
                        case 2: {
                            System.out.println("Введите номер месяца, в котором вы хотите удалить платеж");
                            int month = scan.nextInt();
                            Mortgage.deleteTime(month);
                            break;
                        }
                        case 3:
                            break second;
                        case 4:
                            break first;
                    }
                    HelperWithMortage.printPays(false);
                }
            } else if ("2".equals(choice)) { // изменение платежа
                second:
                while (true) {

                    System.out.println(" Для добавления или изменения платежа нажмите 1 \n " +
                            "Для удаления платежа нажмите 2 \n " +
                            "Для возможности выбора изменения срока нажмите 3 \n " +
                            "Для завершения работы нажмите 4");
                    switch (scan.nextInt()) {
                        case 1: {
                            System.out.println("Введите номер месяца");
                            int month = scan.nextInt();
                            System.out.println("Введите сумму ");
                            double pay = scan.nextDouble();
                            Mortgage.addPay(month, pay);
                            break;
                        }
                        case 2: {
                            System.out.println("Введите номер месяца, в котором вы хотите удалить платеж");
                            int month = scan.nextInt();
                            Mortgage.deletePay(month);
                            break;

                        }
                        case 3:
                            break second;
                        case 4:
                            break first;
                    }
                    HelperWithMortage.printPays(false);
                }
            } else if ("3".equals(choice)) { // сложная схема
                start:
                while (true) {
                    System.out.println(" Для добавления или изменения платежа нажмите 1 \n " +
                            "Для удаления платежа нажмите 2 \n " +
                            "Для завершения работы нажмите 3");
                    switch (scan.nextInt()) {
                        case 1: {
                            System.out.println("Введите номер месяца");
                            int month = scan.nextInt();
                            System.out.println("Введите сумму ");
                            double pay = scan.nextDouble();
                            Mortgage.addDifficultPay(month, pay);
                            break;
                        }
                        case 2: {
                            System.out.println("Введите номер месяца, в котором вы хотите удалить платеж");
                            int month = scan.nextInt();
                            Mortgage.deleteDifficultPay(month);
                            break;
                        }
                        case 3:
                            break first;
                    }
                    HelperWithMortage.printPays(true);
                }
            }

        }
        System.out.println("Ваши изменения были сохранены. \n " +
                "До новых встреч!");
    }
}
