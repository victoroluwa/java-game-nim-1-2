import javax.swing.*;
import java.awt.*;

public class MarblePanel extends JPanel {
    private int marbleCount = 10;
    /**
     * MarblePanel
     *
     * @author  Ifesinachi Paschal Obiors
     * @version 3.0
     */

    public void setMarbleCount(int count) {
        this.marbleCount = count;
        repaint(); // This is the trigger to the paintComponent to re-draw
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // This will draw a square box
        int padding = 20;
        g.setColor(Color.BLACK);
        g.drawRect(padding, padding, getWidth() - 2 * padding, getHeight() - 60);

        // The foregoing will draw marbles (as circles)
        int diameter = 20;
        int maxPerRow = (getWidth() - 2 * padding) / (diameter + 5);
        int x = padding + 5;
        int y = padding + 5;
        for (int i = 0; i < marbleCount; i++) {
            g.setColor(Color.GREEN.darker());
            g.fillOval(x, y, diameter, diameter);
            x += diameter + 5;
            if ((i + 1) % maxPerRow == 0) {
                x = padding + 5;
                y += diameter + 5;
            }
        }

        // The follwoing will draw the number of marbles below the box
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Marbles: " + marbleCount, padding, getHeight() - 20);
    }
}
