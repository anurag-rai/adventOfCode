import java.io.*;
import java.util.*;

public class Day10 {
    public static Scan sc = new Scan();
    public static Print pr = new Print();
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        List<Integer> adapters = new ArrayList<>();
        adapters.add(0);
        while (true) {
            String s = br.readLine();
            if (s == null || s.compareTo("") == 0)
                break;
            adapters.add(Integer.parseInt(s.trim()));
        }
        // findPartOne(adapters);
        findPartTwo(adapters);
        pr.close();
    }

    public static void findPartOne(List<Integer> adapters) throws Exception {
        Collections.sort(adapters);
        int size = adapters.size();
        adapters.add(adapters.get(size - 1) + 3);
        int oneDiff = 0;
        int threeDiff = 0;
        for (int i = 1; i <= size; i++) {
            int diff = adapters.get(i) - adapters.get(i - 1);
            if (diff == 1) {
                oneDiff++;
            } else if (diff == 3) {
                threeDiff++;
            }
        }
        pr.println("" + (oneDiff * threeDiff));
    }

    public static void findPartTwo(List<Integer> adapters) throws Exception {
        Collections.sort(adapters);
        int size = adapters.size();
        adapters.add(adapters.get(size - 1) + 3);
        size++;
        long[] dp = new long[size];
        dp[0] = 1L;
        for (int i = 1; i < size; i++) {
            dp[i] = dp[i - 1];
            if ((i - 2) >= 0 && adapters.get(i) - adapters.get(i - 2) <= 3) {
                dp[i] = dp[i] + dp[i - 2];
            }
            if ((i - 3) >= 0 && adapters.get(i) - adapters.get(i - 3) <= 3) {
                dp[i] = dp[i] + dp[i - 3];
            }
        }
        pr.println("" + dp[size - 1]);
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