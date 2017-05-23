package ru.nsu.fit.g14205.Khabarov;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
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

        setImageA("Data/sea.jpg");
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
            if ( img.getWidth() >= img.getHeight() )
                compressCoeffA = img.getWidth()/350.0;
            else
                compressCoeffA = img.getHeight()/350.0;

            basicImage = img;
            System.out.println("Image: " + basicImage.getWidth() + " " + basicImage.getHeight());

            BufferedImage img2 = new BufferedImage( (int)(img.getWidth()/compressCoeffA),(int)(img.getHeight()/compressCoeffA), BufferedImage.TYPE_3BYTE_BGR );
            Graphics2D g2 = img2.createGraphics();

            g2.drawImage(img, 0,0,img2.getWidth(),img2.getHeight(), null );
            g2.dispose();

            drawPanelA.setImg(img2);
        }
        catch (IOException ioex )
        {
            System.out.println(ioex.toString());
        }
    }
    public void setImageA(File f)
    {
        try {
            BufferedImage img = ImageIO.read(f);
            if ( img.getWidth() >= img.getHeight() )
                compressCoeffA = img.getWidth()/350.0;
            else
                compressCoeffA = img.getHeight()/350.0;

            basicImage = img;

            BufferedImage img2 = new BufferedImage( (int)(img.getWidth()/compressCoeffA),(int)(img.getHeight()/compressCoeffA), BufferedImage.TYPE_3BYTE_BGR );
            Graphics2D g2 = img2.createGraphics();

            g2.drawImage(img, 0,0,img2.getWidth(),img2.getHeight(), null );
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
        double squareWid = drawPanelA.img.getWidth()/compressCoeffA;
        double squareHei = drawPanelA.img.getHeight()/compressCoeffA;
        Graphics2D g = img.createGraphics();
        g.setColor(Color.BLACK);
        if ( squareHei > squareWid )
            squareWid = squareHei;
        else
            squareHei = squareWid;
        // after this moment the square is square
        int xWithShift = x;
        int yWithShift = y;
        if ( x - squareWid/2 < 0 )
            xWithShift = (int)Math.round(squareWid/2);
        if ( x + squareWid/2 >= drawPanelA.img.getWidth() )
            xWithShift = (int)Math.round(drawPanelA.img.getWidth() - squareWid/2);
        if ( y - squareHei/2 < 0 )
            yWithShift = (int)Math.round(squareHei/2);
        if ( y + squareHei/2 >= drawPanelA.img.getHeight() )
            yWithShift = (int)Math.round(drawPanelA.img.getHeight() - squareHei/2);

        //System.out.println("SquareHei is " + squareHei + " " + squareWid + "\n" +
        //    "image params are" + drawPanelA.img.getWidth() +" "+ drawPanelA.img.getHeight());

        WritableRaster writableRaster = img.getRaster();

        drawHorizontalPunctire((int)(xWithShift-squareWid/2),(int)(xWithShift+squareWid/2),(int)(yWithShift-squareWid/2), writableRaster);
        drawHorizontalPunctire((int)(xWithShift-squareWid/2),(int)(xWithShift+squareWid/2),(int)(yWithShift+squareWid/2), writableRaster);
        drawVerticalPunctire((int)(xWithShift-squareWid/2),(int)(yWithShift-squareWid/2),(int)(yWithShift+squareWid/2), writableRaster );
        drawVerticalPunctire((int)(xWithShift+squareWid/2),(int)(yWithShift-squareWid/2),(int)(yWithShift+squareWid/2), writableRaster );


/*
        g.drawLine((int)(xWithShift-squareWid/2), (int)(yWithShift-squareWid/2), (int)(xWithShift-squareWid/2), (int)(yWithShift+squareWid/2) );
        g.drawLine((int)(xWithShift-squareWid/2), (int)(yWithShift-squareWid/2), (int)(xWithShift+squareWid/2), (int)(yWithShift-squareWid/2) );
        g.drawLine((int)(xWithShift+squareWid/2), (int)(yWithShift+squareWid/2), (int)(xWithShift-squareWid/2), (int)(yWithShift+squareWid/2) );
        g.drawLine((int)(xWithShift+squareWid/2), (int)(yWithShift+squareWid/2), (int)(xWithShift+squareWid/2), (int)(yWithShift-squareWid/2) );
*/

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
    void drawHorizontalPunctire(int x1, int x2, int y, WritableRaster raster )
    {
        int[] blackColor = {0,0,0};
        for ( int i = x1; i <= x2; i++ )
        {
            if ( (i-x1)%6 < 3 )
                raster.setPixel(i,y,blackColor);
        }
    }
    void drawVerticalPunctire(int x, int y1, int y2, WritableRaster raster )
    {
        int[] blackColor = {0,0,0};
        for ( int i = y1; i <= y2; i++ )
        {
            if ( (i-y1)%6 < 3 )
                raster.setPixel(x,i,blackColor);
        }
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
