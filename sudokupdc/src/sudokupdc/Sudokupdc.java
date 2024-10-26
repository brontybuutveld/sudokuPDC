package sudokupdc;

public class Sudokupdc {
    public static void main(String[] args) {
        SudokuDB sudokudb = new SudokuDB();
        //sudokudb.deletePuzzleTable();
        int id = 20;
        new BoardUI(id, sudokudb);
    }
}
