import java.io.*;
import java.util.*;

public class Day09 {
    public static Scan sc = new Scan();
    public static Print pr = new Print();
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static final int PREAMBLE_SIZE = 25;

    public static void main(String[] args) throws Exception {
        // solvePartOne();
        solvePartTwo();
        pr.close();
    }

    public static void solvePartOne() throws Exception {
        Queue<Integer> preamble = new ArrayDeque<>();
        while (true) {
            int x;
            try {
                x = sc.scanInt();
            } catch (Exception e) {
                break;
            }
            if (preamble.size() < PREAMBLE_SIZE) {
                preamble.add(x);
            } else {
                if (!isValid(preamble, x)) {
                    pr.println(x);
                    break;
                } else {
                    preamble.remove();
                    preamble.add(x);
                }
            }
        }
    }

    public static void solvePartTwo() throws Exception {
        Queue<Integer> preamble = new ArrayDeque<>();
        List<Integer> list = new ArrayList<>();
        while (true) {
            int x;
            try {
                x = sc.scanInt();
            } catch (Exception e) {
                break;
            }
            if (preamble.size() < PREAMBLE_SIZE) {
                preamble.add(x);
            } else {
                if (!isValid(preamble, x)) {
                    int answer = find(list, x);
                    pr.println(answer);
                    break;
                } else {
                    preamble.remove();
                    preamble.add(x);
                }
            }
            list.add(x);
        }
    }

    public static boolean isValid(Queue<Integer> q, int x) {
        Set<Integer> s = new HashSet<>();
        for (int n : q)
            s.add(n);
        for (int n : s) {
            int target = x - n;
            if (target != n && s.contains(target))
                return true;
        }
        return false;
    }

    public static int find(List<Integer> l, int target) {
        int sum = 0;
        int left = 0;
        int right = 0;
        int size = l.size();

        int[] prefixSum = new int[size + 1];
        for (int i = 1; i <= size; i++) {
            prefixSum[i] = prefixSum[i - 1] + l.get(i - 1);
        }
        while (left < size && right < size) {
            int sumRange = prefixSum[right + 1] - prefixSum[left];
            if (sumRange == target) {
                sum = findMin(l, left, right) + findMax(l, left, right);
                break;
            } else if (sumRange < target) {
                right++;
            } else {
                left++;
                if (left > right)
                    right = left;
            }
        }
        return sum;
    }

    public static int findMin(List<Integer> l, int left, int right) {
        int min = Integer.MAX_VALUE;
        for (int i = left; i <= right; i++) {
            min = Math.min(min, l.get(i));
        }
        return min;
    }

    public static int findMax(List<Integer> l, int left, int right) {
        int max = Integer.MIN_VALUE;
        for (int i = left; i <= right; i++) {
            max = Math.max(max, l.get(i));
        }
        return max;
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