
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * SearchPanel is a JScrollPane that displays the library's books and provides basic search filtering
 * capabilities (search by title, author last name, genre)
 * Search bar input is passed into a PreparedStatement to protect against SQL injection
 * @author Audrey
 */
public class SearchPanel extends JScrollPane {

    JComboBox searchFilters;
    JTextField textField;
    JButton button;
    SQLQueries s;
    JPanel panel;
    String[] values = {"title", "lname", "genre"}; // contains table column names corresponding with text in filters dropdown

    public SearchPanel(SQLQueries s) throws SQLException {
        super();
        this.s = s;
        this.panel = new JPanel();
        panel.setLayout(null);
        this.searchFilters = new JComboBox(new String[]{"Title", "Author", "Genre", "See All"});
        searchFilters.setBounds(10, 10, 80, 20);
        searchFilters.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(searchFilters.getSelectedItem().equals("See All")) {
                        refresh(SearchPanel.this.s.query("SELECT * FROM book_list_data;"));
                        searchFilters.setSelectedItem("Title");
                    }
                }
                catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }
        });
        this.textField = (JTextField) (new LoginTextField("search", false));
        textField.setBounds(100, 8, 250, 25);
        this.button = new JButton("Search");
        button.setBounds(360, 10, 75, 20);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SQLQueries s = SQLQueries.getInstance();
                    String search = textField.getText().equals("search") ? "" : textField.getText();
                    refresh(s.preparedQuery("SELECT * FROM book_list_data WHERE lower(" + values[searchFilters.getSelectedIndex()] + ") LIKE ?;", "%" + search + "%"));

                }
                catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }
        });
        refresh();
        setViewportView(panel);
    }

    private void refresh(ResultSet rs) throws SQLException {
        int count = 0;
        JPanel book;
        panel.removeAll();
        panel.add(this.textField);
        panel.add(this.button);
        panel.add(this.searchFilters);
        while (rs.next()) {
            book = new BookListItem(rs.getInt("book_id"), rs.getString("title"), rs.getString("fname"), rs.getString("lname"), rs.getString("genre"), 0);
            book.setBounds(10, 160 * count++ + 40, 460, 150);
            panel.add(book);
        }
        if (count > 3) {
            panel.setPreferredSize(new Dimension(460, 210 * count));
        } else if (count == 0) {
            JLabel label = new JLabel("no books found matching your search");
            label.setBounds(110, 160, 300, 30);
            panel.add(label);
        }
        repaint();
    }

    public void refresh() throws SQLException {
        String search = textField.getText().equals("search") ? "" : textField.getText();
        refresh(this.s.query("SELECT * FROM book_list_data WHERE lower(" + values[searchFilters.getSelectedIndex()] + ") LIKE '%" + search + "%';"));
    }

}
