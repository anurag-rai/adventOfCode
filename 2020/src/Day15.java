import java.io.*;
import java.util.*;

public class Day15 {
    public static Scan sc = new Scan();
    public static Print pr = new Print();
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        String[] arr = br.readLine().trim().split(",");
        Map<Integer, Pair> m = new HashMap<>();
        int turn = 1;
        int lastSpoken = -1;
        for (String s : arr) {
            int x = Integer.parseInt(s);
            speak(m, x, turn);
            turn++;
        }
        // int partOneLimit = 2021;
        int partTwoLimit = 30000001;
        while (turn < partTwoLimit) {
            lastSpoken = calculate(m, lastSpoken, turn);
            turn++;
        }
        pr.println(lastSpoken);
        pr.close();
    }

    public static int calculate(Map<Integer, Pair> m, int lastSpoken, int currentIndex) {
        if (m.keySet().contains(lastSpoken)) {
            Pair pair = m.get(lastSpoken);
            if (pair.p >= 0) {
                int toReturn = pair.p - pair.pp;
                speak(m, toReturn, currentIndex);
                return toReturn;
            } else {
                speak(m, 0, currentIndex);
                return 0;
            }
        } else {
            speak(m, 0, currentIndex);
            return 0;
        }
    }

    public static void speak(Map<Integer, Pair> m, int number, int currentIndex) {
        Pair pair = m.get(number);
        if (pair == null) {
            m.put(number, new Pair(currentIndex));
        } else {
            if (pair.p >= 0) {
                m.put(number, new Pair(pair.p, currentIndex));
            } else {
                m.put(number, new Pair(pair.pp, currentIndex));
            }
        }
    }

    static class Pair {
        int pp;
        int p;

        public Pair(int pp) {
            this.pp = pp;
            this.p = -1;
        }

        public Pair(int pp, int p) {
            this.pp = pp;
            this.p = p;
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