



import cache.StrategyCache;
import cache.policies.PolicyType;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author fambulov.ak
 */
public class Main {
    public static void main(String[] args) {
         
        StrategyCache<Integer, String> mc = new StrategyCache<Integer,String>(300000,PolicyType.LEAST_FREQUENTLY_USED);
        
        for (int i = 0; i < 10000; i++) {
            mc.putIntoCache(i, ""+i);
        }
        System.out.println("cach size: "+mc.getCacheSize());
    }
}
