package controller;

import java.awt.Point;

/**
 * A PieceObserver interfész, amelyet a bábuk kattintásainak és mozgatásainak kezelésére használnak.
 */
public interface PieceObserver {
    /**
     * Kezeli a bábu kattintásait.
     *
     * @param row A kattintott sor.
     * @param col A kattintott oszlop.
     */
    void onPieceClicked(int row, int col);

    /**
     * Kezeli a bábu mozgatását.
     *
     * @param from A bábu eredeti pozíciója.
     * @param to A bábu új pozíciója.
     */
    void onPieceToMove(Point from, Point to);
}
