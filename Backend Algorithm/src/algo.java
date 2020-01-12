import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.DateUtil;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class algo {
    public static void main(String[] args) {
        Object[][] data = readData("/Users/najulah/Desktop/files/masterops_by_tool_start.xlsx");
        SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
        Date today = new Date();

        // Date date1 = (Date) data[1][7];
        // Date date2 = (Date) data[1][9];

        int max_bays = 26, num_rows = data.length, num_cols = data[0].length;
        ArrayList<Object>[] bay_usage = new ArrayList[max_bays];
        // initialize
        for (int i = 0; i < max_bays; i++) {
            bay_usage[i] = new ArrayList<Object>();
        }

        /**
         * print all columns
         */
        String cols = "";
        for (int i = 0; i < num_cols; i++) {
            if (i < num_cols - 1) {
                cols += data[0][i] + ", ";
            } else {
                cols += data[0][i];
            }
        }
        System.out.println(cols);

        /**
         * Test getEarliestTool method
         <<<<Code test begins here>>>>>
         Object[] earliestTool = getEarliestTool(data);
         for (int i = 0; i < earliestTool.length; i++) {
         System.out.println(earliestTool[i] + " " + earliestTool[i].getClass().getSimpleName());
         }
         reset(data, (int) earliestTool[3]);
         for (int i = 0; i < earliestTool.length; i++) {
         System.out.println(earliestTool[i] + " " + earliestTool[i].getClass().getSimpleName());
         }
         System.out.println(data[0][0]+""+data[0][7]+""+data[0][8]+""+data[0][9]);
         <<<<Test end here>>>>>
         */
        int numToolsAllocated = 0;

        /**
         * for each bay in bay_usage
         *      earliestTool = getEarliestTool(data)
         *      add tool that finishes earliest to bay_usage
         *      toolsToBeQueuedAfterEarliestTool = getListOfTools(earliestTool,data)
         *      add toolsToBeQueuedAfterEarliestTool to bay_usage
         */
        for (int i = 0; i < max_bays; i++) {
            Object[] earliestTool = getEarliestTool(data);
            if (earliestTool[1] == null) {
                break;
            }
            reset(data, (int) earliestTool[3]);
            numToolsAllocated += 1;
            // System.out.println((i+1)+" "+earliestTool[0]);
            bay_usage[i].add(earliestTool);
            ArrayList<Object> toolsToBeQueuedAfterEarliest = new ArrayList<>();
            updateToolsToBeQueuedAfterEarliest(earliestTool, data, toolsToBeQueuedAfterEarliest);
            // System.out.println(toolsToBeQueuedAfterEarliest.size());
            numToolsAllocated += toolsToBeQueuedAfterEarliest.size();
            for (int j = 0; j < toolsToBeQueuedAfterEarliest.size(); j++) {
                bay_usage[i].add(toolsToBeQueuedAfterEarliest.get(j));
            }
            // break;
        }

        System.out.println(numToolsAllocated);
    }

    public static void updateToolsToBeQueuedAfterEarliest(Object[] prevTool, Object[][] data,
                                                          ArrayList<Object> toolList) {
        Object[] nextClosestTool = getNextClosestTool(prevTool, data);
        if (nextClosestTool[0] == null) {
            return;
        }
        toolList.add(nextClosestTool);
        reset(data, (int) nextClosestTool[3]);
        updateToolsToBeQueuedAfterEarliest(nextClosestTool, data, toolList);
    }

    /**
     * @param origTool
     * @param data
     * @return the next closest tool [argoId, tool_start, mrp, position]
     * which will start right after origTool and end the fastest
     */
    public static Object[] getNextClosestTool(Object[] origTool, Object[][] data) {
        Object[] result = new Object[4];
        Date origToolMRP = (Date) origTool[2];
        Date origToolStart = (Date) origTool[1];

        Object[] prevTool = getToolThatStartAfterOrigTool((Date) origTool[2], (int) origTool[3], data);
        if (prevTool[0] == null) {
            // if there is no tool that will start after original tool
            // return null result
            return result;
        }

        Date prevToolMRP = (Date) prevTool[2];
        Date prevToolStart = (Date) prevTool[1];
        int prevToolPos = (int) prevTool[3];

        long prevStartDiff = prevToolStart.getTime() - origToolMRP.getTime();
        long prevEndDiff = prevToolMRP.getTime() - origToolStart.getTime();
        // System.out.println(prevStartDiff + " " + prevEndDiff);

        for (int i = prevToolPos + 1; i < data.length; i++) {
            if (data[i][0] == null) {
                continue;
            }
            Date currToolStart = (Date) data[i][9];
            Date currToolMRP = (Date) data[i][7];
            long currStartDiff = currToolStart.getTime() - origToolMRP.getTime();
            long currEndDiff = currToolMRP.getTime() - origToolStart.getTime();

            if (currToolStart.compareTo(origToolMRP) == 1 && currStartDiff < prevStartDiff && currEndDiff < prevEndDiff) {
                // current tool start later then original tool end date
                // current tool end earlier than previous tools compared
                prevStartDiff = currStartDiff;
                prevEndDiff = currEndDiff;
                prevToolPos = i;
            }
        }
        result[0] = data[prevToolPos][0];
        result[1] = data[prevToolPos][9];
        result[2] = data[prevToolPos][7];
        result[3] = prevToolPos;
        return result;
    }

    /**
     * @param origTool
     * @param data
     * @return Object[] containing [argoId, tool_start, mrp, position]
     */
    public static Object[] getToolThatStartAfterOrigTool(Date prevToolEnd, int prevToolPos, Object[][] data) {
        Object[] result = new Object[4];
        for (int i = prevToolPos + 1; i < data.length; i++) {
            if (data[i][0] == null) {
                continue;
            }
            Date currToolStart = (Date) data[i][9];
            if (currToolStart.compareTo(prevToolEnd) == 1) {
                result[0] = data[i][0];
                result[1] = data[i][9];
                result[2] = data[i][7];
                result[3] = i;
                break;
            }
        }
        return result;
    }

    /**
     * reset the values for the row in data to be null
     *
     * @param data
     * @param row
     */
    public static void reset(Object[][] data, int row) {
        for (int i = 0; i < 10; i++) {
            data[row][i] = null;
        }
    }

    /**
     * @param data
     * @return Object[] containing [argoId, tool_start, mrp, position]
     */
    public static Object[] getEarliestTool(Object[][] data) {
        Object[] result = new Object[4];

        int earliestToolPos = getFirstNonNullPos(data);
        Date earliestToolMRP = (Date) data[earliestToolPos][7]; // firstRowValue

        // i from 2 since 0 is column title
        for (int i = 2; i < data.length; i++) {
            if (data[i][7] == null) {
                continue;
            }

            Date currToolMRP = (Date) data[i][7];
            if (currToolMRP.compareTo(earliestToolMRP) == -1) {
                earliestToolPos = i;
                earliestToolMRP = currToolMRP;
            }
        }

        result[0] = data[earliestToolPos][0];
        result[1] = data[earliestToolPos][9];
        result[2] = data[earliestToolPos][7];
        result[3] = earliestToolPos;
        return result;
    }

    /**
     * @param data
     * @return
     */
    public static int getFirstNonNullPos(Object[][] data) {
        int pos = 1;
        for (int j = 1; j < data.length; j++) {
            if (data[j][7] != null) {
                pos = j;
                break;
            }
        }
        return pos;
    }

    /**
     * To read the data from the xlsx file
     */
    public static Object[][] readData(String directory) {
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
            while (iter.hasNext()) {
                Row row = iter.next();
                int rowNum = row.getRowNum();

                Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    int colNum = cell.getColumnIndex();

                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:    //field that represents string cell type
                            allData[rowNum][colNum] = cell.getStringCellValue();
                            break;
                        case Cell.CELL_TYPE_NUMERIC:    //field that represents number cell type
                            if (DateUtil.isCellDateFormatted(cell)) {
                                allData[rowNum][colNum] = cell.getDateCellValue();
                            } else {
                                allData[rowNum][colNum] = cell.getNumericCellValue();
                            }
                            break;
                    }
                }
            }
            return allData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}