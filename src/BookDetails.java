
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 *
 * @author Audrey
 */
public class BookDetails extends JPanel {
    public BookDetails(int id){
        setLayout(null);
        JButton button = new JButton("back to browse");
        button.setBounds(10,10,140,20);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTabbedPane tabbedPane = (JTabbedPane) SwingUtilities.getAncestorOfClass(JTabbedPane.class, BookDetails.this);
                int browse_index = tabbedPane.indexOfTab("Browse");
                tabbedPane.setSelectedIndex(browse_index); // switch to that tab
            }
        });
        add(button);
        
        
        SQLQueries q = SQLQueries.getInstance();
        try{
            ResultSet rs = q.query("SELECT * FROM book_list_data WHERE book_id = " + id);
            while(rs.next()){
                BookListItem details = new BookListItem(rs.getInt("book_id"), rs.getString("title"), rs.getString("fname"), rs.getString("lname"), rs.getString("summary"));
                details.setBounds(10, 50, 460, 150);
                add(details);
            }
        }
        catch(SQLException sqle){}

        
        
    }
}
