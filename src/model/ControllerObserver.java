package model;

/**
 * A ControllerObserver interfész, amelyet a kontroller állapotváltozásainak megfigyelésére használnak.
 * Az interfészt implementáló osztályok értesítést kapnak, amikor a kontroller állapota megváltozik.
 */
public interface ControllerObserver {
    /**
     * Értesítés a kontroller állapotának frissítéséről.
     */
    void update();
}