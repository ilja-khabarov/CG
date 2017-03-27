/**
 * Created by ilja on 28.02.2017.
 */
public class LifeModel
{
    public byte[][] field;
    int height, width;
    double FST_IMPACT = 1.0, SND_IMPACT = 0.3;
    double BIRTH_BEGIN = 2.3, BIRTH_END = 2.9, LIFE_BEGIN = 1.8, LIFE_END = 3.5;

    LifeModel(int width_in, int height_in)
    {
        this.height = height_in;
        this.width = width_in;
        field = new byte[height][width];
    }
    LifeModel()
    {
        this(30,20);
    }
    void clear()
    {
        field = new byte[height][width];
    }
    void bypass()
    {
        double metric = 0.0;
        double metric_scnd = 0.0;
        int shift = 0;
        byte[][] copy = field.clone();
        for (int i = 0; i < height; i++)
        {
             for (int j = 0; j < width-i%2; j++)
             {
                 metric = 0.0;
                 metric_scnd = 0.0;
                try {
                    if ( i % 2 == 0 )
                        shift = 0;
                    else
                        shift = 1;

                    if ( j-1 >= 0 )
                        metric += field[i][j-1];
                    if ( j+1 < width )
                        metric += field[i][j+1];
                    if ( i-1 >= 0 ) {
                        if ( j - 1 + shift >= 0 )
                            metric += field[i - 1][j - 1 + shift];
                        if ( j + shift < width )
                            metric += field[i - 1][j + shift];
                    }
                    if ( i + 1 < height )
                    {

                        if ( j - 1 + shift >= 0 )
                            metric += field[i + 1][j - 1 + shift];
                        if ( j + shift < width )
                            metric += field[i + 1][j + shift];
                    }
                    metric *= FST_IMPACT;
                    if ( i - 2 >= 0 )
                        metric_scnd += field[i-2][j] ;
                    if ( j - 2 + shift >= 0 )
                    {
                        if ( i - 1 >= 0 )
                            metric_scnd += field[i - 1][j - 2 + shift];
                        if ( i + 1 < height )
                            metric_scnd += field[i + 1][j - 2 + shift];
                    }
                    if ( j + 1 + shift < width )
                    {
                        if ( i - 1 >= 0 )
                            metric_scnd += field[i - 1][j + 1 + shift];
                        if ( i + 1 < height )
                            metric_scnd += field[i + 1][j + 1 + shift];
                    }
                    if ( i + 2 < height )
                        metric_scnd += field[i+2][j];
                    metric += SND_IMPACT*metric_scnd;

                    if ( metric <= LIFE_BEGIN || metric >= LIFE_END )
                    {
                        copy[i][j] = 0;
                        continue;
                    }
                    if ( field[i][j] == 0 ) {
                        if (metric >= BIRTH_BEGIN && metric <= BIRTH_END) {
                            copy[i][j] = 1;
                        }
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
}
