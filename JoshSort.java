/*
JoshSort:
Own sort implementation
Beats Arrays.sort implementation of TimSort for objects by 30% to over 50%*

Minimizes compares and moves
Checks for sections of data sorted in ascending or descending order
Minimum sorted section is 3
Uses a merge sort to combine sorted runs

Uses a binary search to identify insertion point for merge
Stops merge when end of either section is reached
Moves unmerged sections in place
Moves merged sections back to original array

Best: O(n), data is sorted either ascendingly or descendingly
Worst: O(n log n), data is completely random
Partially sorted data: speed is inbetween best and worst cases

Sort is stable

Copyright 2016 Joshua DuFault

This file is part of JoshSort.

JoshSort is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

JoshSort is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with ProtectedDesktops.  If not, see <http://www.gnu.org/licenses/>.

*Testing done on 3 arrays with 1,000,000 records each.
1 array using mostly-sorted data, 1 array using semi-sorted data, 1 array using unsorted data
Testing files are included: mostlySorted.txt, semiSorted.txt, unsorted.txt
Tests were done multiple times, alternating the sorting algorithm performed first and results were consistent.
Platform: i5 laptop with 8Gb RAM

Java Class UML
|-------------------------------------------------------------------------------
| JoshSort
|-------------------------------------------------------------------------------
| - sort : String[]
|-------------------------------------------------------------------------------
| + sort(String) : void
| - JoshSort(int, int, int) : void
| - sortedNums(int, int, int) : int
| - getFlipReversed(int, int, int) : int
| - given2Sort3(int, int) : int
| - simpleInsert(int, int) : void
| - insertSection(int, int, int) : void
| - binaryMerge(int, int, int) : void
| - getInsertGreater(int, int, int) : int
| - getInsertGreaterEqual(int, int, int) : int
| - compare(String, String) : int
--------------------------------------------------------------------------------
*/
package sorttest;

/**
 *
 * @author Josh DuFault
 */
public class JoshSort {
    static private String[] toSort;
    static private String[] tempSort;
    // binaryMerge variables
    
    public static void sort(String[] inputArray) {
        int lastIndex;
        int nulls = 0;

        // If there are less than 2 values, it's sorted
        if (inputArray == null || inputArray.length <= 1 ||
            inputArray[0] == null || inputArray[1] == null)
        {
            return;
        }
        else
        {
            toSort = inputArray;
            lastIndex = toSort.length - 1;
        }
        
        // Don't sort nulls at end of array
        while (nulls < toSort.length
               && toSort[lastIndex - nulls] == null)
        {
            nulls++;
        }

        tempSort = new String[toSort.length - nulls];
        JoshSort(0, lastIndex - nulls, 1);
    }

    // Identifies ascendingly or descendingly sorted sections and calls merge on them
    private static void JoshSort(int lowValue, int highValue, int sortedNums) {
        int centerValue, sortedNumsIndex, unsorted;

        // If this section hasn't been sorted yet, sort it
        if (sortedNums == 1)
        {
            sortedNums = getFlipReversed(lowValue, highValue, sortedNums);
            sortedNums = sortedNums(lowValue, highValue, sortedNums);

            sortedNumsIndex = lowValue + sortedNums - 1;

            // If 2 are sorted at this point, 3rd can be sorted with 1 comparison and 3 or 4 moves
            if (sortedNums == 2 && sortedNumsIndex < highValue)
            {
                sortedNums = given2Sort3(lowValue, sortedNums);
            }
        }

        // Update after getting final sortedNums
        sortedNumsIndex = lowValue + sortedNums - 1;
        unsorted = highValue - sortedNumsIndex;

        // Get center value using already sorted data as minimum
        centerValue = (lowValue + highValue) / 2 > sortedNumsIndex
                      ? (lowValue + highValue) / 2 : sortedNumsIndex;

        // Recurses remaining unsorted array
        // If more than 3 unsorted, run left side again
        if (unsorted >= 4)
        {
            JoshSort(lowValue, centerValue, sortedNums);
        }
        // If more than 1 unsorted, run right side again
        if (unsorted >= 2)
        {
            JoshSort(centerValue + 1, highValue, 1);
        }
        // If any unsorted, merge
        if (unsorted >= 1
            && compare(toSort[centerValue], toSort[centerValue + 1]) > 0)
        {
            binaryMerge(lowValue, centerValue, highValue);
        }
    }
    
