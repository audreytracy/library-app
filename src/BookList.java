
import java.awt.*;
import java.sql.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class BookList extends JFrame {

    public BookList(ResultSet rs) throws Exception {
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Rounded Panel Example");
        setResizable(true);
        setDefaultLookAndFeelDecorated(true);
        setSize(520, 520);
        Container pane = getContentPane();

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 10, 460, 460);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        int count = 0;
        JPanel book;
        while (rs.next()) {
            book = new BookListItem(10, new Book(1, "TITLE", 1, "mystery", rs.getString(2), 1, 200));
            book.setBounds(10, 210 * count++ + 10, 460, 200);
            panel.add(book);
        }
                panel.setPreferredSize(new Dimension(460, 210 * count));

        scrollPane.setViewportView(panel);
        pane.add(scrollPane);
    }


    class BookListItem extends JPanel {

        private int cornerRadius = 15;

        public BookListItem(int cornerRadius, Book book) throws Exception {
            super();
            this.cornerRadius = cornerRadius;
            setBackground(Color.LIGHT_GRAY);
            this.setLayout(null);

            addComponent(10, 25, 120, 140, "image", "C:\\Users\\Audrey\\Desktop\\Final_Project_366\\LibraryManagementSystem\\src\\images\\" + book.getCoverImg() + ".jpg");
            addComponent(170, 30, 200, 20, "label", "<html><h2>" + book.getTitle() + "</h2></html>");
            addComponent(170, 50, 200, 20, "label", "<html><h3 style = \"color:green\">" + book.getAuthorID() + "</h3></html>");
            addComponent(170, 70, 200, 100, "label", book.getSummary());
        }

        public void addComponent(int x, int y, int width, int height, String type, String text) throws IOException {
            Component c;
            if (type.toLowerCase().equals("button")) {
                c = new JButton(text);
            } else if (type.toLowerCase().equals("password")) {
                c = new JPasswordField(text);
            } else if (type.toLowerCase().equals("image")) {
                c = new JLabel(new ImageIcon(ImageIO.read(new File(text)).getScaledInstance(width, height, Image.SCALE_SMOOTH)));
            } else if (type.toLowerCase().equals("label")) {
                c = new JLabel("<html>" + text + "</html>");
            } else {
                c = new JTextField(text);
            }
            System.out.println(c.getClass());
            c.setBounds(x, y, width, height);
            add(c);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius); //paint border
        }
    }
}
