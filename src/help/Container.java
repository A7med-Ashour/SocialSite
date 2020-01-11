package help;

public class Container<T> {
	
	private T value;

	public Container() {
		this.value = null;
	} 
	
	public Container(T value) {
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	
}
