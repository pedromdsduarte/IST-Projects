package domospec;

public abstract class AbstractSpecEntity {

	private int id;

	public AbstractSpecEntity(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
