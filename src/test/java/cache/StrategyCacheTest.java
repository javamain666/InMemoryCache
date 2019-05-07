package cache;

import cache.policies.PolicyType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StrategyCacheTest {
  private static final String KEY0 = "key0";
  private static final String KEY1 = "key1";
  private static final String KEY2 = "key2";
  private static final String KEY3 = "key3";
  private static final Integer VALUE0 = 0;
  private static final Integer VALUE1 = 1;
  private static final Integer VALUE2 = 2;

  private StrategyCache<String, Integer> strategyCache;

  @Before
  public void setStrategyCache() {
    strategyCache = new StrategyCache<>(2, PolicyType.LEAST_FREQUENTLY_USED);
  }

  @After
  public void clearCache() {
    strategyCache.clearCache();
  }

  @Test
  public void testPutGetRemoveObject() {
      System.out.println("Put Get Remove Object");
    strategyCache.putIntoCache(KEY0, VALUE0);
    assertEquals(VALUE0, strategyCache.getFromCache(KEY0));
    assertEquals(1, strategyCache.getCacheSize());

    strategyCache.deleteFromCache(KEY0);
    assertNull(strategyCache.getFromCache(KEY0));
  }

  @Test
  public void testDeleteObjectFromStrategyCache() {
      System.out.println("Delete Object From Strategy Cache");
    strategyCache.putIntoCache(KEY0, VALUE0);
    strategyCache.putIntoCache(KEY1, VALUE1);

    assertEquals(VALUE0, strategyCache.getMemoryCache().getFromCache(KEY0));
   

    strategyCache.deleteFromCache(KEY0);

    assertNull(strategyCache.getMemoryCache().getFromCache(KEY0));
    
  }

  
  @Test
  public void testGetObjectFailedWhenObjectIsNotContained() {
      System.out.println("Get Object Failed When Object Is Not Contained");
    strategyCache.putIntoCache(KEY0, VALUE0);

    assertEquals(VALUE0, strategyCache.getFromCache(KEY0));
    assertNull(strategyCache.getFromCache(KEY3));
  }

 

  @Test
  public void testPutObjectIntoCacheWhenStrategyCacheHasEmptySpace() {
      System.out.println("Put Object Into Cache When Strategy Cache Has Empty Space");
      assertTrue(strategyCache.getMemoryCache().hasFreeSpace());

    strategyCache.putIntoCache(KEY0, VALUE0);

    assertEquals(VALUE0, strategyCache.getFromCache(KEY0));
    assertEquals(VALUE0, strategyCache.getMemoryCache().getFromCache(KEY0));
   
  }

  @Test
  public void testPutObjectIntoCacheWhenObjectIsContainedInStrategyCache() {
      System.out.println("Put Object Into Cache When Object Is Contained In Strategy Cache");
      strategyCache.putIntoCache(KEY0, VALUE0);
    assertEquals(VALUE0, strategyCache.getFromCache(KEY0));
    assertEquals(VALUE0, strategyCache.getMemoryCache().getFromCache(KEY0));
    assertEquals(1, strategyCache.getMemoryCache().getCacheSize());

    strategyCache.putIntoCache(KEY0, VALUE1);                 // Putting an object with the same key but different value

    assertEquals(VALUE1, strategyCache.getFromCache(KEY0));
    assertEquals(VALUE1, strategyCache.getMemoryCache().getFromCache(KEY0));
    assertEquals(1, strategyCache.getMemoryCache().getCacheSize());
  }





  @Test
  public void testPutObjectIntoCacheAndReplaceCachedObject() {
      System.out.println("Put Object Into Cache And Replace Cached Object");
    strategyCache.putIntoCache(KEY0, VALUE0);
    strategyCache.putIntoCache(KEY1, VALUE1);

    assertFalse(strategyCache.hasFreeSpace());
    assertFalse(strategyCache.getCachePolicy().isObjectContained(KEY2));

    strategyCache.putIntoCache(KEY2, VALUE2);    // Replacing a cached object and putting a new object into vacant space

    assertEquals(VALUE2, strategyCache.getFromCache(KEY2));
    assertTrue(strategyCache.getCachePolicy().isObjectContained(KEY2));
    assertTrue(strategyCache.getMemoryCache().isObjectContained(KEY2));
   
  }

  @Test
  public void testGetCacheSize() {
      System.out.println("Get Cache Size");
    strategyCache.putIntoCache(KEY0, VALUE0);
    assertEquals(1, strategyCache.getCacheSize());

    strategyCache.putIntoCache(KEY1, VALUE1);
    assertEquals(2, strategyCache.getCacheSize());
  }

  @Test
  public void testIsObjectContained() {
      System.out.println("Is Object Contained");
    assertFalse(strategyCache.isObjectContained(KEY0));

    strategyCache.putIntoCache(KEY0, VALUE0);
    assertTrue(strategyCache.isObjectContained(KEY0));
  }

  @Test
  public void testHasFreeSpace() {
      System.out.println("Has Free Space");
    assertFalse(strategyCache.isObjectContained(KEY0));
    strategyCache.putIntoCache(KEY0, VALUE0);
    assertTrue(strategyCache.hasFreeSpace());

    strategyCache.putIntoCache(KEY1, VALUE1);
    assertFalse(strategyCache.hasFreeSpace());
  }

  @Test
  public void testClearCache() {
      System.out.println("Clear Cache");
    strategyCache.putIntoCache(KEY0, VALUE0);
    strategyCache.putIntoCache(KEY1, VALUE1);

    assertEquals(2, strategyCache.getCacheSize());
    assertTrue(strategyCache.getCachePolicy().isObjectContained(KEY0));
    assertTrue(strategyCache.getCachePolicy().isObjectContained(KEY1));

    strategyCache.clearCache();

    assertEquals(0, strategyCache.getCacheSize());
    assertFalse(strategyCache.getCachePolicy().isObjectContained(KEY0));
    assertFalse(strategyCache.getCachePolicy().isObjectContained(KEY1));
  }

  @Test
  public void testUseLeastRecentlyUsedCachePolicy() {
      System.out.println("Use Least Recently Used Cache Policy");
    strategyCache = new StrategyCache<>(1,  PolicyType.LEAST_RECENTLY_USED);

    strategyCache.putIntoCache(KEY0, VALUE0);
    assertEquals(VALUE0, strategyCache.getFromCache(KEY0));
    assertEquals(VALUE0, strategyCache.getMemoryCache().getFromCache(KEY0));
    
  }
}