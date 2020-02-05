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
                    prod_count_reg.get(region).put(prod_code, Integer.parseInt(transaction[4])); //4 - QUANTITY
                }
                else {
                    prod_count_reg.get(region).put(prod_code,
                            prod_count_reg.get(region).get(prod_code) + Integer.parseInt(transaction[4]));
                }
            }
        }

        for(Map.Entry<String, Map<String, Integer>> regs : prod_count_reg.entrySet()) {
            System.out.println(regs.getKey());
            String prod_max = "", prod_min = "";
            int prod_max_c = -1, prod_min_c = Integer.MAX_VALUE;
            for(Map.Entry<String, Integer> prods : regs.getValue().entrySet()) {
                if(prods.getValue() > prod_max_c) {
                    prod_max = prods.getKey();
                    prod_max_c = prods.getValue();
                }
                if(prods.getValue() < prod_min_c) {
                    prod_min = prods.getKey();
                    prod_min_c = prods.getValue();
                }
            }
//            System.out.println(regs.getValue().toString());
            System.out.println(prod_max + ":" + prod_max_c);
            System.out.println(prod_min + ":" + prod_min_c);
            System.out.println();
        }
    }
}
