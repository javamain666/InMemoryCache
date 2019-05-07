package cache.policies;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MyComparatorTest {
  private static final String KEY0 = "key0";
  private static final String KEY1 = "key1";
  private static final Long VALUE0 = 0L;
  private static final Long VALUE1 = 1L;

  private MyComparator<String> myComparator;
  private Map<String, Long> baseMap;

  @Before
  public void setUp() {
    baseMap = new HashMap<>();
    myComparator = new MyComparator<>(baseMap);
  }

  @After
  public void clear() {
    baseMap.clear();
  }

  @Test
  public void testValue1EqualsValue2() {
      System.out.println("Value1 Equals Value2");
    baseMap.put(KEY0, VALUE0);
    baseMap.put(KEY1, VALUE0);
    int result = myComparator.compare(KEY0, KEY1);

    assertEquals(-1, result);
  }

  @Test
  public void testValue1IsMoreThanValue2() {
      System.out.println("Value1 Is More Than Value2");  
    baseMap.put(KEY0, VALUE1);
    baseMap.put(KEY1, VALUE0);
    int result = myComparator.compare(KEY0, KEY1);

    assertEquals(1, result);
  }

  @Test
  public void testValue1IsLessThanValue2() {
      System.out.println("Value1 Is Less Than Value2");
    baseMap.put(KEY0, VALUE0);
    baseMap.put(KEY1, VALUE1);
    int result = myComparator.compare(KEY0, KEY1);

    assertEquals(-1, result);
  }

  @Test(expected = NullPointerException.class)
  public void testThrowExceptionWhenPutNullValue() {
      System.out.println("Throw Exception When Put Null Value");
    baseMap.put(KEY0, VALUE0);
    baseMap.put(KEY1, null);

    myComparator.compare(KEY0, KEY1);
  }
}
