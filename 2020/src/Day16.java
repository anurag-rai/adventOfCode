import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day16 {
    public static Scan sc = new Scan();
    public static Print pr = new Print();
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        String s;
        List<String> ruleNames = new ArrayList<>();
        List<List<Pair>> rules = new ArrayList<>();
        while ((s = br.readLine()) != null && !stringCompare(s.trim(), "")) {
            Pattern p = Pattern.compile("(\\d+)-(\\d+)");
            Matcher m = p.matcher(s);
            List<Pair> rule = new ArrayList<>();
            while (m.find()) {
                rule.add(new Pair(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))));
            }
            ruleNames.add(s.split(":")[0]);
            rules.add(rule);
        }
        br.readLine().trim();
        String myTicket = br.readLine().trim();
        br.readLine().trim();
        br.readLine().trim();
        List<String> validTickets = solvePartOne(rules);
        solvePartTwo(ruleNames, rules, myTicket, validTickets);
        pr.close();
    }

    public static List<String> solvePartOne(List<List<Pair>> rules) throws Exception {
        String s;
        int answer = 0;
        List<String> validTickets = new ArrayList<>();
        while ((s = br.readLine()) != null && !stringCompare(s.trim(), "")) {
            String[] nums = s.split(",");
            boolean isTicketValid = true;
            for (String s_num : nums) {
                int num = Integer.parseInt(s_num);
                if (!isValidNumber(rules, num)) {
                    isTicketValid = false;
                    answer += num;
                }
            }
            if (isTicketValid)
                validTickets.add(s);
        }
        // pr.println(answer);
        return validTickets;
    }

    public static void solvePartTwo(List<String> ruleNames, List<List<Pair>> rules, String myTicket,
            List<String> validTickets) throws Exception {
        int ruleSize = rules.size();
        int totalTickets = validTickets.size() + 1;
        int[][] ticketNums = new int[totalTickets][ruleSize];
        String[] split = myTicket.split(",");
        for (int j = 0; j < ruleSize; j++) {
            ticketNums[0][j] = Integer.parseInt(split[j]);
        }
        for (int i = 1; i < totalTickets; i++) {
            split = validTickets.get(i - 1).split(",");
            for (int j = 0; j < ruleSize; j++) {
                ticketNums[i][j] = Integer.parseInt(split[j]);
            }
        }
        List<List<Integer>> ruleIndex = new ArrayList<>();
        for (int i = 0; i < ruleSize; i++) {
            ruleIndex.add(new ArrayList<>());
        }
        for (int j = 0; j < ruleSize; j++) {
            for (int r = 0; r < ruleSize; r++) {
                if (followsRule(ticketNums[0][j], rules.get(r))) {
                    boolean othersAlsoFollowRule = true;
                    for (int i = 1; i < totalTickets; i++) {
                        if (!followsRule(ticketNums[i][j], rules.get(r))) {
                            othersAlsoFollowRule = false;
                            break;
                        }
                    }
                    if (othersAlsoFollowRule) {
                        ruleIndex.get(r).add(j);
                    }
                }
            }
        }

        // prune indexes
        int counter = ruleSize + 1;
        Set<Integer> done = new HashSet<>();
        while (counter > 0) {
            for (int i = 0; i < ruleSize; i++) {
                List<Integer> indexesForRuleI = ruleIndex.get(i);
                if (!done.contains(i) && indexesForRuleI.size() == 1) {
                    int numToRemoveFromOthers = indexesForRuleI.get(0);
                    for (int j = 0; j < ruleSize; j++) {
                        if (j == i)
                            continue;
                        ruleIndex.get(j).remove(Integer.valueOf(numToRemoveFromOthers));
                    }
                    done.add(i);
                    break;
                }
            }
            counter--;
        }

        long answer = 1L;
        for (int i = 0; i < ruleSize; i++) {
            String ruleName = ruleNames.get(i);
            Pattern p = Pattern.compile("departure");
            Matcher m = p.matcher(ruleName);
            if (m.find()) {
                int indexInMyTicket = ruleIndex.get(i).get(0);
                answer = answer * ticketNums[0][indexInMyTicket];
            }
        }

        pr.println(answer);
    }

    public static boolean isValidNumber(List<List<Pair>> rules, int x) {
        for (List<Pair> rule : rules) {
            for (Pair p : rule) {
                if (x >= p.l && x <= p.r)
                    return true;
            }
        }
        return false;
    }

    public static boolean followsRule(int x, List<Pair> rule) {
        for (Pair p : rule) {
            if (x >= p.l && x <= p.r)
                return true;
        }
        return false;
    }

    static class Pair {
        int l;
        int r;

        public Pair(int l, int r) {
            this.l = l;
            this.r = r;
        }
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