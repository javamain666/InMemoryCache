package cache.policies;

import cache.StrategyCache;
import org.junit.After;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class LeastFrequentlyUsedCacheTest {
  private static final String KEY = "key";
  private static final String KEY0 = "key0";
  private static final String KEY1 = "key1";
  private static final String KEY2 = "key2";
  private static final String KEY3 = "key3";
  private static final String KEY4 = "key4";
  private static final String KEY5 = "key5";
  private static final Integer VALUE0 = 0;
  private static final Integer VALUE1 = 1;
  private static final Integer VALUE5 = 5;

  private StrategyCache<String, Integer> strategyCache;

  @After
  public void clearCache() {
    strategyCache.clearCache();
  }

  @Test
  public void testReplaceObjectFromCache() {
      System.out.println("Replace Object From Cache");
    strategyCache = new StrategyCache<>(4, PolicyType.LEAST_FREQUENTLY_USED);

    IntStream.range(0, 5).forEachOrdered(i -> {
      strategyCache.putIntoCache(KEY + i, i);
      assertTrue(strategyCache.isObjectContained(KEY + i));
      strategyCache.getFromCache(KEY + i);
      if (i == 1) return;                                      // Getting an object with key 'key1' from cache only once
      strategyCache.getFromCache(KEY + i);
    });

    assertTrue(strategyCache.isObjectContained(KEY0));
    assertTrue(strategyCache.isObjectContained(KEY2));
    assertTrue(strategyCache.isObjectContained(KEY3));
    assertTrue(strategyCache.isObjectContained(KEY4));
    assertFalse(strategyCache.isObjectContained(KEY1));        // Replaced from 1st level cache as least frequently used

    strategyCache.getFromCache(KEY0);
    strategyCache.getFromCache(KEY3);
    strategyCache.getFromCache(KEY4);
    strategyCache.putIntoCache(KEY5, VALUE5);

    assertTrue(strategyCache.isObjectContained(KEY0));
    assertTrue(strategyCache.isObjectContained(KEY3));
    assertTrue(strategyCache.isObjectContained(KEY4));
    assertTrue(strategyCache.isObjectContained(KEY5));
    assertFalse(strategyCache.isObjectContained(KEY2));        // Replaced from 2nd level cache as least frequently used
  }

  @Test
  public void testDeleteObjectFailedWhenObjectIsNotContained() {
      System.out.println("Delete Object Failed When Object Is Not Contained");
    strategyCache = new StrategyCache<>(2, PolicyType.LEAST_FREQUENTLY_USED);

    strategyCache.putIntoCache(KEY0, VALUE0);
    strategyCache.putIntoCache(KEY1, VALUE1);

    assertTrue(strategyCache.isObjectContained(KEY0));
    assertTrue(strategyCache.isObjectContained(KEY1));
    assertEquals(2, strategyCache.getCacheSize());

    strategyCache.deleteFromCache(KEY2);

    assertTrue(strategyCache.isObjectContained(KEY0));
    assertTrue(strategyCache.isObjectContained(KEY1));
    assertEquals(2, strategyCache.getCacheSize());
  }
}