package cache.policies;

import cache.StrategyCache;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LeastRecentlyUsedCacheTest {
  private static final String KEY = "key";
  private static final String KEY0 = "key0";
  private static final String KEY1 = "key1";
  private static final String KEY2 = "key2";
  private static final String KEY3 = "key3";
  private static final String KEY4 = "key4";
  private static final Integer VALUE4 = 4;

  private StrategyCache<String, Integer> strategyCache;

  @Before
  public void setStrategyCache() {
    strategyCache = new StrategyCache<>(4, PolicyType.LEAST_RECENTLY_USED);
  }

  @After
  public void clearCache() {
    strategyCache.clearCache();
  }

  @Test
  public void testReplaceObjectFromCache() {
      System.out.println("Replace Object From Cache");
    IntStream.range(0, 4).forEachOrdered(i -> {
      strategyCache.putIntoCache(KEY + i, i);
      assertTrue(strategyCache.isObjectContained(KEY + i));
      strategyCache.getFromCache(KEY + i);
    });

    strategyCache.putIntoCache(KEY4, VALUE4);

    assertTrue(strategyCache.isObjectContained(KEY1));
    assertTrue(strategyCache.isObjectContained(KEY2));
    assertTrue(strategyCache.isObjectContained(KEY3));
    assertTrue(strategyCache.isObjectContained(KEY4));
    assertFalse(strategyCache.isObjectContained(KEY0)); // Replaced from 1st level cache as least recently used
  }
}
