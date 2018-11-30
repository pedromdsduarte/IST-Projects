package pt.tecnico.myDrive.service.dto;

import pt.tecnico.myDrive.domain.User;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormat;

public class FileDto {
	private String type;
	private String permissions;
	private int id;
	private String name;
	private DateTime date;
	private DateTime dtoCreation;
	private int size;
	private String ownerName;

	public FileDto(String type, String permissions, int id, String name, DateTime date, int size, User owner) {
		this.type = type;
		this.permissions = permissions;
		this.id = id;
		this.name = name;
		this.date = date;
		this.size = size;
		this.ownerName = owner.getName();
		this.dtoCreation = new DateTime();
	}

	@Override
	public String toString() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}
	public String getPermissions() {
		return this.permissions;
	}
	public String getId() {
		return Integer.toString(this.id);
	}
	public String getName() {
		return this.name;
	}
	public String getDate() {
		DateTimeFormatter dateformat = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
	    return date.toString(dateformat);
	}
	public String getSize() {
		return Integer.toString(this.size);
	}
	public String getOwner() {
		return this.ownerName;
	}

}