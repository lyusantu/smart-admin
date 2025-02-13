package net.lab1024.sa.base.module.support.cache;

import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import net.lab1024.sa.base.constant.ReloadConst;
import net.lab1024.sa.base.module.support.reload.core.annoation.SmartReload;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 缓存操作
 */
@RequiredArgsConstructor
@Service
public class CacheService {

    private final CaffeineCacheManager caffeineCacheManager;

    /**
     * 获取所有缓存名称
     *
     */
    public List<String> cacheNames() {
        return Lists.newArrayList(caffeineCacheManager.getCacheNames());
    }

    /**
     * 某个缓存下的所有key
     *
     */
    public List<String> cacheKey(String cacheName) {
        CaffeineCache cache = (CaffeineCache) caffeineCacheManager.getCache(cacheName);
        if (cache == null) {
            return Lists.newArrayList();
        }
        Set<Object> cacheKey = cache.getNativeCache().asMap().keySet();
        return cacheKey.stream().map(e -> e.toString()).collect(Collectors.toList());
    }

    /**
     * 移除某个key
     *
     */

    public void removeCache(String cacheName) {
        CaffeineCache cache = (CaffeineCache) caffeineCacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }

    @SmartReload(ReloadConst.CACHE_SERVICE)
    public void clearAllCache() {
        Collection<String> cacheNames = caffeineCacheManager.getCacheNames();
        for (String name : cacheNames) {
            CaffeineCache cache = (CaffeineCache) caffeineCacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
            }
        }
    }
}
