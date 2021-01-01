import java.awt.*;

public class Grid {
    private GridBlock[][] grid;

    public Grid() {
        this.grid = new GridBlock[4][5];
    }

    public void addBlock(int x, int y, GridBlock block){
        grid[x][y] = block;
    }

    public GridBlock getAt(int x, int y){
        return grid[x][y];
    }

    /*private boolean isValid(GridBlock block){

    }*/

    public GridBlock[][] getGrid() {
        return grid;
    }
}

class GridBlock {
    private Color color;
    private int value;
    private boolean set;

    public GridBlock (){
        this.color = Main.colors.get("white");
        this.value = 0;
        this.set = false;
    }

    public GridBlock(Color color) {
        this.color = color;
        this.value = 0;
        this.set = false;
    }

    public GridBlock(int value) {
        this.color = Main.colors.get("grey");
        this.value = value;
        this.set = false;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isSet() {
        return set;
    }

    public void setSet(boolean set) {
        this.set = set;
    }
}
