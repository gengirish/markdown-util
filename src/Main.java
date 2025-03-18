import java.util.HashMap;
import java.util.Map;

public class Main {
    static Map<Integer,Integer> fibMap = new HashMap<>();
    public static void main(String[] args) {
        String textBlock = """
                This is a multiline
                string using text blocks.
                It spans multiple lines.
                """;

        int n = 10;

        for (int i = 0; i < n; i++) {
            System.out.printf(calculateFibMem(i) + " ");
        }
    }

    private static int calculateFib(int n) {
        if(n<=1)
            return n;
        else
            return calculateFib(n-1) + calculateFib(n-2);
    }

    private static int calculateFibMem(int n) {
        if (n <= 1) {
            return n;
        }
        if(fibMap.containsKey(n)){
            return fibMap.get(n);
        }
        int result = calculateFibMem(n-1) + calculateFibMem(n-2);
        fibMap.put(n,result);
        return result;
    }
}