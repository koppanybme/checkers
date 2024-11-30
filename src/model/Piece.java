package model;

import java.awt.*;

public class Piece implements java.io.Serializable {
    private Color color; // "white" or "black"
    private boolean isQueen;

    public Piece(Color color, boolean isQueen) {
        this.color = color;
        this.isQueen = isQueen;
    }

    public Piece() {
        this.color = null;
        this.isQueen = false;
    }

    // Getters and setters for the attributes
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isQueen() {
        return isQueen;
    }

    public void setQueen(boolean isQueen) {
        this.isQueen = isQueen;
    }
}