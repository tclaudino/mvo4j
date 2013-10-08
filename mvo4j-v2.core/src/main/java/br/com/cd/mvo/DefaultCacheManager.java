package br.com.cd.mvo;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultCacheManager implements CacheManager {

	private Map<String, Entry<Date, SoftReference<Object>>> caches = new LinkedHashMap<String, Map.Entry<Date, SoftReference<Object>>>();

	private long cacheTime;

	public DefaultCacheManager(long cacheManagerMaxSize) {
		this.cacheTime = cacheManagerMaxSize;
	}

	@Override
	public long getCacheManagerMaxSize() {
		return cacheTime;
	}

	@Override
	public Object getObject(String key) {
		if (key != null && !"".equals(key)) {
			Date now = new Date();

			removeIfExpired(key, now);

			return get(key);
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T> T getObject(Class<T> clazz, String key) {
		Object obj = getObject(key);
		if (obj != null) {
			if (obj.getClass().equals(clazz)) {
				return (T) obj;
			}
			Class c = obj.getClass().getSuperclass();
			do {
				if (c.equals(clazz)) {
					return (T) obj;
				}
				c = c.getSuperclass();
			} while (c.getSuperclass() != null && !c.equals(c.getSuperclass()));
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public java.util.List<?> getObjects(String key) {
		if (key != null && !"".equals(key)) {
			Date now = new Date();

			removeIfExpired(key, now);

			Object obj = get(key);
			if (obj != null && obj instanceof List) {
				try {
					return (List<?>) obj;
				} catch (Exception e) {
					Logger.getLogger(getClass().getName()).log(Level.OFF,
							e.getMessage());
				}
			}
		}
		return new ArrayList();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T> List<T> getObjects(Class<T> clazz, String key) {
		List objList = getObjects(key);

		ArrayList<T> toResult = new ArrayList<T>();
		for (Object obj : objList) {

			if (obj.getClass().equals(clazz)) {
				toResult.add((T) obj);
			}
			Class c = obj.getClass().getSuperclass();
			do {
				if (c.equals(clazz)) {
					toResult.add((T) obj);
				}
				c = c.getSuperclass();
			} while (c.getSuperclass() != null && !c.equals(c.getSuperclass()));
		}
		return toResult;
	}

	private Object get(String key) {
		if (this.caches.containsKey(key)) {
			return this.caches.get(key).getValue().get();
		}
		return null;
	}

	@Override
	public boolean add(String key, Object value, long seconds) {
		if (key != null && !"".equals(key) && value != null) {
			Date now = new Date();

			removeIfExpired(key, now);

			// caching the object
			put(key, value, new Date(now.getTime() + (seconds * 1000)));
			return true;
		}
		return false;
	}

	private void put(String key, final Object value, final Date expires) {
		if (!caches.containsKey(key)) {
			return;
		}
		if (removeEldestEntry()) {
			int index = caches.size() - 1;
			if (index >= 0) {
				caches.remove(caches.values().toArray()[index]);
			}
		}

		final SoftReference<Object> softValue = new SoftReference<Object>(value);
		caches.put(key, new Entry<Date, SoftReference<Object>>() {

			@Override
			public Date getKey() {
				return expires;
			}

			@Override
			public SoftReference<Object> getValue() {
				return softValue;
			}

			@Override
			public SoftReference<Object> setValue(SoftReference<Object> value) {
				throw new UnsupportedOperationException();
			}
		});
	}

	protected void remove(String key) {
		if (caches.containsKey(key)) {
			caches.remove(key);
		}
	}

	protected void removeIfExpired(String key, Date now) {
		if (caches.containsKey(key)) {
			Entry<Date, SoftReference<Object>> expire = caches.get(key);

			if (now.after(expire.getKey())) {
				remove(key);
			}
		}
	}

	protected boolean removeEldestEntry() {
		long maxTime = this.getCacheManagerMaxSize();
		return (maxTime > -1 && caches.size() > maxTime);
	}
}
