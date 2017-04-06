import org.w3c.dom.css.RGBColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import static java.lang.Math.abs;

/**
 * Created by ilja on 15.02.2017.
 */
public class DrawPanel extends JPanel implements MouseListener {
    BufferedImage img;
    WritableRaster raster;
    boolean xormode;
    int[] currHex = {8000000,8000000};
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
        drawField(25,20,raster );
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
                            System.out.println(xormode);
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
                System.out.print("Changed " + xcenter + " " +  currHex[0] );
                System.out.println( " " + ycenter + " " +  currHex[1] );
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
        int shift = 0;
        int[] blackColor  = { 0, 0, 0, 0 };

        int deltax = abs(x2-x1);

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

        int deltay = abs(y2-y1);
        int error = 0;
        int deltaerr = deltay;
        int y = y1;
        for ( int i = x1; i < x2; i++ )
        {
            raster.setPixel(i, y, blackColor);
            error = error+deltaerr;
            if ( 2*error >= deltax )
            {
                if ( y2 - y1 > 0 )
                    y = y+1;
                else
                    y = y - 1;
                error = error - deltax;
            }
        }
    }

    public void drawHexagon(int x, int y, WritableRaster raster )
    {
        myLine(x ,y ,x+14 ,y-7 ,raster);
        myLine(x+14 ,y-7 ,x+28 ,y ,raster);
        myLine( x+28,y ,x+28 ,y+16 ,raster);
        myLine( x,y ,x ,y+16 ,raster);
        myLine( x,y+16 ,x+14 ,y+23 ,raster);
        myLine( x+14, y+23,x+28 ,y+16 ,raster);
    }
    public void drawField( int width, int height, WritableRaster raster )
    {
        int even_oddShift = 0; // 0 for first row
        for ( int i = 0; i < height; i++ )
        {
            even_oddShift = i % 2;

            for ( int j = 0; j < width - even_oddShift; j++ )
            {
                drawHexagon(j*28 + even_oddShift*(14), i*23 + 7, raster );
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
            if ( gotColor[0] != basicColor[0] ||gotColor[1] != basicColor[1] ||gotColor[2] != basicColor[2] )
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
