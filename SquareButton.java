import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

public class SquareButton extends JButton {
    private static final Color BACKGROUND_COLOR = new Color(64, 64, 64);
    public static final int BUTTON_SIZE = 64;

    public SquareButton(String text) {
        super(text);
        setFocusPainted(true);
        setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
        setBackground(BACKGROUND_COLOR);

        setBorder(new EmptyBorder(0, 0, 0, 0));

        setUI(new BasicButtonUI());
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        int size = Math.max(d.width, d.height);
        return new Dimension(size, size);
    }
}