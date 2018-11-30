package App;
import domospec.AbstractSpecEntity;

public class DomoBusSystem extends AbstractSpecEntity {

	private int id;
	private String name;
	private String version;
	private String date;
	


	public DomoBusSystem(int id, String name, String version, String date) {
		super(id);
		this.name = name;
		this.version = version;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
}
