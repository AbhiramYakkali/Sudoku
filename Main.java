import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JComponent {
    private final BufferedImage image;

    private final int WIDTH = 465, HEIGHT = 550, SQUARE_SIDE = 450 / 9;

    private int selX = -1, selY;
    private final HashMap<Integer, Integer> guesses = new HashMap<>();

    //[Row][Column]
    //0 = empty square
    private static final int[][] board = new int[][]{ {0,0,0,0,0,0,0,0,4}, {0,0,0,5,0,4,3,0,0}, {0,0,0,0,0,0,0,0,5},
                                                {0,0,0,0,0,0,0,0,0}, {0,3,0,0,0,0,0,6,0}, {0,9,0,0,0,0,0,3,2},
                                                {0,0,0,0,0,0,0,0,0}, {0,0,0,0,2,0,1,8,0}, {7,0,0,0,9,0,0,0,0}};
    //Tracks which squares are confirmed correct(can no longer be changed)
    private final ArrayList<Integer> correct = new ArrayList<>();
    private int time = 0;
    private long lastSecond;
    private boolean playing = false;

    public Main() {
        new Window(WIDTH, HEIGHT, "Sudoku", this);
        Mouse mouse = new Mouse(this);
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

        this.addMouseListener(mouse);

        drawBoard();
        solveBoard();
        printBoard();

        playing = true;

        //Track time
        lastSecond = System.currentTimeMillis();
        displayTime();
        while(playing) {
            if(System.currentTimeMillis() - lastSecond >= 1000) {
                lastSecond += 1000;
                time++;
                displayTime();
            }
        }
    }

    private void displayTime() {
        if(!playing) return;

        fillRect(0, SQUARE_SIDE * 9 + 1, WIDTH, HEIGHT - (SQUARE_SIDE * 9 + 1), Color.white);

        int min = time / 60, sec = time % 60;
        String t = min + ":";
        if(sec < 10) t += "0";
        t += sec;
        drawText( 25, 490, t, Color.black);
    }

    private void printBoard() {
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void drawBoard() {
        fillRect(0, 0, WIDTH, HEIGHT, Color.white);
        drawLine(0, WIDTH, SQUARE_SIDE * 9, SQUARE_SIDE * 9, Color.black);
        for(int r = 0; r < 9; r++) {
            drawLine(0, WIDTH, r * SQUARE_SIDE, r * SQUARE_SIDE, Color.black);
            for(int c = 0; c < 9; c++) {
                if(r == 0) {
                    drawLine(c * SQUARE_SIDE, c * SQUARE_SIDE, 0, HEIGHT - (HEIGHT - SQUARE_SIDE * 9)
                            , Color.black);
                }
                if(board[r][c] != 0) {
                    drawNumber(c, r);
                    correct.add(c * 10 + r);
                }
            }
        }
    }

    private void solveBoard() {
        solve(0);
    }
    private static boolean solve(int square) {
        //Last square is 80, so square 81 means board is full
        if(square >= 81) return true;

        //Convert square to row and column
        int row = square / 9;
        int col = square % 9;

        if(board[row][col] != 0) {
            return solve(square + 1);
        } else {
            for(int i = 1; i < 10; i++) {
                board[row][col] = i;
                if(checkSquare(row, col)) {
                    if(solve(square + 1)) return true;
                }
            }
            board[row][col] = 0;
            if(square == 1) System.out.println("This sudoku board has no solution.");
            return false;
        }
    }

    //Checks if a certain square is legal
    //Return true if it is legal, false if it is not
    private static boolean checkSquare(int row, int col) {
        return checkRow(row) && checkColumn(col) && checkSection(row / 3 * 3 + col / 3);
    }
    //Checks to see if board is still legal(no duplicates on row, column, or section)
    //Return false if duplicates exist, true if no duplicates exist
    private static boolean checkRow(int row) {
        int[] r = new int[9];
        System.arraycopy(board[row], 0, r, 0, 9);
        return checkDuplicates(r);
    }
    private static boolean checkColumn(int col) {
        int[] column = new int[9];
        for(int i = 0; i < 9; i++) {
            column[i] = board[i][col];
        }
        return checkDuplicates(column);
    }
    private static boolean checkSection(int sec) {
        //Convert section number to row and column of the section(0-2)
        int secR = sec / 3;
        int secC = sec % 3;
        int[] section = new int[9];
        for(int i = 0; i < 9; i++) {
            section[i] = board[i / 3 + secR * 3][i % 3 + secC * 3];
        }
        return checkDuplicates(section);
    }

    private static boolean checkDuplicates(int[] nums) {
        boolean[] num = new boolean[9];
        for(int i = 0; i < 9; i++) {
            if(nums[i] != 0) {
                if(num[nums[i] - 1]) return false;
                else num[nums[i] - 1] = true;
            }
        }
        return true;
    }

    public void mouseClicked(int x, int y) {
        drawRect(selX * SQUARE_SIDE, selY * SQUARE_SIDE, SQUARE_SIDE, SQUARE_SIDE, Color.black);
        selX = x / SQUARE_SIDE;
        selY = y / SQUARE_SIDE;
        drawRect(selX * SQUARE_SIDE, selY * SQUARE_SIDE, SQUARE_SIDE, SQUARE_SIDE, Color.red);
    }

    public void keyPressed(int key) {
        switch(key) {
            case KeyEvent.VK_ESCAPE:
                System.exit(1);
                break;
            case KeyEvent.VK_0:
                if(guesses.get(selX * 10 + selY) != null) { guesses.remove(selX * 10 + selY); }
                clearSquare();
                repaint();
                break;
            case KeyEvent.VK_1:
                drawNumber(1, Color.gray);
                break;
            case KeyEvent.VK_2:
                drawNumber(2, Color.gray);
                break;
            case KeyEvent.VK_3:
                drawNumber(3, Color.gray);
                break;
            case KeyEvent.VK_4:
                drawNumber(4, Color.gray);
                break;
            case KeyEvent.VK_5:
                drawNumber(5, Color.gray);
                break;
            case KeyEvent.VK_6:
                drawNumber(6, Color.gray);
                break;
            case KeyEvent.VK_7:
                drawNumber(7, Color.gray);
                break;
            case KeyEvent.VK_8:
                drawNumber(8, Color.gray);
                break;
            case KeyEvent.VK_9:
                drawNumber(9, Color.gray);
                break;
            case KeyEvent.VK_ENTER:
                int k = selX * 10 + selY;
                if(guesses.get(k) != null) {
                    if(guesses.get(k) == board[selY][selX]) {
                        drawNumber(board[selY][selX], Color.black);
                        correct.add(selX * 10 + selY);
                        if(correct.size() >= 10) {
                            playing = false;
                            drawText(200, 490, "You Won!", Color.black);
                        }
                    } else {
                        clearSquare();
                        repaint();
                    }
                    guesses.remove(k);
                }
                break;
            case KeyEvent.VK_SPACE:
                correct.clear();
                drawBoard();
                playing = false;
                break;
        }
    }

    public void drawNumber(int x, int y, int number, Color color) {
        drawText(x * SQUARE_SIDE + 15, (y + 1) * SQUARE_SIDE - 10,
                String.valueOf(number), color);
    }
    public void drawNumber(int x, int y, Color color) {
        drawNumber(x, y, board[y][x], color);
    }
    public void drawNumber(int x, int y) {
        drawNumber(x, y, Color.black);
    }
    public void drawNumber(int number, Color color) {
        if(correct.contains(selX * 10 + selY)) return;

        clearSquare();
        if(color == Color.gray) {
            int key = selX * 10 + selY;
            if(guesses.get(key) == null) {
                guesses.put(key, number);
            } else {
                guesses.replace(key, number);
            }
        }
        drawNumber(selX, selY, number, color);
    }

    public void drawLine(int x1, int x2, int y1, int y2, Color color) {
        Graphics g = image.getGraphics();
        g.setColor(color);
        g.drawLine(x1, y1, x2, y2);
        g.dispose();
        repaint();
    }
    public void drawText(int x, int y, String text, Color color) {
        Graphics g = image.getGraphics();
        g.setColor(color);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
        g.drawString(text, x, y);
        g.dispose();
        repaint();
    }
    public void fillRect(int x, int y, int width, int height, Color color) {
        Graphics g = image.getGraphics();
        g.setColor(color);
        g.fillRect(x, y, width, height);
        g.dispose();
        repaint();
    }
    public void drawRect(int x, int y, int width, int height, Color color) {
        Graphics g = image.getGraphics();
        g.setColor(color);
        g.drawRect(x, y, width, height);
        g.dispose();
        repaint();
    }
    private void clearSquare(int x, int y) {
        fillRect(x * SQUARE_SIDE + 1, y * SQUARE_SIDE + 1, SQUARE_SIDE - 1, SQUARE_SIDE - 1, Color.white);
    }
    private void clearSquare() {
        clearSquare(selX, selY);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
        g.dispose();
    }

    public static void main(String[] args) {
        new Main();
    }
}