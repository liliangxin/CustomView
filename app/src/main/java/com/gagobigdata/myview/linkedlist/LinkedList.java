package com.gagobigdata.myview.linkedlist;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by llx on 2018/11/7.
 */
// A simple Java program to introduce a linked list
class LinkedList {
    Node head; // head of list

    /* Linked list Node. This inner class is made static so that
    main() can access it */
    static class Node {
        int data;
        Node next;

        Node(int d) {
            data = d;
            next = null;
        } // Constructor
    }

    /* method to create a simple linked list with 3 nodes*/
    public static void main(String[] args) {
        int[] twoSum = twoSum(new int[]{2, 7, 11, 15}, 9);
        System.out.println(twoSum[0]+" "+twoSum[1]);

    }

    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[] { map.get(complement), i };
            }
            map.put(nums[i], i);
        }
        throw new IllegalArgumentException("No two sum solution");
    }
}
