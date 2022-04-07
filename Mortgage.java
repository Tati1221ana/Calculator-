package Calculator;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

import static java.lang.Math.pow;

public class Mortgage {
    static double sum; //сумма, выдаваемая в кредит
    static int time; //срок погашения в месяцах
    static double monthPay; // ежемесячный платеж
    static double monthProcent; //ежемесячная ставка
    static HashMap<Integer, Double> monthsAndPay = new HashMap<>();
    static HashMap<Integer, double[]> payAndPay = new HashMap<>();
    static HashSet<Integer> payMonthsChange = new HashSet<>();

    public static void findTime(double s, double mP, double procent) throws IOException { //расчет срока ипотеки

        sum = s;
        double summa = s;// переменная для уменьшения в цикле
        //double totalSum = 0; // подсчет общей уплаченной банку суммы ( без учета первоначального взноса)
        monthPay = mP;
        monthProcent = procent;

        do {
            time++;
            summa += (summa * monthProcent) - monthPay;
        }
        while (summa > monthPay);

        time++;
        //totalSum = monthPay * (time - 1) + (monthPay - summa);
        //System.out.println("вам потребуется " + time + " месяца (" + time/12 + " лет и " + time%12 + " месяца), чтобы погасить кредит");
        //System.out.println( "переплата банку составит: " + df.format(totalSum-sum));

        for (int i = 1; i < time; i++){
            monthsAndPay.put(i, monthPay);
            payAndPay.put(i, new double[] {monthPay, 0, 0});
        }
        monthsAndPay.put(time, monthPay - summa);
        payAndPay.put(time, new double[] {monthPay - summa, 0, 0});

        HelperWithMortage.saveHashMapWIthPay(payAndPay);
        HelperWithMortage.saveParameters(time, monthProcent, sum, monthPay);
        HelperWithMortage.saveHashMap(monthsAndPay);
    }


    public static void findPay(double creditSum, int creditTime, double procent) throws IOException { // расчет ежемесячного платежа
        sum = creditSum;
        time = creditTime * 12;
        monthProcent = procent;
        DecimalFormat df = new DecimalFormat("###.##");
        double generalProcent = pow(1 + monthProcent, time);
        monthPay = (sum * monthProcent * generalProcent) / (generalProcent - 1);
        System.out.println("Для погашения ипотеки суммой " + sum + " за " + time / 12 + " лет ежемесячный платеж должен составлять " + df.format(monthPay) + " рублей");

        for (int i = 0; i < time; i++){
            payAndPay.put(i+1, new double[] {monthPay, 0, 0});
            monthsAndPay.put(i+1, monthPay);
        }

        HelperWithMortage.saveHashMap(monthsAndPay);
        HelperWithMortage.saveHashMapWIthPay(payAndPay);
        HelperWithMortage.saveParameters(time, monthProcent, sum, monthPay);
    }

    public static void addPay(int month, double pay){
        HelperWithMortage.readParameters();
        HelperWithMortage.readHashMap();
        HelperWithMortage.readHashMapWithPay();
        HelperWithMortage.readHashSet();
        double localSum;
        double newMonthPay;
        double localMonthPay = monthPay;
        double[] pays = payAndPay.get(month);
        if ((month > time) || ( (month > monthsAndPay.size()) && (pays[1] == 0))) {
            System.out.println("К " + month + " месяцу ваша ипотека будет выплачена");
            return;
        }
        pays[1] = pay;
        monthsAndPay.put(month, (pays[1] + pays[2]));
        payAndPay.put(month, pays);
        if (!payMonthsChange.contains(month)) {
            payMonthsChange.add(month);
        } else if (pay == 0) {
            payMonthsChange.remove(month);
        }

        start:
        for (int i = 1; i <= time; i++){// расчет новых ежемесячных платежей
            localSum = sum;
            double[] array = payAndPay.get(i);
            if (array[1] != 0) {
                for (int l = 1; l <= i; l++){
                    localSum += (localSum * monthProcent) - monthsAndPay.get(l); // считаем сумму, которую осталось выплатить на момент ДП

                    if (localSum < 0){
                        monthsAndPay.put(i, localSum + monthsAndPay.get(i));
                        for (int k = time; k > i; k--){ // удаляем лишние элементы
                            monthsAndPay.remove(k);
                        }
                        break start;
                    }
                }
                newMonthPay = (localSum * monthProcent * pow(1 + monthProcent, (time - i))) / (pow(1 + monthProcent, (time - i)) - 1); // новый ежемесячный платеж
                for (int j = i + 1; j <= time; j++){    //заполняем новым ежемесячным платежем
                    if ((monthsAndPay.get(j) - localMonthPay + newMonthPay) < 0){
                        break start;
                    }
                    double[] helper = payAndPay.get(j);
                    helper[0] = newMonthPay;
                    monthsAndPay.put(j, helper[0] + helper[1]);
                    payAndPay.put(j, helper);
                }
                localMonthPay = newMonthPay;
            }
        }
        HelperWithMortage.saveHashMap(monthsAndPay);
        HelperWithMortage.saveHashMapWIthPay(payAndPay);
        HelperWithMortage.saveHashSet(payMonthsChange);
        HelperWithMortage.saveHashSet(payMonthsChange);
    }

