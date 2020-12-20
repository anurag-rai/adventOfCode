import java.io.*;
import java.util.*;

public class Day19 {
    public static Scan sc = new Scan();
    public static Print pr = new Print();
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static Map<Integer, String> ruleToSubrule;
    public static Map<Integer, Set<String>> ruleToSetMap;

    public static void main(String[] args) throws Exception {
        ruleToSubrule = new HashMap<>();
        ruleToSetMap = new HashMap<>();
        String s;
        while ((s = br.readLine()) != null && s.compareTo("") != 0) {
            String[] ruleMain = s.trim().split(":");
            int ruleNumber = Integer.parseInt(ruleMain[0].trim());
            ruleToSubrule.put(ruleNumber, ruleMain[1].trim());
        }

        populate(0);

        // solvePartOne();
        solvePartTwo();

        pr.close();
    }

    public static void solvePartOne() throws Exception {
        String s;
        int answer = 0;

        while ((s = br.readLine()) != null && s.compareTo("") != 0) {
            if (validate(s, 0))
                answer++;
        }
        pr.println(answer);
    }

    public static void solvePartTwo() throws Exception {
        String s;
        int answer = 0;

        List<String> checklist = new ArrayList<>();

        while ((s = br.readLine()) != null && s.compareTo("") != 0) {
            if (validate(s, 0)) {
                answer++;
            } else {
                checklist.add(s);
            }
        }

        for (String check : checklist) {
            // 0 : 8 11
            // 8 : 42 | 42 8
            // 11 : 42 31 | 42 11 31

            // 1 8 and 1 11 ... 42 42 31
            // 1 8 and 2 11 ... 42 42 42 31 31
            // 2 8 and 1 11 ... 42 42 42 31
            // 2 8 and 2 11 ... 42 42 42 42 31 31

            boolean found = false;
            for (String prefix : ruleToSetMap.get(42)) {
                if (check.startsWith(prefix)) {
                    String restOfString = check.substring(prefix.length());
                    for (String prefix2 : ruleToSetMap.get(42)) {
                        if (restOfString.startsWith(prefix2)) {
                            for (String suffix : ruleToSetMap.get(31)) {
                                if (restOfString.endsWith(suffix)) {
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if (found)
                            break;
                    }
                }
                if (found)
                    break;
            }

            if (found) {
                // 42 should appear more than 31
                String toCheck = check;
                int num42 = 0;
                int num31 = 0;
                boolean matched = true;
                while (toCheck.length() > 0 && matched) {
                    matched = false;
                    for (String prefix : ruleToSetMap.get(42)) {
                        if (toCheck.startsWith(prefix)) {
                            matched = true;
                            num42++;
                            toCheck = toCheck.substring(prefix.length());
                            break;
                        }
                    }
                }

                matched = true;
                while (toCheck.length() > 0 && matched) {
                    matched = false;
                    for (String prefix : ruleToSetMap.get(31)) {
                        if (toCheck.startsWith(prefix)) {
                            matched = true;
                            num31++;
                            toCheck = toCheck.substring(prefix.length());
                            break;
                        }
                    }
                }
                if (num42 > num31 && num31 > 0 && toCheck.length() == 0) {
                    answer++;
                }
            }
        }

        pr.println(answer);
    }

    public static void populate(int ruleNumber) {
        String subRule = ruleToSubrule.get(ruleNumber);
        String[] tokens = subRule.trim().split(" ");
        if (tokens.length == 1 && tokens[0].charAt(0) == '"') {
            Set<String> set = new HashSet<>();
            set.add(tokens[0].substring(1, tokens[0].length() - 1));
            ruleToSetMap.put(ruleNumber, set);
            return;
        }
        Set<String> result = new HashSet<>();
        List<Integer> subRuleNumbersToCrossJoin = new ArrayList<>();
        for (String token : tokens) {
            if (token.compareTo("|") == 0) {
                Set<String> j = crossJoin(subRuleNumbersToCrossJoin);
                result.addAll(j);
                subRuleNumbersToCrossJoin = new ArrayList<>();
                continue;
            }
            int subRuleNumber = Integer.parseInt(token);
            subRuleNumbersToCrossJoin.add(subRuleNumber);
            if (ruleToSetMap.keySet().contains(subRuleNumber))
                continue;
            populate(subRuleNumber);
        }
        Set<String> j = crossJoin(subRuleNumbersToCrossJoin);
        result.addAll(j);
        ruleToSetMap.put(ruleNumber, result);
    }

    public static Set<String> crossJoin(List<Integer> subRuleNumbersToCrossJoin) {
        Set<String> result = new HashSet<>();
        if (subRuleNumbersToCrossJoin == null || subRuleNumbersToCrossJoin.size() == 0)
            return result;

        for (int x : subRuleNumbersToCrossJoin) {
            Set<String> currentSetToJoin = ruleToSetMap.get(x);
            result = crossJoingSets(result, currentSetToJoin);
        }
        return result;
    }

    public static Set<String> crossJoingSets(Set<String> a, Set<String> b) {
        Set<String> result = new HashSet<>();
        if (a.size() == 0) {
            result.addAll(b);
            return result;
        }
        if (b.size() == 0) {
            result.addAll(a);
            return result;
        }
        for (String sa : a) {
            for (String sb : b) {
                result.add(sa + sb);
            }
        }
        return result;
    }

    public static boolean validate(String s, int rule) {
        Set<String> allowedStrings = ruleToSetMap.get(rule);
        return allowedStrings.contains(s);
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