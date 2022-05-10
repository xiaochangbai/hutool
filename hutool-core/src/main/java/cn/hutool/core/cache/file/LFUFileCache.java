package cn.hutool.core.cache.file;

import cn.hutool.core.cache.Cache;
import cn.hutool.core.cache.impl.LFUCache;

import java.io.File;

/**
 *  使用LFU缓存文件，以解决频繁读取文件引起的性能问题
 * @author Looly
 *
 */
public class LFUFileCache extends AbstractFileCache{
	private static final long serialVersionUID = 1L;

	/**
	 * 构造<br>
	 * 最大文件大小为缓存容量的一半<br>
	 * 默认无超时
	 * @param capacity 缓存容量
	 */
	public LFUFileCache(final int capacity) {
		this(capacity, capacity / 2, 0);
	}

	/**
	 * 构造<br>
	 * 默认无超时
	 * @param capacity 缓存容量
	 * @param maxFileSize 最大文件大小
	 */
	public LFUFileCache(final int capacity, final int maxFileSize) {
		this(capacity, maxFileSize, 0);
	}

	/**
	 * 构造
	 * @param capacity 缓存容量
	 * @param maxFileSize 文件最大大小
	 * @param timeout 默认超时时间，0表示无默认超时
	 */
	public LFUFileCache(final int capacity, final int maxFileSize, final long timeout) {
		super(capacity, maxFileSize, timeout);
	}

	@Override
	protected Cache<File, byte[]> initCache() {
		return new LFUCache<File, byte[]>(LFUFileCache.this.capacity, LFUFileCache.this.timeout) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isFull() {
				return LFUFileCache.this.usedSize > this.capacity;
			}

			@Override
			protected void onRemove(final File key, final byte[] cachedObject) {
				usedSize -= cachedObject.length;
			}
		};
	}

}