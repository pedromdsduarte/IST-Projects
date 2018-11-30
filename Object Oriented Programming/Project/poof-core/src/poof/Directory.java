package poof;

import java.util.Map;
import java.util.TreeMap;
import java.io.Serializable;
import java.util.Comparator;

public class Directory extends Entry implements Serializable {
	
	/**
	* Holds the absolute path of the directory
	*/
	private String _path = "";
	
	/**
	* The directory itself
	*/
	private Directory _own = null;
	
	/**
	* The parent directory
	*/
	private Directory _parent = null;
	
	/**
	* The entries contained in this directory
	*/
	private Map<String, Entry> _entries = new TreeMap<String, Entry>();

	
	public Directory (String name, User owner) {
		super(name,owner,false);
		_own = this;
	}
	
	public Directory (String name, User owner, Directory parent) {
		super(name,owner,false);
		_own = this;
		_parent = parent;
	}

	/**
	* Gets the absolute path of this directory
	* @return 
	*			the representation of the path
	*/
	public String getPath() {
		if (equals(_parent)) 
			return ("");
		else
			return _parent.getPath() + "/" + getName();
	}

	/**
	* Gets its own directory
	*/
	public Directory getOwn() {
		return _own;
	}

	/**
	* Gets the parent directory
	*/
	public Directory getParent() {
		return _parent;
	}

	/**
	* Gets the entries contained in itself
	*/
	public Map<String, Entry> getEntries() {
		return _entries;
	}

	/**
	* Gets its size
	*/
	@Override
	public int getSize() {
		return _entries.size() * 8; //Cada entrada tem um custo de 8 bytes
	}

	/**
	* Gets a file contained in itself
	* 
	* @param name
	* 			name of the file
	* @return
	* 			the file requested
	*/
	public File getFile(String name){
		return (File)_entries.get(name);
	}

	/**
	* Sets the parent and puts the parent in itself as ".."
	*/
	public void setParent(Directory parent) {
		_parent = parent;
		putParent(parent);
	}

	/**
	* Puts the entry in itself
	* 
	* @param entry
	* 			entry to add
	*/
	public void put(Entry entry) {
			_entries.put(entry.getName(),entry);
	}
	
	/**
	* Puts itself in itself
	*/
	public void putSelf() {
		_entries.put(".",this);
	}
	
	/**
	* Puts the parent in itself as ".."
	*/
	public void putParent(Directory parent) {
		_entries.put("..",parent);
	}

	/**
	* Gets an entry contained in itself
	* 
	* @return 
	* 			the requested entry
	*/
	public Entry get(String name) {
			return _entries.get(name);
	}
	
	
	/**
	* Deletes an entry contained in itself
	*
	* @param name
	* 			name of the requested entry
	*/
	public void deleteEntry(String name){
		_entries.remove(name);
	}
	
}
