package tw.jdi.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Java內部自定義內存模板[V3.0版]</br>
 * key改為用<K>泛型來定義，value改為用<V>泛型來定義[MashMap]改為[ConcurrentHashMap]
 * 
 * @param <K> Key
 * @param <V> value
 * @author PAN
 */

public abstract class Cache<K, V> {

	protected Map<K, V> cacheMap;

	public Cache() {
		this.cacheMap = new ConcurrentHashMap<>();
	}

	public boolean hasData(K key) {
		return cacheMap.containsKey(key);
	}

	public void setData(K key, V data) {
		cacheMap.put(key, data);
	}

	public V getData(K key) {
		return cacheMap.get(key);
	}

	public Set<K> getAllKey() {
		return cacheMap.keySet();
	}

	public Collection<V> getAllValue() {
		return cacheMap.values();
	}

	public Set<Entry<K, V>> getAllKeyAndData() {
		return cacheMap.entrySet();
	}

	public Map<K, V> getMap() {
		return cacheMap;
	}

	public void removeData(K key) {
		cacheMap.remove(key);
	}

}
