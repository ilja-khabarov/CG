package ru.nsu.fit.g14205.Khabarov;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * Created by ilja on 10.05.2017.
 */
public class Filters
{


    static BufferedImage applySobel(BufferedImage inputImage)
    {
        BufferedImage out = null;
        int[][] horizontalMatrix = {  {-1, 0, 1},
                                      {-2, 0, 2},
                                      {-1, 0, 1}  };
        int[] blackColorGrayscale = {0};
        int[] whiteColorGrayscale = {255};

        WritableRaster inRaster = inputImage.getRaster();
        int[] colorbuff = null;
        int[] resultcolor = {0};
        int tempColorValue = 0;
        out = new BufferedImage(inputImage.getWidth()-2, inputImage.getHeight()-2, BufferedImage.TYPE_BYTE_GRAY );
        WritableRaster outRaster = out.getRaster();
        for ( int i = 1; i < inputImage.getHeight()-1; i++ ) {
            for (int j = 1; j < inputImage.getWidth()-1; j++) {
                colorbuff = inRaster.getPixel(j,i,colorbuff);
                tempColorValue = 0;
                for ( int n = 0; n < 3; n++ )
                {
                    for ( int m = 0; m < 3; m++ )
                    {
                        try {
                            tempColorValue += horizontalMatrix[n][m] * inRaster.getPixel(j - 1 + n, i - 1 + m, resultcolor)[0];
                        } catch(ArrayIndexOutOfBoundsException aob)
                        {
                            System.out.println("aob: " + (j - 1 + n) + " " + (i - 1 + m) );
                        }
                    }
                }
                resultcolor[0] = tempColorValue;

                if ( tempColorValue > 100 )
                    outRaster.setPixel(j-1,i-1,resultcolor);
                else
                    outRaster.setPixel(j-1,i-1,blackColorGrayscale);


            }
        }

        return out;
    }
    static BufferedImage applyGrayscale(BufferedImage inputImage)
    {
        BufferedImage out = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY );
        WritableRaster inRaster = inputImage.getRaster();
        WritableRaster outRaster = out.getRaster();
        int[] colorbuff = null;
        int[] resultcolor = {0};
        for ( int i = 0; i < inputImage.getHeight(); i++ )
        {
            for ( int j = 0; j < inputImage.getWidth(); j++ )
            {
                colorbuff = inRaster.getPixel(j,i,colorbuff);
                resultcolor[0] = (int)(0.3*colorbuff[0] + 0.59*colorbuff[1] + 0.11*colorbuff[2]);
                outRaster.setPixel(j,i,resultcolor);
            }
        }
        return out;
    }
}
