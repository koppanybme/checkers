package model;

import java.awt.*;

/**
 * A Piece osztály egy bábut reprezentál.
 * Ez az osztály kezeli a bábu színét és státuszát (dáma-e vagy sem).
 */
public class Piece implements java.io.Serializable {
    private Color color; // "white" or "black"
    private boolean isQueen;

    /**
     * Létrehoz egy új Piece példányt a megadott színnel és státusszal.
     *
     * @param color A bábu színe.
     * @param isQueen A bábu státusza (dáma-e vagy sem).
     */
    public Piece(Color color, boolean isQueen) {
        this.color = color;
        this.isQueen = isQueen;
    }

    /**
     * Létrehoz egy új Piece példányt alapértelmezett beállításokkal.
     */
    public Piece() {
        this.color = null;
        this.isQueen = false;
    }

    /**
     * Visszaadja a bábu színét.
     *
     * @return A bábu színe.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Beállítja a bábu színét.
     *
     * @param color A bábu új színe.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Visszaadja, hogy a bábu dáma-e.
     *
     * @return true, ha a bábu dáma, különben false.
     */
    public boolean isQueen() {
        return isQueen;
    }

    /**
     * Beállítja, hogy a bábu dáma-e.
     *
     * @param isQueen A bábu új státusza (dáma-e vagy sem).
     */
    public void setQueen(boolean isQueen) {
        this.isQueen = isQueen;
    }
}