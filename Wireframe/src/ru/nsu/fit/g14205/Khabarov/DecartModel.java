package ru.nsu.fit.g14205.Khabarov;

/**
 * Created by ilja on 10.04.2017.
 */
class DecartModel
{
    double xmin, xmax, ymin, ymax;
    int imgWid, imgHei;
    double xcent, ycent;
    int xPixCent, yPixCent;
    double coordWid, coordHei;
    double ex, ey;

    DecartModel(double xm, double ym, double xma, double yma, int wid, int hei )
    {
        xmin = xm; ymin = ym; xmax = xma; ymax = yma; imgWid = wid; imgHei = hei;
        coordWid = xmax-xmin;
        coordHei = ymax-ymin;
        xcent = coordWid/2;
        ycent = coordHei/2;
        xPixCent = imgWid /2;
        yPixCent = imgHei /2;
        ex = imgWid/coordWid; // amount of pixels in coordinatic unit
        ey = imgHei/coordHei;
    }
    double getXDecartCoordsOfPixel(int x) // returns real coordinates of x pixel
    {
        return xmin + x/ex;
    }
    double getYDecartCoordsOfPixel(int y)
    {
        return ymin + y/ey;
    }
    int getPixByCoordX(double x)
    {
        return (int)Math.round((x-xmin)*ex);
    }
    int getPixByCoordY(double y)
    {
        return (int)Math.round((y-ymin)*ey);
    }

}