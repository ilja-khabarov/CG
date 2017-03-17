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
    public DrawPanel()
    {
        super(new BorderLayout());
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
                System.out.println(e.getX() + " " +  e.getY() );
                //span(e.getX(), e.getY(), Color.GREEN);
                super.mouseDragged(e);
            }
        });
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
        span(e.getX(), e.getY(),Color.GREEN);
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
        int[] iArray = { 255, 255, 0, 0 }; // yellow

        //img = (BufferedImage) createImage(800, 500 );


        //drawHexagon(0, 7, raster);
        //drawHexagon(28, 7, raster);
        //System.out.print("A");
        //drawHexagon(7*28 , 0*23 + 7, raster );
        //myLine(8*28,23, 8*28, 7, raster );
        /*myLine(0,7,0,23 ,raster);
        myLine(0,23,14,30 ,raster);
        myLine(14,30,28,23 ,raster);
        myLine(28,23,28,7 ,raster);
        myLine(14,0,28,7 ,raster);
        myLine(0,7,14,0 ,raster);*/


        //this.scrollRectToVisible(new Rectangle(0,0,img.getWidth(),img.getHeight()));
        g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null );
    }
    void span(int x, int y, Color color )
    {
        int[] gotColor = null;
        int[] blackColor = {0,0,0};
        int[] placeColor = {color.getRed(), color.getGreen(), color.getBlue(), 0};
        int x1, x2;
        gotColor = raster.getPixel(x, y, gotColor) ;
        if ( blackColor[0] == gotColor[0] && blackColor[1] == gotColor[1] && blackColor[2] == gotColor[2] )
            return; // lets us ignore black borders

        int[] borders = spanBorders(x,y);
        fillSpan(borders[0], borders[1], y, color);
        this.repaint();
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
            gotColor = raster.getPixel(i, y, gotColor) ;
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
}
