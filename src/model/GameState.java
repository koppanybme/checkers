package model;

import java.awt.Color;

public class GameState implements java.io.Serializable {
    private Board board;
    private String turn; // "red" or "black"
    private boolean gameOver;
    private String winner; // "red", "black", or "draw"

    public GameState() {
        this.board = new Board(8, 8);
        this.turn = "red";
        this.gameOver = false;
        this.winner = null;
    }

    public void initializeBoard() {
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
    }

    // Getters and setters for the attributes
    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}