package ru.nsu.fit.g14205.Khabarov;

import org.w3c.dom.css.RGBColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;

import static java.lang.Math.abs;
import static java.lang.Math.signum;

/**
 * Created by ilja on 15.02.2017.
 */
public class DrawPanel extends JPanel implements MouseListener {
    BufferedImage img;
    BufferedImage constimg;
    WritableRaster raster;
    int lineWidth = 1;
    int[] basicColor = { 255, 255, 255, 0 }; //
    boolean impactMode = false;

    public DrawPanel()
    {
        super(new BorderLayout());
        //this.setAutoscrolls(true);
        this.setOpaque(true);
        img = new BufferedImage( 350,350, BufferedImage.TYPE_3BYTE_BGR );
        this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
        raster = img.getRaster();
        addMouseListener(this);

        nullifyImage();
 //       myLine(0,0,100,300,raster);
        Graphics2D g2 = (Graphics2D)img.getGraphics();
        g2.setColor(Color.BLACK);
        //g2.drawString("2.0", angle_h+ 5*2*angle_w + (2%2)*(angle_w),angle_h+vertical/2 + 5 + 2*(angle_h+vertical) );
        //drawHexagon(1*2*angle_w + 1*(angle_w), 1*(angle_h+vertical) + angle_h, raster );
        this.repaint();

    }
    void nullifyImage()
    {
        for ( int i = 0; i < img.getHeight(); i++ )
        {
            for ( int j = 0; j < img.getWidth(); j++ )
            {
                raster.setPixel(j, i, basicColor ); // basically it's black. so let's bake it gray
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e )
    {
        //System.out.println("Clicked: " + e.getX() + " " + e.getY() );
    }
    @Override
    public void mouseEntered(MouseEvent e )
    {
        //System.out.println("Clicked: " + e.getX() + " " + e.getY() );
    }
    public void mousePressed(MouseEvent e )
    {
        System.out.println("Presed: " + e.getX() + " " + e.getY() + " "  );
    }
    public void mouseReleased(MouseEvent e )
    {
        //System.out.println("Clicked: " + e.getX() + " " + e.getY() );
        this.revalidate();
        this.repaint();
    }
    public void mouseClicked(MouseEvent e )
    {
    }

    void myLine(int x1, int y1, int x2, int y2, WritableRaster raster )
    { // this func realizes Bresenham's algorhytm
        if ( lineWidth > 1 )
        {
            Graphics g = img.getGraphics();
            Graphics2D graphics2D = (Graphics2D) g;
            BasicStroke stroke = new BasicStroke(lineWidth);
            graphics2D.setColor(Color.BLACK);
            graphics2D.setStroke(stroke);
            graphics2D.drawLine(x1,y1,x2,y2);

            return;
        }

        int x, y, dx, dy, incx, incy, pdx, pdy, es, el, err;
        int[] blackColor  = { 0, 0, 0, 0 };

        dx = x2 - x1;//проекция на ось x
        dy = y2 - y1;//проекция на ось y

        incx = (int)Math.signum(dx);
	/*
	 * Определяем, в какую сторону нужно будет сдвигаться. Если dx < 0, т.е. отрезок идёт
	 * справа налево по иксу, то incx будет равен -1.
	 * Это будет использоваться в цикле постороения.
	 */
        incy = (int)Math.signum(dy);
	/*
	 * Аналогично. Если рисуем отрезок снизу вверх -
	 * это будет отрицательный сдвиг для y (иначе - положительный).
	 */

        if (dx < 0) dx = -dx;//далее мы будем сравнивать: "if (dx < dy)"
        if (dy < 0) dy = -dy;//поэтому необходимо сделать dx = |dx|; dy = |dy|
        //эти две строчки можно записать и так: dx = Math.abs(dx); dy = Math.abs(dy);

        if (dx > dy)
        //определяем наклон отрезка:
        {
	 /*
	  * Если dx > dy, то значит отрезок "вытянут" вдоль оси икс, т.е. он скорее длинный, чем высокий.
	  * Значит в цикле нужно будет идти по икс (строчка el = dx;), значит "протягивать" прямую по иксу
	  * надо в соответствии с тем, слева направо и справа налево она идёт (pdx = incx;), при этом
	  * по y сдвиг такой отсутствует.
	  */
            pdx = incx;
            pdy = 0;

            es = dy;
            el = dx;
        }
        else//случай, когда прямая скорее "высокая", чем длинная, т.е. вытянута по оси y
        {
            pdx = 0;
            pdy = incy;

            es = dx;
            el = dy;//тогда в цикле будем двигаться по y
        }

        x = x1;
        y = y1;
        err = el/2;
        raster.setPixel(x,y,blackColor);
        //все последующие точки возможно надо сдвигать, поэтому первую ставим вне цикла

        for (int t = 0; t < el; t++)//идём по всем точкам, начиная со второй и до последней
        {
            err -= es;
            if (err < 0)
            {
                err += el;
                x += incx;//сдвинуть прямую (сместить вверх или вниз, если цикл проходит по иксам)
                y += incy;//или сместить влево-вправо, если цикл проходит по y
            }
            else
            {
                x += pdx;//продолжить тянуть прямую дальше, т.е. сдвинуть влево или вправо, если
                y += pdy;//цикл идёт по иксу; сдвинуть вверх или вниз, если по y
            }

            try {
                raster.setPixel(x, y, blackColor);
            }
            catch ( ArrayIndexOutOfBoundsException iob )
            {
                System.out.println("Exception: " + x + " " + y );
                iob.printStackTrace();
            }
        }

        /*
        int maxlen;
        int[] blackColor  = { 0, 0, 0, 0 };


        int deltax = (x2-x1);
        int signx = (int) Math.signum(deltax);
        int deltay = (y2-y1);
        int signy = (int) Math.signum(deltay);

        if (deltax == 0)
        {
            if ( y1 < y2 ) {
                for (int i = y1; i < y2; i++)
                    raster.setPixel(x1, i, blackColor );
            }
            else
            {
                for (int i = y2; i < y1; i++)
                    raster.setPixel(x1, i, blackColor );
            }
            return;
        }
        if ( signx*deltax > signy*deltay )
        {
            maxlen = deltax;
        }

        int error = 0;
        int deltaerr = deltay;
        int y = y1;
        for ( int i = 0; i < maxlen; i++ )
        {
            raster.setPixel(i, y, blackColor);
        }
        */
    }

    @Override
    public void paint(Graphics g )
    {
        super.paint(g);
        g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null );
    }
    void span(int x, int y, Color color )
    {
        int[] gotColor = null;
        int[] basicColor = null;
        int[] blackColor = {0,0,0};
        int[] placeColor = {color.getRed(), color.getGreen(), color.getBlue(), 0};
        int x1, y1;
        gotColor = raster.getPixel(x, y, gotColor) ;
        basicColor = raster.getPixel(x, y, basicColor) ;
        x1 = x; y1 = y;
        if ( blackColor[0] == gotColor[0] && blackColor[1] == gotColor[1] && blackColor[2] == gotColor[2] )
            return; // lets us ignore black borders
        while ( true )
        {
            if ( blackColor[0] == gotColor[0] && blackColor[1] == gotColor[1] && blackColor[2] == gotColor[2] )
                break; // lets us ignore black borders
            if ( gotColor[0] != basicColor[0] || gotColor[1] != basicColor[1] ||gotColor[2] != basicColor[2] )
                break; // paint only basic color
            int[] borders = spanBorders(x,y);
            if ( y != y1 )
                fillSpan(borders[0], borders[1]+1, y, color);
            x = (borders[1]+borders[0])/2;
            y++;
            gotColor = raster.getPixel(x, y, gotColor) ;
        }
        x = x1; y = y1;
        gotColor = raster.getPixel(x, y, gotColor) ;
        while ( true )
        {
            if ( blackColor[0] == gotColor[0] && blackColor[1] == gotColor[1] && blackColor[2] == gotColor[2] )
                break; // lets us ignore black borders
            if ( gotColor[0] != basicColor[0] ||gotColor[1] != basicColor[1] ||gotColor[2] != basicColor[2] )
                break;
            int[] borders = spanBorders(x,y);
            fillSpan(borders[0], borders[1]+1, y, color);
            x = (borders[1]+borders[0])/2;
            y--;
            gotColor = raster.getPixel(x, y, gotColor) ;
        }
        //this.repaint();
    }
    int[] spanBorders(int x, int y)
    {
        int[] blackColor = {0,0,0};
        int[] gotColor = null;
        int[] borders = new int[2];
        for ( int i = x+1; ; i++ )
        {
            gotColor = raster.getPixel(i, y, gotColor) ;
            if ( blackColor[0] == gotColor[0] && blackColor[1] == gotColor[1] && blackColor[2] == gotColor[2] )
            {
                borders[1] = i-1;
                break;
            }
        }
        for ( int i = x-1; ; i-- )
        {
            try {
                gotColor = raster.getPixel(i, y, gotColor);
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                System.out.println(e.toString() + i + " " + y);
                break;
            }
            if ( blackColor[0] == gotColor[0] && blackColor[1] == gotColor[1] && blackColor[2] == gotColor[2] )
            {
                borders[0] = i+1;
                break;
            }
        }
        return borders;
    }
    void fillSpan(int x1, int x2, int y, Color color )
    {
        int[] placeColor = {color.getRed(), color.getGreen(), color.getBlue(), 0};
        for ( int i = x1; i < x2; i++ )
        {
            raster.setPixel(i, y, placeColor);
        }
    }
    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
    void drawConstImage()
    {
        img = deepCopy(constimg);
        this.repaint();
    }
    void setImg(BufferedImage image)
    {
        this.img = image;
        constimg = deepCopy(image);
        this.repaint();
    }

}
