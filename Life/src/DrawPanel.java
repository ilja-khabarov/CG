import org.w3c.dom.css.RGBColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

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
    LifeModel lifeModel;

    public DrawPanel()
    {
        super(new BorderLayout());
        xormode = false;
        //this.setAutoscrolls(true);
        this.setOpaque(true);
        img = new BufferedImage( 800,500, BufferedImage.TYPE_3BYTE_BGR );
        this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
        raster = img.getRaster();
        addMouseListener(this);
        int[] basicColor = { 255, 255, 255, 0 }; //
        for ( int i = 0; i < img.getHeight(); i++ )
        {
            for ( int j = 0; j < img.getWidth(); j++ )
            {
                raster.setPixel(j, i, basicColor ); // basically it's black. so let's bake it gray
            }
        }
 //       myLine(0,0,100,300,raster);
        drawField(fieldWidth,fieldHeigth,raster );
        lifeModel = new LifeModel(fieldWidth,fieldHeigth);

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int[] clrbuff= null;
                int[] checkColor = null;
                int x = e.getX(); int y = e.getY();
                //Integer[] currHex = {8000000,8000000}; // to provide large distance
                try {
                    if (  !hexChecker( x, y ))
                    {
                        super.mouseDragged(e);
                        return;
                    }
                    clrbuff = raster.getPixel(e.getX(), e.getY(), clrbuff);
                    {
                        for ( int i = y; i < raster.getHeight()+1; i++ )
                        {
                            checkColor = raster.getPixel(x,i,checkColor);
                            if ( checkColor[0] == 0 && checkColor[1] == 0 && checkColor[2] == 0 )
                                break;
                        }
                        for ( int i = y; i < raster.getHeight()+1; i-- )
                        {
                            checkColor = raster.getPixel(x,i,checkColor);
                            if ( checkColor[0] == 0 && checkColor[1] == 0 && checkColor[2] == 0 )
                                break;
                        }
                        if (xormode) {
                            if (xormode && clrbuff[0] == 255 && clrbuff[1] == 255 && clrbuff[2] == 255)
                                span(e.getX(), e.getY(), Color.GREEN);
                            if (xormode && clrbuff[0] == 0 && clrbuff[1] == 255 && clrbuff[2] == 0)
                                span(e.getX(), e.getY(), Color.WHITE);
                        }
                        if ( !xormode )
                            span(e.getX(), e.getY(), Color.GREEN);
                    }
                } catch(ArrayIndexOutOfBoundsException oob ) {
                    return;
                }
                repaint();

                super.mouseDragged(e);
            }
        });
    }
    void initModel()
    {
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
        printModel();
    }
    void drawFieldFromModel()
    {
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
        int x = e.getX(); int y = e.getY();

        int[] clrbuff= null;
        int[] checkColor = null;
        clrbuff = raster.getPixel(e.getX(), e.getY(), clrbuff);
        try
        {
            for ( int i = y; i < raster.getHeight()+1; i++ )
            {
                checkColor = raster.getPixel(x,i,checkColor);
                if ( checkColor[0] == 0 && checkColor[1] == 0 && checkColor[2] == 0 )
                    break;
            }
            for ( int i = y; i < raster.getHeight()+1; i-- )
            {
                checkColor = raster.getPixel(x,i,checkColor);
                if ( checkColor[0] == 0 && checkColor[1] == 0 && checkColor[2] == 0 )
                    break;
            }
            if (xormode){
                System.out.println(xormode);
            if ( (clrbuff[0] == 255) && (clrbuff[1] == 255) && (clrbuff[2] == 255)) {
                span(e.getX(), e.getY(), Color.GREEN);
            }
            if ( clrbuff[0] == 0 && clrbuff[1] == 255 && clrbuff[2] == 0) {
                span(e.getX(), e.getY(), Color.WHITE);
            }}
            if (!xormode) {
                span(e.getX(), e.getY(), Color.GREEN);
            }
            this.repaint();
        }
        catch(ArrayIndexOutOfBoundsException OOB)
        {
            return;
        }
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

        dx = x2 - x1;//проекция на ось икс
        dy = y2 - y1;//проекция на ось игрек

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
            pdx = incx;	pdy = 0;
            es = dy;	el = dx;
        }
        else//случай, когда прямая скорее "высокая", чем длинная, т.е. вытянута по оси y
        {
            pdx = 0;	pdy = incy;
            es = dx;	el = dy;//тогда в цикле будем двигаться по y
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

            raster.setPixel(x,y,blackColor);
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
                drawHexagon(j*2*angle_w + even_oddShift*(angle_w), i*(angle_h+vertical) + vertical, raster );
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
}
