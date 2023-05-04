
import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class LibraryApp {

    public void addComponentToPane(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setSize(550, 550);

        
        AccountPanel ap = new AccountPanel();
        tabbedPane.addTab("Account", ap);

        SQLQueries s = SQLQueries.getInstance();
        try {
            // InputComponents.SearchPanel browse = outer.new SearchPanel(s);
            SearchPanel browse = new SearchPanel(s);
            tabbedPane.addTab("Browse", browse);

        } catch (SQLException sqle) {sqle.printStackTrace();}
        pane.add(tabbedPane, BorderLayout.CENTER);
        
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("TabDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LibraryApp demo = new LibraryApp();
        demo.addComponentToPane(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try{
                    cleanHolds();
                }
                catch(SQLException sqle){}
            }
        }, 0, 60000 * 10); // 60000 ms per minute, run every 10 minutes
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
        
    }
    
    public static void cleanHolds() throws SQLException{
        SQLQueries s = SQLQueries.getInstance();
        s.query("DELETE FROM holds WHERE hold_expire > CURRENT_TIMESTAMP;");
    }

}
