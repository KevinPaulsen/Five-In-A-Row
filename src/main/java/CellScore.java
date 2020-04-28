package main.java;

import lombok.Value;

import java.awt.Point;

@Value
public class CellScore {
    Point point;
    int evaluation;

    int getX() {
        return point.x;
    }

    int getY() {
        return point.y;
    }
}
