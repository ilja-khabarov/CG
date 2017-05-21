package ru.nsu.fit.g14205.Khabarov;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import static java.lang.Math.abs;

/**
 * Created by ilja on 03.04.2017.
 */
public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener {
    BufferedImage img;
    WritableRaster raster;
    int lineWidth = 2;
    int zmin, zmax;
    int amountOfLevels = 3;
    int[] basicColor = {240, 240, 240, 0}; //
    int[] blackColor = {0, 0, 0}; //
    int[] redColor = {250, 0, 0}; //
    int[] blueColor = {0, 0, 250}; //
    int gridHeight, gridWidth, gridRowHeight, gridColumnWidth;
    Stack<Span> spanStack = new Stack<>();
    int[][] colors = null;

    public DrawPanel() {
        super(new BorderLayout());
        //this.setAutoscrolls(true);
        this.setOpaque(true);
        //this.set;//new Color(240, 240, 240));
        img = new BufferedImage(600, 600, BufferedImage.TYPE_3BYTE_BGR);
        this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
        raster = img.getRaster();
        addMouseListener(this);
        addMouseMotionListener(this);

        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                if ( i == 0 || i == img.getHeight()-1 || j == 0 || j == img.getWidth()-1 )
                    raster.setPixel(j,i,blackColor);
                else
                    raster.setPixel(j, i, basicColor); // basically it's black. so let's bake it gray
            }
        }

        Graphics2D g2 = (Graphics2D) img.getGraphics();
        g2.setColor(Color.BLACK);
        //this.add(new StatusBar(),BorderLayout.SOUTH);
        this.add(new JLabel(), BorderLayout.SOUTH);
        int[] tempcolor = null;
        tempcolor = raster.getPixel(3,3,tempcolor);
        // SPAN FILLING TEMPLATE
        spanStack.push(new Span(300,100));
        while( spanFilling(tempcolor, Color.BLUE));
        spanStack.push(new Span(300,300));
        while( spanFilling(tempcolor, Color.RED));
        spanStack.push(new Span(1,1));
        while( spanFilling(tempcolor, Color.YELLOW));
        //*/

        this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
        this.repaint();
    }
    void resizeImage(int imgWid, int imgHei)
    {
        img = new BufferedImage(imgWid, imgHei, BufferedImage.TYPE_3BYTE_BGR );
        raster = img.getRaster();
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                if ( i == 0 || i == img.getHeight()-1 || j == 0 || j == img.getWidth()-1 )
                    raster.setPixel(j,i,blackColor);
                else
                    raster.setPixel(j, i, basicColor); // basically it's black. so let's bake it gray
            }
        }
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        //System.out.println("Clicked: " + e.getX() + " " + e.getY() );
    }

    public void mousePressed(MouseEvent e) {
        System.out.println("Presed: " + e.getX() + " " + e.getY() + " ");
    }

    public void mouseReleased(MouseEvent e) {
        //System.out.println("Clicked: " + e.getX() + " " + e.getY() );
    }
    public void mouseExited(MouseEvent e) {

    }

    public void mouseClicked(MouseEvent e) {
        //System.out.println("Clicked: " + e.getX() + " " + e.getY() );
    }
    @Override
    public void mouseMoved(MouseEvent e)
    {
    }
    @Override
    public void mouseDragged(MouseEvent e)
    {
    }

    void myLine(int x1, int y1, int x2, int y2, WritableRaster raster) { // this func realizes Bresenham's algorhytm
        if (lineWidth > 1) {
            Graphics g = img.getGraphics();
            Graphics2D graphics2D = (Graphics2D) g;
            BasicStroke stroke = new BasicStroke(lineWidth);
            graphics2D.setColor(Color.BLACK);
            graphics2D.setStroke(stroke);
            graphics2D.drawLine(x1, y1, x2, y2);

            return;
        }

        int x, y, dx, dy, incx, incy, pdx, pdy, es, el, err;
        int[] blackColor = {0, 0, 0, 0};

        dx = x2 - x1;//проекция на ось x
        dy = y2 - y1;//проекция на ось y

        incx = (int) Math.signum(dx);
	/*
	 * Определяем, в какую сторону нужно будет сдвигаться. Если dx < 0, т.е. отрезок идёт
	 * справа налево по иксу, то incx будет равен -1.
	 * Это будет использоваться в цикле постороения.
	 */
        incy = (int) Math.signum(dy);
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
        } else//случай, когда прямая скорее "высокая", чем длинная, т.е. вытянута по оси y
        {
            pdx = 0;
            pdy = incy;

            es = dx;
            el = dy;//тогда в цикле будем двигаться по y
        }

        x = x1;
        y = y1;
        err = el / 2;
        raster.setPixel(x, y, blackColor);
        //все последующие точки возможно надо сдвигать, поэтому первую ставим вне цикла

        for (int t = 0; t < el; t++)//идём по всем точкам, начиная со второй и до последней
        {
            err -= es;
            if (err < 0) {
                err += el;
                x += incx;//сдвинуть прямую (сместить вверх или вниз, если цикл проходит по иксам)
                y += incy;//или сместить влево-вправо, если цикл проходит по y
            } else {
                x += pdx;//продолжить тянуть прямую дальше, т.е. сдвинуть влево или вправо, если
                y += pdy;//цикл идёт по иксу; сдвинуть вверх или вниз, если по y
            }

            try {
                raster.setPixel(x, y, blackColor);
            } catch (ArrayIndexOutOfBoundsException iob) {
                System.out.println("Exception: " + x + " " + y);
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
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
    }

    int[] spanBorders(int x, int y) {
        int[] blackColor = {0, 0, 0};
        int[] gotColor = null;
        int[] borders = new int[2];
        int[] seedColor = null;
        seedColor = raster.getPixel(x, y, seedColor);
        for (int i = x + 1; ; i++) {
            gotColor = raster.getPixel(i, y, gotColor);
            if (seedColor[0] != gotColor[0] || seedColor[1] != gotColor[1] || seedColor[2] != gotColor[2]) {
                borders[1] = i - 1;
                break;
            }
        }
        for (int i = x - 1; ; i--) {
            try {
                gotColor = raster.getPixel(i, y, gotColor);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(e.toString() + i + " " + y);
                break;
            }
            if (blackColor[0] == gotColor[0] && blackColor[1] == gotColor[1] && blackColor[2] == gotColor[2]) {
                borders[0] = i + 1;
                break;
            }
        }
        return borders;
    }

    boolean spanFilling(int[] seedColor, Color fillColor)
    {
        int[] tempcolor = null;
        if ( !spanStack.isEmpty() )
        {
            Span current = spanStack.pop();
            current.fill(fillColor);
            Span inserting = null;
            for ( int i = current.xleft; i < current.xright; i++ ) // finding new spans
            {
                try {
                    tempcolor = raster.getPixel(i, current.y + 1, tempcolor); // finding spans below
                    if ( !compareColors(tempcolor, seedColor ))
                    {
                        continue;
                    }
                }
                catch ( ArrayIndexOutOfBoundsException aob )
                {
                    aob.printStackTrace();
                    break;
                }
                if (inserting == null) {
                    if (compareColors(seedColor, tempcolor)) // if color is like seed
                    {
                        inserting = new Span(i, current.y + 1);
                        continue;
                    }
                } else // inserting is not null
                {
                    if ( i <= inserting.xright && i >= inserting.xleft ) // if the same span
                    {
                        continue;
                    }
                    else // if it's a new span
                    {
                        spanStack.push(inserting); // push old and now the new king is the role model
                        inserting = new Span(i, current.y+1 );
                        continue;
                    }
                }
            }
            if ( inserting != null )
                spanStack.push(inserting);
            inserting = null;
            for ( int i = current.xleft; i < current.xright; i++ ) // finding new spans
            {
                try {
                    tempcolor = raster.getPixel(i, current.y - 1, tempcolor); // finding spans above
                    if ( !compareColors(tempcolor, seedColor ))
                    {
                        continue;
                    }
                }
                catch ( ArrayIndexOutOfBoundsException aob )
                {
                    aob.printStackTrace();
                    break;
                }
                if (inserting == null) {
                    if (compareColors(seedColor, tempcolor)) // if color is like seed
                    {
                        inserting = new Span(i, current.y - 1);
                        continue;
                    }
                } else // inserting is not null
                {
                    if ( i <= inserting.xright && i >= inserting.xleft ) // if the same span
                    {
                        continue;
                    }
                    else // if it's a new span
                    {
                        spanStack.push(inserting); // push old and now the new king is the role model
                        inserting = new Span(i, current.y-1 );
                        continue;
                    }
                }
            }
            if ( inserting != null )
                spanStack.push(inserting);
            return true;
        }
        else
            return false;
    }
    boolean compareColors( int[] color1, int[] color2 )
    {
        for ( int i = 0; i < 3; i++ )
        {
            if( color1[i] != color2[i] )
                return false;
        }
        return true;
    }

    private class Span
    {
        int xleft, xright, y;

        Span(int xl, int xr, int yin ) // straightforward constructor
        {
            xleft = xl; xright = xr; y = yin;
        }
        Span(int x, int y)
        { // finds and creates Span example
            this.y = y;
            int[] seedColor = null;
            int[] blackColor = {0, 0, 0};
            int[] gotColor = null;
            int[] borders = new int[2];
            seedColor = raster.getPixel(x, y, seedColor);

            for (int i = x + 1; ; i++) {
                gotColor = raster.getPixel(i, y, gotColor);
                if (!compareColors(seedColor, gotColor)) {
                    xright = i;
                    break;
                }
            }
            for (int i = x - 1; ; i--) {
                gotColor = raster.getPixel(i, y, gotColor);
                if (!compareColors(gotColor,seedColor)) {
                    xleft = i + 1;
                    break;
                }
            }
        }
        void fill( Color color)
        {
            int[] placeColor = {color.getRed(), color.getGreen(), color.getBlue()};
            for (int i = xleft; i < xright; i++) {
                raster.setPixel(i, y, placeColor);
            }
        }
    }

}
