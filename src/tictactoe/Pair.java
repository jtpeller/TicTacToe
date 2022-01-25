package tictactoe;

public class Pair<T> {
	private T v1;
	private T v2;
	
	/***** CONSTRUCTORS *****/
	public Pair(T v1, T v2) {
		this.v1 = v1;
		this.v2 = v2;
	}
	
	public Pair(Pair<T> p1) {
		this.v1 = p1.getV1();
		this.v2 = p1.getV2();
	}
	
	/***** STANDARD METHODS *****/
	public boolean equals(T v1, T v2) {
		return (v1 == this.v1 && v2 == this.v2);
	}
	
	public boolean equals(Pair<T> p1) {
		return (p1.getV1() == this.getV1() && p1.getV2() == this.getV2());
	}
	
	public String toString() {
		return (getV1() + ", " + getV2());
	}
	
	/***** GETTERS & SETTERS *****/
	// returns the type of the class
	public String getType() {
		return v1.getClass().getSimpleName();
	}
	
	// returns the first pair value
	public T getV1() {
		return v1;
	}
	
	// returns the second pair value
	public T getV2() {
		return v2;
	}
	
	// set first value
	public void setV1(T v1) {
		this.v1 = v1;
	}
	
	// set second value
	public void setV2(T v2) {
		this.v2 = v2;
	}
	
	// sets the entire pair given values
	public void setPair(T v1, T v2) {
		this.v1 = v1;
		this.v2 = v2;
	}
	
	// sets the entire pair, given another pair
	public void setPair(Pair<T> p1) {
		this.v1 = p1.getV1();
		this.v2 = p1.getV2();
	}
}
