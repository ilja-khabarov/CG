import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.awt.*;
import java.io.*;

/**
 * Created by ilja on 28.02.2017.
 */
public class LifeModel
{
    public byte[][] field;
    public double[][] metrics;
    int height, width;
    double FST_IMPACT = 1.0, SND_IMPACT = 0.3;
    double BIRTH_BEGIN = 2.3, BIRTH_END = 2.9, LIFE_BEGIN = 2.0, LIFE_END = 3.3;
    boolean initiated = false;

    LifeModel(int width_in, int height_in)
    {
        this.height = height_in;
        this.width = width_in;
        field = new byte[height][width];
        metrics = new double[height][width];
    }
    LifeModel()
    {
        this(30,20);
    }
    void clear()
    {
        field = new byte[height][width];
    }
    void setInitiated(boolean status)
    {
        initiated = status;
    }
    boolean isInitiated()
    {
        return initiated;
    }
    void bypass()
    {
        double metric = 0.0;
        double metric_scnd = 0.0;
        int shift = 0;
        byte[][] copy = new byte[height][width];//field.clone();
        for (int i = 0; i < height; i++)
        {
             for (int j = 0; j < width-i%2; j++)
             {
                 metric = 0.0;
                 metric_scnd = 0.0;
                try {
                    if ( i % 2 == 0 ) // shift = i%2
                        shift = 0;
                    else
                        shift = 1;

                    metric = metricAnalyzer(i,j);
                    metrics[i][j] = metric;

                    if ( metric < LIFE_BEGIN || metric > LIFE_END )
                    {
                        copy[i][j] = 0;
                        continue;
                    }
                    else
                    {
                        if ( field[i][j] == 1 )
                            copy[i][j] = 1;
                    }

                    if (metric >= BIRTH_BEGIN && metric <= BIRTH_END) {
                        copy[i][j] = 1;
                    }
                }
                catch ( ArrayIndexOutOfBoundsException outOfBounds )
                {
                    System.out.println("Mistake_bypass_outofbounds");
                }
             }
         }
        field = copy;
    }
    void metricPrinter(Graphics2D g2, int angle_h, int angle_w, int vertical )
    {
        double metric = 0.0;
        double metric_scnd = 0.0;
        int shift = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width - (i % 2); j++) {
                metric = 0.0;
                metric_scnd = 0.0;
                try {

                    metric = metricAnalyzer(i,j);
                    metrics[i][j] = metric;
                    g2.setColor(Color.BLACK);
                    g2.drawString(""+((Math.round(metric*10)/10.0)) , angle_h+ j*2*angle_w + (i%2)*(angle_w),angle_h+vertical/2 + 5 + i*(angle_h+vertical) );

                } catch (ArrayIndexOutOfBoundsException outOfBounds) {
                    System.out.println("Mistake_bypass_outofbounds");
                }
            }
        }
    }
    double metricAnalyzer(int i, int j)
    {
        int shift;
        double metric = 0.0, metric_scnd = 0.0;
        if (i % 2 == 0) // shift = i%2
            shift = 0;
        else
            shift = 1;

        if (j - 1 >= 0)//horizontal left
            metric += field[i][j - 1];
        if (j + 1 < width - (i % 2)) //horizontal right
            metric += field[i][j + 1];
        if (i - 1 >= 0) { // 1 unit higher
            if (j - 1 + shift >= 0)
                metric += field[i - 1][j - 1 + shift];
            if (j + shift < width - i % 2)
                metric += field[i - 1][j + shift];
        }
        if (i + 1 < height) // 1 unit lower
        {
            if (j - 1 + shift >= 0)
                metric += field[i + 1][j - 1 + shift];
            if (j + shift < width)
                metric += field[i + 1][j + shift];
        }
        metric *= FST_IMPACT;
        if (i - 2 >= 0)
            metric_scnd += field[i - 2][j];
        if (j - 2 + shift >= 0) {
            if (i - 1 >= 0)
                metric_scnd += field[i - 1][j - 2 + shift];
            if (i + 1 < height)
                metric_scnd += field[i + 1][j - 2 + shift];
        }
        if (j + 1 + shift < width) {
            if (i - 1 >= 0)
                metric_scnd += field[i - 1][j + 1 + shift];
            if (i + 1 < height)
                metric_scnd += field[i + 1][j + 1 + shift];
        }
        if (i + 2 < height)
            metric_scnd += field[i + 2][j];
        metric += (metric_scnd*SND_IMPACT);
        return metric;
    }
    void initModelFromFile( BufferedReader br, int amount )
    {
        String coords[];
        String line;
        initiated = true;
        int x, y;
        for ( int i = 0; i < amount; i++ )
        {
            try {
                line = br.readLine();
                coords = line.split(" ");
                x = Integer.parseInt(coords[1]);
                y = Integer.parseInt(coords[0]);
                field[x][y] = 1;
            }
            catch ( IOException ioe )
            {
                ioe.printStackTrace();
            }
        }
    }
    void appendFieldToWriter(FileWriter writer)
    {
        StringBuilder result = new StringBuilder();
        int sum = 0;
        for ( int i = 0; i < height; i++ )
        {
            for ( int j = 0; j < width-(i%2); j++ )
            {
                if ( field[i][j] == 1 )
                {
                    sum++;
                    result.append( j + " " + i + System.lineSeparator() );
                }
            }
        }
        try{
            writer.append(sum + System.lineSeparator() + result.toString() );
        }
        catch ( IOException ioe )
        {
            ioe.printStackTrace();
        }
    }
}
