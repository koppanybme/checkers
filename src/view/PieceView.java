package view;

import model.Piece;
import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;

/** 
 * A PieceView osztály egy bábu megjelenítését reprezentálja.
*/
public class PieceView extends JButton {
    private Piece piece;

    /**
     * Létrehoz egy új PieceView példányt a megadott bábuval.
     *
     * @param piece A bábu, amelyet megjelenít.
     */
    public PieceView(Piece piece) {
        this.piece = piece;
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
    }

    /**
     * Visszaadja a bábut.
     *
     * @return A bábu.
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Beállítja a bábut.
     *
     * @param piece A bábu.
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * Kirajzolja a bábut.
     * 
     * @param g A rajzoló objektum.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (piece != null) {
            g.setColor(piece.getColor());
            g.fillOval(0, 0, getWidth(), getHeight());
            if (piece.isQueen()) {
                g.setColor(g.getColor().equals(Color.WHITE) ? Color.BLACK : Color.WHITE);
                g.setFont(new Font("Times New Roman", Font.BOLD, 24));
                FontMetrics fm = g.getFontMetrics();
                int x = (getWidth() - fm.stringWidth("Q")) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g.drawString("Q", x, y);
            }
        }
    }

    /**
     * Visszaadja a preferált méretet.
     *
     * @return A preferált méret.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(50, 50);
    }

    /**
     * Kirajzolja a bábu keretét.
     *
     * @param g A rajzoló objektum.
     */
    @Override
    protected void paintBorder(Graphics g) {
        if (isSelected()) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(3));
            g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
        } else{
            super.paintBorder(g);
        }
    }
}