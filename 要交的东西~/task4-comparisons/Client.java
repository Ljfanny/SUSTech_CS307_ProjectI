package proj.one;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try {
            DataManipulation dm = new DataFactory().createDataManipulation(args[0]);
            long start = System.currentTimeMillis();
            dm.openDatasource();
            //insert
//            String item;
//            try (BufferedReader bufferedReader = new BufferedReader(
//                new FileReader("extraData.txt"))) {
//                while ((item = bufferedReader.readLine()) != null) {
//                    dm.addOrder(item);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            int count = 1;
//            for (int i = 50001; i <= 60000; i++) {
//                dm.selectContractOfEnterById(i);
//                System.out.println(count + dm.selectContractOfEnterById(i));
//                System.out.println(dm.selectOrderById(i));
//                System.out.println(i + " " + dm.selectIdsBySameSalesman(i));
//                dm.deleteById(i);
//                System.out.println("delete id: " + i);
//                dm.updateLogById(i, "2022-02-16");
//                System.out.println("update id: " + i);
//                count++;
//            }
            dm.closeDatasource();
            long end = System.currentTimeMillis();
            System.out.println(10000 + " records successfully loaded");
            System.out.println("Loading speed : "
                + (10000 * 1000) / (end - start)
                + " records/s");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}

