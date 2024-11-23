package controller;

import model.Board;
import model.Piece;
import view.BoardView;
import view.MainFrame;
import view.PieceView;

import java.awt.Color;
import javax.swing.*;

public class GameController {
    private PieceView selectedPieceView;

    public static void main(String[] args) {
        GameController controller = new GameController();
        controller.startGame();
    }

    public void startGame() {
        // Create a new board with 8 rows and 8 columns
        Board board = new Board(8, 8);
        // Set the initial pieces on the board
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 != 0) {
                    board.setPieceAt(row, col, new Piece(Color.WHITE, false, true));
                }
            }
        }
        for (int row = 5; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 != 0) {
                    board.setPieceAt(row, col, new Piece(Color.BLACK, false, true));
                }
            }
        }
        MainFrame frame = new MainFrame(new BoardView(board));
    }

    public void pieceSelected(PieceView pieceView) {
        if (selectedPieceView != null) {
            selectedPieceView.setSelected(false);
            selectedPieceView.repaint();
        }
        selectedPieceView = pieceView;
        selectedPieceView.setSelected(true);
        selectedPieceView.repaint();
    }
}