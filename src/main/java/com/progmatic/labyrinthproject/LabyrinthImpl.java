package com.progmatic.labyrinthproject;

import com.progmatic.labyrinthproject.enums.CellType;
import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.exceptions.CellException;
import com.progmatic.labyrinthproject.exceptions.InvalidMoveException;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author pappgergely
 */
public class LabyrinthImpl implements Labyrinth {

    private CellType[][] labyrinth;
    private Coordinate playerPosition;
    private int width = -1;
    private int height = -1;

    public LabyrinthImpl() {
        playerPosition = new Coordinate(0, 0);
    }

    @Override
    public void loadLabyrinthFile(String fileName) {
        try {
            Scanner sc = new Scanner(new File(fileName));
            int width = Integer.parseInt(sc.nextLine());
            int height = Integer.parseInt(sc.nextLine());

            setSize(width, height);

            for (int hh = 0; hh < height; hh++) {
                String line = sc.nextLine();
                for (int ww = 0; ww < width; ww++) {
                    switch (line.charAt(ww)) {
                        case 'W':
                            labyrinth[ww][hh] = CellType.WALL;
                            break;
                        case 'E':
                            labyrinth[ww][hh] = CellType.END;
                            break;
                        case 'S':
                            labyrinth[ww][hh] = CellType.START;
                            playerPosition = new Coordinate(ww, hh);
                            break;
                        default:
                            labyrinth[ww][hh] = CellType.EMPTY;
                    }
                }
            }
        } catch (FileNotFoundException | NumberFormatException ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public CellType getCellType(Coordinate c) throws CellException {
        if (c.getRow() >= height || c.getRow() < 0 || c.getCol() >= width || c.getCol() < 0) {

            throw new CellException(c, "There are no cells in the labyrinth with the given coordinates.");
        }
        return labyrinth[c.getCol()][c.getRow()];
    }

    @Override
    public void setSize(int width, int height) {
        if (width < 0 || height < 0) {
            return;
        }
        this.width = width;
        this.height = height;
        labyrinth = new CellType[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                labyrinth[j][i] = CellType.EMPTY;
            }
        }
    }

    @Override
    public void setCellType(Coordinate c, CellType type) throws CellException {
        if (c.getRow() >= height || c.getRow() < 0 || c.getCol() >= width || c.getCol() < 0) {
            throw new CellException(c, "There are no cells in the labyrinth with the given coordinates.");
        } else {
            labyrinth[c.getCol()][c.getRow()] = type;
            if (type == CellType.START) {
                playerPosition = c;
            }
        }
    }

    @Override
    public Coordinate getPlayerPosition() {
        return playerPosition;
    }

    @Override
    public boolean hasPlayerFinished() {
        return labyrinth[playerPosition.getCol()][playerPosition.getRow()] == CellType.END;
    }

    @Override
    public List<Direction> possibleMoves() {
        List<Direction> possible = new ArrayList<>();
        if (playerPosition.getCol() > 0
                && labyrinth[playerPosition.getCol() - 1][playerPosition.getRow()] != CellType.WALL) {
            possible.add(Direction.WEST);
        }
        if (playerPosition.getCol() < width
                && labyrinth[playerPosition.getCol() + 1][playerPosition.getRow()] != CellType.WALL) {
            possible.add(Direction.EAST);
        }
        if (playerPosition.getRow() > 0
                && labyrinth[playerPosition.getCol()][playerPosition.getRow() - 1] != CellType.WALL) {
            possible.add(Direction.NORTH);
        }
        if (playerPosition.getRow() < height
                && labyrinth[playerPosition.getCol()][playerPosition.getRow() + 1] != CellType.WALL) {
            possible.add(Direction.SOUTH);
        }
        return possible;
    }

    @Override
    public void movePlayer(Direction direction) throws InvalidMoveException {
        int rowDelta = 0;
        int colDelta = 0;

        switch (direction) {
            case WEST:
                colDelta = -1;
                break;
            case EAST:
                colDelta = +1;
                break;
            case NORTH:
                rowDelta = -1;
                break;
            case SOUTH:
                rowDelta = +1;
                break;
        }

        int newCol = playerPosition.getCol() + colDelta;
        int newRow = playerPosition.getRow() + rowDelta;

        if (newCol >= width || newCol < 0 || newRow >= height || newRow < 0) {
            throw new InvalidMoveException();
        }else if(labyrinth[newCol][newRow]==CellType.WALL){
            throw new InvalidMoveException();
        }else{
            playerPosition = new Coordinate(newCol, newRow);
        }
    }

}