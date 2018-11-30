package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Dir;


import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.UnknownFileTypeException;
import pt.tecnico.myDrive.exception.InvalidTokenException;
import pt.tecnico.myDrive.exception.LinkCyclicityFoundException;
import pt.tecnico.myDrive.exception.CreateLinkWithoutContentException;
import pt.tecnico.myDrive.exception.DuplicateDirectoryException;
import pt.tecnico.myDrive.exception.DuplicateFilenameException;
import pt.tecnico.myDrive.exception.InvalidFilenameException;
import pt.tecnico.myDrive.exception.CreateDirWithContentException;


public class CreateFileService extends MyDriveService {

	private long token;
	private String name;
	private String type;
	private String content;
	
	public CreateFileService(long token, String name, String type) {
		
		this.token = token;
		this.name = name;
		if(type != null)
			this.type = type.toLowerCase();
		this.content = null;
	}

	public CreateFileService(long token, String name, String type, String content) {
		
		this.token = token;
		this.name = name;
		if(type != null)
			this.type = type.toLowerCase();
		this.content = content;
	}
	
	
	@Override
	public void dispatch() throws MyDriveException, CreateLinkWithoutContentException, CreateDirWithContentException,
	InvalidTokenException, InvalidFilenameException, DuplicateFilenameException, DuplicateDirectoryException, LinkCyclicityFoundException {
		
		MyDrive md = MyDrive.getInstance();
		
		User owner = md.getSessionManager().getUserByToken(token);
		Dir parent = md.getSessionManager().getCurrentDirByToken(token);
		
		if(type == null)
			throw new UnknownFileTypeException("null type!!!");
		//else
		switch(type) {
		
		case "plainfile":
			if(content != null)
				md.createPlainFile(name, owner, parent, content);
			else
				md.createPlainFile(name, owner, parent, "");	
			break;
			
		case "app":
			if(content != null)
				md.createApp(name, owner, parent, content);
			else
				md.createApp(name, owner, parent, "");	
			break;
		
		case "link":
			if(content != null)
				md.createLink(token, name, owner, parent, content);
			else
				throw new CreateLinkWithoutContentException(name);
			break;
			
		case "dir":
			if(content == null)
				md.createDir(name, 2, owner, parent);
			else
				throw new CreateDirWithContentException(name);
			break;
			
		default:
			throw new UnknownFileTypeException(type);
		}
	}
}
