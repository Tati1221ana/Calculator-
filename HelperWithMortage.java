package Calculator;

import java.io.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;

public class HelperWithMortage {
    static final String SAVE_PARAMETERS = "save_parameters.txt";
    static final String SAVE_MAP = "save_map.txt";
    static final String SAVE_LONG_MAP = "save_long_map.txt";
    static final String SAVE_SET = "save_set.txt";


    public static void saveHashMap(HashMap<Integer, Double> map){
        try {
            FileOutputStream outStream = new FileOutputStream(SAVE_MAP);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
            objectOutputStream.writeObject(map);
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveParameters(double time, double monthProcent, double sum, double monthPay) {
        double[] parame = {time, sum, monthPay, monthProcent};
        try {
            FileOutputStream outStream = new FileOutputStream(SAVE_PARAMETERS);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
            objectOutputStream.writeObject(parame);
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void saveHashMapWIthPay(HashMap<Integer, double[]> map){
        try (FileOutputStream outStream = new FileOutputStream(SAVE_LONG_MAP);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream))
        {
            objectOutputStream.writeObject(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveHashSet(HashSet<Integer> set){
        try (FileOutputStream outStream = new FileOutputStream(SAVE_SET);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream))
        {
            objectOutputStream.writeObject(set);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readHashMap() {
        try (FileInputStream fReader = new FileInputStream(SAVE_MAP);
            ObjectInputStream objectInputStream = new ObjectInputStream(fReader))
        {
            Mortgage.monthsAndPay = (HashMap<Integer, Double>) objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void readHashMapWithPay() {
        try (FileInputStream freader = new FileInputStream(SAVE_LONG_MAP);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader))
        {
            Mortgage.payAndPay = (HashMap<Integer, double[]>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void readParameters(){
        try (FileInputStream fReader = new FileInputStream(SAVE_PARAMETERS);
            ObjectInputStream objectInputStream = new ObjectInputStream(fReader))
        {
            double[] param;
            param = (double[]) objectInputStream.readObject();
            Mortgage.time = (int)param[0];
            Mortgage.sum = param[1];
            Mortgage.monthPay = param[2];
            Mortgage.monthProcent = param[3];

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void readHashSet(){
        try (FileInputStream fReader = new FileInputStream(SAVE_SET);
            ObjectInputStream objectInputStream = new ObjectInputStream(fReader)){
            Mortgage.payMonthsChange = (HashSet<Integer>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void printPays(boolean kindOfPay){ // true - сложная система погашений, false -  обычная система погашений
        readHashMapWithPay();
        readHashMap();
        DecimalFormat df = new DecimalFormat("###.##");
        System.out.println("Расчет ваших платежей: ");
        for (int i = 1; i <= Mortgage.monthsAndPay.size(); i ++){
            double[] monthAndEnoughPay = Mortgage.payAndPay.get(i);
            if (kindOfPay) {
                if( i == Mortgage.monthsAndPay.size()){
                    System.out.println(i + " месяц:  " + df.format(Mortgage.monthsAndPay.get(i))
                            + " \t обязательный ежемясячный платеж = " + df.format(monthAndEnoughPay[0])
                            + ", \t досрочное погашение = " + df.format(Mortgage.monthsAndPay.get(i) - monthAndEnoughPay[0]) + ",  \t вносимое досрочное погашение = " + df.format(monthAndEnoughPay[2]));
                    break;
                }
                System.out.println(i + " месяц:  " + df.format(monthAndEnoughPay[0] + monthAndEnoughPay[1])
                        + " \t обязательный ежемясячный платеж = " + df.format(monthAndEnoughPay[0])
                        + ", \t досрочное погашение = " + df.format(monthAndEnoughPay[1]) + ",  \t вносимое досрочное погашение = " + df.format(monthAndEnoughPay[2]));
            } else {
                if (i == Mortgage.monthsAndPay.size()){
                    System.out.println(i + " месяц:  " + df.format(Mortgage.monthsAndPay.get(i)) + ", \t где обязательный ежемясячный платеж = " + df.format(monthAndEnoughPay[0])
                            + ", \t досрочное погашение = " + df.format(Mortgage.monthsAndPay.get(i) - monthAndEnoughPay[0]));
                    break;
                }
                System.out.println(i + " месяц:  " + df.format(monthAndEnoughPay[0] + monthAndEnoughPay[1]) + ", \t где обязательный ежемясячный платеж = " + df.format(monthAndEnoughPay[0])
                        + ", \t досрочное погашение = " + df.format(monthAndEnoughPay[1]));
            }
        }
    }
}