    public static void addTime(int month, double pay){
        HelperWithMortage.readParameters();
        HelperWithMortage.readHashMap();
        HelperWithMortage.readHashMapWithPay();
        double[] pays = payAndPay.get(month);
        pays[1] = pay;
        monthsAndPay.put(month, (pays[0] + pays[1]));
        payAndPay.put(month, pays);

        double summa = sum;
        int newTime = 0;
        for (int i = 1; i <= monthsAndPay.size(); i++){
            summa += (summa * monthProcent) - monthsAndPay.get(i);
            newTime++;
            if (summa <= 0) {
                monthsAndPay.put(newTime, summa + monthsAndPay.get(i));
                double[] array = payAndPay.get(newTime);
                if (array[1] == 0){
                    array[0] = monthsAndPay.get(i);
                }
                payAndPay.put(newTime, array);
                break;
            }
        }
        for (int i = time; i > newTime; i--){
            monthsAndPay.remove(i);// в мапе с ДП инфа остается неизменной на случай удаления ДП
        }
        System.out.println(" новое время погашения:  " + newTime/12 + "  лет и " + newTime%12 + " месяцев;  (" + newTime + ") месяцев");
        HelperWithMortage.saveHashMap(monthsAndPay);
        HelperWithMortage.saveHashMapWIthPay(payAndPay);
    }

    public static void deletePay(int month){
        HelperWithMortage.readParameters();
        HelperWithMortage.readHashMap();
        HelperWithMortage.readHashMapWithPay();

        double[] pays = payAndPay.get(month);
        if ( pays[1] == 0){
            System.out.println("В " + month + " месяце не запланирован дополнительный платеж");
            return;
        } else if ((month > time) ||  (month > monthsAndPay.size())) {
            System.out.println("К " + month + " месяцу ваша ипотека будет выплачена");
            return;
        }

        for (int i = monthsAndPay.size(); i <= time; i++) {
               monthsAndPay.put(i, monthPay);
            }
        HelperWithMortage.saveHashMap(monthsAndPay);
        addPay(month, 0);
    }

    public static void deleteTime(int month){
        HelperWithMortage.readParameters();
        HelperWithMortage.readHashMapWithPay();
        HelperWithMortage.readHashMap();
        double[] pays = payAndPay.get(month);

        if (pays[1] == 0) {
            System.out.println("В " + month + " месяце не запланирован дополнительный платеж");
            return;
        } else if ((month > time) || (month > monthsAndPay.size())){
            System.out.println("К " + month + " месяцу ваша ипотека будет выплачена");
            return;
        } else if (monthsAndPay.size() == time) {
            System.out.println("Удаление в данном режиме увеличит срок погашения ипотеки выше заданного, " +
                    "удалите дополнительный платеж в " + month + "месяце в режиме редактирование платежей");
            return;
        }
        for (int i = monthsAndPay.size() - 1; i <= time; i++) {
            double[] helpArray = payAndPay.get(i);
            helpArray[0] = monthPay;
            payAndPay.put(i, helpArray);
            monthsAndPay.put(i, helpArray[0] + helpArray[1]);
        }
        HelperWithMortage.saveHashMap(monthsAndPay);
        HelperWithMortage.saveHashMapWIthPay(payAndPay);
        addTime(month, 0);
    }

