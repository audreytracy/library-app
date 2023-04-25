import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.Statement;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public class SQLQueries {

    private Statement stmt;
    
    public SQLQueries(){
        String url = "jdbc:postgresql://localhost:5432/";
        String user = "postgres";
        String password = "ahi7,$$";
        String filePath = new File("").getAbsolutePath()+"\\src\\sql\\test.sql";
        try {
            Class.forName("org.postgresql.Driver");
            Connection dbmk = DriverManager.getConnection(url, user, password);
            Statement make = dbmk.createStatement();
            make.executeUpdate("DROP DATABASE IF EXISTS libraryapp;");
            make.executeUpdate("CREATE DATABASE libraryapp;");
            dbmk.close();
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/libraryapp", user, password);
            Statement stmt = con.createStatement();
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line.split("--")[0].trim());
                if (line.endsWith(";")) {
                    stmt.execute(sb.toString());
                    sb = new StringBuilder();
                    System.out.println("hsdiofsdfi");
                }
            }
            this.stmt = stmt;
        }
        catch (FileNotFoundException e) { e.printStackTrace(); } 
        catch (IOException | ClassNotFoundException | SQLException e) { e.printStackTrace(); }
    }
    
    public ResultSet query(String query) throws SQLException {
        return this.stmt.executeQuery(query);
    }
    
    public ResultSet insert(String records) throws SQLException {
        return this.stmt.executeQuery(records);
    }
    
    
    
    public static void main(String[] args) {
        SQLQueries s = new SQLQueries();
        try{
            s.query("SELECT * FROM account;");

        }
        catch(SQLException sqle){
            System.out.println("sqlexception");
        }
    }

}
