package main.java.authentication;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Algorithm {
    public void updateToolsToBeQueuedAfterEarliest(Object[] prevTool, Object[][] data,
                                                          ArrayList<Object[]> toolList) {
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
    public Object[] getNextClosestTool(Object[] origTool, Object[][] data) {
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
    public void reset(Object[][] data, int row) {
        for (int i = 0; i < 10; i++) {
            data[row][i] = null;
        }
    }

    /**
     * @param data
     * @return Object[] containing [argoId, tool_start, mrp, position]
     */
    public Object[] getEarliestTool(Object[][] data) {
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

}