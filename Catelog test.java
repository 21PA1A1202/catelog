import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.math.BigInteger;

public class Catelog test{

    public static void main(String[] args) throws Exception {
        
        String tc1= new String(Files.readAllBytes(Paths.get("testcase1.json")));
        String tc2 = new String(Files.readAllBytes(Paths.get("testcase2.json")));

        
        JSONObject t1 = new JSONObject(tc1);
        JSONObject t2= new JSONObject(tc2);

        // Find and print the secret for both test cases
        System.out.println("Secret for Test Case 1: " + findSecret(t1));
        System.out.println("Secret for Test Case 2: " + findSecret(t22));
    }

    public static BigInteger findSecret(JSONObject testCase) {
    
        JSONObject keys = testCase.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

       
        double[] x = new double[k];
        BigInteger[] y = new BigInteger[k];
        int index = 0;

        for (String key : testCase.keySet()) {
            if (key.equals("keys")) continue;
            if (index >= k) break; 

            JSONObject point = testCase.getJSONObject(key);
            int base = point.getInt("base");
            String value = point.getString("value");

            // Decode x and y values
            x[index] = Double.parseDouble(key); 
            y[index] = new BigInteger(value, base); 
            index++;
        }

      
        return lagrangeInterpolation(x, y);
    }

    public static BigInteger lagrangeInterpolation(double[] x, BigInteger[] y) {
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < x.length; i++) {
            BigInteger term = y[i];

            for (int j = 0; j < x.length; j++) {
                if (i != j) {
                    BigInteger numerator = BigInteger.valueOf((long) -x[j]);
                    BigInteger denominator = BigInteger.valueOf((long) (x[i] - x[j]));
                    term = term.multiply(numerator).divide(denominator);
                }
            }

            result = result.add(term);
        }

        return result;
    }
}