public class HashValue {
	private String directory;
	private int count;
	
	public HashValue(String directory,int count) {
		this.directory = directory;
		this.count = count;
	}
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
