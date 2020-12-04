import java.io.*;
import java.util.*;

public class Day03 {
    public static Scan sc = new Scan();
    public static Print pr = new Print();
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        List<String> lines = new ArrayList<>();
        while (true) {
            String s = br.readLine();
            if (s == null)
                break;
            lines.add(s);
        }
        // processPartOne(lines);
        processPartTwo(lines);
    }

    public static void processPartOne(List<String> lines) {
        int x = 3;
        int count = 0;
        int totalLines = lines.size();
        int length = -1;
        for (int i = 0; i < totalLines; i++) {
            if (i == 0) {
                length = lines.get(0).length();
                continue;
            }
            x = x % length;
            // System.out.println(x + " " + i);
            if (lines.get(i).charAt(x) == '#') {
                count++;
            }
            x = x + 3;
        }
        System.out.println(count);
    }

    public static void processPartTwo(List<String> lines) {
        long count = 1L;
        count *= process(lines, 1, 1);
        count *= process(lines, 3, 1);
        count *= process(lines, 5, 1);
        count *= process(lines, 7, 1);
        count *= process(lines, 1, 2);
        System.out.println(count);
    }

    public static long process(List<String> lines, int righStep, int downStep) {
        long count = 0L;
        int totalLines = lines.size();
        int length = lines.get(0).length();
        int x = righStep;
        for (int i = downStep; i < totalLines; i += downStep) {
            x = x % length;
            // System.out.println(x + " " + i);
            if (lines.get(i).charAt(x) == '#') {
                count++;
            }
            x = x + righStep;
        }
        return count;
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