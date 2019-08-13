package com.shoppingcart.exceptionhandling;

class RotateArray {
    public void main(String args[]) {
        int k = 3;
        RotateArray rotate = new RotateArray();
        int[] array = new int[] { 1, 2, 3, 4, 5, 6, 7, 8 };
        array = rotate.rotateArray(array, k);
        rotate.printArray(array);
    }

    private int[] rotateLeftOnce(int[] array) {
        int index, temp;
        temp = array[0];
        int n = array.length;
        for (index = 0; index < n - 1; index++)
            array[index] = array[index + 1];
        array[index] = temp;
        return array;
    }

    public int[] rotateArray(int[] array, int k) {
        for (int index = 0; index < k; index++) {
            array = rotateLeftOnce(array);
        }
        return array;
    }

    private void printArray(int[] array) {
        for (int index = 0; index < array.length; index++) {
            System.out.println(array[index]);
        }

    }

}
