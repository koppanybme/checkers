package controller;

import java.awt.Point;

public interface PieceObserver {
    void onPieceClicked(int row, int col);
    void onPieceMoved(Point from, Point to);
}