    public static void addDifficultPay(int month, double pay){
        HelperWithMortage.readParameters();
        HelperWithMortage.readHashMap();
        HelperWithMortage.readHashMapWithPay();
        double localSum;
        double newMonthPay;
        double localMonthPay = monthPay;
        double sumPays;

        DecimalFormat df = new DecimalFormat("###.##");
        double[] threePays = payAndPay.get(month);
        // в первом элементе массива - обязательный платеж, во втором дополнительный, в третьем вносимый дополнительный

        if (month > monthsAndPay.size()) {
            System.out.println(" к " + month +"  месяцу ипотека будет погашена");
            return;
        }

        threePays[2] = pay;
        threePays[1] = pay;
        sumPays = threePays[0] + threePays[1];
        monthsAndPay.put(month, sumPays);
        payAndPay.put(month, threePays);

        start:
        for (int i = 1; i <= time; i++){
            localSum = sum;
            threePays = payAndPay.get(i);
            threePays[0] = localMonthPay;
            payAndPay.put(i, threePays);

            if (threePays[1] != 0)  { // если сумма ДП не ноль

                for (int l = 1; l <= i; l++){ // расчет суммы, которую осталось выплатить на момент ДП
                    localSum += (localSum * monthProcent) - monthsAndPay.get(l);

                    if ((localSum < 0) && (monthsAndPay.get(i) > localMonthPay)){ // если после ДП ипотека закрывается раньше срока
                        monthsAndPay.put(i, localSum + monthsAndPay.get(i));
                        System.out.println("Из дополнительного платежа в " + i + " месяце вам необходимо только " + df.format(monthsAndPay.get(i) - localMonthPay));
                        for (int k = time; k > i; k--){ // удаляем лишние элементы
                            monthsAndPay.remove(k);
                        }
                        break start;
                    } else if (localSum <= 0) {
                        monthsAndPay.put(i, localSum + monthsAndPay.get(i));
                        break start;
                    }
                }

                newMonthPay = (localSum * monthProcent * pow(1 + monthProcent, (time - i))) / (pow(1 + monthProcent, (time - i)) - 1);  // новый ежемессячный платеж

                for (int j = i + 1; j <= monthsAndPay.size(); j++){
                    double[] helper = payAndPay.get(j);
                    helper[1] = monthPay - newMonthPay + helper[2];
                    helper[0] = newMonthPay;
                    monthsAndPay.put(j, helper[0] + helper[1]);
                    payAndPay.put(j, helper);
                }
                localMonthPay = newMonthPay;
            }
        }
        HelperWithMortage.saveHashMap(monthsAndPay);
        HelperWithMortage.saveHashMapWIthPay(payAndPay);
    }

    public static void deleteDifficultPay(int month){
        HelperWithMortage.readParameters();
        HelperWithMortage.readHashMap();
        HelperWithMortage.readHashMapWithPay();

        double[] threePays = payAndPay.get(month);
        // в первом элементе массива - обязательный платеж, во втором дополнительный, в третьем добавочно - дополнительный

        if (threePays[2] == 0){
            System.out.println(" В этом месяце не запланирован дополнительный платеж");
            return;
        } else if (month > monthsAndPay.size()) {
            System.out.println(" к " + month +"  месяцу ипотека будет погашена");
            return;
        }

        for (int i = 1; i <= time; i++){
            double[] pays = payAndPay.get(i);
            pays[0] = monthPay;
            pays[1] = pays[2];
            if ( i == month){
                pays[1] = 0;
            }
            monthsAndPay.put(i, pays[0] + pays[1]);
            payAndPay.put(i, pays);
        }
        System.out.println(monthsAndPay.size());
        HelperWithMortage.saveHashMap(monthsAndPay);
        HelperWithMortage.saveHashMapWIthPay(payAndPay);
        addDifficultPay(month, 0);
    }
}