import java.io.*;
import java.util.*;

public class Day06 {
    public static Scan sc = new Scan();
    public static Print pr = new Print();
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        List<List<String>> answers = new ArrayList<>();
        List<String> currentGroup = new ArrayList<>();
        while (true) {
            String s = br.readLine();
            if (s == null) {
                answers.add(currentGroup);
                break;
            }
            if (s.trim().compareTo("") == 0) {
                answers.add(currentGroup);
                currentGroup = new ArrayList<>();
                continue;
            }
            currentGroup.add(s.trim());
        }
        // countPartOne(answers);

        countPartTwo(answers);

        pr.close();
    }

    public static void countPartOne(List<List<String>> answers) throws Exception {
        int count = 0;

        for (List<String> group : answers) {
            Set<Character> s = new HashSet<>();
            for (String answer : group) {
                for (char question : answer.toCharArray()) {
                    s.add(question);
                }
            }
            count += s.size();
        }

        pr.println(count);
    }

    public static void countPartTwo(List<List<String>> answers) throws Exception {
        int count = 0;

        for (List<String> group : answers) {
            Map<Character, Integer> m = new HashMap<>();
            int groupSize = group.size();
            for (String answer : group) {
                for (char question : answer.toCharArray()) {
                    m.put(question, m.getOrDefault(question, 0) + 1);
                }
            }

            for (Character q : m.keySet()) {
                if (m.get(q) == groupSize)
                    count++;
            }
        }

        pr.println(count);
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