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
    private static final SQLQueries SQL = new SQLQueries();
    private SQLQueries(){
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
                line = line.split("--")[0];
                sb.append(line.split("--")[0] + " ");
                if(line.equals("$$")){
                    line = reader.readLine();
                    sb.append(line.split("--")[0] + " ");
                    do{
                        line = reader.readLine();
                        sb.append(line.split("--")[0] + " ");
                    }
                    while(!line.equals("$$;"));
                }
                if (line.endsWith(";")) {
                    stmt.execute(sb.toString());
                    sb = new StringBuilder();
                }
            }
            this.stmt = stmt;
        }
        catch (FileNotFoundException e) { e.printStackTrace(); } 
        catch (IOException | ClassNotFoundException | SQLException e) { e.printStackTrace(); }
    }
    
    // Make SQLQueries singleton class
    public static SQLQueries getInstance(){
        return SQL;
    }
    
    public ResultSet query(String query) throws SQLException {
        return this.stmt.executeQuery(query);
    }
    
    public void update(String insert) throws SQLException {
        this.stmt.executeUpdate(insert);
    }
}