    // Gets number now sorted
    private static int sortedNums(int lowValue, int highValue, int sortedNums) {
        int sortedNumsIndex = lowValue + sortedNums - 1;
                
        while (sortedNumsIndex < highValue
               && compare(toSort[sortedNumsIndex + 1], toSort[sortedNumsIndex])
                  >= 0)
        {
            sortedNumsIndex++;
            sortedNums++;
        }

        return sortedNums;
    }
    
    // Gets number strictly reversed and flips
    private static int getFlipReversed(int lowValue, int highValue, int sortedNums) {
        int sortedNumsIndex = lowValue + sortedNums - 1;

        while (sortedNumsIndex < highValue
               && compare(toSort[sortedNumsIndex + 1], toSort[sortedNumsIndex])
                  < 0)
        {
            sortedNumsIndex++;
            sortedNums++;
        }
        
        // Reverses section
        for (int i = 0; i < sortedNums / 2; i++)
        {
            tempSort[lowValue + i] = toSort[lowValue + i];
            toSort[lowValue + i] = toSort[sortedNumsIndex - i];
            toSort[sortedNumsIndex - i] = tempSort[lowValue + i];
        }
        
        if (sortedNums < 2)
        {
            return 2;
        }
        else
        {
            return sortedNums;
        }
    }

    // If 2 are sorted at this point, 3rd can be sorted with 1 comparison and 3 or 4 moves
    private static int given2Sort3(int lowValue, int sortedNums) {
        int sortedNumsIndex = lowValue + sortedNums - 1;

        if (compare(toSort[sortedNumsIndex - 1], toSort[sortedNumsIndex + 1]) > 0)
        {
            simpleInsert(sortedNumsIndex + 1, sortedNumsIndex - 1);
        }
        else
        {
            simpleInsert(sortedNumsIndex + 1, sortedNumsIndex);
        }

        // Sorted is 3
        return 3;
    }

    // Inserts at index
    private static void simpleInsert(int toMove, int insertLocation) {
        tempSort[toMove] = toSort[toMove];

        for (int i = 0; i < toMove - insertLocation; i++)
        {
            toSort[toMove - i] = toSort[toMove - 1 - i];
        }
        toSort[insertLocation] = tempSort[toMove];
    }    

    // Inserts range of array values into specified section using minimum number of operations
    private static void insertSection(int startToInsert, int endToInsert, int insertionPoint) {
        int numToInsert = endToInsert - startToInsert + 1;
        int numToShift = startToInsert - insertionPoint;
        int endToShift = startToInsert - 1;

        // Move the smaller section to the temp array and shift the other
        if (numToInsert <= numToShift) {
            for (int i = 0; i < numToInsert; i++) {
                tempSort[insertionPoint + i] = toSort[startToInsert + i];
            }
            for (int i = 0; i < numToShift; i++) {
                toSort[endToInsert - i] = toSort[endToShift - i];
            }
            for (int i = 0; i < numToInsert; i++) {
                toSort[insertionPoint + i] = tempSort[insertionPoint + i];
            }
        }
        else
        {
            for (int i = 0; i < numToShift; i++) {
                tempSort[endToInsert - i] = toSort[endToShift - i];
            }
            for (int i = 0; i < numToInsert; i++) {
                toSort[insertionPoint + i] = toSort[startToInsert + i];
            }
            for (int i = 0; i < numToInsert; i++) {
                toSort[endToInsert - i] = tempSort[endToInsert - i];
            }
        }
    }    

