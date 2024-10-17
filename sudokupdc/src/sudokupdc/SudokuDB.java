package sudokupdc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SudokuDB {

    private final DBManager dbManager;
    private final Connection conn;
    private Statement statement;
    
    public SudokuDB() {
        dbManager = new DBManager();
        conn = dbManager.getConnection();
    }

    public void createPuzzleTable() {
        if (isTable("PUZZLE")) {
            return;
        }

        String createTable = 
                "CREATE TABLE PUZZLE ("
                    + "ID INT AUTO_INCREMENT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                    + "DATA VARCHAR(81),"
                    + "SOL VARCHAR(81)"
                + ")";
        
        dbManager.updateDB(createTable);
    }
    
    public void deletePuzzleTable() {
        if (isTable("PUZZLE")) {
            dbManager.updateDB("DROP TABLE PUZZLE");
        }
    }
    public void createMoveTable() {
        if (isTable("MOVE")) {
            return;
        }

        String createTable = 
                "CREATE TABLE MOVE ("
                    + "move_id INT AUTO_INCREMENT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                    + "puzzle_id INT NOT NULL,"
                    + "row INT NOT NULL,"
                    + "col INT NOT NULL,"
                    + "value INT NOT NULL,"
                    + "prev INT,"
                    + "FOREIGN KEY (puzzle_id) REFERENCES Puzzles(id)"
                + ")";
        
        dbManager.updateDB(createTable);
    }
    
    public void deleteMoveTable() {
        if (isTable("MOVE")) {
            dbManager.updateDB("DROP TABLE MOVE");
        }
    }
    
    public void insertMoveTable(int id, int row, int col, int val, int prev) {
        
    }
    
    public void insertPuzzleTable(Integer[][] data, Integer[][] sol) {
        String Data = "";
        for (Integer[] row : data) {
            for (Integer col : row) {
                Data += String.valueOf(col);
            }
        }
        String Sol = "";
        for (Integer[] row : sol) {
            for (Integer col : row) {
                Sol += String.valueOf(col);
            }
        }
        String record = "INSERT INTO PUZZLE (DATA, SOL) VALUES ("+ Data +", "+ Sol +")";
        dbManager.updateDB(record);
    }

    /*public ResultSet getWeekSpecial() {
        String query = "SELECT TITLE, PRICE, DISCOUNT FROM BOOK, PROMOTION WHERE BOOK.CATEGORY=PROMOTION.CATEGORY";
        ResultSet rs = dbManager.queryDB(query);
        
        return (rs);
    }*/
    
    public boolean isTable(String tableName) {
        boolean tableExists = false;
        try {
            DatabaseMetaData dbMetaData = conn.getMetaData();
            ResultSet rs = dbMetaData.getTables(null, null, tableName.toUpperCase(), null);

            if (rs.next()) {
                tableExists = true;
            }

            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(SudokuDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tableExists;
    }

    /*public void createWeekSpecialTable(ResultSet rs) {
        try {
            if (isTable("WEEKSPECIAL")) {
                dbManager.updateDB("DROP TABLE WEEKSPECIAL");
            }
            String createTable = "CREATE TABLE WEEKSPECIAL (TITLE VARCHAR(50), SPECIALPRICE FLOAT)";
            dbManager.updateDB(createTable);
            
            while (rs.next()) {
                int discount = rs.getInt("DISCOUNT");
                float price = rs.getFloat("PRICE");
                String title = rs.getString("TITLE");

                Float specialPrice = (1 - (discount / 100.0f)) * price;
                String insertRecords = "INSERT INTO WEEKSPECIAL (TITLE, SPECIALPRICE) VALUES ('" + title + "', " + specialPrice + ")";

                dbManager.updateDB(insertRecords);
            }
            
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(SudokuDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/

}
