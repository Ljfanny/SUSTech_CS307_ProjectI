package proj.one;

import java.sql.Date;

public interface DataManipulation {
    public void openDatasource();

    public void closeDatasource();

    //insert
    public int addOrder(String str);

    //select
    public String selectOrderById(int id);

    public String selectOrderByTimeInterval(String est, String log);

    public String selectContractOfEnterById(int id);

    public String selectIdsBySameSalesman(int id);

    //delete
    public int deleteById(int id);

    //update
    public int updateLogById(int id, Date date);

}
