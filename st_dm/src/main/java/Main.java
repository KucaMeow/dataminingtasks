import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        File time = new File("time.csv");
        Scanner time_scanner = new Scanner(time);

        List<String> weeks = new ArrayList<String>();
        time_scanner.nextLine();
        while (time_scanner.hasNext()) {
            weeks.add(time_scanner.nextLine().split(",")[0]);
        }

        Map<String, Map<String, Integer>> prod_count_reg = new HashMap<String, Map<String, Integer>>();

        for(String week_pref : weeks) {
            Scanner trans_scanner = new Scanner(
                    new File("transactions_" + week_pref + ".csv"));
            trans_scanner.nextLine();
            while (trans_scanner.hasNext()) {
                String[] transaction = trans_scanner.nextLine().split(","); //6 - prod_code 21 - shop_region
                String region = transaction[21];
                String prod_code = transaction[6];
                if(!prod_count_reg.containsKey(region)) {
                    prod_count_reg.put(region, new HashMap<String, Integer>());
                }
                if(!prod_count_reg.get(region).containsKey(prod_code)) {
                    prod_count_reg.get(region).put(prod_code, 1);
                }
                else {
                    prod_count_reg.get(region).put(prod_code,
                            prod_count_reg.get(region).get(prod_code) + 1);
                }
            }
        }

        for(Map.Entry<String, Map<String, Integer>> regs : prod_count_reg.entrySet()) {
            System.out.println(regs.getKey());
            System.out.println(regs.getValue().toString());
            System.out.println();
        }
    }
}
