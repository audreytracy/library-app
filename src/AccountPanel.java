
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * AccountPanel is JScrollPane object providing information about library user's holds & checked out books
 * @author Audrey
 */
public class AccountPanel extends JScrollPane {

    JLabel warning = new JLabel("");
    JPanel pane;
    JComboBox comboBox;
    JLabel message;
    JButton logout;
    int user_id;

    public void refresh() {
        pane.repaint();
    }

    public AccountPanel() {

        super();

        pane = new JPanel();

        pane.setLayout(null);
        pane.add(warning);

        this.comboBox = new JComboBox(new String[]{"Summary", "My Holds", "Checkout History"});
        comboBox.setBounds(270, 80, 110, 20);
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JLabel label = new JLabel("<html><h2>" + comboBox.getSelectedItem() + "</h2></html>");
                label.setBounds(40, 80, 100, 25);
                pane.add(label); // make sure the old one is gone when this one is added (idk how to test that but like.. check it later)
                SQLQueries s = SQLQueries.getInstance();
                try {
                    if (comboBox.getSelectedItem().equals("Summary")) {
                        pane.removeAll();
                        pane.add(logout);
                        pane.add(comboBox);
                        pane.add(message);
                        JLabel summary = new JLabel("<html><h2>Summary</h2></html>");
                        summary.setBounds(40, 80, 100, 25);
                        pane.add(summary);
                        // get the number of checked out books
                        ResultSet rs = s.query("SELECT count(*) FROM borrowing_history WHERE date_returned IS NULL AND account_id = " + user_id);
                        rs.next();
                        int checked_out = rs.getInt("count");
                        rs = s.query("SELECT count(*) FROM borrowing_history WHERE date_returned IS NULL AND due_date < CURRENT_TIMESTAMP AND account_id = " + user_id);
                        rs.next();
                        int overdue = rs.getInt("count");
                        rs = s.query("SELECT count(*) FROM holds WHERE account_id = " + user_id);
                        rs.next();
                        int holds = rs.getInt("count");
                        AccountPanelItem a = new AccountPanelItem(checked_out, overdue, holds);
                        a.setBounds(40, 110, 340, 100);
                        pane.add(a);
                        repaint();
                    } else if (comboBox.getSelectedItem().equals("My Holds")) {
                        pane.removeAll();
                        pane.add(logout);
                        pane.add(comboBox);
                        pane.add(message);
                        JLabel holds = new JLabel("<html><h2>My holds</h2></html>");
                        holds.setBounds(40, 80, 100, 25);
                        pane.add(holds);
                        ResultSet rs = s.query("SELECT * FROM holds_data LEFT JOIN book ON holds_data.book_id = book.book_id LEFT JOIN author ON author.author_id = book.author_id JOIN avail_copies_data ON avail_copies_data.book_id = holds_data.book_id WHERE account_id = " + user_id);
                        int y_pos = 0;
                        while (rs.next()) {
                            y_pos += 110;
                            int iu = rs.getInt("in_use_copies");
                            int t = rs.getInt("total_copies");
                            int avail = t - iu;
                            AccountPanelItem a = new AccountPanelItem(rs.getInt("book_id"), rs.getString("title"), rs.getString("fname"), rs.getString("lname"), rs.getString("time_placed"), rs.getInt("hold_pos"), rs.getInt("num_copies"), avail, user_id);
                            a.setBounds(40, y_pos, 340, 100);
                            pane.add(a);
                        }
                        repaint();
                    } else if (comboBox.getSelectedItem().equals("Checkout History")) {
                        pane.removeAll();
                        pane.add(logout);
                        pane.add(comboBox);
                        pane.add(message);
                        JLabel chkt_hs = new JLabel("<html><h2>Checkout History</h2></html>");
                        chkt_hs.setBounds(40, 80, 200, 25);
                        pane.add(chkt_hs);
                        ResultSet rs = s.query("SELECT * FROM book_list_data JOIN borrowing_history ON book_list_data.book_id = borrowing_history.book_id WHERE account_id = " + user_id);
                        int y_pos = 0;
                        while (rs.next()) {
                            y_pos += 110;
                            AccountPanelItem a = new AccountPanelItem(rs.getInt("checkout_id"), rs.getString("title"), rs.getString("fname"), rs.getString("lname"), rs.getString("date_checked_out"), rs.getString("date_returned"), rs.getTimestamp("due_date"));
                            a.setBounds(40, y_pos, 340, 100);
                            pane.add(a);
                        }
                        repaint();
                    }
                } catch (SQLException sqle) {}
            }
        });

        JLabel greeting = new JLabel("<html><h2>Sign in</h2><html>");
        greeting.setBounds(50, 10, 300, 30);
        pane.add(greeting);

        JTextField username_field = (JTextField) (new LoginTextField("username", false));
        username_field.setBounds(50, 50, 100, 20);
        username_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AccountPanel.this.remove(warning);
                AccountPanel.this.repaint();
            }
        });

        JPasswordField pin_field = new LoginTextField("****");
        pin_field.setBounds(160, 50, 100, 20);
        pin_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AccountPanel.this.remove(warning);
                AccountPanel.this.repaint();
            }
        });
        pane.add(pin_field);

        pane.add(username_field);

        JButton login_button = new JButton("Login");
        login_button.setBounds(270, 50, 100, 20);
        SQLQueries q = SQLQueries.getInstance();
        login_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    pane.remove(warning);
                    String p = pin_field.getText();
                    String username = username_field.getText();
                    if (p.equals("****") || p.length() != 4 || !isInteger(p)) {
                        throw new SQLException();
                    }
                    int pin = Integer.parseInt(p);
                    ResultSet user = q.preparedQuery("SELECT account_id FROM account WHERE pin = ? AND username = ?;", pin, username);
                    user.next();
                    user_id = user.getInt("account_id");
                    pane.removeAll();
                    message = new JLabel("<html><h1>Hello, " + username + "</h1></html>");
                    message.setBounds(40, 30, 3000, 30);
                    pane.add(message);

                    pane.add(comboBox);
                    pane.add(logout);
                    comboBox.selectWithKeyChar('s');
                    repaint();
                } catch (SQLException sqle) {
                    pane.remove(warning);
                    warning = new JLabel("<html><h4 style=\"color:red;\">invalid login</h4></html>");
                    warning.setBounds(50, 65, 100, 20);
                    AccountPanel.this.pane.add(warning);
                    AccountPanel.this.repaint();
                }
            }
        });

        logout = new JButton("logout");
        logout.setBounds(365, 10, 70, 20);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTabbedPane tabbedPane = (JTabbedPane) SwingUtilities.getAncestorOfClass(JTabbedPane.class, AccountPanel.this);
                int account_index = tabbedPane.indexOfTab("Account");
                AccountPanel ap = new AccountPanel();
                tabbedPane.setComponentAt(account_index, ap);
                tabbedPane.setSelectedComponent(ap); // switch to that tab
            }
        });

        pane.add(login_button);

        pane.setPreferredSize(new Dimension(460, 110 * 90));

        setViewportView(pane);
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

}
