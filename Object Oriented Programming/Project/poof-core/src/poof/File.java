package poof;
import java.io.Serializable;

public class File extends Entry {
	
	/**
	 * Holds the content of the File
	*/
	private String _content = "";

	
	public File(String name, User owner, boolean publicMode, String content) { 
		super(name,owner,publicMode);
		_content = content;
	}

	/**
	 * Gets the size of the file
	 * @return
	 * 		Size of the file
	*/
	@Override
	public int getSize() {
		return _content.length();
	}
	
	
	/**
	 * Gets the content of the file
	 * @return 
	 * 		Content of the File
	 */
	public String getContent(){
		return _content;
	}


	/**
	 * Adds content to the file
	 * @param to_add
	 * 			content to add
	 */

	public void addContent(String to_add){
		_content = _content + to_add + "\n";
	}

}
