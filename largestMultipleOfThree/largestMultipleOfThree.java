package largestMultipleOfThree;

import java.util.Arrays;

class Solution {
    public String largestMultipleOfThree(int[] digits) {
        Arrays.sort(digits);
        int sum = 0;
        for (int digit : digits) {
            sum+=digit;
        }
        if(sum % 3 == 0) {
            // If the sum is divisible by 3, we can use all digits
            StringBuilder sb = new StringBuilder();
            for (int i = digits.length - 1; i >= 0; i--) {
                sb.append(digits[i]);
            }
            return buildResult(sb);
        }else if(sum % 3 == 1) {
            // If the sum leaves a remainder of 1 when divided by 3, we need to remove the smallest digit that also leaves a remainder of 1
            for (int i = 0; i < digits.length; i++) {
                if (digits[i] % 3 == 1) {
                    // Remove this digit and return the result
                    StringBuilder sb = new StringBuilder();
                    for (int j = digits.length - 1; j >= 0; j--) {
                        if (j == i) continue;
                        sb.append(digits[j]);
                    }
                    return buildResult(sb);
                }
            }
            int first=-1;
            int second=-1;
            int count=0;
            for(int i=0;i<digits.length;i++) {
                if(digits[i]%3==2) {
                    count++;
                    if(count==1) {
                        first=i;
                    }else if(count==2) {
                        second=i;
                    }else {
                        break;
                    }
                }
            }
            StringBuilder sb=new StringBuilder();
            for(int i=digits.length-1;i>=0;i--) {
                if(i==first || i==second)continue;
                sb.append(digits[i]);
            }
            return buildResult(sb);


        } else {
            // If the sum leaves a remainder of 2 when divided by 3, we need to remove the smallest digit that also leaves a remainder of 2
            for (int i = 0; i < digits.length; i++) {
                if (digits[i] % 3 == 2) {
                    // Remove this digit and return the result
                    StringBuilder sb = new StringBuilder();
                    for (int j = digits.length - 1; j >= 0; j--) {
                        if (j == i) continue;
                        sb.append(digits[j]);
                    }
                    return buildResult(sb);
                }
            }

            int count=0;
            int first=-1;
            int second=-1;
            for(int i=0;i<digits.length;i++) {
                if(digits[i]%3==1) {
                    count++;
                    if (count == 1) {
                        first = i;
                    } else if (count == 2) {
                        second = i;
                    } else {
                        break;
                    }
                }
            }

            StringBuilder sb=new StringBuilder();
            for(int i=digits.length-1;i>=0;i--) {
                if(i==first || i==second)continue;
                sb.append(digits[i]);
            }
            return buildResult(sb);
        }
    }


    private String buildResult(StringBuilder sb) {

        if(sb.isEmpty())return "";
        for(int i=0;i<sb.length();i++) {
            if(sb.charAt(i)!='0') {
                return sb.substring(i);
            }
        }
        return "0";
    }
}
