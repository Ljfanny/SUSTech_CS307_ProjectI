package proj.one;

import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try {
            DataManipulation dm = new DataFactory().createDataManipulation(args[0]);
            dm.openDatasource();
            //test for database;
//            dm.addOrder("2021-02-16;;260;11910621;TvBaseR1;CSE0004998");
//            System.out.println(dm.selectContractOfEnterById(1));
//            System.out.println(dm.selectOrderById(1));
//            System.out.println(dm.selectIdsBySameSalesman(1));
//            System.out.println(dm.selectOrderByTimeInterval("2010-04-11","2010-04-21"));
//            dm.deleteById(50006);
//            dm.deleteById(50007);
//            dm.updateLogById(50006,"2022-01-02");
            //test for files;
//            dm.addOrder("2021-02-16,,260,119,542,CSE0004998");
//            System.out.println(dm.selectContractOfEnterById(1));
//            System.out.println(dm.selectOrderById(1));
//            System.out.println(dm.selectIdsBySameSalesman(1));
//            System.out.println(dm.selectOrderByTimeInterval("2010-04-11","2010-04-21"));
//            dm.deleteById(50001);
            dm.updateLogById(50001, "2222-22-22");
            dm.closeDatasource();
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}

