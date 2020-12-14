import java.io.*;
import java.util.*;

public class Day13 {
    public static Scan sc = new Scan();
    public static Print pr = new Print();
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        // solvePartOne();
        solvePartTwo();
        pr.close();
    }

    public static void solvePartOne() throws Exception {
        int t = Integer.parseInt(br.readLine());
        String[] arr = br.readLine().split(",");
        List<Integer> l = new ArrayList<>();
        for (String s : arr) {
            try {
                l.add(Integer.parseInt(s));
            } catch (Exception e) {
                continue;
            }
        }
        int min = Integer.MAX_VALUE;
        int id = -1;
        for (int x : l) {
            int mod = t % x;
            int next = 0;
            if (mod == 0) {
                min = Math.min(min, t);
                id = x;
            } else {
                next = (t - mod) + x;
            }
            if (next < min) {
                min = next;
                id = x;
            }
        }
        int answer = (min - t) * id;
        pr.println("" + answer);
    }

    public static void solvePartTwo() throws Exception {
        br.readLine();
        String[] arr = br.readLine().split(",");
        List<Pair> l = new ArrayList<>();
        long index = 0L;
        for (String s : arr) {
            try {
                l.add(new Pair(Long.parseLong(s), index));
                index++;
            } catch (Exception e) {
                index++;
                continue;
            }
        }
        long[] nums = new long[l.size()];
        long[] rem = new long[l.size()];
        long pr = 1L;
        for (int i = 0; i < l.size(); i++) {
            Pair p = l.get(i);
            nums[i] = p.n;
            rem[i] = nums[i] - p.rem;
            pr = pr * rem[i] > 0 ? rem[i] : 1L;
        }

        long answer = findMinX(nums, rem, l.size());

        System.out.println(answer);
    }

    static long findMinX(long num[], long rem[], int k) {
        long prod = 1L;
        for (int i = 0; i < k; i++)
            prod *= num[i];

        // Initialize result
        long result = 0;

        // Apply above formula
        for (int i = 0; i < k; i++) {
            long pp = prod / num[i];
            result += rem[i] * inv(pp, num[i]) * pp;
        }

        return result % prod;
    }

    public static long inv(long a, long m) {
        long m0 = m, t, q;
        long x0 = 0L, x1 = 1L;

        if (m == 1L)
            return 0L;

        // Apply extended Euclid Algorithm
        while (a > 1) {
            // q is quotient
            q = a / m;
            t = m;
            // m is remainder now, process
            // same as euclid's algo
            m = a % m;
            a = t;

            t = x0;
            x0 = x1 - q * x0;
            x1 = t;
        }

        // Make x1 positive
        if (x1 < 0)
            x1 += m0;

        return x1;
    }

    static class Pair {
        long n;
        long rem;

        public Pair(long n, long rem) {
            this.n = n;
            this.rem = rem;
        }
    }

    public static boolean stringCompare(String a, String b) {
        return a.compareTo(b) == 0;
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