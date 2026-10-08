package luogu.P1055;


import java.util.Scanner;

/**
 * 洛谷 P1055 [NOIP 2008 普及组] ISBN 号码(Easy)
 *
 * ISBN 码 x-xxx-xxxxx-x:前 9 位数字分别乘 1..9 求和,对 11 取模即为识别码(余 10 记 'X')。
 * 识别码正确输出 Right,否则输出替换识别码后的完整号码。
 * 按 '-' 分组后,除最后一组外的所有字符依次乘上递增权重即可。
 */
class Main{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println(solve(sc.nextLine()));
    }

    /** 返回判定结果:识别码正确为 Right,否则为修正后的 ISBN。 */
    static String solve(String s){
        String[]arr=s.split("-");
        int i=1;
        long sum=0;
        for(int j=0;j<arr.length-1;j++){
            for(char c:arr[j].toCharArray()){
                long num = c - '0';
                sum += num * i;
                sum %= 11;
                i++;
            }
        }
        String check = (sum == 10L) ? "X" : String.valueOf(sum);
        if(check.equals(arr[arr.length-1])){
            return "Right";
        }else {
            arr[arr.length-1]= check;
            return String.join("-",arr);
        }
    }
}
