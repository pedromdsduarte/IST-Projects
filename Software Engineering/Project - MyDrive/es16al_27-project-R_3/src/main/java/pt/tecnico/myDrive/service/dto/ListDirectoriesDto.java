package pt.tecnico.myDrive.service.dto;


import java.util.List;
import java.util.ArrayList;

public class ListDirectoriesDto {
	private ArrayList<FileDto> files;

	public ListDirectoriesDto() {
		files = new ArrayList<FileDto>();
	}

	public boolean addFile(FileDto f) {
		if (f == null) return false;
		return files.add(f);
	}
	public boolean removeFile(FileDto f) {
		return files.remove(f);
	}

	@Override
	public String toString() {
		String result = String.format("%-12s %-15s %-7s %-23s %-3s %-22s %-23s", "Type", "Permissions", "Size", "Filename", "Id", "Date", "Owner name");
		for (FileDto f : files) {
			result += System.getProperty("line.separator");
			result += String.format("%-12s %-15s %-7s %-23s %-3s %-22s %-23s", f.getType(), f.getPermissions(), f.getSize(), f.getName(), f.getId(), f.getDate(), f.getOwner());
		}
		return result;
	}

	public void print() {
		System.out.println(String.format("%-12s %-15s %-7s %-23s %-3s %-22s %-23s", "Type", "Permissions", "Size", "Filename", "Id", "Date", "Owner name"));
		for (FileDto f : files) {
			System.out.println(String.format("%-12s %-15s %-7s %-23s %-3s %-22s %-23s", f.getType(), f.getPermissions(), f.getSize(), f.getName(), f.getId(), f.getDate(), f.getOwner()));
		}
	}

}