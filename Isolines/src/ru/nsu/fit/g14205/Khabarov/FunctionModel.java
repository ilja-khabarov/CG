package ru.nsu.fit.g14205.Khabarov;
/**
 * Created by ilja on 03.04.2017.
 */
public class FunctionModel
{
    final String func = "sqrt(x*x+y*y)+3*cos(sqrt(x*x+y*y))";

    FunctionModel()
    {

    }

    double getz(double x, double y )
    {
        return ((x*x)+(y*y))/8;
    }
    double getz2(double x, double y)
    {
        double root = Math.sqrt(x*x + y*y);
        return root + 3*Math.cos(root);
    }
    double getz3(double x, double y)
    {
        return 1/(x*x+y*y)+0.2/((x+1.2)*(x+1.2)+(y-1.5)*(y-1.5))+ 0.3/((x-0.9)*(x-0.9)+(y+1.1)*(y+1.1));
    }

}
