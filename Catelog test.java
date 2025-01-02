import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.*;

public class Main {

    public static void main(String[] args) throws Exception {
        
        String con = new String(Files.readAllBytes(Paths.get("src/main/java/testcase1.json")));
        Map<String, Map<String, String>> inp = parseJSON(con);

        
        Map<String, String> keys = inp.get("keys");
        int k = Integer.parseInt(keys.get("k"));

       
        BigDecimal[] xValues = new BigDecimal[k];
        BigDecimal[] yValues = new BigDecimal[k];
        int in= 0;

       
        for (String i  : inp.keySet()) {
            if (!i.equals("keys")) {
                Map<String, String> root = inp.get(i);
                int b= Integer.parseInt(root.get("base"));
                BigDecimal y = new BigDecimal(new BigInteger(root.get("value"), b));
                xValues[in] = new BigDecimal(i);
                yValues[in] = y;
                in++;
                if (in>= k) break;
            }
        }

       
        BigDecimal ct = lagrang(xValues, yValues);
        System.out.println("The constant t (c) is: " + ct);
    }
    private static BigDecimal lagrang(BigDecimal[] x, BigDecimal[] y) {
        BigDecimal r = BigDecimal.ZERO;

        for (int i = 0; i < x.length; i++) {
            BigDecimal t = y[i];

            for (int j = 0; j < x.length; j++) {
                if (i != j) {
                    BigDecimal num = x[j].negate(MathContext.DECIMAL128);
                    BigDecimal den = x[i].subtract(x[j], MathContext.DECIMAL128);
                    t = t.multiply(num.divide(den, MathContext.DECIMAL128), MathContext.DECIMAL128);
                }
            }

            r = r.add(t, MathContext.DECIMAL128);
        }

        return r;
    }

    private static Map<String, Map<String, String>> parseJSON(String con) {
        Map<String, Map<String, String>> r = new HashMap<>();
        Pattern out = Pattern.compile("\"(\\w+)\":\\s*\\{([^}]*)}");
        Matcher outm = out.matcher(con);

        while (outm.find()) {
            String i = outm.group(1);
            String incon = outm.group(2);

            Map<String, String> inmap = new HashMap<>();
            Pattern inpa = Pattern.compile("\"(\\w+)\":\\s*\"?(\\w+)\"?");
            Matcher inma = inpa.matcher(incon);

            while (inma.find()) {
                inmap.put(inma.group(1), inma.group(2));
            }

            r.put(i, inmap);
        }

        return r;
    }


}
