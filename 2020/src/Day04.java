import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day04 {
    public static Scan sc = new Scan();
    public static Print pr = new Print();
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        // List<Set<String>> passports = partOne();
        List<Set<String>> passports = partTwo();

        countValidPassportsPartOne(passports);
    }

    public static List<Set<String>> partOne() throws Exception {
        Set<String> keys = new HashSet<>();
        List<Set<String>> passports = new ArrayList<>();
        while (true) {
            String s = br.readLine();
            if (s == null) {
                break;
            }
            if (s.trim().compareTo("") == 0) {
                passports.add(keys);
                keys = new HashSet<>();
                continue;
            }
            String[] kvs = s.trim().split("\\s+");
            for (String kv : kvs) {
                Pattern p = Pattern.compile("(.*):.*");
                Matcher m = p.matcher(kv);
                if (m.find()) {
                    keys.add(m.group(1));
                }
            }
        }
        passports.add(keys);
        return passports;
    }

    public static List<Set<String>> partTwo() throws Exception {
        Set<String> keys = new HashSet<>();
        List<Set<String>> passports = new ArrayList<>();
        while (true) {
            String s = br.readLine();
            if (s == null) {
                break;
            }
            if (s.trim().compareTo("") == 0) {
                passports.add(keys);
                keys = new HashSet<>();
                continue;
            }
            String[] kvs = s.trim().split("\\s+");
            for (String kv : kvs) {
                Pattern p = Pattern.compile("(.*):(.*)");
                Matcher m = p.matcher(kv);
                if (m.find()) {
                    String key = m.group(1).trim();
                    String value = m.group(2).trim();
                    if (stringEquals(key, "byr")) {
                        if (isValidBirth(value))
                            keys.add(key);
                    } else if (stringEquals(key, "iyr")) {
                        if (isValidIssue(value))
                            keys.add(key);
                    } else if (stringEquals(key, "eyr")) {
                        if (isValidExpiry(value))
                            keys.add(key);
                    } else if (stringEquals(key, "hgt")) {
                        if (isValidHeight(value))
                            keys.add(key);
                    } else if (stringEquals(key, "hcl")) {
                        if (isValidHair(value))
                            keys.add(key);
                    } else if (stringEquals(key, "ecl")) {
                        if (isValidEye(value))
                            keys.add(key);
                    } else if (stringEquals(key, "pid")) {
                        if (isValidPassportId(value))
                            keys.add(key);
                    }
                }
            }
        }
        passports.add(keys);
        return passports;
    }

    public static boolean stringEquals(String x, String y) {
        return x.compareTo(y) == 0 ? true : false;
    }

    public static boolean isValidBirth(String x) {
        if (x.length() != 4)
            return false;
        int year = Integer.parseInt(x);
        return year >= 1920 && year <= 2002;
    }

    public static boolean isValidIssue(String x) {
        if (x.length() != 4)
            return false;
        int year = Integer.parseInt(x);
        return year >= 2010 && year <= 2020;
    }

    public static boolean isValidExpiry(String x) {
        if (x.length() != 4)
            return false;
        int year = Integer.parseInt(x);
        return year >= 2020 && year <= 2030;
    }

    public static boolean isValidHeight(String x) {
        Pattern p = Pattern.compile("(\\d+)(.*)");
        Matcher m = p.matcher(x);
        if (m.find()) {
            int number = Integer.parseInt(m.group(1));
            String type = m.group(2);
            if (stringEquals(type, "cm")) {
                return number >= 150 && number <= 193;
            } else if (stringEquals(type, "in")) {
                return number >= 59 && number <= 76;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isValidEye(String x) {
        String[] valid = { "amb", "blu", "brn", "gry", "grn", "hzl", "oth" };
        for (String v : valid) {
            if (stringEquals(x, v))
                return true;
        }
        return false;
    }

    public static boolean isValidHair(String x) {
        Pattern p = Pattern.compile("#(.*)");
        Matcher m = p.matcher(x);
        if (m.find()) {
            String color = m.group(1).trim();
            if (color.length() != 6)
                return false;
            for (char c : color.toCharArray()) {
                if (!(c >= '0' && c <= '9') && !(c >= 'a' && c <= 'f'))
                    return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidPassportId(String x) {
        if (x.length() != 9) {
            return false;
        }
        for (char c : x.toCharArray()) {
            if (c < '0' || c > '9')
                return false;
        }
        return true;
    }

    public static void countValidPassportsPartOne(List<Set<String>> passports) {
        int count = 0;
        String[] neededArr = { "byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid" };
        Set<String> needed = new HashSet<>();
        for (String n : neededArr)
            needed.add(n);
        for (Set<String> passport : passports) {
            if (passport.containsAll(needed))
                count++;
        }
        System.out.println(count);
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