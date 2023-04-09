/* Class contains methods to interact with postresql database */

//import the necessary tools
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;


public class postgresql {
    private final String url = "jdbc:postgresql://localhost/arduino";   //url to database
    private final String user = "postgres";                             //username of database
    private final String password = "2001";                             //database password 

    //connect to the database
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
 

    //insert data row
    public long insertData(String dn, String dk, float dv) {    //parameter device name, data key, data value
        String SQL = "INSERT INTO data(device_name, data_key, data_value) VALUES(?, ?, ?)"; //sql query
        long id = 0;
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(SQL,
                Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, dn);
            pstmt.setString(2, dk);
            pstmt.setFloat(3, dv);

            int affectedRows = pstmt.executeUpdate();
            // check if the row is affected 
            if (affectedRows > 0) {
                // get the id
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getLong(1);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return id;
    }


    //retourner la device et le valeur de la temperature
    public String[] getTmpData(String dev) {
        String[] data = new String[2];
        String SQL = "select data_value from data where device_name = '" + dev + "' and data_key = 'TMP' and id_data = (select max(id_data) from data where data_key = 'TMP')";
    
        try (Connection conn = connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {
            // display line information
            System.out.println("reading line");
            //fill data with value
            while (rs.next()) {
                data[0] = rs.getString("data_value");
                System.out.println(data[0]);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return data;
    }


    //retourner la device et le valeur de l'humidite'
    public String[] getHumData(String dev) {
        String[] data = new String[1];
        String SQL = "select device_name, data_value from data where device_name = '" + dev + "' and data_key = 'HUM' and id_data = (select max(id_data) from data where data_key = 'HUM')";
    
        try (Connection conn = connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {
            // display line information
            System.out.println("reading line");
            //fill data with value
            while (rs.next()) {
                data[0] = rs.getString("data_value");
                System.out.println(data[0]);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return data;
    }
}    
