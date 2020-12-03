import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day02 {
    public static Scan sc = new Scan();
    public static Print pr = new Print();
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        // partOne();
        partTwo();
    }

    public static void partOne() throws Exception {
        int answer = 0;
        while (true) {
            String s = br.readLine();
            if (s == null)
                break;
            Pattern pattern = Pattern.compile("(\\d+)-(\\d+) ([a-z]): (.*)");
            Matcher matcher = pattern.matcher(s.trim());
            if (matcher.find()) {
                int low = Integer.parseInt(matcher.group(1));
                int high = Integer.parseInt(matcher.group(2));
                char c = matcher.group(3).toCharArray()[0];
                String password = matcher.group(4);
                if (isValidPassword(low, high, c, password))
                    answer++;
            }
        }
        System.out.println(answer);
    }

    public static void partTwo() throws Exception {
        int answer = 0;
        while (true) {
            String s = br.readLine();
            if (s == null)
                break;
            Pattern pattern = Pattern.compile("(\\d+)-(\\d+) ([a-z]): (.*)");
            Matcher matcher = pattern.matcher(s.trim());
            if (matcher.find()) {
                int firstIndex = Integer.parseInt(matcher.group(1)) - 1;
                int secondIndex = Integer.parseInt(matcher.group(2)) - 1;
                char c = matcher.group(3).toCharArray()[0];
                String password = matcher.group(4);
                if (isValidPassword2(firstIndex, secondIndex, c, password))
                    answer++;
            }
        }
        System.out.println(answer);
    }

    public static boolean isValidPassword(int low, int high, char c, String p) {
        int freq = 0;
        for (char x : p.toCharArray()) {
            if (x == c)
                freq++;
        }
        if (freq >= low && freq <= high)
            return true;
        return false;
    }

    public static boolean isValidPassword2(int low, int high, char c, String p) {
        int len = p.length();
        int count = 0;
        if (low >= 0 && low < len) {
            if (p.charAt(low) == c)
                count++;
        }
        if (high >= 0 && high < len) {
            if (p.charAt(high) == c)
                count++;
        }
        if (count == 1)
            return true;
        return false;
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