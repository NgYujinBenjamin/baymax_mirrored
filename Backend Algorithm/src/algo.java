import java.io.File;  
import java.io.FileInputStream;  
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;  
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.DateUtil;

import org.apache.poi.xssf.usermodel.XSSFSheet;  
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class algo{
    public static void main(String[] args){
        Object[][] allData = readData("C:\\Users\\User\\Desktop\\FYP\\Copy of School MasterOpsPlan_10.14.19 FC.xlsx");
        // Test
        // System.out.println(allData[0][0]);
    }

    /**
     * To read the data from the xlsx file
     */
    public static Object[][] readData (String directory){
        try {  
            File file = new File(directory);   //creating a new file instance  
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file 
            //creating Workbook instance that refers to .xlsx file  
            XSSFWorkbook wb = new XSSFWorkbook(fis);   
            XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object
            int numRows = sheet.getPhysicalNumberOfRows();
            int numCols = sheet.getRow(0).getPhysicalNumberOfCells();

            Object[][] allData = new Object[numRows][numCols];

            Iterator<Row> iter = sheet.iterator();    //iterating over excel file             
            while (iter.hasNext()){  
                Row row = iter.next();
                int rowNum = row.getRowNum();

                Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column  

                while (cellIterator.hasNext()){  
                    Cell cell = cellIterator.next();  
                    int colNum = cell.getColumnIndex();
                    
                    switch (cell.getCellType()){  
                        case Cell.CELL_TYPE_STRING:    //field that represents string cell type  
                            allData[rowNum][colNum] = cell.getStringCellValue();  
                            break;  
                        case Cell.CELL_TYPE_NUMERIC:    //field that represents number cell type  
                            if (DateUtil.isCellDateFormatted(cell)){
                                allData[rowNum][colNum] = cell.getDateCellValue();
                            } else {
                                allData[rowNum][colNum] = cell.getNumericCellValue();
                            }
                            break;
                    }  
                }  
            }
            return allData;
        }  
        catch(Exception e) {  
            e.printStackTrace();
            return null;
        }
    }
}