
import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;

public class LibraryApp {

    public void addComponentToPane(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setSize(550, 550);

        InputComponents outer = new InputComponents();
        InputComponents.LoginPanel login = outer.new LoginPanel("Login");
        InputComponents.LoginPanel create_acct = outer.new LoginPanel("Create");

        SQLQueries s = new SQLQueries();
        try {
            InputComponents.SearchPanel browse = outer.new SearchPanel(s);
            tabbedPane.addTab("Browse", browse);

        } catch (SQLException sqle) { }

        tabbedPane.addTab("Login", login);
        tabbedPane.addTab("Create Account", create_acct);

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
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

}
