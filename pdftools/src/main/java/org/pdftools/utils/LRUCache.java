package org.pdftools.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Lease Recently Used cache based on the size.
 *
 * @author asingh7!
 * @param <K> the key type
 * @param <V> the value type
 */
public class LRUCache <K,V> extends LinkedHashMap<K,V> {

    private static final long serialVersionUID = 1227095126364054009L;

    private final int maxEntries;

    /**
     * Instantiates a new LRU cache.
     */
    public LRUCache(final int maxEntries) {
        super(maxEntries + 1, 1.0f, true);
        this.maxEntries = maxEntries;
    }
    
    /**
     * Removes the eldest entry.
     *
     * @return true, if successful
     */
    @Override
    protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
        return super.size() > maxEntries;
    }
}