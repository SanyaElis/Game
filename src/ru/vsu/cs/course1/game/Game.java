package ru.vsu.cs.course1.game;

import java.util.*;

/**
 * Класс, реализующий логику игры
 */
public class Game {
    /**
     * объект Random для генерации случайных чисел
     * (можно было бы объявить как static)
     */
    private final Random rnd = new Random();

    /**
     * двумерный массив для хранения игрового поля
     * (в данном случае цветов, 0 - пусто; создается / пересоздается при старте игры)
     */
    private int[][] field = null;
    private List<Integer> clicks = new ArrayList<>();
    public int points = 0;
    public int purpose = 150;
    /**
     * Максимальное кол-во цветов
     */
    private int colorCount = 0;

    public Game() {
    }

    public void newGame(int rowCount, int colCount, int colorCount) {
        // создаем поле
        field = new int[rowCount][colCount];
        this.colorCount = colorCount;
        setRandomColor();
        fillCell();
        points = 0;
    }

    public void fillCell() {
        while (true) {
            int[][] copyField = new int[getRowCount()][getColCount()];
            for (int i = 0; i < field.length; i++) {
                copyField[i] = Arrays.copyOf(field[i], getColCount());
            }
            checkField();
            omitCells();
            fillEmptyCell();
            if (Arrays.deepEquals(copyField, field)) {
                break;
            }
        }
    }

    public void setRandomColor() {
        for (int r = 0; r < field.length; r++) {
            for (int c = 0; c < field[0].length; c++) {
                field[r][c] = rnd.nextInt(colorCount) + 1;
            }
        }
    }

    public void clicksXY(int row, int col) {
        clicks.add(row);
        clicks.add(col);
        if (clicks.size() == 4) {
            int copyScore = points;
            if (Objects.equals(clicks.get(0), clicks.get(2)) || Objects.equals(clicks.get(1), clicks.get(3))) {
                int savedColor = field[clicks.get(0)][clicks.get(1)];
                field[clicks.get(0)][clicks.get(1)] = field[clicks.get(2)][clicks.get(3)];
                field[clicks.get(2)][clicks.get(3)] = savedColor;
                checkHor(clicks.get(0));
                checkVer(clicks.get(1));
                checkHor(clicks.get(2));
                checkVer(clicks.get(3));
                if (points == copyScore) {
                    savedColor = field[clicks.get(0)][clicks.get(1)];
                    field[clicks.get(0)][clicks.get(1)] = field[clicks.get(2)][clicks.get(3)];
                    field[clicks.get(2)][clicks.get(3)] = savedColor;
                }
            }
            clicks.clear();
        }
    }

    public void checkVer(int col) {
        for (int i = 0; i < field.length - 2; i++) {
            int j = i + 1;
            int k = 1;
            while (j != field.length && field[i][col] == field[j][col]) {
                k++;
                j++;
            }
            if (k >= 3 && field[i][col] != 0) {
                points += k;
                for (; k > 0; k--) {
                    field[i + k - 1][col] = 0;
                }
            }
        }
    }

    public void checkHor(int row) {
        for (int i = 0; i < field[0].length - 2; i++) {
            int j = i + 1;
            int k = 1;
            while (j != field[0].length && field[row][i] == field[row][j]) {
                k++;
                j++;
            }
            if (k >= 3 && field[row][i] != 0) {
                points += k;
                for (; k > 0; k--) {
                    field[row][i + k - 1] = 0;
                }
            }
        }

    }

    public void fillEmptyCell() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                if (field[i][j] == 0) {
                    field[i][j] = rnd.nextInt(colorCount) + 1;
                }
            }
        }
    }

    public void omitCells() {
        for (int i = field.length - 2; i >= 0; i--) {
            for (int j = field[0].length - 1; j >= 0; j--) {
                int k = i + 1;
                while (k != field.length && field[k][j] == 0) {
                    field[k][j] = field[k - 1][j];
                    field[k - 1][j] = 0;
                    k++;
                }
            }
        }
    }

    public void checkField() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                checkHor(i);
                checkVer(j);
            }
        }
    }

    public boolean checkWin() {
        return points > purpose;
    }

    public void leftMouseClick(int row, int col) {
        int rowCount = getRowCount(), colCount = getColCount();
        if (row < 0 || row >= rowCount || col < 0 || col >= colCount) {
            return;
        }
        clicksXY(row, col);
        fillCell();
        Timer timer = new Timer();
        int begin = 0;
        int timeInterval = 1000;
        timer.schedule(new TimerTask() {
            int counter = 0;
            @Override
            public void run() {
                counter++;
                if (counter >= 20){
                    timer.cancel();
                }
            }
        }, begin, timeInterval);

    }

   /* public void rightMouseClick(int row, int col) {
        int rowCount = getRowCount(), colCount = getColCount();
        if (row < 0 || row >= rowCount || col < 0 || col >= colCount) {
            return;
        }
        System.out.println("Очки: " + points);
    }*/

    public int getRowCount() {
        return field == null ? 0 : field.length;
    }

    public int getColCount() {
        return field == null ? 0 : field[0].length;
    }

    public int getColorCount() {
        return colorCount;
    }

    public int getCell(int row, int col) {
        return (row < 0 || row >= getRowCount() || col < 0 || col >= getColCount()) ? 0 : field[row][col];
    }
}
