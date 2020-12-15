import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14 {
    public static Scan sc = new Scan();
    public static Print pr = new Print();
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        // solvePartOne();
        solvePartTwo();
        pr.close();
    }

    public static void solvePartOne() throws Exception {
        long answer = 0L;
        String s;
        Map<Long, Long> memory = new HashMap<>();
        String mask = "";
        Pattern p = Pattern.compile("mem\\[(\\d+)\\] = (\\d+)");
        while ((s = br.readLine()) != null) {
            String[] arr = s.split(" ");
            if (stringCompare(arr[0], "mask")) {
                mask = arr[2];
            } else {
                Matcher m = p.matcher(s);
                if (m.find()) {
                    long memo = Long.parseLong(m.group(1));
                    long value = Long.parseLong(m.group(2));
                    for (int i = 0; i < 36; i++) {
                        int bit = 35 - i;
                        if (mask.charAt(i) == 'X')
                            continue;
                        long x = 1L << bit;
                        long d = (long) (mask.charAt(i) - '0');
                        value = (value & ~x) | ((d << bit) & x);
                    }
                    memory.put(memo, value);
                } else {
                    throw new Error("Did not match pattern " + s);
                }
            }
        }
        for (long key : memory.keySet()) {
            long value = memory.get(key);
            answer += value;
        }
        System.out.println(answer);
    }

    public static void solvePartTwo() throws Exception {
        long answer = 0L;
        String s;
        Map<Long, Long> memory = new HashMap<>();
        String mask = "";
        Pattern p = Pattern.compile("mem\\[(\\d+)\\] = (\\d+)");
        while ((s = br.readLine()) != null) {
            String[] arr = s.split(" ");
            if (stringCompare(arr[0], "mask")) {
                mask = arr[2];
            } else {
                Matcher m = p.matcher(s);
                if (m.find()) {
                    long memo = Long.parseLong(m.group(1));
                    long value = Long.parseLong(m.group(2));
                    List<Long> memos = getMemos(mask, memo);
                    for (long memos_m : memos) {
                        memory.put(memos_m, value);
                    }
                } else {
                    throw new Error("Did not match pattern " + s);
                }
            }
        }
        for (long key : memory.keySet()) {
            long value = memory.get(key);
            answer += value;
        }
        System.out.println(answer);
    }

    public static List<Long> getMemos(String mask, long memo) {
        List<Long> l = new ArrayList<>();
        List<Integer> floatingIndices = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
            int bit = 35 - i;
            if (mask.charAt(i) == '0')
                continue;
            if (mask.charAt(i) == '1') {
                long x = 1L << bit;
                long d = (long) (mask.charAt(i) - '0');
                memo = (memo & ~x) | ((d << bit) & x);
                continue;
            }
            floatingIndices.add(i);
        }
        if (floatingIndices.size() == 0) {
            l.add(memo);
        } else {
            l = floatNumbers(memo, floatingIndices);
        }
        return l;
    }

    public static List<Long> floatNumbers(long memo, List<Integer> floatingIndices) {
        int size = floatingIndices.size();
        List<Long> l = new ArrayList<>();
        int max = (int) Math.pow(2, size);
        for (int i = 0; i < max; i++) {
            String s = pad(Integer.toBinaryString(i), 32);
            long newMemo = memo;
            int bitIndex = 31;
            for (int index : floatingIndices) {
                int bit = 35 - index;
                long x = 1L << bit;
                long d = (long) (s.charAt(bitIndex) - '0');
                newMemo = (newMemo & ~x) | ((d << bit) & x);
                bitIndex--;
            }
            l.add(newMemo);
        }
        return l;
    }

    public static String pad(String s, int total) {
        int length = s.length();
        int more = total - length;
        StringBuilder newS = new StringBuilder();
        for (int i = 0; i < more; i++) {
            newS.append("0");
        }
        for (int i = 0; i < length; i++) {
            newS.append(s.charAt(i));
        }
        return newS.toString();
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