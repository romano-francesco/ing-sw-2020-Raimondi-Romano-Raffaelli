package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

    private static final int NUM_ROW = 5;
    private static final int NUM_COLUMNS = 5;
    private static final int capacityPlayerPosition = 6;
    private Cell[][] map;
    private Map<PositionContainer, PlayerIndex> playerPosition;


    public Board() {
        this.map = new Cell[NUM_ROW][NUM_COLUMNS];
        for (int i = 0; i < NUM_ROW; i++) {
            for (int j = 0; j < NUM_COLUMNS; j++)
                this.map[i][j] = new Cell(i, j);
        }
        this.playerPosition = new HashMap<>(capacityPlayerPosition);
        for (int i = 0; i < capacityPlayerPosition; i++) {
            this.playerPosition.put(new PositionContainer(), PlayerIndex.values()[i]);
        }
    }

    //Return if in the cell there is a player or a dome
    public boolean isFreeCell(Position cellPosition) throws NullPointerException, InvalidPositionException {
        if (cellPosition == null)
            throw new NullPointerException("cellPosition");
        if (cellPosition.isIllegal())
            throw new InvalidPositionException(cellPosition.row, cellPosition.col);
        if (map[cellPosition.row][cellPosition.col].hasDome())
            return false;

        for (PositionContainer positionContainer : playerPosition.keySet()) {
            for (int i = 0; i < 2; i++)
                if (positionContainer.getOccupiedPosition().equals(cellPosition))
                    return false;
        }
        return true;
    }

    //Return a List which contains copy of all adjacentCell
    public List<Cell> getAdjacentCells(Position centralPosition) throws InvalidPositionException, NullPointerException {
        if (centralPosition == null)
            throw new NullPointerException("centralPosition");
        if (centralPosition.isIllegal())
            throw new InvalidPositionException(centralPosition.row, centralPosition.col);

        List<Cell> adjacentCells = new ArrayList<>();
        for (int r = centralPosition.row - 1; r <= centralPosition.row + 1; r++) {
            for (int c = centralPosition.col - 1; c <= centralPosition.col + 1; c++) {
                if (r >= 0 && r <= 4 && c >= 0 && c <= 4 && (r != centralPosition.row || c != centralPosition.col))
                    adjacentCells.add(new Cell(this.map[r][c]));
            }
        }
        return adjacentCells;
    }

    public Cell getCell(Position position) throws InvalidPositionException, NullPointerException {
        if (position == null)
            throw new NullPointerException("Position");
        if (position.isIllegal())
            throw new InvalidPositionException(position.row, position.col);
        return new Cell(this.map[position.row][position.col]);
    }

    public Map<Position, PlayerIndex> getAdjacentPlayers(Position centralPosition) throws InvalidPositionException, NullPointerException {
        if (centralPosition == null)
            throw new NullPointerException("centralPosition");
        if (centralPosition.isIllegal())
            throw new InvalidPositionException(centralPosition.row, centralPosition.col);
        HashMap<Position, PlayerIndex> adjacentPlayers = new HashMap<>(capacityPlayerPosition);
        for (Map.Entry<PositionContainer, PlayerIndex> entry : this.playerPosition.entrySet()) {
            if (entry.getKey().getOccupiedPosition().isAdjacent(centralPosition))
                adjacentPlayers.put(entry.getKey().getOccupiedPosition(), entry.getValue());
        }
        return adjacentPlayers;
    }

    //Update player position
    //TODO: controllare qui se la newposition non è vuota? direi di no
    public void changeWorkerPosition(Position oldPosition, Position newPosition) throws NotPresentWorkerException, InvalidPositionException, NullPointerException {
        if (oldPosition == null)
            throw new NullPointerException("oldPosition");
        if (oldPosition.isIllegal())
            throw new InvalidPositionException(oldPosition.row, oldPosition.col);
        if (newPosition == null)
            throw new NullPointerException("newPosition");
        if (newPosition.isIllegal())
            throw new InvalidPositionException(newPosition.row, newPosition.col);

        for (Map.Entry<PositionContainer, PlayerIndex> entry : this.playerPosition.entrySet()) {
            if (entry.getKey().getOccupiedPosition().equals(oldPosition)) {
                entry.getKey().put(newPosition);
                return;
            }
        }

        throw new NotPresentWorkerException(oldPosition.row, oldPosition.col);
    }

    public void constructBlock(Position buildPosition) throws InvalidPositionException, NullPointerException, InvalidIncrementLevelException {
        if (buildPosition == null)
            throw new NullPointerException("buildPosition");
        if (buildPosition.isIllegal())
            throw new InvalidPositionException(buildPosition.row, buildPosition.col);

        this.map[buildPosition.row][buildPosition.col].incrementLevel();
    }
}