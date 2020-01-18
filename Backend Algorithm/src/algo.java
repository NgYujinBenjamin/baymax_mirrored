import java.io.File;  
import java.io.FileInputStream;  
import java.util.*;
import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.Cell;  
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil;

import org.apache.poi.xssf.usermodel.XSSFSheet;  
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class algo{
    public static void main(String[] args){
        // 1. Read the Data from the XLSX file (not needed once integrate w frontend)
        List<Map<String, Object>> allData = readData("C:\\Users\\User\\Desktop\\FYP\\Copy of School MasterOpsPlan_10.14.19 FC_Filtered.xlsx");
    
        // Variable allProduct will store an ArrayList of Product
        // Overall Goal: Convert each row of data (represented as a HashMap in allData) into a Product Object
        List<Product> allProduct = new ArrayList<>();
        
        // 2. Create an instance of product class for each row of data. Compute the Tool Start Date using calculateToolStart(). Then, add into ArrayList of product
        for (int i = 0; i < allData.size(); i++){
            Product p = new Product(allData.get(i));
            p.calculateToolStart();
            allProduct.add(p);
            
        }
        // System.out.println(allData.size() + " vs " + allProduct.size());  // Check: Check that the number of rows matches the number of elements in the ArrayList

        // 3. Sort the ArrayList of Product by ToolStartDate.
        // Purpose: We will start assigning Products to the Bays, starting with the earliest first. 
        // This is because the end-date/ MRP date of an earlier product will mark the earliest available date to take in the next product
        Collections.sort(allProduct);

        // 4a. Generate Schedule
        // generateSchedule() will assign the product to the bays, automatically creating a new Bay if none of the Bays are available to handle the product
        // baySchedule.size() gives us the miniumum number of Bays required for 100% fulfillment
        BaySchedule baySchedule = new BaySchedule(allProduct);
        baySchedule.generateSchedule();
        System.out.println(baySchedule.getSchedule().size());

        // 4b. Generate Schedule with Cap
        // generateSchedule(int maxBays) will try to fulfill as many products as possible, while ensuring that the total number of bays used < max bays specified
        baySchedule.generateSchedule(26);
        System.out.println(baySchedule.getSchedule().size());

    }



    /**
     * To read the data from the xlsx file. Returns an ArrayList of HashMap, with each HashMap representing a single row of data in the xlsx file.
     * @param directory
     * @return
     */
    public static List<Map<String, Object>> readData (String directory){
        try {  
            File file = new File(directory);   //creating a new file instance  
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file 
            //creating Workbook instance that refers to .xlsx file  
            XSSFWorkbook wb = new XSSFWorkbook(fis);   
            XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object
            int numRows = sheet.getPhysicalNumberOfRows();
            int numCols = sheet.getRow(0).getPhysicalNumberOfCells();

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
            }
            return allData;
        }  
        catch(Exception e) {  
            e.printStackTrace();
            return null;
        }
    }
}