import java.io.File;  
import java.io.FileInputStream;  
import java.util.*;
import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.Cell;  
<<<<<<< Updated upstream
import org.apache.poi.ss.usermodel.Row;
=======
import org.apache.poi.ss.usermodel.*;
>>>>>>> Stashed changes
import org.apache.poi.ss.usermodel.DateUtil;

import org.apache.poi.xssf.usermodel.XSSFSheet;  
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

<<<<<<< Updated upstream
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
=======

public class algo{
    public static void main(String[] args){
        // 1. Read the Data from the XLSX file (not needed once integrate w frontend)
        List<Map<String, Object>> allData = readData("C:\\Users\\User\\Desktop\\FYP\\Copy of School MasterOpsPlan_10.14.19 FC_Filtered.xlsx");
    
        
        List<Product> allProduct = new ArrayList<>();
        // 2. Compute the Tool Start Date and Append the information in the HashMap (which represents each row of Data)
        
        for (int i = 0; i < allData.size(); i++){
            Product p = new Product(allData.get(i));
            p.calculateToolStart();
            allProduct.add(p);
            
        }
        // System.out.println(allData.size() + " vs " + allProduct.size());  // Check: Check that the number of rows matches the number of elements in the ArrayList

    }



    /**
     * To read the data from the xlsx file. Returns an ArrayList of HashMap, with each HashMap representing a single row of data in the xlsx file.
     * @param directory
     * @return
     */
    public static List<Map<String, Object>> readData (String directory){
>>>>>>> Stashed changes
        try {  
            File file = new File(directory);   //creating a new file instance  
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file 
            //creating Workbook instance that refers to .xlsx file  
            XSSFWorkbook wb = new XSSFWorkbook(fis);   
            XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object
            int numRows = sheet.getPhysicalNumberOfRows();
            int numCols = sheet.getRow(0).getPhysicalNumberOfCells();

<<<<<<< Updated upstream
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
=======
            Row firstRow = sheet.getRow(0);
            
            int rowCount = sheet.getLastRowNum();
            int colCount = sheet.getRow(0).getLastCellNum();

            List<Map<String, Object>> allData = new ArrayList<Map<String, Object>>();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            
            for (int rowNum = 1; rowNum < rowCount; rowNum++){  // For each subsequent row
                Map<String, Object> rowData = new HashMap<String, Object>();
                Row r = sheet.getRow(rowNum);

                for (int colNum = 0; colNum < colCount; colNum++){ // For each cell in the row
                    Cell cell = r.getCell(colNum, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);  

                    String cellColName = firstRow.getCell(colNum).getStringCellValue();

                    if (cell == null){
                        rowData.put(cellColName, null);
                        // System.out.print(null + " | ");
                    } else {
                        switch (cell.getCellType()){  
                            case Cell.CELL_TYPE_STRING:    //field that represents string cell type  
                                rowData.put(cellColName, cell.getStringCellValue());
                                // System.out.print(cell.getStringCellValue() + " | ");
                                break;  
                            
                            case Cell.CELL_TYPE_NUMERIC:    //field that represents number cell type  
                                if (DateUtil.isCellDateFormatted(cell)){
                                    String date = dateFormat.format(cell.getDateCellValue());
                                    rowData.put(cellColName, date);
                                    // System.out.print(date + " | ");
                                } else {
                                    // System.out.print(cell.getNumericCellValue() + " | ");
                                    rowData.put(cellColName, (int) cell.getNumericCellValue());
                                }
                                break;
                        }
                    }
                }
                allData.add(rowData);
                // System.out.println("-*-*-*-*-*-*-"); 
>>>>>>> Stashed changes
            }
            return allData;
        }  
        catch(Exception e) {  
            e.printStackTrace();
            return null;
        }
    }
}