package model;

public class Board implements java.io.Serializable {
    private Piece[][] board;
    private int whitePieces;
    private int blackPieces;
    private int rows;
    private int cols;

    public Board(int rows, int cols) {
        this.board = new Piece[rows][cols];
        this.whitePieces = 12;
        this.blackPieces = 12;
        this.rows = rows;
        this.cols = cols;
    }

    public Piece getPieceAt(int row, int col) {
        return board[row][col];
    }

    public void setPieceAt(int row, int col, Piece piece) {
        board[row][col] = piece;
    }

    public int getWhitePieces() {
        return whitePieces;
    }

    public void setWhitePieces(int redPieces) {
        this.whitePieces = redPieces;
    }

    public int getBlackPieces() {
        return blackPieces;
    }

    public void setBlackPieces(int blackPieces) {
        this.blackPieces = blackPieces;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}