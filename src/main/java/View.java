import javax.swing.*;
import java.awt.*;

public class View {

    private JSlider slider;


    public View() {
        JFrame frame = new JFrame();

        JPanel panel = new JPanel();
        slider = new JSlider(6, 10, 10);
        slider.setMajorTickSpacing(1);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        JLabel label = new JLabel("Set DLI for system");

        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(0, 1));
        frame.add(panel, BorderLayout.CENTER);

        panel.add(label);
        panel.add(slider);


        frame.setTitle("Adaptive Light System");
        frame.pack();
        frame.setVisible(true);


    }

    public int getSlider() {
        return slider.getValue();
    }

}
