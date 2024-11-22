package view;

import model.Board;
import model.Piece;
import javax.swing.*;
import java.awt.*;

public class BoardView extends JPanel {
    private Board board;
    private PieceView[][] pieceViews;

    public BoardView(Board board) {
        this.board = board;
        this.pieceViews = new PieceView[board.getRows()][board.getCols()];
        setLayout(null); // Use absolute positioning

        // Initialize PieceView components
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                Piece piece = board.getPieceAt(row, col);
                if (piece != null) {
                    PieceView pieceView = new PieceView(piece);
                    pieceViews[row][col] = pieceView;
                    add(pieceView);
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawPieces();
    }

    private void drawBoard(Graphics g) {
        int cellHeight = getHeight() / board.getRows();
        int cellWidth = cellHeight;
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                if ((row + col) % 2 == 0) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(new Color(196,164,132));
                }
                g.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
            }
        }
    }

    private void drawPieces() {
        int cellHeight = getHeight() / board.getRows();
        int cellWidth = cellHeight;
        int pieceSize = (int) (cellWidth * 0.9); // 90% of the cell size
        int offset = (cellWidth - pieceSize) / 2; // Centering offset

        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                Piece piece = board.getPieceAt(row, col);
                PieceView pieceView = pieceViews[row][col];
                if (piece != null) {
                    if (pieceView == null) {
                        pieceView = new PieceView(piece);
                        pieceViews[row][col] = pieceView;
                        add(pieceView);
                    }
                    pieceView.setBounds(col * cellWidth + offset, row * cellHeight + offset, pieceSize, pieceSize);
                    pieceView.setVisible(true);
                } else if (pieceView != null) {
                    pieceView.setVisible(false);
                }
            }
        }
        revalidate();
        repaint();
    }
}