
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author Audrey
 */
public class CheckoutHold extends JPanel {

    JLabel warning = new JLabel("invalid login");

    /**
     *
     * @param id
     * @param isFromHolds
     */
    public CheckoutHold(int id, boolean isFromHolds) {
        setLayout(null);

        JButton button = new JButton("back to browse");
        button.setBounds(10, 10, 120, 20);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTabbedPane tabbedPane = (JTabbedPane) SwingUtilities.getAncestorOfClass(JTabbedPane.class, CheckoutHold.this);
                int browse_index = tabbedPane.indexOfTab("Browse");
                int checkout_index = tabbedPane.indexOfTab("Checkout/Hold");
                tabbedPane.removeTabAt(checkout_index);
                tabbedPane.setSelectedIndex(browse_index); // switch to that tab
            }
        });
        add(button);

        JTextField username_field = (JTextField) (new LoginTextField("username", false));
        username_field.setBounds(200, 40, 100, 20);
        add(username_field);
        JPasswordField pin_field = new LoginTextField("****");
        // password_field.
        pin_field.setBounds(200, 65, 100, 20);
        pin_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CheckoutHold.this.remove(warning);
                CheckoutHold.this.repaint();
            }
        });
        username_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CheckoutHold.this.remove(warning);
                CheckoutHold.this.repaint();
            }
        });
        add(pin_field);
        SQLQueries q = SQLQueries.getInstance();
        try {
            ResultSet rs = q.query("SELECT * FROM avail_copies_data WHERE book_id = " + id);
            rs.next();
            int total_copies = rs.getInt("total_copies");
            int avail_copies = total_copies - rs.getInt("in_use_copies");
            JLabel l = new JLabel(avail_copies + " of " + total_copies + " copies available");
            l.setBounds(10, 50, 150, 20);
            if (avail_copies > 0 || (isFromHolds && avail_copies > 0)) {
                JButton checkout = new JButton("Checkout");
                checkout.setBounds(200, 100, 100, 20);
                checkout.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            String p = pin_field.getText();
                            String username = username_field.getText();
                            if (p.equals("****") || p.length() != 4 || !isInteger(p)) {
                                throw new SQLException();
                            }
                            int pin = Integer.parseInt(p);
                            ResultSet user = q.query("SELECT account_id FROM account WHERE pin = " + pin + " AND username = '" + username + "';");
                            user.next();
                            int user_id = user.getInt("account_id");
                            int num_holds_on_book = 0;
                            boolean thrown = false;
                            try {
                                user = q.query("SELECT count(*) FROM holds WHERE book_id = " + id);
                                user.next();
                                num_holds_on_book = user.getInt("count");
                                user = q.query("SELECT time_first_on_list FROM holds WHERE account_id = " + user_id + " AND book_id = " + id);
                                user.next();
                                boolean isFirst = user.getString("time_first_on_list") != null;
                                if (num_holds_on_book > 0 && !isFirst) {
                                    throw new SQLException();
                                }
                            } catch (SQLException s) {
                                thrown = true;
                                s.printStackTrace();
                                warning = new JLabel("<html><h4 style=\"color:red;\">there are holds on this book</h4></html>");
                                warning.setBounds(217, 130, 100, 20);
                                CheckoutHold.this.add(warning);
                                CheckoutHold.this.repaint();
                            }
if(!thrown){
                            q.update("INSERT INTO borrowing_history (book_id, account_id) VALUES (" + id + ", " + user_id + ");");
                            CheckoutHold.this.remove(checkout);
                            JLabel success = new JLabel("<html><h4 style = \"color:green\">checked out</h4></html>");
                            success.setBounds(217, 125, 100, 20);
                            CheckoutHold.this.add(success);
                            try {
                                q.update("DELETE FROM holds WHERE book_id = " + id + " AND account_id = " + user_id); // remove hold once book checked out, if hold exists
                            } catch (SQLException sqle) {
                                sqle.printStackTrace();
                            }
                            CheckoutHold.this.repaint();}
                        } catch (SQLException sqle) {
                            warning = new JLabel("<html><h4 style=\"color:red;\">invalid login</h4></html>");
                            warning.setBounds(217, 130, 100, 20);
                            CheckoutHold.this.add(warning);
                            CheckoutHold.this.repaint();
                        }

                    }
                });
                add(checkout);
            } else {
                JButton place_hold = new JButton("Place Hold");
                place_hold.setBounds(200, 100, 100, 20);
                place_hold.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            String p = pin_field.getText();
                            String username = username_field.getText();
                            if (p.equals("****") || p.length() != 4 || !isInteger(p)) {
                                throw new SQLException();
                            }
                            int pin = Integer.parseInt(p);
                            ResultSet user = q.preparedQuery("SELECT account_id FROM account WHERE pin = ? AND username = ?;", pin, username);
                            user.next();
                            int user_id = user.getInt("account_id");
                            try {
                                ResultSet info = q.query("SELECT count(*) FROM borrowing_history WHERE account_id = " + user_id + " AND book_id = " + id + " AND date_returned IS NULL");
                                info.next();
                                int num_user_checked_out = info.getInt("count");
                                info = q.query("SELECT num_copies FROM book_inventory WHERE book_id = " + id);
                                info.next();
                                int total = info.getInt("num_copies");
                                if (num_user_checked_out == total) {
                                    throw new IllegalStateException("THROWN FROM LN138");
                                }
                                q.update("INSERT INTO holds (book_id, account_id) VALUES (" + id + ", " + user_id + ");");
                                CheckoutHold.this.remove(place_hold);
                                JLabel success = new JLabel("<html><h4 style = \"color:green\">hold placed</h4></html>");
                                success.setBounds(217, 125, 100, 20);
                                CheckoutHold.this.add(success);
                                CheckoutHold.this.repaint();
                            } catch (IllegalStateException ise) {
                                CheckoutHold.this.remove(place_hold);
                                warning = new JLabel("<html><h4 style = \"color:red\">Cannot place hold, you have all copies checked out</h4></html>");
                                warning.setBounds(150, 125, 150, 50);
                                CheckoutHold.this.add(warning);
                                CheckoutHold.this.repaint();
                            } catch (SQLException sqle) { // exception thrown if unique PK constraint violated (already have hold on book)
                                CheckoutHold.this.remove(place_hold);
                                warning = new JLabel("<html><h4 style = \"color:red\">You already have a hold on this book</h4></html>");
                                warning.setBounds(170, 125, 220, 20);
                                CheckoutHold.this.add(warning);
                                CheckoutHold.this.repaint();
                            }
                        } catch (SQLException sqle) { // thrown if user doesn't exist or login is wrong format
                            warning = new JLabel("<html><h4 style=\"color:red;\">invalid login</h4></html>");
                            warning.setBounds(217, 130, 100, 20);
                            CheckoutHold.this.add(warning);
                            CheckoutHold.this.repaint();
                        }
                    }
                });
                add(place_hold);
            }
            add(l);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(200, 200, 200));
        Dimension arcs = new Dimension(15, 15);
        g.fillRoundRect(190, 30, 120, 100, arcs.width, arcs.height);
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
