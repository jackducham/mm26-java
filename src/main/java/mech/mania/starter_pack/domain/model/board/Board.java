package mech.mania.starter_pack.domain.model.board;

import mech.mania.engine.domain.model.BoardProtos;
import mech.mania.starter_pack.domain.model.characters.Position;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Tile[][] grid;
    private final List<Position> portals;

    public Board(BoardProtos.Board board) {
        int width = board.getWidth();
        int height = board.getHeight();
        grid = new Tile[width][height];

        for (int r = 0; r < width; r++) {
            for (int c = 0; c < height; c++) {
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
