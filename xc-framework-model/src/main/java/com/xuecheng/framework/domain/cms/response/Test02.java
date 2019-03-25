package com.xuecheng.framework.domain.cms.response;

import java.util.Arrays;

public class Test02 {

    public static void main(String[] args) {
        int[] array = {12, 20, 5, 16, 15, 1, 30, 45, 23, 9};
   /* Random random = new Random();
    int[] array = new int[1000000];
    for (int i = 0; i < array.length-1; i++) {
       array[i]= random.nextInt(1000000);
    }*/
        int start = 0;
        int end = array.length - 1;
        mySort(array, start, end);
   /* long s1 = System.currentTimeMillis();
    long s2 = System.currentTimeMillis();
    System.out.println(s2-s1);*/
        System.out.println(Arrays.toString(array));
    }
    private static void mySort(int[] array, int low, int high) {
        if (low > high) {
            return;
        }
        int i = low;
        int j = high;
        int key = array[low];
        while (i < j) {
            while (i < j && array[j] > key) {
                j--;
            }
            while (i < j && array[i] <= key) {
                i++;
            }
            int number = array[i];
            array[i] = array[j];
            array[j] = number;
        }
        int p = array[i];
        array[i] = array[low];
        array[low] = p;
        mySort(array, low, i - 1);
        mySort(array, i + 1, high);
    }

}
