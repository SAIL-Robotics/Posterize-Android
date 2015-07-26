package com.sailrobotics.posterize;

import android.graphics.Point;

/**
 * Created by Anandh on 07-25-15.
 */
public class FindDistanceUtil {

    Point k1, k2, u1, u2;
    double knownRealDistance, unknownRealDistance;
    public FindDistanceUtil(Point known1, Point known2, Point unknown1, Point unknown2, double realDistance)
    {
        knownRealDistance = realDistance;
    }

    double coordinateDistance(Point p1, Point p2)
    {
        double dist = Math.pow(p2.x - p1.x, 2.0) + Math.pow(p2.y - p1.y, 2.0);
        return Math.sqrt(dist);
    }

    public double calculateDistance()
    {
        double knownCoordinateDistance = coordinateDistance(k1, k2);
        double unknownCoordinateDistance = coordinateDistance(u1, u2);

        unknownRealDistance = (knownRealDistance * unknownCoordinateDistance) / knownCoordinateDistance;

        return unknownRealDistance;
    }
}
