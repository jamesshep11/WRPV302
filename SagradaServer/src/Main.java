import com.example.segrada.Grid;
import com.example.segrada.GridBlock;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static HashMap<String, Color> colors = new HashMap<String, Color>(){{
        put("purple", new Color(0x9C27B0)); put("red", new Color(0xF44336)); put("yellow", new Color(0xFFEB3B));
        put("green", new Color(0x4CAF50)); put("blue", new Color(0x03A9F4)); put("grey", new Color(0x333333)); put("White", new Color(0xFFFFFF));
    }};
    private static ArrayList<Grid> grids;

    public static void main(String args[]) {
        grids = new ArrayList<>();
        buildGrids();

        new Server().run();
    }

    public static ArrayList<Grid> getGrids(){
        ArrayList<Grid> newGrids = new ArrayList<>();
        for(Grid grid : grids)
            newGrids.add(grid.clone());

        return newGrids;
    }

    private static void buildGrids(){
        grids.add(buildGrid1());
        grids.add(buildGrid2());
        grids.add(buildGrid3());
        grids.add(buildGrid4());
    }

    //region Build each grid
    private static Grid buildGrid1(){
        Grid grid = new Grid();

        //region Row 1
        grid.addBlock(0, 0, new GridBlock(6));
        grid.addBlock(0, 1, new GridBlock("blue"));
        grid.addBlock(0, 2, new GridBlock());
        grid.addBlock(0, 3, new GridBlock());
        grid.addBlock(0, 4, new GridBlock(1));
        //endregion

        //region Row 2
        grid.addBlock(1, 0, new GridBlock());
        grid.addBlock(1, 1, new GridBlock(5));
        grid.addBlock(1, 2, new GridBlock("blue"));
        grid.addBlock(1, 3, new GridBlock());
        grid.addBlock(1, 4, new GridBlock());
        //endregion

        //region Row 3
        grid.addBlock(2, 0, new GridBlock(4));
        grid.addBlock(2, 1, new GridBlock("red"));
        grid.addBlock(2, 2, new GridBlock(2));
        grid.addBlock(2, 3, new GridBlock("blue"));
        grid.addBlock(2, 4, new GridBlock());
        //endregion

        //region Row 4
        grid.addBlock(3, 0, new GridBlock("green"));
        grid.addBlock(3, 1, new GridBlock(6));
        grid.addBlock(3, 2, new GridBlock("yellow"));
        grid.addBlock(3, 3, new GridBlock(3));
        grid.addBlock(3, 4, new GridBlock("purple"));
        //endregion

        return grid;
    }

    private static Grid buildGrid2(){
        Grid grid = new Grid();

        //region Row 1
        grid.addBlock(0, 0, new GridBlock(2));
        grid.addBlock(0, 1, new GridBlock());
        grid.addBlock(0, 2, new GridBlock(1));
        grid.addBlock(0, 3, new GridBlock());
        grid.addBlock(0, 4, new GridBlock());
        //endregion

        //region Row 2
        grid.addBlock(1, 0, new GridBlock());
        grid.addBlock(1, 1, new GridBlock("purple"));
        grid.addBlock(1, 2, new GridBlock("yellow"));
        grid.addBlock(1, 3, new GridBlock(1));
        grid.addBlock(1, 4, new GridBlock("green"));
        //endregion

        //region Row 3
        grid.addBlock(2, 0, new GridBlock("blue"));
        grid.addBlock(2, 1, new GridBlock(6));
        grid.addBlock(2, 2, new GridBlock("green"));
        grid.addBlock(2, 3, new GridBlock());
        grid.addBlock(2, 4, new GridBlock());
        //endregion

        //region Row 4
        grid.addBlock(3, 0, new GridBlock());
        grid.addBlock(3, 1, new GridBlock(5));
        grid.addBlock(3, 2, new GridBlock("blue"));
        grid.addBlock(3, 3, new GridBlock(6));
        grid.addBlock(3, 4, new GridBlock());
        //endregion

        return grid;
    }

    private static Grid buildGrid3(){
        Grid grid = new Grid();

        //region Row 1
        grid.addBlock(0, 0, new GridBlock(4));
        grid.addBlock(0, 1, new GridBlock());
        grid.addBlock(0, 2, new GridBlock());
        grid.addBlock(0, 3, new GridBlock());
        grid.addBlock(0, 4, new GridBlock(5));
        //endregion

        //region Row 2
        grid.addBlock(1, 0, new GridBlock("red"));
        grid.addBlock(1, 1, new GridBlock());
        grid.addBlock(1, 2, new GridBlock(1));
        grid.addBlock(1, 3, new GridBlock());
        grid.addBlock(1, 4, new GridBlock("blue"));
        //endregion

        //region Row 3
        grid.addBlock(2, 0, new GridBlock());
        grid.addBlock(2, 1, new GridBlock("purple"));
        grid.addBlock(2, 2, new GridBlock(6));
        grid.addBlock(2, 3, new GridBlock("green"));
        grid.addBlock(2, 4, new GridBlock());
        //endregion

        //region Row 4
        grid.addBlock(3, 0, new GridBlock());
        grid.addBlock(3, 1, new GridBlock(2));
        grid.addBlock(3, 2, new GridBlock("yellow"));
        grid.addBlock(3, 3, new GridBlock(3));
        grid.addBlock(3, 4, new GridBlock());
        //endregion

        return grid;
    }

    private static Grid buildGrid4(){
        Grid grid = new Grid();

        //region Row 1
        grid.addBlock(0, 0, new GridBlock(3));
        grid.addBlock(0, 1, new GridBlock());
        grid.addBlock(0, 2, new GridBlock("red"));
        grid.addBlock(0, 3, new GridBlock());
        grid.addBlock(0, 4, new GridBlock(2));
        //endregion

        //region Row 2
        grid.addBlock(1, 0, new GridBlock());
        grid.addBlock(1, 1, new GridBlock("green"));
        grid.addBlock(1, 2, new GridBlock(6));
        grid.addBlock(1, 3, new GridBlock("purple"));
        grid.addBlock(1, 4, new GridBlock());
        //endregion

        //region Row 3
        grid.addBlock(2, 0, new GridBlock("yellow"));
        grid.addBlock(2, 1, new GridBlock());
        grid.addBlock(2, 2, new GridBlock(1));
        grid.addBlock(2, 3, new GridBlock());
        grid.addBlock(2, 4, new GridBlock("blue"));
        //endregion

        //region Row 4
        grid.addBlock(3, 0, new GridBlock());
        grid.addBlock(3, 1, new GridBlock(5));
        grid.addBlock(3, 2, new GridBlock());
        grid.addBlock(3, 3, new GridBlock(4));
        grid.addBlock(3, 4, new GridBlock());
        //endregion

        return grid;
    }
    //endregion
}
