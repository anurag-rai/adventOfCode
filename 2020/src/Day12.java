import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day12 {
    public static Scan sc = new Scan();
    public static Print pr = new Print();
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        List<String> instructions = new ArrayList<>();
        String s = "";
        while ((s = br.readLine()) != null) {
            if (stringCompare(s, ""))
                break;
            instructions.add(s.trim());
        }
        // solvePartOne(instructions);
        solvePartTwo(instructions);

        pr.close();
    }

    public static void solvePartOne(List<String> instructions) throws Exception {
        int x = 0;
        int y = 0;
        char facing = 'E';
        Pattern p = Pattern.compile("([NSEWLRF])(\\d+)");
        for (String ins : instructions) {
            Matcher m = p.matcher(ins);
            if (m.find()) {
                String action = m.group(1);
                int value = Integer.parseInt(m.group(2));
                if (stringCompare(action, "N")) {
                    y += value;
                } else if (stringCompare(action, "S")) {
                    y -= value;
                } else if (stringCompare(action, "E")) {
                    x += value;
                } else if (stringCompare(action, "W")) {
                    x -= value;
                } else if (stringCompare(action, "L")) {
                    value = value % 360;
                    if (value % 90 != 0)
                        throw new Error("Turning left by " + value);
                    if (value == 90) {
                        if (facing == 'E') {
                            facing = 'N';
                        } else if (facing == 'N') {
                            facing = 'W';
                        } else if (facing == 'W') {
                            facing = 'S';
                        } else {
                            facing = 'E';
                        }
                    } else if (value == 180) {
                        if (facing == 'E') {
                            facing = 'W';
                        } else if (facing == 'N') {
                            facing = 'S';
                        } else if (facing == 'W') {
                            facing = 'E';
                        } else {
                            facing = 'N';
                        }
                    } else if (value == 270) {
                        if (facing == 'E') {
                            facing = 'S';
                        } else if (facing == 'N') {
                            facing = 'E';
                        } else if (facing == 'W') {
                            facing = 'N';
                        } else {
                            facing = 'W';
                        }
                    }
                } else if (stringCompare(action, "R")) {
                    value = value % 360;
                    if (value % 90 != 0)
                        throw new Error("Turning right by " + value);
                    if (value == 90) {
                        if (facing == 'E') {
                            facing = 'S';
                        } else if (facing == 'S') {
                            facing = 'W';
                        } else if (facing == 'W') {
                            facing = 'N';
                        } else {
                            facing = 'E';
                        }
                    } else if (value == 180) {
                        if (facing == 'E') {
                            facing = 'W';
                        } else if (facing == 'N') {
                            facing = 'S';
                        } else if (facing == 'W') {
                            facing = 'E';
                        } else {
                            facing = 'N';
                        }
                    } else if (value == 270) {
                        if (facing == 'E') {
                            facing = 'N';
                        } else if (facing == 'N') {
                            facing = 'W';
                        } else if (facing == 'W') {
                            facing = 'S';
                        } else {
                            facing = 'E';
                        }
                    }
                } else { // F
                    if (facing == 'E') {
                        x += value;
                    } else if (facing == 'N') {
                        y += value;
                    } else if (facing == 'W') {
                        x -= value;
                    } else {
                        y -= value;
                    }
                }
            } else {
                throw new Exception("Could not parse regex for " + ins);
            }
        }
        pr.println("" + (Math.abs(x) + Math.abs(y)));

    }

    public static void solvePartTwo(List<String> instructions) throws Exception {
        int ship_x = 0;
        int ship_y = 0;
        int waypoint_x = 10;
        int waypoint_y = 1;
        Pattern p = Pattern.compile("([NSEWLRF])(\\d+)");
        for (String ins : instructions) {
            Matcher m = p.matcher(ins);
            if (m.find()) {
                String action = m.group(1);
                int value = Integer.parseInt(m.group(2));
                if (stringCompare(action, "N")) {
                    waypoint_y += value;
                } else if (stringCompare(action, "S")) {
                    waypoint_y -= value;
                } else if (stringCompare(action, "E")) {
                    waypoint_x += value;
                } else if (stringCompare(action, "W")) {
                    waypoint_x -= value;
                } else if (stringCompare(action, "L")) {
                    value = value % 360;
                    if (value % 90 != 0)
                        throw new Error("Turning left by " + value);
                    if (value == 90) {
                        int temp = waypoint_x;
                        waypoint_x = -waypoint_y;
                        waypoint_y = temp;
                    } else if (value == 180) {
                        waypoint_x = -waypoint_x;
                        waypoint_y = -waypoint_y;
                    } else if (value == 270) {
                        int temp = waypoint_x;
                        waypoint_x = waypoint_y;
                        waypoint_y = -temp;
                    }
                } else if (stringCompare(action, "R")) {
                    value = value % 360;
                    if (value % 90 != 0)
                        throw new Error("Turning left by " + value);
                    if (value == 90) {
                        int temp = waypoint_x;
                        waypoint_x = waypoint_y;
                        waypoint_y = -temp;
                    } else if (value == 180) {
                        waypoint_x = -waypoint_x;
                        waypoint_y = -waypoint_y;
                    } else if (value == 270) {
                        int temp = waypoint_x;
                        waypoint_x = -waypoint_y;
                        waypoint_y = temp;
                    }
                } else { // F
                    int xDiff = value * waypoint_x;
                    int yDiff = value * waypoint_y;
                    ship_x += xDiff;
                    ship_y += yDiff;
                }
            } else {
                throw new Exception("Could not parse regex for " + ins);
            }
        }
        pr.println("" + (Math.abs(ship_x) + Math.abs(ship_y)));

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