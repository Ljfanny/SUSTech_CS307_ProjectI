package proj.one;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Wrapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class DatabaseManipulation implements DataManipulation {
    private Connection con = null;
    private ResultSet resultSet;

    private String host = "localhost";
    private String dbname = "proj1";
    private String user = "postgres";
    private String pwd = "216122";
    private String port = "5432";


    @Override
    public void openDatasource() {
        try {
            Class.forName("org.postgresql.Driver");

        } catch (Exception e) {
            System.err.println("Cannot find the PostgreSQL driver. Check CLASSPATH.");
            System.exit(1);
        }

        try {
            String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbname;
            con = DriverManager.getConnection(url, user, pwd);

        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void closeDatasource() {
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int addOrder(String str) {
        int res = 0;
        String sql =
            "insert into orders(estimated_delivery_date, Lodgement_date, quantity, salesman_id, model_id, contract_number)" +
                " values(?,?,?,(select salesman_id from salesmen where salesman_number = ?)," +
                "(select model_id from models where product_model = ?),?)";
        String[] orderInfo = str.split(";");
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date d1 = null;
            java.util.Date d2 = null;
            d1 = format.parse(orderInfo[0]);
            if (!Objects.equals(orderInfo[1], "")) {
                d2 = format.parse(orderInfo[1]);
            }
            assert d1 != null;
            Date dateEst = new Date(d1.getTime());
            Date dateLog = null;
            if (d2 != null) {
                dateLog = new Date(d2.getTime());
            }
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setDate(1, dateEst);
            preparedStatement.setDate(2, dateLog);
            preparedStatement.setInt(3, Integer.parseInt(orderInfo[2]));
            preparedStatement.setString(4, orderInfo[3]);
            preparedStatement.setString(5, orderInfo[4]);
            preparedStatement.setString(6, orderInfo[5]);
            System.out.println(preparedStatement.toString());
            res = preparedStatement.executeUpdate();
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public String selectOrderById(int id) {
        StringBuilder stringBuilder = new StringBuilder();
        String sql = "select * from orders where order_id = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                stringBuilder.append("order_id: ").append(resultSet.getString("order_id"))
                    .append("\n");
                stringBuilder.append("estimated_delivery_date: ")
                    .append(resultSet.getString("estimated_delivery_date")).append("\n");
                stringBuilder.append("lodgement_date: ")
                    .append(resultSet.getString("lodgement_date")).append("\n");
                stringBuilder.append("quantity: ").append(resultSet.getString("quantity"))
                    .append("\n");
                stringBuilder.append("salesman: ").append(resultSet.getString("salesman_id"))
                    .append("\n");
                stringBuilder.append("model_id: ").append(resultSet.getString("model_id"))
                    .append("\n");
                stringBuilder.append("contract_number: ")
                    .append(resultSet.getString("contract_number")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    @Override
    public String selectOrderByTimeInterval(String est, String log) {
        StringBuilder stringBuilder = new StringBuilder();
        String sql =
            "select * from orders where estimated_delivery_date = ? and Lodgement_date = ?";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date d1 = null;
            java.util.Date d2 = null;
            d1 = format.parse(est);
            if (!Objects.equals(log, "")) {
                d2 = format.parse(log);
            }
            assert d1 != null;
            Date dateEst = new Date(d1.getTime());
            Date dateLog = null;
            if (d2 != null) {
                dateLog = new Date(d2.getTime());
            }
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setDate(1, dateEst);
            preparedStatement.setDate(2, dateLog);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                stringBuilder.append(resultSet.getString("order_id")).append("\n");
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    @Override
    public String selectContractOfEnterById(int id) {
        String res = "";
        String sql = "select ce.name as nm from orders " +
            "join contracts c on c.contract_number = orders.contract_number " +
            "join client_enterprises ce on ce.client_enterprise_id = c.client_enterprise_id " +
            "where order_id = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                res = resultSet.getString("nm");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    //salesman_id -> id
    public String selectIdsBySameSalesman(int id) {
        String res = "";
        String sql = "select sub.salesman_id, string_agg(cast(order_id as varchar), ',') as ors " +
            "from (" +
            "         select distinct order_id, salesman_id " +
            "         from orders) sub " +
            "where salesman_id = ? " +
            "group by sub.salesman_id";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                res = resultSet.getString("ors");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public int deleteById(int id) {
        int res = 0;
        String sql = "delete from orders where order_id = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            res = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public int updateLogById(int id, String date) {
        int res = 0;
        String sql = "update orders " +
            "set lodgement_date = ? " +
            "where order_id = ?";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date d = null;
            if (date == null)
                return 0;
            d = format.parse(date);
            assert d != null;
            Date goal = new Date(d.getTime());
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setDate(1, goal);
            preparedStatement.setInt(2, id);
            res = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }
}
