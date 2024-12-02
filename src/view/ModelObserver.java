package view;

/**
 * A ModelObserver interfész, amelyet a modell állapotváltozásainak megfigyelésére használnak.
 * Az interfészt implementáló osztályok értesítést kapnak, amikor a modell állapota megváltozik.
 */
public interface ModelObserver {
    /**
     * Értesítés a modell állapotának frissítéséről.
     */
    void updateView();
}
