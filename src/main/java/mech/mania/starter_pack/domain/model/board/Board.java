package mech.mania.starter_pack.domain.model.board;

import mech.mania.engine.domain.model.BoardProtos;
import mech.mania.starter_pack.domain.model.characters.Position;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Tile[][] grid;
    private final List<Position> portals;

    public Board(int width, int height, Tile[] grid, Position[] portals) {
        this.grid = new Tile[width][height];

        for (int x = 0; x < width; x++) {
            if (height >= 0) System.arraycopy(grid, x * height, this.grid[x], 0, height);
        }

        this.portals = List.of(portals);
    }

    public Board(BoardProtos.Board board) {
        int width = board.getWidth();
        int height = board.getHeight();
        grid = new Tile[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Tile(board.getGrid(x * height + y));
            }
        }

        portals = new ArrayList<>();
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

    public Tile getTileAtPosition(Position position) {
        return getGrid()[position.getX()][position.getY()];
    }

}
