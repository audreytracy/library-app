
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JPasswordField;


/**
 * LoginTextField allows for an HTML-esque 'placeholder' in the text box that is greyed out and disappears on user click
 * @author Audrey
 */
public class LoginTextField extends JPasswordField {
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
