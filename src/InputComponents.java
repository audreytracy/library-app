
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Audrey
 */
public class InputComponents extends Component {

    String[] formData = new String[0];

    public String[] getFormData() {
        return this.formData;
    }

    public class LoginPanel extends JPanel {

        private String[] form_data;

        public LoginPanel(String button_text) {
            super();
            JPasswordField password_field = new LoginTextField("****");
            add(password_field);
            JTextField username_field = (JTextField) (new LoginTextField("username", false));
            add(username_field);
//            SubmitFormButton button = new SubmitFormButton(button_text, password_field, username_field);
//            add(button);
            form_data = new String[0];
        }

        public String[] getLoginInfo() {
            return form_data;
        }
    }

    public class SearchPanel extends JScrollPane implements I {

        JComboBox searchFilters;
        JTextField textField;
        SubmitFormButton button;
        SQLQueries s;
        JPanel panel;
        String[] values = {"title", "lname", "genre", "1=1;--"};//, "date_added", "publication_date"};


        public SearchPanel(SQLQueries s) throws SQLException {
            super();
            this.s = s;
            this.panel = new JPanel();
            panel.setLayout(null);
            this.searchFilters = new JComboBox(new String[] {"Title", "Author", "Genre", "All"});
            searchFilters.setBounds(10, 10, 80, 20);
            this.textField = (JTextField) (new LoginTextField("search", false));
            textField.setBounds(100, 8, 250, 25);
            this.button = new SubmitFormButton("Search", searchFilters, textField, this);
            button.setBounds(360, 10, 75, 20);
            buttonReaction();
            //addStuff(s.query("SELECT * FROM book;"));
            setViewportView(panel);
        }

        private void refresh(ResultSet rs) throws SQLException {
            int count = 0;
            JPanel book;
            panel.removeAll();
            panel.add(this.textField);
            panel.add(this.button);
            panel.add(this.searchFilters);
            
            System.out.println(rs);
            while (rs.next()) {
                System.out.println(rs.toString());
                book = new BookListItem(10, new Book(rs.getInt("book_id"), rs.getString("title"), rs.getString("fname"), rs.getString("lname"), rs.getString("genre"), rs.getString("summary"), rs.getInt("pages")));
                book.setBounds(10, 160 * count++ + 40, 460, 150);
                panel.add(book);
            }
            if (count > 3) panel.setPreferredSize(new Dimension(460, 210 * count));
            else if(count == 0){
                JLabel label = new JLabel("no books found matching your search");
                label.setBounds(110,160,300,30);
                panel.add(label);
            }
            repaint();
        }

        @Override
        public void buttonReaction() throws SQLException {
            String search = textField.getText().equals("search") ? "" : textField.getText();
            //if(textField.getText().equals("search")) addStuff(this.s.query("SELECT * from book;"));
            refresh(this.s.query("SELECT book.*, fname, lname, count(*) "
                    + "FROM book JOIN author "
                               + "ON book.author_id = author.author_id JOIN book_inventory "
                               + "ON book_inventory.book_id = book.book_id "
                               + "GROUP BY title, fname, lname, book.book_id, summary, pages, book.genre, book.author_id "
                               + "HAVING lower("+values[searchFilters.getSelectedIndex()]+") LIKE '%" + search + "%';"));
        }
        
    }

    private class LoginTextField extends JPasswordField {

        public LoginTextField(String text) {
            super(text);
            setForeground(Color.GRAY);
            addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    setText("");
                    setForeground(Color.BLACK);
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (getText().length() == 0) {
                        setText(text);
                        setForeground(Color.GRAY);
                    }
                }
            });
            setPreferredSize(new Dimension(10 * text.length(), getPreferredSize().height)); // set width to 100 pixels
        }

        public LoginTextField(String text, boolean isPwd) {
            this(text);
            if (!isPwd) {
                setEchoChar('\u0000'); //•
            }
        }
    }

    private interface I {
        public void buttonReaction() throws SQLException;
    }

    private class SubmitFormButton extends JButton {

        private String[] form_data = new String[0];

        public SubmitFormButton(String text, JTextField field1, JTextField field2, I reactionMethod) {
            super(text);
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        reactionMethod.buttonReaction();
                    } catch (SQLException sqle) {
                        sqle.printStackTrace();
                    }
                }
            });
        }

        public SubmitFormButton(String text, JComboBox field1, JTextField field2, I reactionMethod) {
            super(text);
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        reactionMethod.buttonReaction();
                    } catch (SQLException sqle) {
                        sqle.printStackTrace();
                    }
                }
            });
        }
    }

    final class BookListItem extends JPanel {

        private int cornerRadius = 15;

        public BookListItem(int cornerRadius, Book book) {
            super();
            this.cornerRadius = cornerRadius;
            this.setLayout(null);
//            JLabel label = new JLabel("<html><h3>" + book.getTitle() + "</h3></html>");
//            label.setBounds(10,15,100,120);
            addComponent(10, 15, 100, 120, "image", new File("").getAbsolutePath()+"\\src\\images\\" + book.getID() + ".png");
            addComponent(120, 20, 300, 20, "label", "<html><h3>" + book.getTitle() + "</h3></html>");
            addComponent(120, 40, 300, 20, "label", "<html><h4>" + book.getAuthor() + "</h4></html>");
            addComponent(120, 55, 300, 100, "label", book.getSummary());
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

}
