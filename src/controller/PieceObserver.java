package controller;

import java.awt.Point;

public interface PieceObserver {
    void onPieceClicked(int row, int col);
    void onPieceToMove(Point from, Point to);
}
