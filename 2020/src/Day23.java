import java.io.*;
import java.util.*;

public class Day23 {
    public static Scan sc = new Scan();
    public static Print pr = new Print();
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        // simulatePartOne();

        simulatePartTwo();
        pr.close();
    }

    public static void simulatePartOne() throws Exception {
        String input = br.readLine().trim();
        Cup previous = null;
        Cup head = null;
        int lowest = Integer.MAX_VALUE;
        for (char c : input.toCharArray()) {
            int l = c - '0';
            Cup cup = new Cup(l);
            if (previous == null) {
                head = cup;
                previous = cup;
            } else {
                previous.next = cup;
                cup.prev = previous;
                previous = cup;
            }
            lowest = Math.min(lowest, l);
        }
        previous.next = head;
        head.prev = previous;

        int moves = 100;
        Cup newCup = head;

        Map<Integer, Cup> m = makeMap(head);

        while (moves > 0) {
            newCup = simulate(m, newCup, lowest, 9, 8, 7, 6);
            moves--;
        }

        String answer = getOrder(head);
        pr.println(answer);
    }

    public static void simulatePartTwo() throws Exception {
        String input = br.readLine().trim();
        Cup previous = null;
        Cup head = null;
        int lowest = Integer.MAX_VALUE;
        int highest = Integer.MIN_VALUE;
        for (char c : input.toCharArray()) {
            int l = c - '0';
            Cup cup = new Cup(l);
            if (previous == null) {
                head = cup;
                previous = cup;
            } else {
                previous.next = cup;
                cup.prev = previous;
                previous = cup;
            }
            lowest = Math.min(lowest, l);
            highest = Math.max(highest, l);
        }

        highest++;
        while (highest <= 1000000) {
            Cup cup = new Cup(highest);
            previous.next = cup;
            cup.prev = previous;
            previous = cup;
            highest++;
        }
        previous.next = head;
        head.prev = previous;

        Map<Integer, Cup> m = makeMap(head);

        int highest1 = 1000000;
        int highest2 = highest1 - 1;
        int highest3 = highest2 - 1;
        int highest4 = highest3 - 1;

        int moves = 10000000;
        Cup newCup = head;
        while (moves > 0) {
            // pr.println(" Move = " + moves + " " + getOrder(newCup));
            newCup = simulate(m, newCup, lowest, highest1, highest2, highest3, highest4);
            moves--;
        }

        long answer = getNodesAfter1(head);
        pr.println(answer);
    }

    public static Cup simulate(Map<Integer, Cup> m, Cup current, int lowest, int h1, int h2, int h3, int h4) {
        Cup next1 = current.next;
        Cup next2 = next1.next;
        Cup next3 = next2.next;
        int destination = current.label - 1;

        Cup destinationCup = null;
        while (destinationCup == null) {
            if (destination < lowest) {
                // find the Cup that is highest that is not taken
                if (h1 != next1.label && h1 != next2.label && h1 != next3.label) {
                    destinationCup = m.get(h1);
                    break;
                }
                if (h2 != next1.label && h2 != next2.label && h2 != next3.label) {
                    destinationCup = m.get(h2);
                    break;
                }
                if (h3 != next1.label && h3 != next2.label && h3 != next3.label) {
                    destinationCup = m.get(h3);
                    break;
                }
                destinationCup = m.get(h4);
                break;
            }
            if (destination == next1.label || destination == next2.label || destination == next3.label) {
                destination--;
                continue;
            }
            destinationCup = m.get(destination);
            break;
        }

        current.next = next3.next;
        next3.next.prev = current;

        destinationCup.next.prev = next3;
        next3.next = destinationCup.next;

        destinationCup.next = next1;
        next1.prev = destinationCup;
        return current.next;
    }

    public static String getOrder(Cup head) {
        while (head.label != 1) {
            head = head.next;
        }
        StringBuilder s = new StringBuilder();
        Cup curr = head.next;
        while (curr != head) {
            s.append("" + curr.label);
            curr = curr.next;
        }
        return s.toString();
    }

    public static long getNodesAfter1(Cup head) {
        while (head.label != 1) {
            head = head.next;
        }
        Cup c1 = head.next;
        Cup c2 = head.next.next;
        return 1L * c1.label * c2.label;
    }

    public static Map<Integer, Cup> makeMap(Cup head) {
        Map<Integer, Cup> m = new HashMap<>();
        m.put(head.label, head);
        Cup curr = head.next;

        while (curr != head) {
            m.put(curr.label, curr);
            curr = curr.next;
        }
        return m;
    }

    static class Cup {
        int label;
        Cup next;
        Cup prev;

        public Cup(int label) {
            this.label = label;
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