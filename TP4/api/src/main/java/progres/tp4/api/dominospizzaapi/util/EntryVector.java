package progres.tp4.api.dominospizzaapi.util;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class EntryVector<K, V> {
	private Entry<K, V>[] entries;
	
	@SuppressWarnings("unchecked")
	private EntryVector(int initialCapacity) {
		this.entries = new Entry[initialCapacity];
	}
	
	public EntryVector() {
		this(0);
	}
	
	public V get(K key) {
		return stream().filter(entry -> Objects.equals(entry.getKey(), key))
		               .map(Entry::getValue)
		               .findAny()
		               .orElse(null);
	}
	
	public V getAt(int idx) {
		if (idx >= 0 && idx < entries.length)
			return entries[idx].getValue();
		return null;
	}
	
	public int indexOf(K key) {
		for (int i = 0; i < entries.length; i++)
			if (Objects.equals(entries[i].getKey(), key))
				return i;
		return -1;
	}
	
	public boolean exists(K key) {
		return stream().anyMatch(entry -> Objects.equals(entry.getKey(), key));
	}
	
	public boolean add(K key, V value) {
		if (exists(key)) return false;
		this.entries = ArrayUtils.add(entries, new Entry<>(key, value));
		return true;
	}
	
	public V remove(K key) {
		int idx     = indexOf(key);
		V   removed = null;
		if (idx >= 0) {
			removed = getAt(idx);
			this.entries = ArrayUtils.remove(entries, idx);
		}
		return removed;
	}
	
	@Contract(pure = true)
	public int size() {
		return this.entries.length;
	}
	
	public Entry<K, V>[] toArray() {
		return ArrayUtils.clone(entries);
	}
	
	public Stream<Entry<K, V>> stream() {
		return Arrays.stream(entries);
	}
	
	public Chained<K, V> chained() {
		return new Chained<>(this);
	}
	
	public Set<K> keys() {
		return stream().map(Entry::getKey).collect(Collectors.toSet());
	}
	
	public List<V> values() {
		return stream().map(Entry::getValue).collect(Collectors.toList());
	}
	
	private static class Chained<K, V> {
		private final EntryVector<K, V> vector;
		
		private Chained(@NotNull EntryVector<K, V> vector) {
			this.vector = vector;
		}
		
		@Contract("_, _ -> this")
		public final Chained<K, V> add(K key, V value) {
			vector.add(key, value);
			return this;
		}
		
		@SafeVarargs
		@Contract("_ -> this")
		public final Chained<K, V> remove(K... keys) {
			for (K key : keys) vector.remove(key);
			return this;
		}
		
		@Contract("_ -> this")
		public final Chained<K, V> remove(@NotNull BiPredicate<K, V> condition) {
			vector.stream()
			      .filter(entry -> condition.test(entry.getKey(), entry.getValue()))
			      .map(Entry::getKey)
			      .forEachOrdered(vector::remove);
			return this;
		}
		
		@Contract("_ -> this")
		public final Chained<K, V> each(@NotNull BiConsumer<K, V> action) {
			vector.stream().forEachOrdered(entry -> action.accept(entry.getKey(), entry.getValue()));
			return this;
		}
		
		@NotNull
		@Contract(pure = true)
		public final EntryVector<K, V> vector() {
			return this.vector;
		}
	}
}
