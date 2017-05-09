package ru.nsu.fit.g14205.Khabarov;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by ilja on 09.05.2017.
 */
public class MainPane extends JPanel
{
    DrawPanel drawPanelA;
    DrawPanel drawPanelB;
    DrawPanel drawPanelC;
    double compressCoeffA = 0.0;
    BufferedImage basicImage = null;

    public MainPane()
    {
        drawPanelA = new DrawPanel();
        drawPanelB = new DrawPanel();
        drawPanelC = new DrawPanel();

        setImageA("Data/Lena.bmp");
        addMotionAdapterA();

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        this.setPreferredSize(new Dimension(1080, 350));
        this.setMaximumSize(new Dimension(1080, 350));

        this.add(drawPanelA);
        this.add(drawPanelB);
        this.add(drawPanelC);
    }
    void addMotionAdapterA()
    {
        drawPanelA.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                drawPanelA.drawConstImage();
                drawPunctireSquare(drawPanelA.img, e.getX(),e.getY());
                drawPanelA.repaint();
            }
        });
    }

    public void setImageA(String filename)
    {
        File f = new File(filename);
        try {
            BufferedImage img = ImageIO.read(f);
            compressCoeffA = img.getWidth()/350.0;
            basicImage = img;
            System.out.println("Image: " + basicImage.getWidth() + " " + basicImage.getHeight());

            BufferedImage img2 = new BufferedImage( 350,350, BufferedImage.TYPE_3BYTE_BGR );
            Graphics2D g2 = img2.createGraphics();

            g2.drawImage(img, 0,0,350,350, null );
            g2.dispose();

            drawPanelA.setImg(img2);
        }
        catch (IOException ioex )
        {
            ioex.printStackTrace();
        }
    }
    public void drawPunctireSquare(BufferedImage img, int x, int y )
    {
        double squareWid = 350/compressCoeffA;
        Graphics2D g = img.createGraphics();
        g.setColor(Color.BLACK);
        int xWithShift = x;
        int yWithShift = y;
        if ( x - squareWid/2 < 0 )
            xWithShift = (int)Math.round(squareWid/2);
        if ( x + squareWid/2 >= 350 )
            xWithShift = (int)Math.round(350 - squareWid/2);
        if ( y - squareWid/2 < 0 )
            yWithShift = (int)Math.round(squareWid/2);
        if ( y + squareWid/2 >= 350 )
            yWithShift = (int)Math.round(350 - squareWid/2);

        g.drawLine((int)(xWithShift-squareWid/2), (int)(yWithShift-squareWid/2), (int)(xWithShift-squareWid/2), (int)(yWithShift+squareWid/2) );
        g.drawLine((int)(xWithShift-squareWid/2), (int)(yWithShift-squareWid/2), (int)(xWithShift+squareWid/2), (int)(yWithShift-squareWid/2) );
        g.drawLine((int)(xWithShift+squareWid/2), (int)(yWithShift+squareWid/2), (int)(xWithShift-squareWid/2), (int)(yWithShift+squareWid/2) );
        g.drawLine((int)(xWithShift+squareWid/2), (int)(yWithShift+squareWid/2), (int)(xWithShift+squareWid/2), (int)(yWithShift-squareWid/2) );

        drawPanelB.setImg(getImageAtSquare(xWithShift, yWithShift));

    }
    BufferedImage getImageAtSquare(int x, int y)
    {
        double squareWid = 350/compressCoeffA;
        Graphics2D g = basicImage.createGraphics();
        BufferedImage out = null;

        try {
            out = basicImage.getSubimage( (int)((x - 175/compressCoeffA)*compressCoeffA), (int)((y - 175/compressCoeffA)*compressCoeffA), 350, 350);
        }
        catch (Exception e)
        {
            System.out.println(compressCoeffA + " " + x + " " + y + " " +(int)((x - 175)*compressCoeffA)+ " " +(int)((y - 175)*compressCoeffA)+ " " + 350 + " " +350 ) ;
            e.printStackTrace();
        }
        return out;
    }

    private class MainPaneElement extends JPanel
    {
        DrawPanel drawPanel;
        JLabel label;

        public MainPaneElement()
        {
            this.drawPanel = new DrawPanel();
            label = new JLabel();
            label.setPreferredSize(new Dimension(15,350 ));

            this.setLayout(new BorderLayout());
        }

    }
}
