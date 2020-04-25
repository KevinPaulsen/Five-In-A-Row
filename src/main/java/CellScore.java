package main.java;

import lombok.Setter;
import lombok.Value;

import java.awt.*;

@Value
public class CellScore {
    Point point;
    int evaluation;
}
