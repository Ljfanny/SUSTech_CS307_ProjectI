package proj.one;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Properties;
import java.sql.*;
import java.net.URL;

public class AwfulLoader {
//    private static URL propertyURL = AwfulLoader.class
//        .getResource("/loader.cnf");

    private static Connection con = null;
    private static boolean verbose = false;

    private static void openDB(String host, String dbname,
                               String user, String pwd) {
        try {
            //
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            System.err.println("Cannot find the Postgres driver. Check CLASSPATH.");
            System.exit(1);
        }
        String url = "jdbc:postgresql://" + host + "/" + dbname;
        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", pwd);
        try {
            con = DriverManager.getConnection(url, props);
            if (verbose) {
                System.out.println("Successfully connected to the database "
                    + dbname + " as " + user);
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private static void closeDB() {
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (Exception e) {
                // Forget about it
            }
        }
    }

    private static void loadData(String estimated_delivery_date, String lodgement_date,
                                 int quantity, String number, String pro_model,
                                 String contract_number)
        throws SQLException {
        Statement stmt;
        if (con != null) {
            stmt = con.createStatement();
            if (lodgement_date.equals("")) {
                stmt.execute(
                    "insert into orders(estimated_delivery_date, Lodgement_date, quantity, salesman_id, model_id, contract_number)"
                        +
                        " values('" + estimated_delivery_date + "'," +
                        null + "," +
                        quantity +
                        "," +
                        "(select salesman_id from salesmen where salesman_number = '" +
                        number.replace("'", "''") + "')" +
                        "," +
                        "(select model_id from models where product_model = '" +
                        pro_model.replace("'", "''") + "')" +
                        ", '" +
                        contract_number.replace("'", "''") + "')");
            } else {
                stmt.execute(
                    "insert into orders(estimated_delivery_date, Lodgement_date, quantity, salesman_id, model_id, contract_number)"
                        +
                        " values('" + estimated_delivery_date + "','" +
                        lodgement_date + "'," +
                        quantity +
                        "," +
                        "(select salesman_id from salesmen where salesman_number = '" +
                        number.replace("'", "''") + "')" +
                        "," +
                        "(select model_id from models where product_model = '" +
                        pro_model.replace("'", "''") + "')" +
                        ", '" +
                        contract_number.replace("'", "''") + "')");
            }
        }
    }

    public static void main(String[] args) {
        String fileName = null;
        boolean verbose = false;
        long start;
        long end;

        switch (args.length) {
            case 1:
                fileName = args[0];
                break;
            case 2:
                switch (args[0]) {
                    case "-v":
                        verbose = true;
                        break;
                    default:
                        System.err.println("Usage: java [-v] AwfulLoader filename");
                        System.exit(1);
                }
                fileName = args[1];
                break;
            default:
                System.err.println("Usage: java [-v] AwfulLoader filename");
                System.exit(1);
        }

//        if (propertyURL == null) {
//            System.err.println("No configuration file (loader.cnf) found");
//            System.exit(1);
//        }
        Properties defprop = new Properties();
        defprop.put("host", "localhost");
        defprop.put("user", "postgres");
        defprop.put("password", "216122");
        defprop.put("database", "proj1");
        Properties prop = new Properties(defprop);
//        try (BufferedReader conf
//                 = new BufferedReader(new FileReader(propertyURL.getPath()))) {
//            prop.load(conf);
//        } catch (IOException e) {
//            // Ignore
//            System.err.println("No configuration file (loader.cnf) found");
//        }
        try (BufferedReader infile
                 = new BufferedReader(new FileReader(fileName))) {
            String line;
            String[] parts;
            int cnt = 0;
            // Empty target table
            openDB(prop.getProperty("host"), prop.getProperty("database"),
                prop.getProperty("user"), prop.getProperty("password"));
            Statement stmt0;
            if (con != null) {
                stmt0 = con.createStatement();
                stmt0.execute("truncate table orders");
                stmt0.close();
            }
            closeDB();
            //
            start = System.currentTimeMillis();
            while ((line = infile.readLine()) != null) {
                parts = line.split(";");
                if (parts.length > 1) {
                    openDB(prop.getProperty("host"), prop.getProperty("database"),
                        prop.getProperty("user"), prop.getProperty("password"));
                    loadData(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3], parts[4],
                        parts[5]);
                    closeDB();
                    cnt++;
                }
            }
            end = System.currentTimeMillis();
            System.out.println(cnt + " records successfully loaded");
            System.out.println("Loading speed : "
                + (cnt * 1000) / (end - start)
                + " records/s");
        } catch (SQLException se) {
            System.err.println("SQL error: " + se.getMessage());
            closeDB();
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Fatal error: " + e.getMessage());
            closeDB();
            System.exit(1);
        }
        closeDB();
    }
}

