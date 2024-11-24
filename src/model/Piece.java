package model;

import java.awt.*;

public class Piece implements java.io.Serializable {
    private Color color; // "red" or "black"
    private boolean isQueen;
    private boolean isVisible;

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public Piece(Color color, boolean isQueen, boolean isVisible) {
        this.color = color;
        this.isQueen = false;
        this.isVisible = isVisible;
    }

    public Piece() {
        this.color = null;
        this.isQueen = false;
        this.isVisible = false;
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