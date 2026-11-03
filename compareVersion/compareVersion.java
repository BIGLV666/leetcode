package compareVersion;

class Solution {
    public int compareVersion(String version1, String version2) {
        String[] s2 = version2.split("\\.");
        String[] s1 = version1.split("\\.");
        int[] a1 = build(s1, Math.max(s1.length, s2.length));
        int[] a2 = build(s2, Math.max(s1.length, s2.length));

        return check(a1, a2);
    }

    private int check(int[] a1, int[] a2) {
        for (int i = 0; i < a1.length; i++) {
            if (a1[i] < a2[i]) {
                return -1;
            } else if (a1[i] > a2[i])
                return 1;
        }
        return 0;
    }

    private int[] build(String[] s, int len) {
        int[] arr = new int[len];
        int i = 0;
        for (String str : s) {
            arr[i++] = Integer.valueOf(str);
        }
        return arr;
    }
}
