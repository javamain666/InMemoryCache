package cache.policies;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PolicyTypeTest {
  private static final String LEAST_FREQUENTLY_USED = "LEAST_FREQUENTLY_USED";
  private static final String LEAST_RECENTLY_USED = "LEAST_RECENTLY_USED";
  
  private static final String ANOTHER_POLICY_TYPE = "another policy type";

  @Test
  public void testGetCorrectEnumTypes() {
      System.out.println("Get Correct Enum Types");
    assertEquals(PolicyType.LEAST_FREQUENTLY_USED, PolicyType.valueOf(LEAST_FREQUENTLY_USED));
    assertEquals(PolicyType.LEAST_RECENTLY_USED, PolicyType.valueOf(LEAST_RECENTLY_USED));
   
  }

  @Test(expected = IllegalArgumentException.class)
  public void testThrowExceptionWhenTypeIsNotCorrect() {
      System.out.println("Throw Exception When Type Is Not Correct");
    PolicyType.valueOf(ANOTHER_POLICY_TYPE);
  }
}