    // Merges already sorted arrays
    private static void binaryMerge(int startValue, int midValue, int highValue){
        int tempIndex = startValue;
        int lowerIndex = startValue;
        int upperIndex = midValue + 1;
        int insertPoint = 0;
        int i;
        
        // If all of lower array is strictly larger than all of higher array, no more comparisons needed
        if (compare(toSort[startValue], toSort[highValue]) > 0)
        {
            insertSection(midValue + 1, highValue, startValue);
            return;
        }
        
        // Get insertion point and make known moves
        if (compare(toSort[startValue], toSort[midValue + 1]) <= 0)
        {
            insertPoint = getInsertGreater(midValue + 1, startValue + 1, midValue);
            lowerIndex = insertPoint;
            tempSort[startValue] = toSort[midValue + 1];
            tempIndex++;
            upperIndex++;
        }
        else
        {
            if (midValue + 2 == highValue)
            {
                insertPoint = highValue;
            }
            else
            {
                insertPoint = getInsertGreaterEqual(startValue, midValue + 2, highValue);
            }
            upperIndex = insertPoint;
            tempSort[startValue] = toSort[startValue];
            tempIndex++;
            lowerIndex++;
        }

        // Compare element in lower and upper arrays and sort into temp array
        while(lowerIndex <= midValue && upperIndex <= highValue)
        {
            if(compare(toSort[lowerIndex], toSort[upperIndex]) <= 0)
            {
                tempSort[tempIndex] = toSort[lowerIndex];
                lowerIndex++;
            }
            else
            {
                tempSort[tempIndex] = toSort[upperIndex];
                upperIndex++;
            }
            tempIndex++;
        }

        // If there's leftover at the start of the upper array, move it to the start of the array
        // Make sure there's room first
        while(lowerIndex - startValue < insertPoint - (midValue + 1))
        {
            tempSort[tempIndex] = toSort[lowerIndex];
            lowerIndex++;
            tempIndex++;
        }
        i = 0;
        while (midValue + 1 + i < insertPoint )
        {
            toSort[startValue + i] = toSort[midValue + 1 + i];
            i++;
        }

        // If there's leftover at the end of the lower array, move it to the end of the array
        i = 0;
        while(lowerIndex <= midValue)
        {
            toSort[highValue - i] = toSort[midValue - i];
            i++;
            lowerIndex++;
        }
 
        // Get point to insert temp back into array
        if (insertPoint >= midValue + 1)
        {
            insertPoint = startValue + insertPoint - (midValue + 1);
        }
        
        for (i = startValue; i < tempIndex; i++)
        {
            toSort[insertPoint] = tempSort[i];
            insertPoint++;
        }
    }

    // Finds first value greater or equal using binary search
    private static int getInsertGreater(int toFind, int low, int high) {
        int mid;
        
        while (low != high)
        {
            mid = low + (high - low) / 2;

            if (compare(toSort[mid], toSort[toFind]) <= 0)
            {
                low = mid + 1;
            }
            else
            {
                high = mid;
            }
        }
        
        return high;
    }
    
    // Finds first value greater or equal using binary search
    private static int getInsertGreaterEqual(int toFind, int low, int high) {
        int mid;
        
        while (low != high)
        {
            mid = low + (high - low) / 2;

            if (compare(toSort[mid], toSort[toFind]) < 0)
            {
                low = mid + 1;
            }
            else
            {
                high = mid;
            }
        }

        return high;
    }

    private static int compare(String o1, String o2) {
        // Nulls move to end or array
        if (o1 == null && o2 == null)
        {
            return 0;
        }
        else if (o1 == null)
        {
            return 1;
        }
        else if (o2 == null)
        {
            return -1;
        }
        else
        {
            return o1.compareTo(o2);
        }
    }
}
