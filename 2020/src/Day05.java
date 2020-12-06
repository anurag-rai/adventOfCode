import java.io.*;
import java.util.*;

public class Day05 {
    public static Scan sc = new Scan();
    public static Print pr = new Print();
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        List<String> passes = new ArrayList<>();
        while (true) {
            String s = br.readLine();
            if (s == null || s.compareTo("") == 0)
                break;
            passes.add(s.trim());
        }
        // calculatePartOne(passes);
        calculatePartTwo(passes);
    }

    public static void calculatePartOne(List<String> passes) {
        int answer = -1;
        for (String pass : passes) {
            answer = Math.max(answer, getSeatId(pass));
        }
        System.out.println(answer);
    }

    public static void calculatePartTwo(List<String> passes) {
        List<Integer> l = new ArrayList<>();
        for (String pass : passes) {
            l.add(getSeatId(pass));
        }
        Collections.sort(l);
        int missing = -1;
        int prev = l.get(0) - 1;
        for (int x : l) {
            if (x != prev + 1) {
                missing = prev + 1;
                break;
            }
            prev = x;
        }
        System.out.println(missing);
    }

    public static int getSeatId(String s) {
        int row = 0;
        for (int i = 0; i < 7; i++) {
            char c = s.charAt(i);
            row = row << 1;
            if (c == 'B') {
                row++;
            }
        }

        int column = 0;
        for (int i = 7; i < 10; i++) {
            char c = s.charAt(i);
            column = column << 1;
            if (c == 'R') {
                column++;
            }
        }
        int answer = row * 8 + column;
        return answer;
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