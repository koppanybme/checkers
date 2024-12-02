package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import view.ModelObserver;

/**
 * A GameState osztály a játék állapotát reprezentálja.
 * Ez az osztály kezeli a játék állapotát, beleértve a tábla állapotát, a játékosok körét, a játék végét és a győztest.
 * Ezen kívül megfigyelőket is kezel, akik értesítést kapnak az állapot változásairól.
 */
public class GameState implements java.io.Serializable, ControllerObserver {
    private List<ModelObserver> observers = new ArrayList<ModelObserver>();
    private Board board;
    private String turn; // "white" or "black"
    private boolean gameOver;
    private String winner; // "white", "black", or "draw"
    public int whitePieces;
    public int blackPieces;

    /**
     * Hozzáad egy új megfigyelőt az állapotváltozások értesítéséhez.
     * 
     * @param observer Az új megfigyelő.
     */
    public void addObserver(ModelObserver observer) {
        observers.add(observer);
    }

    /**
     * Eltávolít egy megfigyelőt az állapotváltozások értesítéséből.
     * 
     * @param observer Az eltávolítandó megfigyelő.
     */
    public void removeObserver(ModelObserver observer) {
        observers.remove(observer);
    }

    /**
     * Értesíti a megfigyelőket az állapotváltozásokról.
     */
    public void notifyObservers() {
        for (ModelObserver observer : observers) {
            observer.updateView();
        }
    }

    /**
     * Létrehoz egy új GameState példányt.
     */
    public GameState() {
        this.board = new Board(8, 8);
        this.turn = "white";
        this.gameOver = false;
        this.winner = null;        
    }

    /**
     * Felállítja a táblát.
     */
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

    /**
     * Frissíti a játék állapotát, ha értesíti a kontroller.
     */
    @Override
    public void update() {
        // Logic to update the model
        System.out.println("Model updated");
        notifyObservers();
    }

    /**
     * Visszaadja a táblát.
     * @return
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Beállítja a táblát.
     * @param board
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Visszaadja a játékos körét.
     * @return
     */
    public String getTurn() {
        return turn;
    }

    /**
     * Beállítja a játékos körét.
     * @param turn
     */
    public void setTurn(String turn) {
        this.turn = turn;
    }

    /**
     * Visszaadja, hogy a játék véget ért-e.
     * @return
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Beállítja, hogy a játék véget ért-e.
     * @param gameOver
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * Visszaadja a győztest.
     * @return
     */
    public String getWinner() {
        return winner;
    }

    /**
     * Beállítja a győztest.
     * @param winner
     */
    public void setWinner(String winner) {
        this.winner = winner;
    }
}