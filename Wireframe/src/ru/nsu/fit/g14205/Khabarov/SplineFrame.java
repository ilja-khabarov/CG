package ru.nsu.fit.g14205.Khabarov;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by ilja on 24.04.2017.
 */
public class SplineFrame extends JFrame
{
    DrawPanel splineDrawPane;
    SettingsPane settingsPane = new SettingsPane();


    public SplineFrame()
    {
        super();
        this.setSize(800,600);
        this.setMinimumSize(new Dimension(800,600));
        this.setLocationRelativeTo(null);
        splineDrawPane = new DrawPanel();
        splineDrawPane.setPreferredSize(new Dimension(800,450));
        splineDrawPane.resizeImage(800,450);
        settingsPane = new SettingsPane();
        this.setLayout(new BorderLayout());
        this.add(splineDrawPane, BorderLayout.NORTH);
        //splineDrawPane.myLine(0,0,200,200,splineDrawPane.raster);
        this.add(settingsPane, BorderLayout.SOUTH);

        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class SettingsPane extends  JPanel
    {
        JLabel a,b,c,d,n,m,k,red,green,blue;
        JTextField aEdit,bEdit,cEdit,dEdit, nEdit,
                   mEdit,kEdit,redEdit,greenEdit,blueEdit;
        SettingsPane()
        {
            initLabels();
            initTextFields();
            this.setLayout(new GridLayout(4,10));
            addElementsToGrid();

        }
        void initLabels()
        {
            a = new JLabel();
            b = new JLabel();
            c = new JLabel();
            d = new JLabel();
            n = new JLabel();
            m = new JLabel();
            k = new JLabel();
            red = new JLabel();
            green = new JLabel();
            blue = new JLabel();
            a.setText("a");
            b.setText("b");
            c.setText("c");
            d.setText("d");
            m.setText("m");
            n.setText("n");
            k.setText("k");
            red.setText("R");
            blue.setText("B");
            green.setText("G");
        }
        void initTextFields()
        {
            aEdit = new JTextField("0");
            bEdit = new JTextField("1");
            cEdit = new JTextField("0");
            dEdit = new JTextField("5");
            mEdit = new JTextField("10");
            nEdit = new JTextField("10");
            kEdit = new JTextField("5");
            redEdit = new JTextField("0");
            greenEdit = new JTextField("255");
            blueEdit = new JTextField("255");
        }
        void addElementsToGrid()
        {
            this.add(a);
            this.add(aEdit);
            this.add(b);
            this.add(bEdit);
            this.add(c);
            this.add(cEdit);
            this.add(d);
            this.add(dEdit);
            this.add(m);
            this.add(mEdit);
            this.add(n);
            this.add(nEdit);
            this.add(k);
            this.add(kEdit);
        }
    }
}
