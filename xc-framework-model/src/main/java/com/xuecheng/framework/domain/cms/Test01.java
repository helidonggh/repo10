package com.xuecheng.framework.domain.cms;


import java.util.Arrays;

public  class  Test01 {


    public static void main(String[] args) {
        int[] arr = {12, 20, 5, 16, 15, 1, 6, 3, 23, 9};
        sortTest(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }

    public static void sortTest(int[] arr, int left, int right) {
        if (right < left) {
            return;
        }
        int i = left;
        int j = right;
        int baseNum = arr[left];
        while (i != j) {
            while (j > i && arr[j] >= baseNum) {
                j--;
            }
            while (i < j && arr[i] <= baseNum) {
                i++;
            }
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        int temp = arr[i];
        arr[i] = arr[left];
        arr[left] =  temp;
        sortTest(arr, left, i-1);
        sortTest(arr, j+1, right);
    }
}

