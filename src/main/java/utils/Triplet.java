package utils;

public class Triplet<K, T, V> {
	private K _first;
	private T _second;
	private V _third;

	public K getFst() {
		return _first;
	}

	public T getSnd() {
		return _second;
	}

	public V getTrd() {
		return _third;
	}

	public Triplet(K first, T second, V third) {
		_first = first;
		_second = second;
		_third = third;
	}

}
