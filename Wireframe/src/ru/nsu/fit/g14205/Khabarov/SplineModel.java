package ru.nsu.fit.g14205.Khabarov;

/**
 * Created by ilja on 10.04.2017.
 */
public class SplineModel
{
    double[][] points; // spline points
    double[] splineCoeffs;
    int amOfPoints;

    SplineModel()
    {
        splineCoeffs = new double[4];
        points = new double[1][2];
        amOfPoints = 1;
    }

    final static int[][] coeffMatrix = {
            {-1, 3,-3, 1},
            { 3,-6, 3, 0},
            {-3, 0, 3, 0},
            { 1, 4, 1, 0}
    };

    double[] matMulVec(double[] vec)
    { // in fact we have spline coefficients here
        double[] result = new double[4];

        for ( int i = 0; i < 4; i++ )
        {
            result[i] = 0;
            for ( int j = 0; j < 4; j++ ) // stringXvec
            {
                result[i] += coeffMatrix[i][j] * vec[j];
            }
            result[i] = result[i]/6.0; // as matrix has 1/6 coeff
        }
        return result;
    }
    double vecMulVec( double[] vec1, double[] vec2 )
    { // let's think it's verticalXhorizontal
        double result = 0;
        for ( int i = 0; i < 4; i++ )
        {
            result += vec1[i]*vec2[i];
        }
        return result;
    }

}
