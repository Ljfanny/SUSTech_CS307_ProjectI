package proj.one;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ClientMultiThread extends Thread{
    private static final ArrayList<String> items = new ArrayList<>();
    private static int count = 0;
    private final int index;
    ClientMultiThread(){
        count++;
        this.index = count;
    }
    int getIndex(){
        return this.index;
    }
    static {
        String item;
        try (BufferedReader bufferedReader = new BufferedReader(
            new FileReader("extraData.txt"))) {
            while ((item = bufferedReader.readLine()) != null) {
                items.add(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    //线程体, 启动线程之后会执行run函数里面的代码;
    public void run(){
        DataManipulation dm = new DataFactory().createDataManipulation("file");
        long start = System.currentTimeMillis();
        dm.openDatasource();
        int size = items.size();
        for (String item : items) {
            dm.addOrder(item);
        }
        System.out.println(this.index + " " + dm.selectOrderById(50002));
        System.out.println(this.index + " " + dm.selectIdsBySameSalesman(3));
        dm.deleteById(50012);
        System.out.println(this.index + " " + dm.selectOrderById(50010));
        dm.closeDatasource();
        long end = System.currentTimeMillis();
        System.out.println(this.index + " spend time: "
            + (end - start) + "s");
    }
    //相对同时启动两个线程
    public static void main(String[] args) {
        ClientMultiThread thread1 = new ClientMultiThread();
        ClientMultiThread thread2 = new ClientMultiThread();
        thread1.start();
        thread2.start();
    }
}


