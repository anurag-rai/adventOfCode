import java.io.*;
import java.util.*;

public class Day24 {
    public static Scan sc = new Scan();
    public static Print pr = new Print();
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    // 0 - e - 2,0
    // 1 - se - 1,-1
    // 2 - sw - -1,-1
    // 3 - w - -2,0
    // 4 - nw - -1,1
    // 5 - ne - 1,1
    public static int[][] dir = { { 2, 0 }, { 1, -1 }, { -1, -1 }, { -2, 0 }, { -1, 1 }, { 1, 1 } };

    public static void main(String[] args) throws Exception {
        String s;

        Set<Pair> set = new HashSet<>();
        while ((s = br.readLine()) != null && s.compareTo("") != 0) {
            List<Integer> directions = getDirections(s);
            Pair coord = getCoordinatesFromDirections(directions);

            if (set.contains(coord)) {
                set.remove(coord);
            } else {
                set.add(coord);
            }
        }

        pr.println(set.size());

        // Part two
        int time = 1;
        while (time < 101) {
            set = simulate(set);
            time++;
        }

        pr.println(set.size());

        pr.close();
    }

    public static Set<Pair> simulate(Set<Pair> s) {
        Set<Pair> whiteThatGoBlack = getWhitesThatGoBlack(s);
        Set<Pair> blacksThatRemainBlack = getBlacksThatRemainBlack(s);
        Set<Pair> answer = new HashSet<>();
        answer.addAll(whiteThatGoBlack);
        answer.addAll(blacksThatRemainBlack);
        return answer;
    }

    public static Set<Pair> getWhitesThatGoBlack(Set<Pair> s) {
        Set<Pair> whitesThatGoBlack = new HashSet<>();
        for (Pair b : s) {
            for (int[] d : dir) {
                int x = b.x + d[0];
                int y = b.y + d[1];
                if (!s.contains(new Pair(x, y))) {
                    int blackTilesAdjascentToThis = 0;
                    for (int[] d2 : dir) {
                        int nx = x + d2[0];
                        int ny = y + d2[1];
                        if (s.contains(new Pair(nx, ny)))
                            blackTilesAdjascentToThis++;
                    }
                    if (blackTilesAdjascentToThis == 2) {
                        whitesThatGoBlack.add(new Pair(x, y));
                    }
                }
            }
        }
        return whitesThatGoBlack;
    }

    public static Set<Pair> getBlacksThatRemainBlack(Set<Pair> s) {
        Set<Pair> blacksThatRemainBlack = new HashSet<>();
        for (Pair b : s) {
            int blackTilesAdjascentToThis = 0;
            for (int[] d : dir) {
                int x = b.x + d[0];
                int y = b.y + d[1];
                if (s.contains(new Pair(x, y))) {
                    blackTilesAdjascentToThis++;
                }
            }
            if (blackTilesAdjascentToThis == 0 || blackTilesAdjascentToThis > 2) {

            } else {
                blacksThatRemainBlack.add(b);
            }
        }
        return blacksThatRemainBlack;
    }

    public static List<Integer> getDirections(String s) {
        int l = s.length();
        List<Integer> dirs = new ArrayList<>();
        for (int i = 0; i < l; i++) {
            char c = s.charAt(i);
            if (c == 'e') {
                dirs.add(0);
            } else if (c == 'w') {
                dirs.add(3);
            } else if (c == 's') {
                char c2 = s.charAt(i + 1);
                if (c2 == 'e') {
                    dirs.add(1);
                } else {
                    dirs.add(2);
                }
                i++;

            } else {
                char c2 = s.charAt(i + 1);
                if (c2 == 'e') {
                    dirs.add(5);
                } else {
                    dirs.add(4);
                }
                i++;

            }
        }
        return dirs;
    }

    public static Pair getCoordinatesFromDirections(List<Integer> directions) {
        int x = 0;
        int y = 0;
        for (int d : directions) {
            x = x + dir[d][0];
            y = y + dir[d][1];
        }
        return new Pair(x, y);
    }

    public static int countTrue(Map<Pair, Boolean> m) {
        int count = 0;
        for (boolean x : m.values()) {
            if (x)
                count++;
        }
        return count;
    }

    static class Pair implements Comparable<Pair> {
        int x;
        int y;

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Pair that = (Pair) o;

            if (x != that.x)
                return false;
            if (y != that.y)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

        @Override
        public int compareTo(Pair other) {
            if (this.x != other.x)
                return this.x - other.x;
            if (this.y != other.y)
                return this.y - other.y;
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