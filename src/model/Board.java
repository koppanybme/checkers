package model;

/**
 * A Board osztály a játék tábla modelljét reprezentálja.
 * Ez az osztály kezeli a tábla állapotát, beleértve a bábuk elhelyezkedését és számát.
 */
public class Board implements java.io.Serializable {
    private Piece[][] board;
    private int rows;
    private int cols;

    /**
     * Létrehoz egy új Board példányt a megadott sorokkal és oszlopokkal.
     *
     * @param rows A tábla sorainak száma.
     * @param cols A tábla oszlopainak száma.
     */
    public Board(int rows, int cols) {
        this.board = new Piece[rows][cols];
        this.rows = rows;
        this.cols = cols;
    }

    /**
     * Visszaadja a megadott pozícióban lévő bábut.
     *
     * @param row A sor indexe.
     * @param col Az oszlop indexe.
     * @return A megadott pozícióban lévő bábu.
     * @throws ArrayIndexOutOfBoundsException Ha a megadott pozíció érvénytelen.
     */
    public Piece getPieceAt(int row, int col) throws ArrayIndexOutOfBoundsException {
        return board[row][col];
    }

    /**
     * Beállítja a megadott pozícióban lévő bábut.
     *
     * @param row A sor indexe.
     * @param col Az oszlop indexe.
     * @param piece A beállítandó bábu.
     */
    public void setPieceAt(int row, int col, Piece piece) {
        board[row][col] = piece;
    }

    /**
     * Visszadja a tábla sorainak számát
     * @return
     */
    public int getRows() {
        return rows;
    }

    /**
     * Visszadja a tábla oszlopainak számát
     * @return
     */
    public int getCols() {
        return cols;
    }
}