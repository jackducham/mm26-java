package mech.mania.starter_pack.domain.model.characters;

import mech.mania.engine.domain.model.CharacterProtos;

import java.util.Objects;

public class Position {
    private final int x;
    private final int y;
    private final String board_id;

    public Position(final int x, final int y, final String board_id) {
        this.x = x;
        this.y = y;
        this.board_id = board_id;
    }

    public Position(CharacterProtos.Position positionProto) {
        x = positionProto.getX();
        y = positionProto.getY();
        board_id = positionProto.getBoardId();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getBoardID() {
        return board_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y &&
                Objects.equals(board_id, position.board_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, board_id);
    }

    public int manhattanDistance(Position other) {
        int x = Math.abs(this.x - other.getX());
        int y = Math.abs(this.y - other.getY());
        return x + y;
    }
}
