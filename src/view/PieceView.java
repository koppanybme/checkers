package view;

import model.Piece;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

public class PieceView extends JButton {
    private Piece piece;

    public PieceView(Piece piece) {
        this.piece = piece;
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);

        // this.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(java.awt.event.ActionEvent e) {
        //         isSelected = !isSelected;
        //         repaint();
        //     }
        // });
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    @Override
    protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (piece != null) {
        g.setColor(piece.getColor());
        g.fillOval(0, 0, getWidth(), getHeight());
        if(piece.isQueen()){
            setText("Q");
        }
    }
}

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(50, 50);
    }

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