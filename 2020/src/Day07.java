import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day07 {
    public static Scan sc = new Scan();
    public static Print pr = new Print();
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        Pattern p1 = Pattern.compile("(.*) bags contain (.*)");
        Pattern p2 = Pattern.compile("((\\d+) ([A-Za-z ]*) bags?[,.])+");
        Map<String, Node> map = new HashMap<>();
        while (true) {
            String s = br.readLine();
            if (s == null || s.compareTo("") == 0)
                break;
            Matcher m1 = p1.matcher(s.trim());
            if (m1.find()) {
                String sourceNodeColor = m1.group(1);
                Node src;
                if (map.get(sourceNodeColor) != null) {
                    src = map.get(sourceNodeColor);
                } else {
                    src = new Node(sourceNodeColor);
                    map.put(sourceNodeColor, src);
                }
                String rest = m1.group(2);
                Matcher m2 = p2.matcher(rest);
                while (m2.find()) {
                    String targetColor = m2.group(3);
                    int targetNumber = Integer.parseInt(m2.group(2));

                    Node targetNode;
                    if (map.get(targetColor) != null) {
                        targetNode = map.get(targetColor);
                    } else {
                        targetNode = new Node(targetColor);
                        map.put(targetColor, targetNode);
                    }
                    src.n.put(targetNode, targetNumber);
                }
            } else {
                throw new Error("M1 failed to find");
            }
        }

        // findPartOne(map);
        findPartTwo(map);

        pr.close();
    }

    public static void findPartOne(Map<String, Node> map) throws IOException {
        int count = 0;
        String targerColor = "shiny gold";
        for (String color : map.keySet()) {
            if (color.compareTo(targerColor) == 0)
                continue;
            Node src = map.get(color);
            if (find(src, targerColor)) {
                count++;
            }
        }
        pr.println(count);
    }

    public static void findPartTwo(Map<String, Node> map) throws IOException {
        int count = 0;
        String sourceColor = "shiny gold";
        for (String color : map.keySet()) {
            if (color.compareTo(sourceColor) == 0) {
                count = calculate(map.get(color));
                break;
            }
        }
        pr.println(count);
    }

    public static boolean find(Node src, String targerColor) {
        if (src.color.compareTo(targerColor) == 0)
            return true;
        for (Node n : src.n.keySet()) {
            if (find(n, targerColor))
                return true;
        }
        return false;
    }

    public static int calculate(Node src) {
        int count = 0;
        for (Map.Entry<Node, Integer> en : src.n.entrySet()) {
            Node node = en.getKey();
            int value = en.getValue();
            count += value;
            count += value * calculate(node);
        }
        return count;
    }

    static class Node {
        String color;
        Map<Node, Integer> n;

        public Node(String color) {
            this.color = color;
            n = new HashMap<>();
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