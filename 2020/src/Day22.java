import java.io.*;
import java.util.*;

public class Day22 {
    public static Scan sc = new Scan();
    public static Print pr = new Print();
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static Queue<Integer> A;
    public static Queue<Integer> B;

    public static void main(String[] args) throws Exception {
        String s;
        A = new ArrayDeque<>();
        B = new ArrayDeque<>();
        br.readLine();
        while ((s = br.readLine()) != null && s.compareTo("") != 0) {
            A.add(Integer.parseInt(s.trim()));
        }
        br.readLine();
        while ((s = br.readLine()) != null && s.compareTo("") != 0) {
            B.add(Integer.parseInt(s.trim()));
        }

        // solvePartOne();

        solvePartTwo();

        pr.close();
    }

    public static void solvePartOne() throws IOException {
        simulate();
        Queue<Integer> winner;
        if (A.size() > 0) {
            winner = A;
        } else {
            winner = B;
        }

        pr.println(calculateScore(winner));
    }

    public static void solvePartTwo() throws IOException {
        Answer a = simulateRecursive(new ArrayDeque<>(A), new ArrayDeque<>(B));
        pr.println(calculateScore(a.q));
    }

    public static void simulate() {
        while (A.size() > 0 && B.size() > 0) {
            int aNum = A.poll();
            int bNum = B.poll();
            if (aNum > bNum) {
                A.add(aNum);
                A.add(bNum);
            } else if (aNum < bNum) {
                B.add(bNum);
                B.add(aNum);
            } else {
                A.add(aNum);
                B.add(bNum);
            }
        }
    }

    public static Answer simulateRecursive(Queue<Integer> a, Queue<Integer> b) {
        List<List<List<Integer>>> states = new ArrayList<>();
        while (a.size() > 0 && b.size() > 0) {
            if (checkOrAddState(states, a, b)) {
                return new Answer(0, a);
            }

            int numA = a.poll();
            int numB = b.poll();

            int winner;
            if (a.size() >= numA && b.size() >= numB) {
                Queue<Integer> copyOfA = new ArrayDeque<>(a);
                Queue<Integer> copyOfB = new ArrayDeque<>(b);
                Queue<Integer> newA = new ArrayDeque<>();
                Queue<Integer> newB = new ArrayDeque<>();
                int aSize = numA;
                int bSize = numB;
                while (aSize > 0) {
                    newA.add(copyOfA.poll());
                    aSize--;
                }
                while (bSize > 0) {
                    newB.add(copyOfB.poll());
                    bSize--;
                }
                Answer answer = simulateRecursive(newA, newB);
                winner = answer.winner;
            } else {
                winner = numA > numB ? 0 : 1;
            }
            if (winner == 0) {
                a.add(numA);
                a.add(numB);
            } else {
                b.add(numB);
                b.add(numA);
            }
        }
        return a.size() > 0 ? new Answer(0, a) : new Answer(1, b);
    }

    public static boolean checkOrAddState(List<List<List<Integer>>> states, Queue<Integer> a, Queue<Integer> b) {
        for (List<List<Integer>> state : states) {
            List<Integer> aState = state.get(0);
            List<Integer> bState = state.get(1);
            if (a.size() == aState.size() && b.size() == bState.size()) {
                boolean matches = true;
                int index = 0;
                for (int x : a) {
                    if (x != aState.get(index)) {
                        matches = false;
                        break;
                    }
                    index++;
                }
                if (!matches)
                    continue;
                index = 0;
                for (int x : b) {
                    if (x != bState.get(index)) {
                        matches = false;
                        break;
                    }
                    index++;
                }
                if (matches)
                    return true;
            } else {
                continue;
            }
        }
        List<List<Integer>> s = new ArrayList<>();
        List<Integer> aList = new ArrayList<>(a);
        List<Integer> bList = new ArrayList<>(b);
        s.add(aList);
        s.add(bList);
        states.add(s);
        return false;
    }

    public static long calculateScore(Queue<Integer> q) {
        int size = q.size();
        long answer = 0L;
        for (int x : q) {
            answer = answer + (size * x);
            size--;
        }
        return answer;
    }

    static class Answer {
        int winner;
        Queue<Integer> q;

        public Answer(int winner, Queue<Integer> q) {
            this.winner = winner;
            this.q = q;
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