import java.util.*;

public class TwentyFour {
    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        
        printInstructions();
        
        processInput(scanner);
    }
    
    private static void processInput(Scanner scanner) {
        String input = scanner.nextLine();
        int[] values;
        while (!input.equals("q")) {
            values = Arrays.stream(input.split(",")).mapToInt(Integer::parseInt).toArray();
            while (values.length != 4) {
                System.out.println("There should be 4 total values. " + 
                                   "Please double check your input format!");
                printInstructions();
                input = scanner.nextLine();
                if (input.equals("q")) {
                  return;
                }
                values = Arrays.stream(input.split(",")).mapToInt(Integer::parseInt).toArray();
            }
            String message = "This set of values is " + (solve(values) ? "solvable":"not solvable");
            System.out.println(message);
            System.out.println();
            printInstructions();
            input = scanner.nextLine();
        }
    }
    
    private static void printInstructions() {
        System.out.println("Please enter the values of the cards ");
        System.out.println("in a comma separated list with no spaces (eg. '1,2,3,4') ");
        System.out.println("or enter 'q' to quit:");
    }

    public static boolean solve(int[] arr) {
        Set<Integer> s01 = operate(arr[0], arr[1]);
        Set<Integer> s02 = operate(arr[0], arr[2]);
        Set<Integer> s03 = operate(arr[0], arr[3]);
        Set<Integer> s12 = operate(arr[1], arr[2]);
        Set<Integer> s13 = operate(arr[1], arr[3]);
        Set<Integer> s23 = operate(arr[2], arr[3]);

        Set<Integer> s012 = operateOnSet(arr[0], s12);
        s012.addAll(operateOnSet(arr[1], s02));
        s012.addAll(operateOnSet(arr[2], s01));
        Set<Integer> s123 = operateOnSet(arr[1], s23);
        s123.addAll(operateOnSet(arr[2], s13));
        s123.addAll(operateOnSet(arr[3], s12));
        Set<Integer> s013 = operateOnSet(arr[0], s13);
        s013.addAll(operateOnSet(arr[1], s23));
        s013.addAll(operateOnSet(arr[3], s01));
        Set<Integer> s023 = operateOnSet(arr[0], s23);
        s023.addAll(operateOnSet(arr[2], s03));
        s023.addAll(operateOnSet(arr[3], s02));

        Set<Integer> total = operateTwoSets(s01, s23);
        if (total.contains(24)) {
            System.out.println("01, 23");
            return true;
        }
        total = operateTwoSets(s02, s13);
        if (total.contains(24)) {
            System.out.println("02, 13");
            return true;
        }
        total = operateTwoSets(s03, s12);
        if (total.contains(24)) {
            System.out.println("03, 12");
            return true;
        }

        total = operateOnSet(arr[3], s012);
        if (total.contains(24)) {
            System.out.println("3, 012");
            return true;
        }
        total = operateOnSet(arr[2], s013);
        if (total.contains(24)) {
            System.out.println("2, 013");
            return true;
        }
        total = operateOnSet(arr[1], s023);
        if (total.contains(24)) {
            System.out.println("1, 023");
            return true;
        }
        total = operateOnSet(arr[0], s123);
        if (total.contains(24)) {
            System.out.println("0, 123");
            return true;
        }

        return false;
    }

    public static Set<Integer> operate(int a, int b) {
        Set<Integer> set = new HashSet<Integer>();
        set.add(a+b);
        set.add(Math.abs(a-b));
        set.add(a*b);
        if (a == 0 || b == 0) {
            return set;
        }
        if (a > b) {
            if (a % b == 0) {
                set.add(a/b);
            }
        } else {
            if (b % a == 0) {
                set.add(b/a);
            }
        }
        return set;
    }

    public static Set<Integer> operateOnSet(int b, Set<Integer> set1) {
        Set<Integer> ans = new HashSet<Integer>();
        for (int a : set1) {
            ans.addAll(operate(a, b));
        }
        return ans;
    }

    public static Set<Integer> operateTwoSets(Set<Integer> setA, Set<Integer> setB) {
        Set<Integer> ans = new HashSet<Integer>();
        for (int a : setA) {
            ans.addAll(operateOnSet(a, setB));
        }
        return ans;
    }
}