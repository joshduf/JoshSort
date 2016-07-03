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

