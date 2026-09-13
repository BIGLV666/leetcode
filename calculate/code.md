# 227. 基本计算器 II — 代码

```java
class Solution {
    public int calculate(String s) {
        Deque<String> stack = new ArrayDeque<>();

        char[] chars = s.toCharArray();
        for (int i = 0; i < s.length(); ) {
            if (Character.isDigit(chars[i])) {
                Object[] temp = f(chars, i);
                stack.addLast((String) temp[0]);
                i = (int) temp[1];
            } else if (chars[i] == '+' || chars[i] == '-') {
                stack.addLast(String.valueOf(chars[i]));
                i++;
            } else if (chars[i] == '*') {
                int r;
                for (; i < s.length(); i++) {
                    if (Character.isDigit(chars[i])) {
                        break;
                    }
                }
                Object[] temp = f(chars, i);

                r = Integer.parseInt((String) temp[0]);
                var l = Integer.parseInt(String.valueOf(stack.pollLast()));
                stack.addLast(String.valueOf((r * l)));
                i = (int) temp[1];
            } else if (chars[i] == '/') {
                int r;
                for (; i < s.length(); i++) {
                    if (Character.isDigit(chars[i])) {
                        break;
                    }
                }
                Object[] temp = f(chars, i);

                r = Integer.parseInt((String) temp[0]);
                var l = Integer.parseInt(String.valueOf(stack.pollLast()));
                stack.addLast(String.valueOf((l / r)));
                i = (int) temp[1];
            } else {
                i++;
                continue;
            }
        }

        while (stack.size() > 1) {
            Long l = Long.valueOf(stack.pollFirst());
            String m = stack.pollFirst();
            Long r = Long.valueOf(stack.pollFirst());
            switch (m) {
                case "+" -> stack.addFirst(String.valueOf(l + r));
                case "-" -> stack.addFirst(String.valueOf(l - r));
            }
        }
        return Integer.parseInt(stack.pop());

    }

    private Object[] f(char[] c, int i) {
        StringBuilder s = new StringBuilder();
        for (; i < c.length; i++) {
            if (!Character.isDigit(c[i])) {
                break;
            }
            if (Character.isDigit(c[i])) s.append(c[i]);
        }
        return new Object[]{s.toString(), i};
    }

}
```
