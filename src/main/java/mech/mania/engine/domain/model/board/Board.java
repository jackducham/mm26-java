package mech.mania.engine.domain.model.board;

import mech.mania.engine.domain.model.BoardProtos;
import mech.mania.engine.domain.model.characters.Position;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Tile[][] grid;
    private final List<Position> portals;

    public Board(BoardProtos.Board board) {
        int rows = board.getRows();
        int cols = board.getColumns();
        grid = new Tile[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new Tile(board.getGrid(r * c + c));
            }
        }

        portals = new ArrayList<>(board.getPortalsCount());
        for (int i = 0; i < board.getPortalsCount(); i++) {
            portals.add(i, new Position(board.getPortals(i)));
        }
    }

    public Tile[][] getGrid() {
        return grid;
    }

    public List<Position> getPortals() {
        return portals;
    }

}
