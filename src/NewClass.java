
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class NewClass {


    public static void main(String[] args) {
        String jdbcURL = "jdbc:postgresql://localhost:5432/audrey";
        String username = "postgres";
        String password = "ahi7,$$";
        try {
            Class.forName("org.postgresql.Driver");

            // Establishing Connection
            Connection con = DriverManager.getConnection(jdbcURL, username, password);

            if (con != null) {
                System.out.println("Connected");
            } else {
                System.out.println("Not Connected");
            }
            Scanner scan = new Scanner(System.in);
            String selectEmployee = "SELECT * FROM employee where employee_id = ?";
            PreparedStatement pstmt = con.prepareStatement(selectEmployee);
            System.out.print("Enter employee id:");
            String id1 = scan.nextLine();
            pstmt.setInt(1, Integer.parseInt(id1));
            ResultSet rs = pstmt.executeQuery();
            
            BookList gui = new BookList(rs);
            gui.setVisible(true);

            con.close();
        } catch (ClassNotFoundException cnfe) {
            System.out.println(cnfe);
        }
        catch(SQLException sqle){
            System.out.println(sqle);
        }

        catch (Exception e) {
        }

    }
}
