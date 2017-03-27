import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by ilja on 26.03.2017.
 */
public class ControlsPane extends JPanel implements ChangeListener {

    JSlider slider;
    JTextField textField;
    KeyAdapter lineThicknessTextFieldKeyAdapter;
    SettingsElementPane lineThickness;
    SettingsElementPane hexSize;
    SettingsElementPane rowsAmount;
    SettingsElementPane columnsAmount;
    ControlsPane()
    {
        //setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        lineThickness = new SettingsElementPane("Line thickness", 5, 1, 2);
        hexSize = new SettingsElementPane("Hex size", 100, 5, 10);
        rowsAmount = new SettingsElementPane("Rows", 100,1,20);
        columnsAmount= new SettingsElementPane("Columns", 100,1,25);



        this.add(columnsAmount);
        this.add(rowsAmount);
        this.add(lineThickness);
        this.add(hexSize);

    }
    @Override
    public void stateChanged(ChangeEvent e )
    {
        JSlider source = (JSlider)e.getSource();
        textField.setText(source.getValue() + "");
        System.out.println(source.getValue());
    }
    void initLineThicknessTextFieldKeyAdapter()
    {
        lineThicknessTextFieldKeyAdapter = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if ( !textField.getText().equals("") )
                    slider.setValue(Integer.parseInt(textField.getText()));
            }
        };
    }
    private class SettingsElementPane extends JPanel
    {
        JSlider slider;
        JTextField textField;
        KeyAdapter keyAdapter;
        ChangeListener changeListener;

        SettingsElementPane(String name, int maxval, int minval, int val)
        {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            slider = new JSlider(JSlider.HORIZONTAL, minval, maxval, val);
            textField = new JTextField("" +val, 5);
            initLineThicknessTextFieldKeyAdapter();
            initChangeListener();
            textField.setSize(50,20);
            slider.setMajorTickSpacing((maxval-minval+1)/5);
            slider.setMinorTickSpacing((maxval-minval+1)/20);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);

            textField.addKeyListener(lineThicknessTextFieldKeyAdapter);
            slider.addChangeListener(changeListener);
            //this.setPreferredSize(new Dimension(380,50));

            this.add(slider);
            this.add(textField);
            this.add(new JLabel(name));
        }
        void initLineThicknessTextFieldKeyAdapter()
        {
            lineThicknessTextFieldKeyAdapter = new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    super.keyTyped(e);
                    if ( !textField.getText().equals("") )
                        slider.setValue(Integer.parseInt(textField.getText()));
                }
            };
        }
        void initChangeListener()
        {
            changeListener = new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    JSlider source = (JSlider)e.getSource();
                    textField.setText(source.getValue() + "");
                    System.out.println(source.getValue());
                }
            };
        }
    }
}
