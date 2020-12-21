import java.io.*;
import java.util.*;

public class Day20 {
    public static Scan sc = new Scan();
    public static Print pr = new Print();
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static Map<Integer, List<String>> numToTileMap;
    public static Map<Integer, List<Edge>> numToEdgesMap;
    public static Map<Integer, Tile> tileMap;
    public static int n;
    public static int m;

    public static List<Integer> corners;
    public static List<Integer> edges;
    public static List<Integer> rest;

    public static Tile[][] picture;

    public static void main(String[] args) throws Exception {
        String s;
        numToTileMap = new HashMap<>();
        numToEdgesMap = new HashMap<>();
        corners = new ArrayList<>();
        edges = new ArrayList<>();
        rest = new ArrayList<>();
        int tileNum = -1;
        List<String> tiles = new ArrayList<>();
        while ((s = br.readLine()) != null && s.compareTo("z") != 0) {
            if (s.trim().compareTo("") == 0) {
                n = tiles.size();
                m = tiles.get(0).length();
                numToTileMap.put(tileNum, tiles);
                tiles = new ArrayList<>();
                tileNum = -1;
                continue;
            }
            if (tileNum == -1) {
                String t = s.trim().split(" ")[1];
                t = t.substring(0, t.length() - 1);
                tileNum = Integer.parseInt(t);
                continue;
            }
            tiles.add(s);
        }

        makeEdges();

        List<Integer> corners = solvePartOne();
        long answer = 1L;
        for (int corner : corners) {
            answer = answer * corner;
        }
        pr.println(answer);

        solvePartTwo();

        pr.close();
    }

    public static List<Integer> solvePartOne() throws IOException {
        // get 4 tiles whose 2 edges don't match any other edge
        for (Map.Entry<Integer, List<Edge>> entry1 : numToEdgesMap.entrySet()) {
            int tile1 = entry1.getKey();
            int numOfEdgesCommon = 0;
            for (Edge edge1 : entry1.getValue()) {
                boolean found = false;
                for (Map.Entry<Integer, List<Edge>> entry2 : numToEdgesMap.entrySet()) {
                    int tile2 = entry2.getKey();
                    if (tile1 == tile2)
                        continue;

                    for (Edge edge2 : entry2.getValue()) {
                        if (edge1.canPair(edge2)) {
                            edge1.isCommon = true;
                            edge2.isCommon = true;
                            found = true;
                            break;
                        }
                    }
                    if (found)
                        break;
                }
                if (found) {
                    numOfEdgesCommon++;
                    continue;
                }
            }
            if (numOfEdgesCommon == 2) {
                corners.add(tile1);
            } else if (numOfEdgesCommon == 3) {
                edges.add(tile1);
            } else {
                rest.add(tile1);
            }
        }

        return corners;
    }

    public static void solvePartTwo() throws Exception {
        tileMap = new HashMap<>();
        for (int cornerTileNumber : corners) {
            tileMap.put(cornerTileNumber, createTile(cornerTileNumber));
        }
        for (int edgeTileNumber : edges) {
            tileMap.put(edgeTileNumber, createTile(edgeTileNumber));
        }
        for (int restTileNumber : rest) {
            tileMap.put(restTileNumber, createTile(restTileNumber));
        }

        Set<Integer> yetToFix = new HashSet<>();
        yetToFix.addAll(tileMap.keySet());

        int x = (int) Math.sqrt(corners.size() + edges.size() + rest.size());
        picture = new Tile[x][x];

        for (int cornerTileNumber : corners) {
            Tile cornerTile = tileMap.get(cornerTileNumber);
            cornerTile.rotateAndFixAsTopLeftCorner();

            picture[0][0] = cornerTile;
            yetToFix.remove(cornerTileNumber);

            if (solve(yetToFix, x, 0, 1)) {
                break;
            }

            cornerTile.flip();
            cornerTile.rotateAndFixAsTopLeftCorner();

            picture[0][0] = cornerTile;
            yetToFix.remove(cornerTileNumber);

            if (solve(yetToFix, x, 0, 1)) {
                break;
            }

            picture[0][0] = null;
            yetToFix.add(cornerTileNumber);
        }

        char[][] picture = paint(x);
        pr.println(findSeaMonster(picture));

    }

