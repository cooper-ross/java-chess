import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;

public class GamePanel extends JPanel {
    private SquareButton[] buttons;
    private static final int PADDING = 10;
    private static final Color BACKGROUND_COLOR = new Color(95, 67, 42);
    private static final Color BORDER_COLOR = new Color(128, 80, 38);

    public GamePanel() {
        buttons = new SquareButton[64];
        setLayout(new GridLayout(8, 8));
        initializeButtons();

        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

        // Add an inner margin to thicken the cell borders outer rim, so it looks like
        // the board is made of lines instead of cells
        // I don't like working with java swing :(
        Border innerMargin = new LineBorder(BORDER_COLOR, 5);
        setBorder(BorderFactory.createCompoundBorder(getBorder(), innerMargin));
    }

    private void initializeButtons() {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                final Color color = (j % 2 + (i % 2) == 1) ? new Color(169, 121, 79) : new Color(235, 177, 126);
                final int index = (i * 8) + j;

                buttons[index] = new SquareButton("");
                buttons[index].setBackground(color);
                buttons[index].setFont(new Font("Arial", Font.PLAIN, 30));
                add(buttons[index]);
            }
        }
    }

    public SquareButton[] getButtons() {
        return buttons;
    }
}