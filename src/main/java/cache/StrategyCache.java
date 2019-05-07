package cache;

import cache.policies.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import static java.lang.String.format;

public class StrategyCache<K, V  > implements Cache<K, V> {
  private static final Logger LOG = LoggerFactory.getLogger(StrategyCache.class);

  private final MemoryCache<K, V> firstLevelCache;

  private final CachePolicy<K> cachePolicy;

  public StrategyCache(final int memorySize, final PolicyType policyType) {
    this.firstLevelCache = new MemoryCache<>(memorySize);
  
    this.cachePolicy = getPolicyByType(policyType);
  }

  public StrategyCache(final int memorySize, final int fileSystemSize) {
    this.firstLevelCache = new MemoryCache<>(memorySize);
   
    this.cachePolicy = getPolicyByType(PolicyType.LEAST_FREQUENTLY_USED);
  }


  CachePolicy<K> getCachePolicy() {
    return cachePolicy;
  }

  private CachePolicy<K> getPolicyByType(PolicyType policyType) {
    if (policyType == PolicyType.LEAST_RECENTLY_USED) {
      return new LeastRecentlyUsedCachePolicy<>();
    } 
    return new LeastFrequentlyUsedCachePolicy<>();
  }

  @Override
  public synchronized void putIntoCache(K newKey, V newValue) {
    if (firstLevelCache.isObjectContained(newKey) || firstLevelCache.hasFreeSpace()) {
      LOG.debug(format("Put an object with key '%s' to the 1st level cache", newKey));
      firstLevelCache.putIntoCache(newKey, newValue);
      
    }
  else {
      replaceObject(newKey, newValue);
    }

    if (!cachePolicy.isObjectContained(newKey)) {
      LOG.debug(format("Put an object with key '%s' to cache policy repository", newKey));
      cachePolicy.putObject(newKey);
    }
  }

  private void replaceObject(K key, V value) {
    K keyToReplace = cachePolicy.getKeyToReplace();
    if (firstLevelCache.isObjectContained(keyToReplace)) {
      LOG.debug(format("Replace an object with key '%s' from 1st level cache", keyToReplace));
      firstLevelCache.deleteFromCache(keyToReplace);
      firstLevelCache.putIntoCache(key, value);
    } 
    cachePolicy.deleteObject(keyToReplace);
  }

  @Override
  public synchronized V getFromCache(K key) {
    if (firstLevelCache.isObjectContained(key)) {
      cachePolicy.putObject(key);
      return firstLevelCache.getFromCache(key);
    }
    return null;
  }

  @Override
  public synchronized void deleteFromCache(K key) {
    if (firstLevelCache.isObjectContained(key)) {
      LOG.debug(format("Delete an object with key '%s' from 1st level cache", key));
      firstLevelCache.deleteFromCache(key);
    } 
    cachePolicy.deleteObject(key);
  }

  MemoryCache<K, V> getMemoryCache() {
    return firstLevelCache;
  }
   
  @Override
  public int getCacheSize() {
    return firstLevelCache.getCacheSize();
  }
  
  @Override
  public boolean isObjectContained(K key) {
    return firstLevelCache.isObjectContained(key);
  }

  @Override
  public boolean hasFreeSpace() {
    return firstLevelCache.hasFreeSpace();
  }

  @Override
  public void clearCache() {
    firstLevelCache.clearCache();
    cachePolicy.clear();
  }
}
