
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 *
 * @author Audrey
 */
public class AccountSummaryItem extends JPanel {

    public AccountSummaryItem(int num_checked_out, int num_overdue, int num_holds) {
        super();
        setLayout(null);
        JLabel c = new JLabel("You have " + num_checked_out + " books currently checked out.");
        c.setBounds(10, 10, 250, 20);
        add(c);
        JLabel o = new JLabel("You have " + num_overdue + " overdue books.");
        o.setBounds(10, 30, 250, 20);
        add(o);
        JLabel h = new JLabel("You have " + num_holds + " holds.");
        h.setBounds(10, 50, 250, 20);
        add(h);
    }

    public AccountSummaryItem(int checkout_id, String title, String fname, String lname, String checkout_date, String return_date) {
        super();
        setLayout(null);
        JLabel t = new JLabel("<html><h3>" + title + "</h3></html>");
        t.setBounds(10, 10, 300, 20);
        add(t);
        JLabel n = new JLabel(fname + " " + lname);
        n.setBounds(10, 30, 300, 20);
        add(n);
        JLabel c = new JLabel("Checked out: " + checkout_date);
        c.setBounds(10, 50, 200, 20);
        add(c);
        JLabel r = new JLabel("Returned:       " + ((return_date == null) ? "not returned" : return_date));
        r.setBounds(10, 70, 200, 20);
        add(r);
        if(return_date == null){
            JButton return_book = new JButton("Return book");
            return_book.setBounds(250,70,80,20);
            return_book.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SQLQueries s = SQLQueries.getInstance();
                    s.update("UPDATE borrowing_history SET date_returned = CURRENT_DATE WHERE checkout_id = " + checkout_id);
                    AccountPanel parent = (AccountPanel) SwingUtilities.getAncestorOfClass(JScrollPane.class, AccountSummaryItem.this);
                    parent.comboBox.selectWithKeyChar('c');
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }
        });
            add(return_book);
        }
        
    }

    public AccountSummaryItem(int book_id, String title, String fname, String lname, String hold_placed, int pos_on_list, int copies, int user_id) {
        super();
        setLayout(null);
        JLabel t = new JLabel("<html><h3>" + title + "</h3></html>");
        t.setBounds(10, 10, 300, 20);
        add(t);
        JLabel n = new JLabel(fname + " " + lname);
        n.setBounds(10, 30, 300, 20);
        add(n);
        JLabel c = new JLabel("Hold placed: " + hold_placed);
        c.setBounds(10, 50, 200, 20);
        add(c);
        JLabel p = new JLabel("Hold position: " + (pos_on_list+1) + " (on " + copies + ((copies == 1) ? " copy)" : " copies)"));
        p.setBounds(10, 70, 200, 20);
        add(p);
        JButton cancel = new JButton("cancel hold");
        cancel.setBounds(220, 45, 100, 20);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SQLQueries s = SQLQueries.getInstance();
                    s.update("DELETE FROM holds WHERE book_id = " + book_id + " AND account_id = " + user_id + ";");
                    AccountPanel parent = (AccountPanel) SwingUtilities.getAncestorOfClass(JScrollPane.class, AccountSummaryItem.this);
                    parent.comboBox.selectWithKeyChar('m');
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }
        });
        add(cancel);
        if (pos_on_list == 0) {
            JButton checkout = new JButton("checkout");
            checkout.setBounds(220, 70, 100, 20);
            checkout.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JTabbedPane tabbedPane = (JTabbedPane) SwingUtilities.getAncestorOfClass(JTabbedPane.class, AccountSummaryItem.this);
                    int checkout_index = tabbedPane.indexOfTab("Checkout/Hold");
                    if (checkout_index == -1) { // if details tab not open yet
                        tabbedPane.addTab("Checkout/Hold", new JPanel());
                        checkout_index = tabbedPane.indexOfTab("Checkout/Hold");
                    }
                    CheckoutHold ch = new CheckoutHold(book_id, true);
                    tabbedPane.setComponentAt(checkout_index, ch);
                    tabbedPane.setSelectedComponent(ch); // switch to that tab
                }
            });
            add(checkout);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(200, 200, 200));
        Dimension arcs = new Dimension(15, 15);
        g.fillRoundRect(0, 0, 340, 100, arcs.width, arcs.height);
    }
}
