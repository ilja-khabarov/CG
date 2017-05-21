package ru.nsu.fit.g14205.Khabarov;
import javax.swing.*;
import java.awt.*;

/**
 * Created by ilja on 03.04.2017.
 */
public class StatusBar extends JPanel
{
    JLabel xName = new JLabel("X:");
    JLabel yName = new JLabel("Y:");
    JLabel zName = new JLabel("Z:");
    JLabel x;
    JLabel y;
    JLabel z;
    JPanel basicPanel = new JPanel(new GridLayout(1,2));
    JPanel statusPanel = new JPanel(null);

    StatusBar()
    {
        this.setLayout(new FlowLayout(FlowLayout.TRAILING));
        this.setSize(600,30 );

        initStatusPane();

        this.add(basicPanel);
    }
    public void setCoords( double xVal, double yVal, double zVal )
    {
        x.setText(""+xVal);
        y.setText(""+yVal);
        z.setText(""+zVal);
        x.repaint();
        y.repaint();
    }
    private void initStatusPane()
    {
        x = new JLabel();
        y = new JLabel();
        z = new JLabel();
        x.setBounds(20,0,40,20);
        y.setBounds(80,0,40,20);
        z.setBounds(140,0,40,20);
        xName.setBounds(0,0,20,20);
        yName.setBounds(60,0,20,20);
        zName.setBounds(120,0,20,20);
        basicPanel.setPreferredSize(new Dimension(600, 15));
        statusPanel.add(xName);
        statusPanel.add(x);
        statusPanel.add(yName);
        statusPanel.add(y);
        statusPanel.add(zName);
        statusPanel.add(z);
        basicPanel.add(new JLabel());
        basicPanel.add(statusPanel);
    }
}