package checkValid;

class Solution {
    public boolean checkValid(int[][] matrix) {

        for (int[] ints : matrix) {
            boolean[] arr = new boolean[matrix.length];
            for (int num:ints) {
                arr[num - 1] = true;
            }
            for (boolean is : arr) if (!is) return false;
        }

        for(int j=0;j< matrix.length;j++){
            boolean[] arr = new boolean[matrix.length];
            for (int[] ints : matrix) {
                arr[ints[j]-1] = true;
            }
            for(boolean is:arr)if(!is)return false;
        }
        return true;
    }
}

