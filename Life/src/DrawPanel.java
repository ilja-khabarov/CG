import org.w3c.dom.css.RGBColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.Stack;

import static java.lang.Math.abs;
import static java.lang.Math.signum;

/**
 * Created by ilja on 15.02.2017.
 */
public class DrawPanel extends JPanel implements MouseListener {
    BufferedImage img;
    WritableRaster raster;

    double hexScale = 1.0;
    int fieldWidth = 25;
    int fieldHeigth = 20;
    boolean xormode;
    int lineWidth = 1;
    int[] currHex = {8000000,8000000};
    int vertical = (int) (16*hexScale);
    int angle_h = (int)(7*hexScale);
    int angle_w = (int)(14*hexScale);
    Timer timer;
    LifeModel lifeModel;
    boolean isFieldActive = true;
    int[] basicColor = { 255, 255, 255, 0 }; //
    boolean impactMode = false;
    Stack<Span> spanStack = new Stack<>();

    public DrawPanel()
    {
        super(new BorderLayout());
        xormode = false;
        //this.setAutoscrolls(true);
        this.setOpaque(true);
        img = new BufferedImage( (int)(28*fieldWidth*hexScale)+1,(int)(23*(fieldHeigth+1)*hexScale), BufferedImage.TYPE_3BYTE_BGR );
        this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
        raster = img.getRaster();
        addMouseListener(this);

        initTimer();
        for ( int i = 0; i < img.getHeight(); i++ )
        {
            for ( int j = 0; j < img.getWidth(); j++ )
            {
                raster.setPixel(j, i, basicColor ); // basically it's black. so let's bake it gray
            }
        }
 //       myLine(0,0,100,300,raster);
        drawField(fieldWidth,fieldHeigth,raster );
        //myLine(300,100,0,200,raster );
        lifeModel = new LifeModel(fieldWidth,fieldHeigth);

        Graphics2D g2 = (Graphics2D)img.getGraphics();
        g2.setColor(Color.BLACK);
        //g2.drawString("2.0", angle_h+ 5*2*angle_w + (2%2)*(angle_w),angle_h+vertical/2 + 5 + 2*(angle_h+vertical) );
        //drawHexagon(1*2*angle_w + 1*(angle_w), 1*(angle_h+vertical) + angle_h, raster );
        this.repaint();

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isActive()) {
                    int[] clrbuff = null;
                    int[] checkColor = null;
                    int x = e.getX();
                    int y = e.getY();
                    //Integer[] currHex = {8000000,8000000}; // to provide large distance
                    try {
                        if (!hexChecker(x, y)) {
                            super.mouseDragged(e);
                            return;
                        }
                        clrbuff = raster.getPixel(e.getX(), e.getY(), clrbuff);
                        {
                            for (int i = y; i < raster.getHeight() + 1; i++) {
                                checkColor = raster.getPixel(x, i, checkColor);
                                if (checkColor[0] == 0 && checkColor[1] == 0 && checkColor[2] == 0)
                                    break;
                            }
                            for (int i = y; i < raster.getHeight() + 1; i--) {
                                checkColor = raster.getPixel(x, i, checkColor);
                                if (checkColor[0] == 0 && checkColor[1] == 0 && checkColor[2] == 0)
                                    break;
                            }
                            if (xormode) {
                                if (xormode && clrbuff[0] == 255 && clrbuff[1] == 255 && clrbuff[2] == 255)
                                    span(e.getX(), e.getY(), Color.GREEN);
                                if (xormode && clrbuff[0] == 0 && clrbuff[1] == 255 && clrbuff[2] == 0)
                                    span(e.getX(), e.getY(), Color.WHITE);
                            }
                            if (!xormode)
                                span(e.getX(), e.getY(), Color.GREEN);
                        }
                    } catch (ArrayIndexOutOfBoundsException oob) {
                        //oob.printStackTrace();
                        return;
                    }
                    repaint();

                    super.mouseDragged(e);
                }
            }
        });
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

    void initTimer()
    {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*initModel();
                lifeModel.bypass();
                drawFieldFromModel();*/
                if ( !lifeModel.isInitiated() )
                    initModel();
                lifeModel.bypass();
                if ( impactMode ) {
                    nullifyImage();
                    drawFieldFromModel();
                    metricPrinter();
                }
                drawFieldFromModel();
            }
        });
    }
    void metricPrinter()
    {
        lifeModel.metricPrinter((Graphics2D)(img.getGraphics()), angle_h, angle_w, vertical );
        this.repaint();
    }
    void start()
    {
        timer.start();
        this.setInactive();
    }
    void stop()
    {
        timer.stop();
        this.setActive();
    }
    void initModel()
    {
        lifeModel.setInitiated(true);
        int[] redColor  = { 255, 0, 0, 0 };
        int[] color = null;
        for ( int i = 0; i < fieldHeigth; i++ )
        {
            for ( int j = 0 ; j < fieldWidth-(i%2); j++ )
            {
                try {
                    //raster.setPixel(angle_w + (2*angle_w)*j + (i%2)*angle_w, angle_h + (vertical) + (vertical+angle_h)*i , redColor);
                    color = raster.getPixel(angle_w + (2*angle_w)*j + (i%2)*angle_w, angle_h + (vertical) + (vertical+angle_h)*i , color );
                    if ( color[0] == 0 && color[1] == 255 && color[2] == 0 ) // if hex is green
                    {
                        lifeModel.field[i][j] = 1;
                    }
                } catch (ArrayIndexOutOfBoundsException aob )
                {
                    aob.printStackTrace();
                }
            }
        }
    }
    boolean isActive()
    {
        return isFieldActive;
    }
    void setActive()
    {
        isFieldActive = true;
    }
    void setInactive()
    {
        isFieldActive = false;
    }
    void drawFieldFromModel()
    {
        printModel();
        //drawField(fieldWidth, fieldHeigth, raster);
        for ( int i = 0; i < fieldHeigth; i++ )
        {
            for (int j = 0; j < fieldWidth - (i % 2); j++)
            {
                if ( lifeModel.field[i][j] == 1 )
                {
                    span(angle_w + (2*angle_w)*j + (i%2)*angle_w, angle_h + (vertical) + (vertical+angle_h)*i, Color.GREEN );
                }
                else
                {
                    span(angle_w + (2*angle_w)*j + (i%2)*angle_w, angle_h + (vertical) + (vertical+angle_h)*i, Color.WHITE );
                }
            }
        }
        this.repaint();
    }
    void printModel()
    {
        for ( int i = 0; i < fieldHeigth; i++ )
        {
            for (int j = 0; j < fieldWidth - (i % 2); j++)
            {
                System.out.print(lifeModel.field[i][j]);
            }
            System.out.println();
        }
    }

    void setXormode(boolean mode)
    {
        this.xormode = mode;
    }
    void setLineWidth(int width)
    {
        this.lineWidth = width;
    }
    boolean hexChecker( int x, int y)
    { // returns true when we move to another hex
        int i = 0;
        int[] colorBuff = null;
        int left, upper, right, lower,xcenter, ycenter, difference;
        colorBuff = raster.getPixel(x,y,colorBuff);
        if ( colorBuff[0] == 0 && colorBuff[1] == 0 && colorBuff[2] == 0 )
            return false; // we think, it's the same hex
        else
        { // looking for the hex's center
            for ( i = x; ; i++ )
            {
                colorBuff = raster.getPixel(i,y,colorBuff);
                if ( colorBuff[0] == 0 && colorBuff[1] == 0 && colorBuff[2] == 0 ) {
                    right = i;
                    break;
                }
            }
            for ( i = x; ; i-- )
            {
                colorBuff = raster.getPixel(i,y,colorBuff);
                if ( colorBuff[0] == 0 && colorBuff[1] == 0 && colorBuff[2] == 0) {
                    left = i;
                    break;
                }
            }
            for ( i = y; ; i-- )
            {
                colorBuff = raster.getPixel(x,i,colorBuff);
                if ( colorBuff[0] == 0 && colorBuff[1] == 0 && colorBuff[2] == 0) {
                    upper = i;
                    break;
                }
            }
            for ( i = y; ; i++ )
            {
                colorBuff = raster.getPixel(x,i,colorBuff);
                if ( colorBuff[0] == 0 && colorBuff[1] == 0 && colorBuff[2] == 0) {
                    lower = i;
                    break;
                }
            }
            xcenter = (left+right)/2;
            ycenter = (lower+upper)/2; // got the center or current hexagon
            if ( abs(xcenter - currHex[0]) > 7 || abs(ycenter - currHex[1]) > 7 )
            {
                //System.out.print("Changed " + xcenter + " " +  currHex[0] );
                //System.out.println( " " + ycenter + " " +  currHex[1] );
                currHex[0] = xcenter;
                currHex[1] = ycenter;
                return true;
            }
            else
                return false;
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
        //System.out.println("Clicked: " + e.getX() + " " + e.getY() );
        if ( isActive() ) {
            int x = e.getX();
            int y = e.getY();

            int[] clrbuff = null;
            int[] checkColor = null;
            clrbuff = raster.getPixel(e.getX(), e.getY(), clrbuff);
            try {
                for (int i = y; i < raster.getHeight() + 1; i++) {
                    checkColor = raster.getPixel(x, i, checkColor);
                    if (checkColor[0] == 0 && checkColor[1] == 0 && checkColor[2] == 0)
                        break;
                }
                for (int i = y; i < raster.getHeight() + 1; i--) {
                    checkColor = raster.getPixel(x, i, checkColor);
                    if (checkColor[0] == 0 && checkColor[1] == 0 && checkColor[2] == 0)
                        break;
                }
                if (xormode) {
                    System.out.println(xormode);
                    if ((clrbuff[0] == 255) && (clrbuff[1] == 255) && (clrbuff[2] == 255)) {
                        span(e.getX(), e.getY(), Color.GREEN);
                    }
                    if (clrbuff[0] == 0 && clrbuff[1] == 255 && clrbuff[2] == 0) {
                        span(e.getX(), e.getY(), Color.WHITE);
                    }
                }
                if (!xormode) {
                    span(e.getX(), e.getY(), Color.GREEN);
                }
                this.repaint();
            } catch (ArrayIndexOutOfBoundsException OOB) {
                return;
            }
        }
    }

    void myLine(int x1i, int y1i, int x2i, int y2i, WritableRaster raster )
    { // this func realizes Bresenham's algorhytm
        if ( lineWidth > 1 )
        {
            Graphics g = img.getGraphics();
            Graphics2D graphics2D = (Graphics2D) g;
            BasicStroke stroke = new BasicStroke(lineWidth);
            graphics2D.setColor(Color.BLACK);
            graphics2D.setStroke(stroke);
            graphics2D.drawLine(x1i,y1i,x2i,y2i);

            return;
        }

        int x, y, dx, dy, incx, incy, pdx, pdy, es, el, err, curerr, errstep;
        int x1,x2,y1,y2;
        int signy = 1, signx = 1;
        int temp;
        int[] blackColor  = { 0, 0, 0, 0 };

//BEGIN
        x1 = x1i;
        x2 = x2i;
        y1 = y1i;
        y2 = y2i;
        dx = Math.abs(x2-x1);
        dy = Math.abs(y2-y1);

        if ( dx > dy ) // if it's more horizontal
        {
            if ( x2i < x1i )
            {
                x1 = x2i;
                x2 = x1i;
                y1 = y2i;
                y2 = y1i;
            }
            signy = (int)Math.signum(y2-y1);
            y = y1;
            curerr = 0;
            for ( int i = x1; i <= x2; i++ )
            {
                try {
                    raster.setPixel(i, y, blackColor);
                }
                catch ( ArrayIndexOutOfBoundsException AOB )
                {
                    System.out.println("AOEX" + i + " " + y );
                }
                curerr+=dy;
                if ( 2*curerr >= dx )
                {
                    y+=signy;
                    curerr -= dx;
                }
            }
        }
        else // if it's more vertical
        {
            if ( y2i < y1i )
            {
                y1 = y2i;
                y2 = y1i;
                x1 = x2i;
                x2 = x1i;
            }
            signx = (int)Math.signum(x2-x1);
            x = x1;
            curerr = 0;
            for ( int i = y1; i <= y2; i++ )
            {
                raster.setPixel(x, i, blackColor);
                curerr+=dx;
                if( 2*curerr >= dy )
                {
                    x+=signx;
                    curerr -= dy;
                }
            }
        }
        if ( true )
            return;
//END

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

    public void drawHexagon(int x, int y, WritableRaster raster )
    {
        myLine(x ,y ,x+angle_w ,y-angle_h ,raster);
        myLine(x+angle_w,y-angle_h ,x+2*angle_w ,y ,raster);
        myLine( x+2*angle_w,y ,x+2*angle_w ,y+vertical ,raster);
        myLine( x,y ,x ,y+vertical ,raster);
        myLine( x,y+vertical ,x+angle_w ,y+vertical+angle_h ,raster);
        myLine( x+angle_w, y+vertical+angle_h,x+2*angle_w ,y+vertical ,raster);
    }
    public void drawField( int width, int height, WritableRaster raster )
    {
        int even_oddShift = 0; // 0 for first row
        for ( int i = 0; i < height; i++ )
        {
            even_oddShift = i % 2;

            for ( int j = 0; j < width - even_oddShift; j++ )
            {
                drawHexagon(j*2*angle_w + even_oddShift*(angle_w), i*(angle_h+vertical) + angle_h, raster );
            }
        }
    }

    @Override
    public void paint(Graphics g )
    {
        super.paint(g);
        g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null );
    }
    void span(int x, int y, Color color )
    {
        if ( false )
        {
            int[] gotColor = null;
            int[] basicColor = null;
            gotColor = raster.getPixel(x, y, gotColor) ;
            basicColor = raster.getPixel(x, y, basicColor) ;
            final int[] blackColor = {0,0,0};
            final int greenColor[] = {0,255,0};
            final int[] whiteColor = {255,255,255};
            Color fillcolor = color;
            int[] fillcolorInt = {color.getRed(), color.getGreen(), color.getBlue() };
            int[] tempcolor = null;
            tempcolor = raster.getPixel(x,y,tempcolor);

            if ( compareColors(tempcolor,blackColor))
                return;

            if ( !xormode && compareColors(tempcolor, fillcolorInt)) {
                return;
            }
            if ( xormode && compareColors(tempcolor, fillcolorInt)) {
                fillcolor = Color.WHITE;
            }
            else if ( xormode && compareColors(tempcolor, whiteColor ))
            {
                fillcolor = Color.GREEN;
            }

            spanStack.push(new Span(x,y));
            while (spanFilling(tempcolor, fillcolor ));
            return;
        }
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
    void clearSockets()
    {
        lifeModel.clear();
        int[] clrbuff = null;
        for ( int i = 0; i < raster.getHeight(); i++ )
        {
            for ( int j = 0; j < raster.getWidth(); j++ )
            {
                try {
                    clrbuff = raster.getPixel(j, i, clrbuff);
                } catch(ArrayIndexOutOfBoundsException e )
                {
                    System.out.println("OOB: " + j + " " + i );
                }
                if ( (clrbuff[0] == 0 &&  clrbuff[1] == 0 &&  clrbuff[2] == 0) ||
                        (clrbuff[0] == 255 && clrbuff[1] == 255 &&clrbuff[2] == 255 ))
                {}
                else
                    raster.setPixel(j,i,new int[]{255,255,255});
            }
        }
        repaint();
    }
    void initModelFromFile (File in )
    {
        try {
            int coloredAmount;
            FileReader fr = new FileReader(in);
            BufferedReader br = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            String sizes[];

            sizes = line.split(" ");
            fieldWidth = Integer.parseInt(sizes[0]);
            fieldHeigth = Integer.parseInt(sizes[1]);
            line = br.readLine();
            this.lineWidth = Integer.parseInt(line);
            line = br.readLine();
            this.hexScale = Integer.parseInt(line) / 10.0;

            img = new BufferedImage( (int)(28*fieldWidth*hexScale)+1,(int)(23*(fieldHeigth+1)*hexScale), BufferedImage.TYPE_3BYTE_BGR );
            raster = img.getRaster();
            for ( int i = 0; i < img.getHeight(); i++ )
            {
                for ( int j = 0; j < img.getWidth(); j++ )
                {
                    raster.setPixel(j, i, basicColor ); // basically it's black. so let's bake it gray
                }
            }
            this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));

            drawField(fieldWidth,fieldHeigth,raster );
            lifeModel = new LifeModel(fieldWidth,fieldHeigth);

            line = br.readLine();
            coloredAmount = Integer.parseInt(line);

            lifeModel.initModelFromFile( br, coloredAmount  );
            drawFieldFromModel();

        }
        catch ( FileNotFoundException fnf )
        {
            fnf.printStackTrace();
        }
        catch ( IOException ioe )
        {
            System.out.println("ioex");
            ioe.printStackTrace();
        }
    }
    void setSizesAccordigToScale()
    {
        angle_h = (int)Math.round(7*hexScale);
        angle_w = (int)Math.round(14*hexScale);
        vertical = (int)Math.round(16*hexScale);
    }
    void saveStateInFile( File file )
    {
        try {
            FileWriter writer = new FileWriter(file);
            String buffer;

            buffer = fieldWidth + " " + fieldHeigth + System.lineSeparator();
            writer.append(buffer);
            buffer = lineWidth + System.lineSeparator();
            writer.append(buffer);
            buffer = (int)Math.round(hexScale*10)+System.lineSeparator();
            writer.append(buffer);
            if ( !lifeModel.isInitiated())
                initModel();
            lifeModel.appendFieldToWriter(writer);
            writer.flush();

        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
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
