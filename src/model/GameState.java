package model;

public class GameState {
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