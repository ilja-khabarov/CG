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
    StatusBar statusBar;
    FunctionModel func = new FunctionModel();
    int gridHeight, gridWidth, gridRowHeight, gridColumnWidth;
    Model model;
    Square[][] squares;
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
        model = new Model(-10.0,-10.0,10.0,10.0,img.getWidth(), img.getHeight());
        zmin = 0;
        zmax = (int)func.getz(10.0,10.0);
        initGrid(4,4);
        squares = new Square[4][4];
        for ( int i =0; i < 4; i++ )
        {
            for ( int j = 0; j < 4; j++ )
            {
                squares[i][j] = new Square();
            }
        }

        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                if ( i == 0 || i == img.getHeight()-1 || j == 0 || j == img.getWidth()-1 )
                    raster.setPixel(j,i,blackColor);
                else
                    raster.setPixel(j, i, basicColor); // basically it's black. so let's bake it gray
            }
        }


        //       myLine(0,0,100,300,raster);

        Graphics2D g2 = (Graphics2D) img.getGraphics();
        g2.setColor(Color.BLACK);
        //this.add(new StatusBar(),BorderLayout.SOUTH);
        this.add(new JLabel(), BorderLayout.SOUTH);
        //System.out.println("Test coords: " + model.getXCoords(200) + " " + model.getYCoords(0));
        //casualDraw();
        //bypass(4.0);
        //bypass(1.0);
        //drawIsoline(8.0);
        //drawIsoline(4.0);
        int[] tempcolor = null;
        tempcolor = raster.getPixel(3,3,tempcolor);
        /*
        spanStack.push(new Span(300,100));
        while( spanFilling(tempcolor, Color.BLUE));
        spanStack.push(new Span(300,300));
        while( spanFilling(tempcolor, Color.RED));
        spanStack.push(new Span(1,1));
        while( spanFilling(tempcolor, Color.YELLOW));
        */

        readFile(new File("resources/Data/data.txt"));
        this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
        this.repaint();
    }
    void initGrid(int x, int y )
    {
        gridHeight = y;
        gridWidth = x;

        gridRowHeight = img.getHeight()/y;
        gridColumnWidth = img.getWidth()/x;
    }
    public void setStatusBar( StatusBar sb )
    {
        statusBar = sb;
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
        if ( e != null )
         statusBar.setCoords(model.getXCoords(e.getX()) , model.getYCoords(e.getY()),
                 Math.round(100*func.getz(model.getXCoords(e.getX()), model.getYCoords(e.getY())))/100.0);
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

    void span(int x, int y, Color color) {
        int[] gotColor = null;
        int[] basicColor = null;
        int[] blackColor = {0, 0, 0};
        int[] placeColor = {color.getRed(), color.getGreen(), color.getBlue(), 0};
        int x1, y1;
        gotColor = raster.getPixel(x, y, gotColor);
        basicColor = raster.getPixel(x, y, basicColor);
        x1 = x;
        y1 = y;
        if (blackColor[0] == gotColor[0] && blackColor[1] == gotColor[1] && blackColor[2] == gotColor[2])
            return; // lets us ignore black borders
        while (true) {
            if (blackColor[0] == gotColor[0] && blackColor[1] == gotColor[1] && blackColor[2] == gotColor[2])
                break; // lets us ignore black borders
            if (gotColor[0] != basicColor[0] || gotColor[1] != basicColor[1] || gotColor[2] != basicColor[2])
                break; // paint only basic color
            int[] borders = spanBorders(x, y);
            if (y != y1)
                fillSpan(borders[0], borders[1] + 1, y, color);
            x = (borders[1] + borders[0]) / 2;
            y++;
            gotColor = raster.getPixel(x, y, gotColor);
        }
        x = x1;
        y = y1;
        gotColor = raster.getPixel(x, y, gotColor);
        while (true) {
            if (blackColor[0] == gotColor[0] && blackColor[1] == gotColor[1] && blackColor[2] == gotColor[2])
                break; // lets us ignore black borders
            if (gotColor[0] != basicColor[0] || gotColor[1] != basicColor[1] || gotColor[2] != basicColor[2])
                break;
            int[] borders = spanBorders(x, y);
            fillSpan(borders[0], borders[1] + 1, y, color);
            x = (borders[1] + borders[0]) / 2;
            y--;
            gotColor = raster.getPixel(x, y, gotColor);
        }
        //this.repaint();
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

    void fillSpan(int x1, int x2, int y, Color color) {
        int[] placeColor = {color.getRed(), color.getGreen(), color.getBlue(), 0};
        for (int i = x1; i < x2; i++) {
            raster.setPixel(i, y, placeColor);
        }
    }

    void casualDraw()
    {
        int i,j;
        int xcenter = img.getWidth()/2;
        int ycenter = img.getHeight()/2;
        int hei = img.getHeight();
        int wid = img.getWidth();

        for ( i = 0; i < hei; i++ )
        {
            for ( j = 0; j < wid; j++ )
            {
                if ( func.getz(model.getXCoords(j), model.getYCoords(i)) >= 7.9 &&
                        func.getz(model.getXCoords(j), model.getYCoords(i)) <= 8.1 )
                {
                    try {
                        raster.setPixel(j, i, blackColor);
                    }
                    catch (ArrayIndexOutOfBoundsException aob )
                    {
                        System.out.println(i + " " + j + " of " + hei + " " + wid );
                        aob.printStackTrace();
                    }
                }
                /*
                if ( Math.round(func.getz((j-xcenter)/10.0, (i-ycenter)/10.0)) > 7 )
                {
                    raster.setPixel(j, i, redColor);
                }
                if ( Math.round(func.getz((j-xcenter)/10.0, (i-ycenter)/10.0)) < 7 )
                {
                    raster.setPixel(j, i, blueColor);
                }
                */
            }
        }
    }

    void bypass(double izovalue)
    {
        double xtemp = 0.0, ytemp = 0.0;
        for ( int i = 0; i < gridHeight; i++ ) // i,j is idx of square
        {
            for ( int j = 0; j < gridWidth; j++ ) // at this moment we have a square
            {
                bypassSquare(i,j,izovalue);
            }
        }

    }
    void fillAll()
    {
        int[] tempcolor = null;
        int coloridx = 0;
        double[] higherBorder = new double[amountOfLevels+1];
        for ( int i = 0; i <= amountOfLevels; i++ )
        {
            higherBorder[i] = (i+1) * (zmax-zmin)/(amountOfLevels+1);
        }
        for ( int i = 1; i < img.getHeight()-1; i++ )
        {
            for ( int j = 1; j < img.getWidth()-1; j++ )
            {
                tempcolor = raster.getPixel(j,i,tempcolor);
                if ( compareColors(tempcolor, basicColor ))
                {
                    double tempz = func.getz(model.getXCoords(j), model.getYCoords(i));
                    System.out.println("Filling value " + j + " " + i + " " + tempz );
                    for ( int k = 0; k <= amountOfLevels; k++ )
                    {
                        if ( tempz > higherBorder[k] )
                        {
                            continue;
                        }
                        else
                        {
                            coloridx = k;
                            break;
                        }
                    }
                    spanStack.push(new Span(j,i));
                    while(spanFilling(tempcolor, new Color(colors[coloridx][0],
                            colors[coloridx][1], colors[coloridx][2])));
                }
            }
        }
        this.repaint();
    }
    void bypassSquare(int i, int j, double izovalue) {
        double xtemp = 0.0, ytemp = 0.0;
        for (int k = j * gridColumnWidth; k < (j + 1) * gridColumnWidth; k++)
        { // here we do bypass a square's higher border
            horizontalLineOfSquareAnalysis(k, i, j, true, izovalue);
        }
        int shift = 0;
        if ( j == gridWidth )
            shift = -1;
        for ( int k = j*gridColumnWidth; k < (j+1)*gridColumnWidth+shift; k++ )
        {
            horizontalLineOfSquareAnalysis(k, i+1, j, false, izovalue);
        }

        for ( int k = i*gridRowHeight; k < (i+1)*gridRowHeight; k++ )
        { // here we go by left vertical line
            verticalLineOfSquareAnalysis(k, i, j, true, izovalue);
        }
        for ( int k = i*gridRowHeight; k < (i+1)*gridRowHeight; k++ )
        { // here we go by left vertical line
            verticalLineOfSquareAnalysis(k, i, j+1, false, izovalue);
        }
    }
    void horizontalLineOfSquareAnalysis(int k, int i, int j, boolean top, double izovalue )
    { // i,j = square coords, k - x coord of pixel

        int shift = 0;
        if ( i == gridHeight )
            shift = -1;
        int ishift = 0;
        if ( !top )
            ishift = -1;
        int[] gotColor = null;
        double xtemp = 0.0, ytemp = 0.0;
        try {
            //raster.setPixel(k, i * gridRowHeight+shift, blackColor);
            xtemp = model.getXCoords(k);
            ytemp = model.getYCoords(i * gridRowHeight);

            if (func.getz(model.getXCoords(k), model.getYCoords(i * gridRowHeight)) >= izovalue-0.1 &&
                    func.getz(model.getXCoords(k), model.getYCoords(i * gridRowHeight)) <= izovalue+0.1) {
                //raster.setPixel(k, i * gridRowHeight+shift, redColor);
                gotColor = raster.getPixel(k, i*gridRowHeight+shift, gotColor );
                if ( !(gotColor[0] == redColor[0] &&
                        gotColor[1] == redColor[1] &&
                        gotColor[2] == redColor[2] )) {
                    //drawPoint(k, i * gridRowHeight + shift, redColor);
                    squares[i+ishift][j].addValues(k, i*gridRowHeight+shift);
                    //squares[i][j].addValues(k, i*gridRowHeight+shift);

                }
            }
        } catch (ArrayIndexOutOfBoundsException aob) {
            System.out.println(i + " " + j + " coords: " + k + " " + i * gridRowHeight + " real coords are" +
                    xtemp + " " + ytemp);
            aob.printStackTrace();
        }
    }
    void verticalLineOfSquareAnalysis(int k, int i, int j, boolean left, double izovalue )
    {
        int shift = 0;
        if ( j == gridWidth )
            shift = -1;
        int jshift = 0;
        if ( !left )
            jshift = -1;
        int[] gotColor = null;
        double xtemp = 0.0, ytemp = 0.0;
        final int thisX = j*gridColumnWidth+shift;
        try {
            //raster.setPixel( j*gridColumnWidth+shift, k, blackColor);
            xtemp = model.getXCoords(j*gridColumnWidth);
            ytemp = model.getYCoords(k);

            if (func.getz(model.getXCoords(thisX), model.getYCoords(k)) >= izovalue-0.1 &&
                    func.getz(model.getXCoords(thisX), model.getYCoords(k)) <= izovalue+0.1) {
                //raster.setPixel(thisX, k, redColor);
                gotColor = raster.getPixel(thisX, k , gotColor );
                if ( !(gotColor[0] == redColor[0] &&
                        gotColor[1] == redColor[1] &&
                        gotColor[2] == redColor[2] )) {
                    //drawPoint(thisX, k, redColor);
                    squares[i][j+jshift].addValues(thisX, k);
                    //squares[i][j].addValues(thisX, k);
                }
                //drawPoint(thisX, k, redColor);
            }
        } catch (ArrayIndexOutOfBoundsException aob) {
            System.out.println( i + " " + j + " coords: " + thisX + " " + k + " real coords are" +
                    xtemp + " " + ytemp);
            aob.printStackTrace();
        }
    }
    void drawPoint(int xpix, int ypix, int[] color)
    {
        try {
            raster.setPixel(xpix, ypix, color);
            raster.setPixel(xpix - 1, ypix, color);
            raster.setPixel(xpix + 1, ypix, color);
            raster.setPixel(xpix, ypix + 1, color);
            raster.setPixel(xpix - 1, ypix + 1, color);
            raster.setPixel(xpix + 1, ypix + 1, color);
            raster.setPixel(xpix + 1, ypix + 1, color);
            raster.setPixel(xpix - 1, ypix - 1, color);
            raster.setPixel(xpix + 1, ypix - 1, color);
            raster.setPixel(xpix + 1, ypix - 1, color);
        }
        catch ( ArrayIndexOutOfBoundsException aob )
        {}
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

    void updateModel(double xm,double ym, double xma, double yma, int wid, int hei  )
    {
        model = new Model( xm, ym, xma, yma, img.getWidth(), img.getHeight() );
    }
    private class Model
    {
        double xmin, xmax, ymin, ymax;
        int imgWid, imgHei;
        double xcent, ycent;
        int xPixCent, yPixCent;
        double coordWid, coordHei;
        double ex, ey;

        Model(double xm, double ym, double xma, double yma, int wid, int hei )
        {
            xmin = xm; ymin = ym; xmax = xma; ymax = yma; imgWid = wid; imgHei = hei;
            coordWid = xmax-xmin;
            coordHei = ymax-ymin;
            xcent = coordWid/2;
            ycent = coordHei/2;
            xPixCent = imgWid /2;
            yPixCent = imgHei /2;
            ex = imgWid/coordWid; // amount of pixels in coordinatic unit
            ey = imgHei/coordHei;
        }
        double getXCoords(int x) // returns real coordinates of x pixel
        {
            return xmin + x/ex;
        }
        double getYCoords(int y)
        {
            return ymin + y/ey;
        }
        int getPixByCoordX(double x)
        {
            System.out.println("Getting xcoord: " + x + " " + xmin + " " + ex );
            return (int)Math.round((x-xmin)*ex);
        }
        int getPixByCoordY(double y)
        {
            return (int)Math.round((y-ymin)*ey);
        }

    }
    public class Square
    {
        ArrayList<Point> isolineEntries;

        Square()
        {
            isolineEntries = new ArrayList<Point>(4);
        }


        void addValues( int x, int y )
        {
            Point toInsert = new Point(x,y);
            for ( int i = 0; i < isolineEntries.size(); i++ )
            {
                Point tempPoint;
                tempPoint = isolineEntries.get(i);
                if ( Math.abs(tempPoint.getX() - x) + Math.abs(tempPoint.getY() - y) < 8 )
                    return; // if this point is already in
            }
            isolineEntries.add(toInsert);
        }
        int getSize()
        {
            return isolineEntries.size();
        }
        Point readValue(int idx)
        {
            return isolineEntries.get(idx);
        }
        void printSquarePoints()
        {
            Point temp;
            for ( int i = 0; i < isolineEntries.size(); i++ )
            {
                temp = isolineEntries.get(i);
                System.out.print(temp.getX() + " " + temp.getY()+ " " );
            }
        }
    }
    void drawIsoline(double isovalue)
    {
        int x1,x2,y1,y2;
        bypass(isovalue);

        Graphics g = img.getGraphics();
        Graphics2D graphics2D = (Graphics2D) g;
        BasicStroke stroke = new BasicStroke(1);
        graphics2D.setColor(Color.GREEN);
        graphics2D.setStroke(stroke);
        for ( int i = 0; i < gridHeight; i++ )
        {
            for ( int j = 0; j < gridWidth; j++ )
            {
                if ( squares[i][j].getSize() < 2 )
                    continue;
                x1 = (int)Math.round(squares[i][j].readValue(0).getX());
                y1 = (int)Math.round(squares[i][j].readValue(0).getY());
                x2 = (int)Math.round(squares[i][j].readValue(1).getX());
                y2 = (int)Math.round(squares[i][j].readValue(1).getY());
                graphics2D.drawLine(x1,y1,x2,y2);
                System.out.println("Drawing line btween " + x1 + " " + y1+ " " + x2+ " " + y2);
            }
        }
        for ( int i =0; i < 4; i++ )
        {
            for ( int j = 0; j < 4; j++ )
            {
                squares[i][j] = new Square();
            }
        }
    }

    void readFile(File file)
    {
        try
        {
            FileReader fr = new FileReader(file);
            BufferedReader br  = new BufferedReader(fr);
            String[] netValues;
            String temp;
            String[] stringColors;

            temp = br.readLine();
            netValues = temp.split(" " );
            initGrid(Integer.parseInt(netValues[0]), Integer.parseInt(netValues[1]));
            temp = br.readLine();
            amountOfLevels = Integer.parseInt(temp);

            for (int i = 0; i < img.getHeight(); i++) {
                for (int j = 0; j < img.getWidth(); j++) {
                    if ( i == 0 || i == img.getHeight()-1 || j == 0 || j == img.getWidth()-1 )
                        raster.setPixel(j,i,blackColor);
                    else
                        raster.setPixel(j, i, basicColor); // basically it's black. so let's bake it gray
                }
            }
            for ( int i = 1; i <= amountOfLevels; i++ )
            {
                drawIsoline(i*(zmax-zmin)/(amountOfLevels));
            }

            colors = new int[amountOfLevels+1][3];
            for ( int i = 0; i <= amountOfLevels; i++ )
            {
                temp = br.readLine();
                stringColors = temp.split(" ");

                colors[i][0] = Integer.parseInt(stringColors[0]);
                colors[i][1] = Integer.parseInt(stringColors[1]);
                colors[i][2] = Integer.parseInt(stringColors[2]);
            }

            fillAll();
            this.repaint();

        }
        catch( IOException ioex )
        {
            ioex.printStackTrace();
        }
    }

}
