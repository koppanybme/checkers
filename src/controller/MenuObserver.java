package controller;

/**
 * A MenuObserver interfész, amelyet a menüelemek kattintásainak kezelésére használnak.
 */
public interface MenuObserver {
    /**
     * Kezeli a menüelemek kattintásait.
     *
     * @param menuItem A kattintott menüelem neve.
     */
    void onMenuItemClicked(String menuItem);
}
