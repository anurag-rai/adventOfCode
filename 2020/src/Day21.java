import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day21 {
    public static Scan sc = new Scan();
    public static Print pr = new Print();
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static List<String> allIngredients;
    public static Set<String> allergens;

    public static Map<String, List<List<String>>> allergenToFoodMap;
    public static Map<String, Set<String>> allergenToPossibleIngredietsMap;

    public static void main(String[] args) throws Exception {

        allergens = new HashSet<>();
        allIngredients = new ArrayList<>();
        allergenToFoodMap = new HashMap<>();
        allergenToPossibleIngredietsMap = new HashMap<>();

        String s;
        Pattern p = Pattern.compile("\\w+");
        while ((s = br.readLine()) != null && s.compareTo("") != 0) {
            Matcher m = p.matcher(s);
            List<String> ingredients = new ArrayList<>();
            while (m.find()) {
                String ingredient = m.group(0);
                if (ingredient.compareTo("contains") == 0)
                    break;
                allIngredients.add(ingredient);
                ingredients.add(ingredient);
            }
            while (m.find()) {
                String allergen = m.group(0);
                allergens.add(allergen);

                List<List<String>> possibleIngredients = allergenToFoodMap.getOrDefault(allergen, new ArrayList<>());
                possibleIngredients.add(ingredients);
                allergenToFoodMap.put(allergen, possibleIngredients);
            }
        }

        findPossibleIngredientsForAllergens();


        prunePossibleIngredients();

        Set<String> bannedIngredients = getBannedIngredients();
        List<String> notBanned = countNotBannedIngredients(bannedIngredients);


        pr.println(notBanned.size());

        String partTwoAnswer = getIngredientsSortedByAllergen();

        pr.println(partTwoAnswer);

        pr.close();
    }

    public static void findPossibleIngredientsForAllergens() {
        for (Map.Entry<String, List<List<String>>> e : allergenToFoodMap.entrySet()) {
            String allergen = e.getKey();
            List<List<String>> food = e.getValue();

            Set<String> possibleIngredients = new HashSet<>();

            int numOfFood = food.size();
            for (int i = 0; i < numOfFood; i++) {
                List<String> ingredients = food.get(i);
                for (String ingredient : ingredients) {
                    boolean isPossible = true;

                    for (int j = 0; j < numOfFood; j++) {
                        if (j == i)
                            continue;
                        List<String> ingredientsToCheck = food.get(j);
                        Set<String> ingredientsToCheckSet = new HashSet<>(ingredientsToCheck);
                        if (!ingredientsToCheckSet.contains(ingredient)) {
                            isPossible = false;
                            break;
                        }
                    }

                    if (isPossible) {
                        possibleIngredients.add(ingredient);
                    }
                }
            }

            allergenToPossibleIngredietsMap.put(allergen, possibleIngredients);
        }
    }

    public static void prunePossibleIngredients() {
        int time = 100;
        while (time > 0) {
            for (Map.Entry<String, Set<String>> e : allergenToPossibleIngredietsMap.entrySet()) {
                String allergen = e.getKey();
                Set<String> possibleIngredients = e.getValue();

                if (possibleIngredients.size() == 1) {
                    String ingredientToRemove = possibleIngredients.iterator().next();
                    // remove this ingredient from all other sets
                    for (Map.Entry<String, Set<String>> e2 : allergenToPossibleIngredietsMap.entrySet()) {
                        if (e2.getKey().compareTo(allergen) == 0)
                            continue;
                        e2.getValue().remove(ingredientToRemove);
                    }
                }
            }
            time--;
        }
    }

    public static Set<String> getBannedIngredients() {
        Set<String> banned = new HashSet<>();
        for (Map.Entry<String, Set<String>> e : allergenToPossibleIngredietsMap.entrySet()) {
            Set<String> possibleIngredients = e.getValue();
            String ingredient = possibleIngredients.iterator().next();
            banned.add(ingredient);
        }
        return banned;
    }

    public static List<String> countNotBannedIngredients(Set<String> bannedIngredients) {
        List<String> notBanned = new ArrayList<>();
        for (String ingredient : allIngredients) {
            if (!bannedIngredients.contains(ingredient))
                notBanned.add(ingredient);
        }
        return notBanned;
    }


    public static String getIngredientsSortedByAllergen() {
        StringBuilder s = new StringBuilder();
        List<String> allergensList = new ArrayList<>(allergens);
        Collections.sort(allergensList);
        for (String allergen : allergensList) {
            Set<String> possibleIngredients = allergenToPossibleIngredietsMap.get(allergen);
            String ingredient = possibleIngredients.iterator().next();
            s.append(ingredient);
            s.append(",");
        }
        s.setCharAt(s.length() - 1, ' ');
        return s.toString().trim();
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