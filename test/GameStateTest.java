import java.awt.Color;
import java.awt.Point;

import org.junit.Test;
import static org.junit.Assert.*;

import model.*;
import controller.*;

public class GameStateTest {

    @Test
    public void testWinner() {
        GameController controller = new GameController();
        controller.newGame();
        Board board = new Board(8, 8);
        board.setPieceAt(0, 1, new Piece(Color.WHITE, false));
        board.setPieceAt(1, 2, new Piece(Color.BLACK, false));
        controller.model.setBoard(board);
        controller.model.blackPieces = 1;
        controller.model.whitePieces = 1;
        controller.onPieceToMove(new Point(0, 1), new Point(2, 3));
        assertEquals(controller.model.getWinner(), "white");
    }

    @Test
    public void testQueen() {
        GameController controller = new GameController();
        controller.newGame();
        Board board = new Board(8, 8);
        board.setPieceAt(1, 2, new Piece(Color.WHITE, false));
        board.setPieceAt(2, 3, new Piece(Color.BLACK, false));
        controller.model.setBoard(board);
        controller.model.blackPieces = 1;
        controller.model.whitePieces = 1;
        controller.model.setTurn("black");
        controller.onPieceToMove(new Point(2, 3), new Point(0, 1));
        assertTrue(controller.model.getBoard().getPieceAt(0, 1).isQueen());
    }
    
}
