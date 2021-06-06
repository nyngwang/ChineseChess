package chess;

import exception.InvalidMoveException;
import game.ChessBoard;

import java.awt.*;

import static view.GUI.team1Logger;

@SuppressWarnings("SuspiciousNameCombination")
public class PutChessCommand implements ChessCommand {

    private Chess chess;
    private Point previousPoint;
    private Chess removeChess;
    private Point removeChessPoint;

    @Override
    public void execute(ChessBoard chessBoard, Point selectPoint, Point nextPoint) {
        team1Logger.info("in PutChessCommand, selectPoint={},{}, nextPoint={},{}",
                selectPoint.x, selectPoint.y, nextPoint.x, nextPoint.y);

        Chess selectChess = chessBoard.getChessByPoint(selectPoint)
                .orElseThrow(() -> new IllegalArgumentException("No chess found at the point " + selectPoint));
        Chess nextChess = chessBoard.getChessByPoint(nextPoint).orElse(null);

        if (nextChess != null && nextChess.getColor() != selectChess.getColor()) {
            moveChess(selectChess, nextPoint);
            removeChess(nextChess);
        } else if (nextChess == null) {
            moveChess(selectChess, nextPoint);
        } else {
            team1Logger.info("in putChessCommand execute(), the same color good good eat.");
            throw new InvalidMoveException("Cannot eat same color chess");
        }
    }

    private void moveChess(Chess chess, Point point) {
        this.chess = chess;
        previousPoint = new Point(chess.getCurrentPoint().y, chess.getCurrentPoint().x);
        chess.move(point);
        team1Logger.info("in moveChess, chess is moved: chess={}, point={},{}",
                chess.getClass().getName(), point.x, point.y);
    }

    private void removeChess(Chess chess) {
        removeChessPoint = new Point(chess.getCurrentPoint().y, chess.getCurrentPoint().x);
        removeChess = chess;
        chess.remove();
        team1Logger.info("in removeChess, chess is removed: chess={}",
                chess.getClass().getName());
    }

    @Override
    public void undo() {
        chess.undo(previousPoint);
        if (removeChess != null) {
            removeChess.undo(removeChessPoint);
        }
    }

    public Point getPreviousPoint() {
        return previousPoint;
    }

}
