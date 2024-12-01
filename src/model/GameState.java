package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import view.ModelObserver;

public class GameState implements java.io.Serializable, ControllerObserver {
    private List<ModelObserver> observers = new ArrayList<ModelObserver>();
    private Board board;
    private String turn; // "white" or "black"
    private boolean gameOver;
    private String winner; // "white", "black", or "draw"
    public int whitePieces;
    public int blackPieces;

    public void addObserver(ModelObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ModelObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (ModelObserver observer : observers) {
            observer.updateView();
        }
    }

    public GameState() {
        this.board = new Board(8, 8);
        this.turn = "white";
        this.gameOver = false;
        this.winner = null;        
    }

    public void initializeBoard() {
        // Set the initial pieces on the board
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 != 0) {
                    board.setPieceAt(row, col, new Piece(Color.WHITE, false));
                    whitePieces++;
                }
            }
        }
        for (int row = 5; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 != 0) {
                    board.setPieceAt(row, col, new Piece(Color.BLACK, false));
                    blackPieces++;
                }
            }
        }
    }

    @Override
    public void update() {
        // Logic to update the model
        System.out.println("Model updated");
        notifyObservers();
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