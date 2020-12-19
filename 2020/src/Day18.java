import java.io.*;
import java.util.*;

public class Day18 {
    public static Scan sc = new Scan();
    public static Print pr = new Print();
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        String s;
        long answer = 0L;
        while ((s = br.readLine()) != null && s.compareTo("") != 0) {
            // answer += evaluateExpression(s.trim());
            answer += evaluateExpression2(s.trim());
        }
        pr.println(answer);
        pr.close();
    }

    public static long evaluateExpression(String s) throws Exception {
        Stack<String> stack = new Stack<>();
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c == ' ') {
                continue;
            } else if (c == '*' || c == '+' || c == '(') {
                stack.push("" + c);
            } else if (Character.isDigit(c)) {
                long n = c - '0';
                for (int j = i + 1; j < len; j++) {
                    char nc = s.charAt(j);
                    if (Character.isDigit(nc)) {
                        n = (n * 10L) + (nc - '0');
                    } else {
                        break;
                    }
                    i++;
                }
                stack.push("" + n);
            } else if (c == ')') {
                String num = stack.pop();
                stack.pop();
                stack.push(num);
            }
            evaluate(stack);
        }
        evaluate(stack);
        if (stack.size() != 1)
            throw new Error("Could not parse for " + s);
        long answer = Long.parseLong(stack.pop());

        return answer;
    }

    public static void evaluate(Stack<String> stack) {
        if (stack.isEmpty() || stack.size() < 3) {
            return;
        }
        String three = stack.pop();
        String two = stack.pop();
        String one = stack.pop();
        if (!isNumber(three) || !isNumber(one)) {
            stack.push(one);
            stack.push(two);
            stack.push(three);
            return;
        }
        long num2 = Long.parseLong(three);
        long num1 = Long.parseLong(one);
        if (two.compareTo("+") == 0) {
            stack.push("" + (num1 + num2));
        } else {
            stack.push("" + (num1 * num2));
        }
    }

    public static long evaluateExpression2(String s) throws Exception {
        Stack<String> stack = new Stack<>();
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c == ' ') {
                continue;
            } else if (c == '*' || c == '+' || c == '(') {
                stack.push("" + c);
            } else if (Character.isDigit(c)) {
                long n = c - '0';
                for (int j = i + 1; j < len; j++) {
                    char nc = s.charAt(j);
                    if (Character.isDigit(nc)) {
                        n = (n * 10L) + (nc - '0');
                    } else {
                        break;
                    }
                    i++;
                }
                stack.push("" + n);

            } else if (c == ')') {
                evaluate2(stack);
            }
        }
        while (stack.size() != 1) {
            evaluate2(stack);
        }
        long answer = Long.parseLong(stack.pop());

        return answer;
    }

    public static void evaluate2(Stack<String> stack) {
        if (stack.isEmpty() || stack.size() < 3) {
            return;
        }
        Stack<String> stack2 = new Stack<>();
        while (!stack.isEmpty() && stack.size() >= 3) {
            String three = stack.pop();
            String two = stack.pop();
            String one = stack.pop();
            if (isNumber(three) && two.compareTo("(") == 0) {
                stack.push(one);
                stack.push(three);
                break;
            } else if (!isNumber(three) || !isNumber(one)) {
                stack.push(one);
                stack.push(two);
                stack.push(three);
                break;
            }
            long num2 = Long.parseLong(three);
            long num1 = Long.parseLong(one);
            if (two.compareTo("+") == 0) {
                stack.push("" + (num1 + num2));
            } else {
                stack2.push(three);
                stack.push(one);
            }
        }
        if (stack.size() == 2) {
            String three = stack.pop();
            String two = stack.pop();
            if (isNumber(three) && two.compareTo("(") == 0) {
                stack.push(three);
            }
        }
        if (!stack2.isEmpty()) {
            long x = Long.parseLong(stack.pop());
            while (!stack2.isEmpty()) {
                x = x * Long.parseLong(stack2.pop());
            }
            stack.push("" + x);
        }
    }

    public static boolean isNumber(String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    static class Scan {
        private byte[] buf = new byte[1024];
        private int index;
        private InputStream in;
        private int total;

        public Scan() {
            in = System.in;
        }

        public int scan() throws IOException {
            if (total < 0)
                throw new InputMismatchException();
            if (index >= total) {
                index = 0;
                total = in.read(buf);
                if (total <= 0)
                    return -1;
            }
            return buf[index++];
        }

        public int scanInt() throws IOException {
            int integer = 0;
            int n = scan();
            while (isWhiteSpace(n))
                n = scan();
            int neg = 1;
            if (n == '-') {
                neg = -1;
                n = scan();
            }
            while (!isWhiteSpace(n)) {
                if (n >= '0' && n <= '9') {
                    integer *= 10;
                    integer += n - '0';
                    n = scan();
                } else
                    throw new InputMismatchException();
            }
            return neg * integer;
        }

        public double scanDouble() throws IOException {
            double doub = 0;
            int n = scan();
            while (isWhiteSpace(n))
                n = scan();
            int neg = 1;
            if (n == '-') {
                neg = -1;
                n = scan();
            }
            while (!isWhiteSpace(n) && n != '.') {
                if (n >= '0' && n <= '9') {
                    doub *= 10;
                    doub += n - '0';
                    n = scan();
                } else
                    throw new InputMismatchException();
            }
            if (n == '.') {
                n = scan();
                double temp = 1;
                while (!isWhiteSpace(n)) {
                    if (n >= '0' && n <= '9') {
                        temp /= 10;
                        doub += (n - '0') * temp;
                        n = scan();
                    } else
                        throw new InputMismatchException();
                }
            }
            return doub * neg;
        }

        public String scanString() throws IOException {
            StringBuilder sb = new StringBuilder();
            int n = scan();
            while (isWhiteSpace(n))
                n = scan();
            while (!isWhiteSpace(n)) {
                sb.append((char) n);
                n = scan();
            }
            return sb.toString();
        }

        private boolean isWhiteSpace(int n) {
            if (n == ' ' || n == '\n' || n == '\r' || n == '\t' || n == -1)
                return true;
            return false;
        }
    }

    static class Print {
        private final BufferedWriter bw;

        public Print() {
            this.bw = new BufferedWriter(new OutputStreamWriter(System.out));
        }

        public void print(Object object) throws IOException {
            bw.append("" + object);
        }

        public void println(Object object) throws IOException {
            print(object);
            bw.append("\n");
        }

        public void close() throws IOException {
            bw.close();
        }
    }
}