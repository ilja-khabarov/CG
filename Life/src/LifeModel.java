/**
 * Created by ilja on 28.02.2017.
 */
public class LifeModel
{
    byte[][] field;
    int height, width;
    double FST_IMPACT, SND_IMPACT;
    double BIRTH_BEGIN, BIRTH_END, LIFE_BEGIN, LIFE_END;

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
    void bypass()
    {
        double metric = 0.0;
        int shift = 0;
        byte[][] copy = field.clone();
        for (int i = 0; i < height; i++) // looking without the borders
         {
             for (int j = 0; j < width; j++)
             {
                try {
                    if ( i % 2 == 0 )
                        shift = 0;
                    else
                        shift = 1;

                    metric += FST_IMPACT*(field[i][j-1] + field[i][j+1] + field[i-1][j-1+shift] + field[i-1][j+shift] + field[i+1][j-1+shift] + field[i+1][j+shift] );
                    metric += SND_IMPACT*(field[i-2][j] + field[i-1][j-2+shift] + field[i-1][j+1+shift] + field[i+1][j-2+shift] + field[i+1][j+1+shift] + field[i+2][j]);
                    if ( metric <= LIFE_BEGIN || metric >= LIFE_END )
                    {
                        copy[i][j] = 0;
                        continue;
                    }
                    if ( field[i][j] == 0 )
                    {
                        if ( metric >= BIRTH_BEGIN && metric <= BIRTH_END )
                        {
                            field[i][j] = 1;
                        }
                    }

                }
                catch ( ArrayIndexOutOfBoundsException outOfBounds )
                {}
             }
         }
    }
}
