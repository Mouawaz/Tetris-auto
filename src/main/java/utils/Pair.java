package utils;

public class Pair<K, T> {
	private K _first;
	private T _second;

	public K getFst() {
		return _first;
	}

	public T getSnd() {
		return _second;
	}

	public Pair(K first, T second) {
		_first = first;
		_second = second;
	}

}
