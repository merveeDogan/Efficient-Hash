public class TableEntry <S, T> {
	 private S key;
	 private T value;
	 private States state;
	 private enum States {CURRENT, REMOVED} 
	 public TableEntry(S searchKey,T dataValue)
	 {
	 key = searchKey;
	 value=dataValue;
	 state = States.CURRENT;
	 
	 }public TableEntry(S searchKey)
	 {
	 key = searchKey;
	 state = States.CURRENT;
	 } 
	 public S getKey() {
		return key;
	}
	public void setKey(S key) {
		this.key = key;
	}
	public T getValue() {
		return value;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
	public boolean isIn() {
		if(state==States.CURRENT)
		return true;
		return false;
	}
	public boolean isRemoved() { //if entry removed before return true
		if (state==States.REMOVED) 
			return true;
		return false;
	}
	public void setToRemoved(){
		key = null;
		value = null;
		 state = States.REMOVED;
	} 
	    
}
