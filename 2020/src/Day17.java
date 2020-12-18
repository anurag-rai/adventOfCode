import java.io.*;
import java.util.*;

public class Day17 {
    public static Scan sc = new Scan();
    public static Print pr = new Print();
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        String s;
        List<String> rows = new ArrayList<>();
        while ((s = br.readLine()) != null && s.trim().compareTo("") != 0) {
            rows.add(s);
        }
        solvePartOne(rows, 6);
        // solvePartTwo(rows, 6);
        pr.close();
    }

    public static void solvePartOne(List<String> rows, int time) throws Exception {
        int numRows = rows.size();
        int numColumns = rows.get(0).length();
        int t = 0;

        Set<Coord> state = new HashSet<>();

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                if (rows.get(i).charAt(j) == '#') {
                    state.add(new Coord(i, j, 0));
                }
            }
        }

        int zStart = 0;
        int rowStart = 0;
        int columnStart = 0;

        Set<Coord> previousState;
        while (t < time) {
            zStart--;
            rowStart--;
            columnStart--;
            numRows += 2;
            numColumns += 2;
            previousState = state;
            state = new HashSet<>();
            for (int z = zStart; z <= Math.abs(zStart); z++) {
                for (int i = rowStart; i < numRows; i++) {
                    for (int j = columnStart; j < numColumns; j++) {
                        if (isNextStateActive(previousState, i, j, z)) {
                            state.add(new Coord(i, j, z));
                        }
                    }
                }
            }
            t++;
        }
        pr.println(state.size());
    }

    public static void solvePartTwo(List<String> rows, int time) throws Exception {
        int numRows = rows.size();
        int numColumns = rows.get(0).length();
        int t = 0;

        Set<Coord4> state = new HashSet<>();

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                if (rows.get(i).charAt(j) == '#') {
                    state.add(new Coord4(i, j, 0, 0));
                }
            }
        }

        int pStart = 0;
        int qStart = 0;
        int rowStart = 0;
        int columnStart = 0;

        Set<Coord4> previousState;
        while (t < time) {
            pStart--;
            qStart--;
            rowStart--;
            columnStart--;
            numRows += 2;
            numColumns += 2;
            previousState = state;
            state = new HashSet<>();
            for (int p = pStart; p <= Math.abs(pStart); p++) {
                for (int q = qStart; q <= Math.abs(qStart); q++) {
                    for (int i = rowStart; i < numRows; i++) {
                        for (int j = columnStart; j < numColumns; j++) {
                            if (isNextStateActive(previousState, i, j, p, q)) {
                                state.add(new Coord4(i, j, p, q));
                            }
                        }
                    }
                }
            }
            t++;
        }
        pr.println(state.size());
    }

    public static boolean isNextStateActive(Set<Coord> previousState, int x, int y, int z) {
        boolean isActive = isActive(previousState, x, y, z);
        int activeNeighbours = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                for (int k = z - 1; k <= z + 1; k++) {
                    if (x == i && y == j && z == k)
                        continue;
                    if (isActive(previousState, i, j, k))
                        activeNeighbours++;
                }
            }
        }
        if (isActive) {
            return activeNeighbours == 2 || activeNeighbours == 3;
        } else {
            return activeNeighbours == 3;
        }
    }

    public static boolean isActive(Set<Coord> state, int x, int y, int z) {
        return state.contains(new Coord(x, y, z));
    }

    public static boolean isNextStateActive(Set<Coord4> previousState, int x, int y, int p, int q) {
        boolean isActive = isActive(previousState, x, y, p, q);
        int activeNeighbours = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                for (int k = p - 1; k <= p + 1; k++) {
                    for (int l = q - 1; l <= q + 1; l++) {
                        if (x == i && y == j && p == k && q == l)
                            continue;
                        if (isActive(previousState, i, j, k, l))
                            activeNeighbours++;
                    }
                }
            }
        }
        if (isActive) {
            return activeNeighbours == 2 || activeNeighbours == 3;
        } else {
            return activeNeighbours == 3;
        }
    }

    public static boolean isActive(Set<Coord4> state, int x, int y, int p, int q) {
        return state.contains(new Coord4(x, y, p, q));
    }

    static class Pair {
        int x;
        int y;

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Coord implements Comparable<Coord> {
        int x;
        int y;
        int z;

        public Coord(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Coord that = (Coord) o;

            if (x != that.x)
                return false;
            if (y != that.y)
                return false;
            if (z != that.z)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            result = 31 * result + z;
            return result;
        }

        @Override
        public int compareTo(Coord other) {
            if (this.x != other.x)
                return this.x - other.x;
            if (this.y != other.y)
                return this.y - other.y;
            if (this.z != other.z)
                return this.z - other.z;
            return 0;
        }
    }

    static class Coord4 implements Comparable<Coord4> {
        int a;
        int b;
        int c;
        int d;

        public Coord4(int a, int b, int c, int d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Coord4 that = (Coord4) o;

            if (a != that.a)
                return false;
            if (b != that.b)
                return false;
            if (c != that.c)
                return false;
            if (d != that.d)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = this.a;
            result = 31 * result + this.b;
            result = 17 * result + this.c;
            result = 11 * result + this.d;
            return result;
        }

        @Override
        public int compareTo(Coord4 other) {
            if (this.a != other.a)
                return this.a - other.a;
            if (this.b != other.b)
                return this.b - other.b;
            if (this.c != other.c)
                return this.c - other.c;
            if (this.d != other.d)
                return this.d - other.d;
            return 0;
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