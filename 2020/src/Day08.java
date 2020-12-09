import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day08 {
    public static Scan sc = new Scan();
    public static Print pr = new Print();
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        List<String> instructions = new ArrayList<>();
        while (true) {
            String s = br.readLine();
            if (s == null)
                break;
            instructions.add(s);

        }
        // getPartOne(instructions);
        getPartTwo(instructions);
        pr.close();
    }

    public static void getPartOne(List<String> instructions) throws Exception {
        Pattern p = Pattern.compile("(\\bacc\\b|\\bjmp\\b|\\bnop\\b) ?([+-]?)(\\d+)?");

        int acc = 0;
        if (instructions.size() == 0) {
            pr.println(acc);
            return;
        }
        Set<Integer> visited = new HashSet<>();
        int currentIndex = 0;
        while (true) {
            if (visited.contains(currentIndex) || currentIndex >= instructions.size())
                break;
            visited.add(currentIndex);
            String i = instructions.get(currentIndex);
            Matcher m = p.matcher(i.trim());
            if (m.find()) {
                if (m.group(1).compareTo("nop") == 0) {
                    currentIndex++;
                } else {
                    int value = Integer.parseInt(m.group(3));
                    if (m.group(2).trim().compareTo("") != 0 && m.group(2).trim().compareTo("-") == 0) {
                        value = -value;
                    }

                    if (m.group(1).compareTo("jmp") == 0) {
                        currentIndex += value;
                    } else {
                        currentIndex++;
                        acc += value;
                    }
                }
            } else {
                throw new Error("Cannot decypher instruction");
            }
        }
        pr.println(acc);
    }

    public static void getPartTwo(List<String> instructions) throws Exception {
        int answer = 0;
        List<String> instructionToDo = new ArrayList<>();
        Pattern p = Pattern.compile("(\\bacc\\b|\\bjmp\\b|\\bnop\\b) ?([+-]?)(\\d+)?");
        if (find(instructions, new HashSet<>(), instructionToDo, 0, 0)) {
            for (String i : instructionToDo) {
                Matcher m = p.matcher(i.trim());
                if (m.find()) {
                    if (m.group(1).compareTo("acc") == 0) {
                        int value = Integer.parseInt(m.group(3));
                        if (m.group(2).trim().compareTo("") != 0 && m.group(2).trim().compareTo("-") == 0) {
                            value = -value;
                        }
                        answer += value;
                    }
                } else {
                    throw new Error("Cannot decypher instruction");
                }
            }
            pr.println(answer);
        } else {
            throw new Error("could not fin path to end");
        }
    }

    public static boolean find(List<String> instructions, Set<Integer> visited, List<String> tbe, int currentIndex,
            int changesDone) {
        if (currentIndex >= instructions.size())
            return false;
        if (currentIndex == instructions.size() - 1) {
            tbe.add(instructions.get(currentIndex));
            return true;
        }
        if (visited.contains(currentIndex))
            return false;
        visited.add(currentIndex);

        String i = instructions.get(currentIndex);
        Pattern p = Pattern.compile("(\\bacc\\b|\\bjmp\\b|\\bnop\\b) ?([+-]?)(\\d+)?");
        Matcher m = p.matcher(i.trim());
        if (m.find()) {
            if (m.group(1).compareTo("nop") == 0 || m.group(1).compareTo("jmp") == 0) {

                // keep operation as is
                tbe.add(i);
                int nextIndex = currentIndex;
                if (m.group(1).compareTo("nop") == 0) {
                    nextIndex += 1;
                } else {
                    int value = Integer.parseInt(m.group(3));
                    if (m.group(2).trim().compareTo("") != 0 && m.group(2).trim().compareTo("-") == 0) {
                        value = -value;
                    }
                    nextIndex += value;
                }

                if (find(instructions, visited, tbe, nextIndex, changesDone)) {
                    return true;
                }
                
                tbe.remove(tbe.size() - 1);

                if (changesDone > 0) {
                    visited.remove(currentIndex);
                    return false;
                }
                changesDone++;
                // replace
                String originalInstruction = i;
                String replacedInstruction;
                nextIndex = currentIndex;
                if (m.group(1).compareTo("nop") == 0) {
                    replacedInstruction = i.replace("nop", "jmp");
                    int value = Integer.parseInt(m.group(3));
                    if (m.group(2).trim().compareTo("") != 0 && m.group(2).trim().compareTo("-") == 0) {
                        value = -value;
                    }
                    nextIndex += value;
                } else {
                    replacedInstruction = i.replace("jmp", "nop");
                    nextIndex += 1;
                }
                instructions.set(currentIndex, replacedInstruction);
                tbe.add(replacedInstruction);

                if (find(instructions, visited, tbe, nextIndex, changesDone)) {
                    return true;
                }
                // keep it as is
                instructions.set(currentIndex, originalInstruction);
                tbe.remove(tbe.size() - 1);
                visited.remove(currentIndex);
                return false;
            } else {
                tbe.add(i);
                if (find(instructions, visited, tbe, currentIndex + 1, changesDone)) {
                    return true;
                } else {
                    tbe.remove(tbe.size() - 1);
                    visited.remove(currentIndex);
                    return false;
                }
            }
        } else {
            throw new Error("Cannot decypher instruction");
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