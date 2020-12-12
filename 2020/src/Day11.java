import java.io.*;
import java.util.*;

public class Day11 {
    public static Scan sc = new Scan();
    public static Print pr = new Print();
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static int[][] dirs = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 },
            { 1, 1 } };

    public static void main(String[] args) throws Exception {
        List<String> rows = new ArrayList<>();
        while (true) {
            String s = br.readLine();
            if (s == null || s.compareTo("") == 0)
                break;
            rows.add(s);
        }
        pr.println("" + find(rows, 1));
        pr.close();
    }

    public static int find(List<String> rows, int choiceAlgo) {
        int m = rows.size();
        int n = rows.get(0).length();
        char[][] arr = new char[m][n];
        char[][] prev = new char[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                arr[i][j] = rows.get(i).charAt(j);
            }
        }
        boolean changes = true;
        while (changes) {
            prev = copy(arr);
            changes = false;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    char originalChar = prev[i][j];
                    char newChar = choiceAlgo == 1 ? findChange(prev, i, j) : findChange2(prev, i, j);
                    if (originalChar != newChar)
                        changes = true;
                    ;
                    arr[i][j] = newChar;
                }
            }
        }
        return count(arr, '#');
    }

    public static char findChange(char[][] arr, int i, int j) {
        int m = arr.length;
        int n = arr[0].length;

        if (arr[i][j] == 'L') {
            for (int[] d : dirs) {
                int x = i + d[0];
                int y = j + d[1];
                if (x >= 0 && y >= 0 && x < m && y < n) {
                    if (arr[x][y] == '#')
                        return 'L';
                }
            }
            return '#';
        } else if (arr[i][j] == '#') {
            int count = 0;
            for (int[] d : dirs) {
                int x = i + d[0];
                int y = j + d[1];
                if (x >= 0 && y >= 0 && x < m && y < n) {
                    if (arr[x][y] == '#')
                        count++;
                }
            }
            if (count >= 4)
                return 'L';
            return '#';
        }
        return arr[i][j];
    }

    public static char findChange2(char[][] arr, int i, int j) {
        int m = arr.length;
        int n = arr[0].length;

        if (arr[i][j] == 'L') {
            for (int[] d : dirs) {
                int x = i + d[0];
                int y = j + d[1];
                while (x >= 0 && y >= 0 && x < m && y < n) {
                    if (arr[x][y] == '#')
                        return 'L';
                    if (arr[x][y] == 'L')
                        break;
                    x = x + d[0];
                    y = y + d[1];
                }
            }
            return '#';
        } else if (arr[i][j] == '#') {
            int count = 0;
            for (int[] d : dirs) {
                int x = i + d[0];
                int y = j + d[1];
                while (x >= 0 && y >= 0 && x < m && y < n) {
                    if (arr[x][y] == '#') {
                        count++;
                        break;
                    }
                    if (arr[x][y] == 'L')
                        break;

                    x = x + d[0];
                    y = y + d[1];
                }
            }
            if (count >= 5)
                return 'L';
            return '#';
        }
        return arr[i][j];
    }

    public static int count(char[][] arr, char t) {
        int m = arr.length;
        int n = arr[0].length;
        int count = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (arr[i][j] == t)
                    count++;
            }
        }
        return count;
    }

    public static char[][] copy(char[][] arr) {
        int m = arr.length;
        int n = arr[0].length;
        char[][] newarr = new char[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                newarr[i][j] = arr[i][j];
            }
        }
        return newarr;
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