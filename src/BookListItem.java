
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
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
final class BookListItem extends JPanel {

        private int cornerRadius = 15;
        private int book_id;
        
        public BookListItem(int id, String title, String fname, String lname){
            super();
            this.setLayout(null);
            this.book_id = id;
            addComponent(10, 15, 100, 120, "image", new File("").getAbsolutePath()+"\\src\\images\\" + id + ".png");
            addComponent(120, 20, 300, 20, "label", "<html><h3>" + title + "</h3></html>");
            addComponent(120, 40, 300, 20, "label", "<html><h4>" + fname + " " + lname + "</h4></html>");
        }

        public BookListItem(int id, String title, String fname, String lname, String genre, int n){//, int num_avail) {
            this(id, title, fname, lname);
            addComponent(120, 60, 300, 20, "label", "<html><h4> Genre: " + genre + "</h4></html>");
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JTabbedPane tabbedPane = (JTabbedPane) SwingUtilities.getAncestorOfClass(JTabbedPane.class, BookListItem.this);
                    int prev_details_index = tabbedPane.indexOfTab("Details");
                    if(prev_details_index == -1){ // if details tab not open yet
                        tabbedPane.addTab("Details", new JPanel());
                        prev_details_index = tabbedPane.indexOfTab("Details");
                    }
                    BookDetails bd = new BookDetails(BookListItem.this.book_id);
                    tabbedPane.setComponentAt(prev_details_index,bd);
                    tabbedPane.setSelectedComponent(bd); // switch to that tab
                }
            });
        }
        
        public BookListItem(int id, String title, String fname, String lname, String summary) {
            this(id, title, fname, lname);          
            addComponent(120, 45, 300, 100, "label", summary);

            JButton checkout_button = new JButton("checkout / place hold");
            checkout_button.setBounds(210,125,180,20);
            checkout_button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                        JTabbedPane tabbedPane = (JTabbedPane) SwingUtilities.getAncestorOfClass(JTabbedPane.class, BookListItem.this);
                        int checkout_index = tabbedPane.indexOfTab("Checkout/Hold");
                        if(checkout_index == -1){ // if details tab not open yet
                            tabbedPane.addTab("Checkout/Hold", new JPanel());
                            checkout_index = tabbedPane.indexOfTab("Checkout/Hold");
                        }
                        CheckoutHold ch = new CheckoutHold(BookListItem.this.getID(), false);
                        tabbedPane.setComponentAt(checkout_index, ch);
                        tabbedPane.setSelectedComponent(ch); // switch to that tab
                }
            });
            add(checkout_button);

        }
        
        public int getID(){
            return book_id;
        }

        public void addComponent(int x, int y, int width, int height, String type, String text) {
            Component c;
            if (type.toLowerCase().equals("button")) {
                c = new JButton(text);
            } else if (type.toLowerCase().equals("password")) {
                c = new JPasswordField(text);
            } else if (type.toLowerCase().equals("image")) {
                try {
                    c = new JLabel(new ImageIcon(ImageIO.read(new File(text)).getScaledInstance(width, height, Image.SCALE_SMOOTH)));
                } catch (IOException ioe) {
                    c = new JLabel("book cover not found");
                } // initialize
            } else if (type.toLowerCase().equals("label")) {
                c = new JLabel("<html>" + text + "</html>");
            } else {
                c = new JTextField(text);
            }
            c.setBounds(x, y, width, height);
            add(c);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(200, 200, 200));
            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            g.fillRoundRect(0, 0, 425, 150, arcs.width, arcs.height);
        }
    }