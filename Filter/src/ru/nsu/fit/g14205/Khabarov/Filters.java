package ru.nsu.fit.g14205.Khabarov;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * Created by ilja on 10.05.2017.
 */
public class Filters {


    static BufferedImage applySobel(BufferedImage inputImage) {
        int sobelBorder = 100;
        BufferedImage out = null;
        int[][] horizontalMatrix = {{-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1}};
        int[] blackColorGrayscale = {0};
        int[] whiteColorGrayscale = {255};

        WritableRaster inRaster = inputImage.getRaster();
        int[] colorbuff = null;
        int[] resultcolor = {0};
        int tempColorValue = 0;
        out = new BufferedImage(inputImage.getWidth() - 2, inputImage.getHeight() - 2, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster outRaster = out.getRaster();
        for (int i = 1; i < inputImage.getHeight() - 1; i++) {
            for (int j = 1; j < inputImage.getWidth() - 1; j++) {
                colorbuff = inRaster.getPixel(j, i, colorbuff);
                tempColorValue = 0;
                for (int n = 0; n < 3; n++) {
                    for (int m = 0; m < 3; m++) {
                        try {
                            tempColorValue += horizontalMatrix[n][m] * inRaster.getPixel(j - 1 + n, i - 1 + m, resultcolor)[0];
                        } catch (ArrayIndexOutOfBoundsException aob) {
                            System.out.println("aob: " + (j - 1 + n) + " " + (i - 1 + m));
                        }
                    }
                }
                resultcolor[0] = tempColorValue;

                if (tempColorValue > sobelBorder)
                    outRaster.setPixel(j - 1, i - 1, resultcolor);
                else
                    outRaster.setPixel(j - 1, i - 1, blackColorGrayscale);

            }
        }

        return out;
    }

    static BufferedImage applyGrayscale(BufferedImage inputImage) {
        BufferedImage out = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster inRaster = inputImage.getRaster();
        WritableRaster outRaster = out.getRaster();
        int[] colorbuff = null;
        int[] resultcolor = {0};
        for (int i = 0; i < inputImage.getHeight(); i++) {
            for (int j = 0; j < inputImage.getWidth(); j++) {
                colorbuff = inRaster.getPixel(j, i, colorbuff);
                resultcolor[0] = (int) (0.3 * colorbuff[0] + 0.59 * colorbuff[1] + 0.11 * colorbuff[2]);
                outRaster.setPixel(j, i, resultcolor);
            }
        }
        return out;
    }

    static BufferedImage zoom2(BufferedImage inputImage) {
        int wid = inputImage.getWidth();
        int hei = inputImage.getHeight();
        BufferedImage out = null;
        BufferedImage temp = inputImage;
        out = new BufferedImage(wid, hei, BufferedImage.TYPE_3BYTE_BGR);
        int[] clrbuff = null;

        WritableRaster tempraster = temp.getRaster();
        WritableRaster outraster = out.getRaster();

        for (int i = 0; i < hei; i++) {
            for (int j = 0; j < wid; j++) {
                try {
                    clrbuff = tempraster.getPixel(wid / 4 + j / 2, hei / 4 + i / 2, clrbuff);
                } catch (ArrayIndexOutOfBoundsException aob) {
                    System.out.println("get aob");
                }
                try {
                    outraster.setPixel(j, i, clrbuff);
                } catch (ArrayIndexOutOfBoundsException aob2) {
                    System.out.println("set aob");
                }
            }
        }

        return out;
    }

    static  BufferedImage applyNegative(BufferedImage inputImage)
    {
        BufferedImage out = new BufferedImage(inputImage.getWidth(),inputImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR );
        int wid = inputImage.getWidth();
        int hei = inputImage.getHeight();
        WritableRaster inRaster = inputImage.getRaster();
        WritableRaster outRaster = out.getRaster();

        int[] colorbuff = null;
        int[] setcolor = {0,0,0};

        for ( int i = 0; i < hei; i++ )
        {
            for ( int j = 0; j < wid; j++ )
            {
                colorbuff = inRaster.getPixel(j,i,colorbuff);
                setcolor[0] = 255-colorbuff[0];
                setcolor[1] = 255-colorbuff[1];
                setcolor[2] = 255-colorbuff[2];
                outRaster.setPixel(j,i,setcolor);
            }
        }
        return out;
    }
    static BufferedImage applyEmboss(BufferedImage inputImage)
    {
        BufferedImage out = new BufferedImage(inputImage.getWidth()-2, inputImage.getHeight()-2, BufferedImage.TYPE_BYTE_GRAY );
        int wid = out.getWidth();
        int hei = out.getHeight();
        WritableRaster inRaster = inputImage.getRaster();
        WritableRaster outRaster = out.getRaster();
        int[] upcolor = null;
        int[] leftcolor = null;
        int[] botcolor = null;
        int[] rightcolor = null;
        int[] resultcolor = {0};

        for ( int i = 0; i < hei; i++ )
        {
            for ( int j = 0; j < wid; j++ )
            {
                upcolor = inRaster.getPixel(j+1, i, upcolor );
                leftcolor = inRaster.getPixel(j, i+1, leftcolor );
                botcolor = inRaster.getPixel(j+1, i+2, botcolor );
                rightcolor = inRaster.getPixel(j+2, i+1, rightcolor );

                resultcolor[0] = leftcolor[0] - upcolor[0] + botcolor[0] - rightcolor[0] + 128;
                /*if ( resultcolor[0] > 255 )
                    resultcolor[0] = 255;
                    */
                outRaster.setPixel(j,i,resultcolor);
            }
        }
        return out;
    }
    static BufferedImage applyRotation(BufferedImage inputImage, int angleDegrees )
    {
        BufferedImage out = new BufferedImage(inputImage.getWidth()-2, inputImage.getHeight()-2, BufferedImage.TYPE_3BYTE_BGR );
        int wid = out.getWidth();
        int hei = out.getHeight();
        WritableRaster inRaster = inputImage.getRaster();
        WritableRaster outRaster = out.getRaster();
        double angleRadians = Math.PI*angleDegrees/180;
        double realx, realy;
        double sourcex, sourcey;
        int[] whiteColor = {255,255,255};
        int[] buffColor = null;

        double[][] rotationMatrix = {
                {Math.cos(angleRadians), Math.sin(angleRadians)},
                {-Math.sin(angleRadians), Math.cos(angleRadians)}
        };

        for ( int i = 0; i < hei; i++ )
        {
            for ( int j = 0; j < wid; j++ )
            {
                realx = j - wid/2;
                realy = i - hei/2;
                sourcex = realx*Math.cos(angleRadians) + realy*Math.sin(angleRadians);
                sourcey = realy*Math.cos(angleRadians) - realx*Math.sin(angleRadians);
                sourcex = Math.round(sourcex);
                sourcey = Math.round(sourcey);
                if ( sourcex < - wid/2 || sourcex > wid/2 || sourcey < -hei/2 || sourcey > hei/2 )
                {
                    outRaster.setPixel(j,i,whiteColor);
                }
                else {
                    buffColor = inRaster.getPixel((int) (sourcex + wid / 2), (int) (sourcey + hei / 2), buffColor);
                    outRaster.setPixel(j, i, buffColor);
                }
            }
        }

        return out;
    }
}
