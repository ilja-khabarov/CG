import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by ilja on 26.03.2017.
 */
public class ControlsPane extends JPanel implements ChangeListener {

    ControlsPane()
    {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        slider.setMajorTickSpacing(25);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(this);
        this.setPreferredSize(new Dimension(400,300));

        this.add(slider);

    }
    @Override
    public void stateChanged(ChangeEvent e )
    {
        JSlider source = (JSlider)e.getSource();
        System.out.println(source.getValue());
    }
}
