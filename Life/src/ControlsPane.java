import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

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
    DrawPanel drawPanel;
    ActionListener okListener;
    JPanel radioPane;
    JRadioButton xor;
    JRadioButton replace;
    ControlsPane(DrawPanel dp )
    {
        //setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        drawPanel = dp;
        lineThickness = new SettingsElementPane("Line thickness", 5, 1, drawPanel.lineWidth);
        hexSize = new SettingsElementPane("Hex size", 50, 6, (int)Math.round(drawPanel.hexScale*10));
        rowsAmount = new SettingsElementPane("Rows", 101,1,drawPanel.fieldHeigth);
        columnsAmount= new SettingsElementPane("Columns", 101,1,drawPanel.fieldWidth);
        JButton ok = new JButton("OK");
        initRadioPane();
        initOkListener();
        ok.addActionListener(okListener);



        this.add(columnsAmount);
        this.add(rowsAmount);
        this.add(lineThickness);
        this.add(hexSize);
        this.add(radioPane);
        this.add(ok);

    }
    void initRadioPane()
    {
        radioPane = new JPanel();
        radioPane.setLayout(new GridLayout(1,2));
        replace = new JRadioButton("Replace");
        xor = new JRadioButton("Xor");
        radioPane.add(replace);
        radioPane.add(xor);


        replace.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xor.setSelected(false);
            }
        });
        xor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                replace.setSelected(false);
            }
        });
    }
    void initOkListener()
    {
        okListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //drawPanel = new DrawPanel(); // here should be some code about initialisation
                drawPanel.fieldWidth = Integer.parseInt(columnsAmount.getVal());
                drawPanel.fieldHeigth = Integer.parseInt(rowsAmount.getVal());
                drawPanel.hexScale = Integer.parseInt(hexSize.getVal()) / 10.0;
                System.out.println("Scale is " + drawPanel.hexScale );
                drawPanel.lineWidth = Integer.parseInt(lineThickness.getVal());
                //System.out.println(columnsAmount.getVal()+" is width");
                drawPanel.setXormode(xor.isSelected());
                drawPanel.img =
                        new BufferedImage( (int)(28*drawPanel.fieldWidth*drawPanel.hexScale)+1,(int)(23*(drawPanel.fieldHeigth+1)*drawPanel.hexScale), BufferedImage.TYPE_3BYTE_BGR );
                drawPanel.raster = drawPanel.img.getRaster();
                drawPanel.nullifyImage();
                drawPanel.setPreferredSize(new Dimension(drawPanel.img.getWidth(), drawPanel.img.getHeight()));
                drawPanel.setSizesAccordigToScale();


                drawPanel.drawField(drawPanel.fieldWidth, drawPanel.fieldHeigth, drawPanel.raster );
                drawPanel.lifeModel = new LifeModel(drawPanel.fieldWidth, drawPanel.fieldHeigth);
                drawPanel.repaint();
            }
        };
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
        int max, min;

        SettingsElementPane(String name, int maxval, int minval, int val)
        {
            max = maxval; min = minval;
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
        String getVal()
        {
            return textField.getText();
        }
        void initLineThicknessTextFieldKeyAdapter()
        {
            lineThicknessTextFieldKeyAdapter = new KeyAdapter() {
                /*@Override
                public void keyTyped(KeyEvent e) {
                    super.keyTyped(e);
                    if ( !textField.getText().equals("") ) {
                        if ( Integer.parseInt(textField.getText()) > max )
                        {
                            textField.setText(max+"");
                        }
                        else
                            slider.setValue(Integer.parseInt(textField.getText()));
                    }
                }*/
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    if ( !textField.getText().equals("") ) {
                        if ( Integer.parseInt(textField.getText()) > max )
                        {
                            textField.setText(max+"");
                        }
                        else
                            slider.setValue(Integer.parseInt(textField.getText()));
                    }
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
                    //System.out.println(source.getValue());
                }
            };
        }
    }
}