    public static int findSeaMonster(char[][] picture) {
        for (int i = 0; i < 4; i++) {
            rotatePixels270(picture);
            int num = findNumOfSeaMonsters(picture);
            if (num > 0) {
                int answer = count(picture) - num * 15;
                return answer;
            }
        }

        for (int i = 0; i < 4; i++) {
            rotatePixels270(picture);
            char[][] copied = getCopy(picture);
            flipPixels(copied);
            int num = findNumOfSeaMonsters(copied);
            if (num > 0) {
                int answer = count(picture) - num * 15;
                return answer;
            }
        }
        return -1;
    }

    public static int count(char[][] p) {
        int count = 0;
        int x = p.length;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < x; j++) {
                if (p[i][j] == '#')
                    count++;
            }
        }
        return count;

    }

    public static char[][] getCopy(char[][] c) {
        int x = c.length;
        char[][] newC = new char[x][x];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < x; j++) {
                newC[i][j] = c[i][j];
            }
        }
        return newC;
    }

    public static int findNumOfSeaMonsters(char[][] picture) {
        int n = 0;
        int x = picture.length;
        for (int i = 0; i < x - 2; i++) {
            for (int j = 18; j < x - 1; j++) {
                if (picture[i][j] == '#' && isSeaMonster(picture, i, j))
                    n++;
            }
        }
        return n;
    }

    public static boolean isSeaMonster(char[][] picture, int i, int j) {
        return picture[i][j] == '#' && picture[i + 1][j] == '#' && picture[i + 1][j + 1] == '#'
                && picture[i + 1][j - 1] == '#' && picture[i + 2][j - 2] == '#' &&

                picture[i + 2][j - 5] == '#' && picture[i + 1][j - 6] == '#' && picture[i + 1][j - 7] == '#'
                && picture[i + 2][j - 8] == '#' &&

                picture[i + 2][j - 11] == '#' && picture[i + 1][j - 12] == '#' && picture[i + 1][j - 13] == '#'
                && picture[i + 2][j - 14] == '#' &&

                picture[i + 2][j - 17] == '#' && picture[i + 1][j - 18] == '#';

    }

    public static char[][] paint(int x) {
        int totalSize = x * n;
        char[][] actualPicture = new char[totalSize][totalSize];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < x; j++) {
                Tile t = picture[i][j];

                for (int pi = 0; pi < n; pi++) {
                    for (int pj = 0; pj < n; pj++) {
                        actualPicture[i * n + pi][j * n + pj] = t.pixels[pi][pj];
                    }
                }

            }
        }

        rotatePixels270(actualPicture);
        rotatePixels270(actualPicture);
        flipPixels(actualPicture);
        rotatePixels270(actualPicture);

        // remove columns
        for (int i = 0; i < totalSize; i = i + n) {
            for (int j = 0; j < totalSize; j++) {
                actualPicture[i][j] = ' ';
                actualPicture[i + n - 1][j] = ' ';
            }

        }

        for (int j = 0; j < totalSize; j = j + n) {
            for (int i = 0; i < totalSize; i++) {
                actualPicture[i][j] = ' ';
                actualPicture[i][j + n - 1] = ' ';
            }

        }

        int columns = 0;
        for (int i = 0; i < totalSize; i++) {
            if (actualPicture[1][i] != ' ')
                columns++;
        }
        int rows = 0;
        for (int i = 0; i < totalSize; i++) {
            if (actualPicture[i][1] != ' ')
                rows++;
        }

        char[][] trimmedPicture = new char[rows][columns];
        int ti = 0;
        int tj = 0;
        for (int i = 0; i < totalSize; i++) {
            for (int j = 0; j < totalSize; j++) {
                if (actualPicture[i][j] != ' ') {
                    trimmedPicture[ti][tj] = actualPicture[i][j];
                    tj++;
                    if (tj == columns) {
                        ti++;
                        tj = 0;
                    }
                }
            }
        }

        return trimmedPicture;
    }

    public static boolean solve(Set<Integer> yetToFix, int x, int i, int j) throws Exception {
        if (isLastCorner(x, i, j)) {

            if (yetToFix.size() != 1)
                throw new Error("Last corner, but " + yetToFix.size() + " remaining");
            for (int tileNumber : yetToFix) {
                Tile top = null;
                Tile left = null;
                if (i > 0) {
                    top = picture[i - 1][j];
                }
                if (j > 0) {
                    left = picture[i][j - 1];
                }

                Tile t = tileMap.get(tileNumber);
                for (Tile tileState : getTileStates(t)) {
                    if (canFit(top, left, tileState)) {
                        picture[i][j] = tileState;
                        return true;
                    }
                }
            }
            return false;
        } else {
            for (int tileNumber : yetToFix) {
                Tile top = null;
                Tile left = null;
                if (i > 0) {
                    top = picture[i - 1][j];
                }
                if (j > 0) {
                    left = picture[i][j - 1];
                }

                Set<Integer> nextYetToFix = new HashSet<>(yetToFix);
                Tile t = tileMap.get(tileNumber);
                for (Tile tileState : getTileStates(t)) {
                    if (canFit(top, left, tileState)) {
                        picture[i][j] = tileState;
                        nextYetToFix.remove(tileNumber);
                        if (solve(nextYetToFix, x, getNextI(x, i, j), getNextJ(x, i, j)))
                            return true;

                        picture[i][j] = null;
                    }
                }
            }
            return false;
        }

    }

    public static void printPicture(int x, int r, int c) throws IOException {
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < x; j++) {
                pr.print(picture[i][j].tileNumber + "");
            }
            pr.println("");
        }
        for (int j = 0; j <= c; j++) {
            pr.print(picture[r][j] + " ");
            pr.println("");
        }
    }

    public static boolean canFit(Tile top, Tile left, Tile t) {
        if (top != null) {
            if (top.s.tile.compareTo(t.n.tile) != 0)
                return false;
        }
        if (left != null) {
            if (left.e.tile.compareTo(t.w.tile) != 0)
                return false;
        }
        return true;
    }

    public static List<Tile> getTileStates(Tile t) {
        List<Tile> states = new ArrayList<>();
        Tile r1 = t.getCopy();
        Tile r2 = t.getCopy();
        Tile r3 = t.getCopy();
        Tile r4 = t.getCopy();
        Tile f1 = t.getCopy();
        Tile f2 = t.getCopy();
        Tile f3 = t.getCopy();
        Tile f4 = t.getCopy();

        r2.rotate(90);
        r3.rotate(180);
        r4.rotate(270);
        f2.rotate(90);
        f3.rotate(180);
        f4.rotate(270);
        f1.flip();
        f2.flip();
        f3.flip();
        f4.flip();
        states.add(r1);
        states.add(r2);
        states.add(r3);
        states.add(r4);
        states.add(f1);
        states.add(f2);
        states.add(f3);
        states.add(f4);
        return states;
    }

    public static boolean isCorner(int x, int i, int j) {
        return ((i == 0 && j == 0) || (i == 0 && j == x - 1) || (i == x - 1 && j == 0) || (i == x - 1 && j == x - 1));
    }

    public static boolean isLastCorner(int x, int i, int j) {
        return (i == x - 1 && j == x - 1);
    }

    public static boolean isEdge(int x, int i, int j) {
        return ((i == 0 && j > 0 && j < x - 1) || (j == 0 && i > 0 && i < x - 1) || (j == x - 1 && i > 0 && i < x - 1)
                || (i == x - 1 && j > 0 && j < x - 1));
    }

    public static int getNextI(int x, int i, int j) {
        if (j == x - 1) {
            return i + 1;
        }
        return i;
    }

    public static int getNextJ(int x, int i, int j) {
        if (j == x - 1) {
            return 0;
        }
        return j + 1;
    }

    public static void makeEdges() {
        for (Map.Entry<Integer, List<String>> e : numToTileMap.entrySet()) {
            int tileNum = e.getKey();
            List<String> tiles = e.getValue();
            numToEdgesMap.put(tileNum, getEdges(tiles));
        }
    }

    public static List<Edge> getEdges(List<String> tiles) {
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(tiles.get(0), 'N'));
        edges.add(new Edge(tiles.get(n - 1), 'S'));
        StringBuilder left = new StringBuilder();
        StringBuilder right = new StringBuilder();
        for (String tile : tiles) {
            left.append(tile.charAt(0));
            right.append(tile.charAt(m - 1));
        }
        edges.add(new Edge(left.toString(), 'W'));
        edges.add(new Edge(right.toString(), 'E'));
        return edges;
    }

    public static Tile createTile(int tileNumber) {
        Edge n = null;
        Edge s = null;
        Edge e = null;
        Edge w = null;
        for (Edge edge : numToEdgesMap.get(tileNumber)) {
            if (edge.dir == 'N') {
                n = edge;
            } else if (edge.dir == 'S') {
                s = edge;
            } else if (edge.dir == 'E') {
                e = edge;
            } else if (edge.dir == 'W') {
                w = edge;
            }
        }

        int size = n.tile.length();
        char[][] maybeThis = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                maybeThis[i][j] = numToTileMap.get(tileNumber).get(i).charAt(j);
            }
        }

        Tile tile = new Tile(tileNumber, n, s, e, w);

        for (int i = 0; i < 4; i++) {
            rotatePixels270(maybeThis);
            if (pixelsMatchEdges(maybeThis, n, s, e, w)) {
                tile.pixels = maybeThis;
                return tile;
            }
        }
        for (int i = 0; i < 4; i++) {
            rotatePixels270(maybeThis);
            flipPixels(maybeThis);
            if (pixelsMatchEdges(maybeThis, n, s, e, w)) {
                tile.pixels = maybeThis;
                return tile;
            }
        }

        return null;
    }

    public static boolean pixelsMatchEdges(char[][] pixels, Edge n, Edge s, Edge e, Edge w) {
        int size = n.tile.length();
        for (int i = 0; i < size; i++) {
            if (n.tile.charAt(i) != pixels[0][i])
                return false;
        }
        for (int i = 0; i < size; i++) {
            if (s.tile.charAt(i) != pixels[size - 1][i])
                return false;
        }
        for (int i = 0; i < size; i++) {
            if (e.tile.charAt(i) != pixels[i][size - 1])
                return false;
        }
        for (int i = 0; i < size; i++) {
            if (w.tile.charAt(i) != pixels[i][0])
                return false;
        }
        return true;
    }

    public static void rotatePixels270(char mat[][]) {
        int N = mat.length;
        // Consider all squares one by one
        for (int x = 0; x < N / 2; x++) {
            // Consider elements in group
            // of 4 in current square
            for (int y = x; y < N - x - 1; y++) {
                // Store current cell in
                // temp variable
                char temp = mat[x][y];

                // Move values from right to top
                mat[x][y] = mat[y][N - 1 - x];

                // Move values from bottom to right
                mat[y][N - 1 - x] = mat[N - 1 - x][N - 1 - y];

                // Move values from left to bottom
                mat[N - 1 - x][N - 1 - y] = mat[N - 1 - y][x];

                // Assign temp to left
                mat[N - 1 - y][x] = temp;
            }
        }
    }

    public static void flipPixels(char mat[][]) {
        int N = mat.length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < (N / 2); j++) {
                char temp = mat[i][j];
                mat[i][j] = mat[i][N - 1 - j];
                mat[i][N - 1 - j] = temp;
            }
        }
    }

    static class Edge {
        String tile;
        char dir;
        boolean isCommon;

        public Edge(String tile, char dir) {
            this.tile = tile;
            this.dir = dir;
            isCommon = false;
        }

        public boolean canPair(Edge e) {

            char targetDirection = this.getOppositeDirection();
            String rotatedTarget = e.rotateTo(targetDirection);
            String flippedTargert = new StringBuilder(rotatedTarget).reverse().toString();
            return rotatedTarget.compareTo(this.tile) == 0 || flippedTargert.compareTo(this.tile) == 0;

        }

        public char getOppositeDirection() {
            if (this.dir == 'N')
                return 'S';
            if (this.dir == 'S')
                return 'N';
            if (this.dir == 'E')
                return 'W';
            if (this.dir == 'W')
                return 'E';
            return ' ';
        }

        public String rotateTo(char targerDir) {
            if (this.dir == targerDir)
                return "" + this.tile;
            if (this.dir == 'N') {
                if (targerDir == 'E') {
                    return this.tile;
                } else {
                    return new StringBuilder(this.tile).reverse().toString();
                }
            } else if (this.dir == 'E') {
                if (targerDir == 'N') {
                    return this.tile;
                } else {
                    return new StringBuilder(this.tile).reverse().toString();
                }
            } else if (this.dir == 'S') {
                if (targerDir == 'W') {
                    return this.tile;
                } else {
                    return new StringBuilder(this.tile).reverse().toString();
                }
            } else if (this.dir == 'W') {
                if (targerDir == 'S') {
                    return this.tile;
                } else {
                    return new StringBuilder(this.tile).reverse().toString();
                }
            }
            return "";
        }

        public Edge getCopy() {
            Edge newEdge = new Edge(this.tile, this.dir);
            newEdge.isCommon = this.isCommon;
            return newEdge;
        }
    }

    static class Tile {
        int tileNumber;
        Edge n;
        Edge s;
        Edge e;
        Edge w;

        char[][] pixels;

        public Tile(int tileNumber, Edge n, Edge s, Edge e, Edge w) {
            this.tileNumber = tileNumber;
            this.n = n;
            this.s = s;
            this.e = e;
            this.w = w;
            pixels = new char[n.tile.length()][n.tile.length()];
        }

        public void rotateAndFixAsTopLeftCorner() {
            if (e.isCommon && s.isCommon)
                return;
            if (s.isCommon && w.isCommon) {
                rotate(270);
            } else if (w.isCommon && n.isCommon) {
                rotate(180);
            } else if (n.isCommon && e.isCommon) {
                rotate(90);
            } else {
                throw new Error("wtf?");
            }
        }

        private void flip() {
            this.n.tile = new StringBuilder(this.n.tile).reverse().toString();
            this.s.tile = new StringBuilder(this.s.tile).reverse().toString();

            Edge temp = e.getCopy();
            this.e = w.getCopy();
            this.e.dir = 'E';

            this.w = temp;
            this.w.dir = 'W';

            flipPixels(this.pixels);
        }

        public void rotate(int c) {
            c = c % 360;
            if (c == 0)
                return;
            if (c == 270) {
                rotate90();
                rotate90();
                rotate90();
            } else if (c == 180) {
                rotate90();
                rotate90();
            } else {
                rotate90();
            }
        }

        private void rotate90() {
            this.n.tile = this.n.rotateTo('E');
            this.n.dir = 'E';
            this.e.tile = this.e.rotateTo('S');
            this.e.dir = 'S';
            this.s.tile = this.s.rotateTo('W');
            this.s.dir = 'W';
            this.w.tile = this.w.rotateTo('N');
            this.w.dir = 'N';
            Edge temp = this.n;
            this.n = this.w;
            this.w = this.s;
            this.s = this.e;
            this.e = temp;
            rotatePixels270(this.pixels);
            rotatePixels270(this.pixels);
            rotatePixels270(this.pixels);
        }

        public Tile getCopy() {
            Tile newTile = new Tile(this.tileNumber, this.n.getCopy(), this.s.getCopy(), this.e.getCopy(),
                    this.w.getCopy());
            int x = this.pixels.length;
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < x; j++) {
                    newTile.pixels[i][j] = this.pixels[i][j];
                }
            }
            return newTile;
        }

        public void printPixels() {
            int x = this.pixels.length;
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < x; j++) {
                    System.out.print(this.pixels[i][j] + " ");
                }
                System.out.println();
            }
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