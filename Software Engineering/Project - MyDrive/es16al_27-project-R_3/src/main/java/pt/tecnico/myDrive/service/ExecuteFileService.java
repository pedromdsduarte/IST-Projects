package pt.tecnico.myDrive.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import pt.tecnico.myDrive.domain.App;
import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.Link;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.MyDriveFile;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.SessionManager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.DirectoryNotFoundException;
import pt.tecnico.myDrive.exception.FileIsADirectoryException;
import pt.tecnico.myDrive.exception.InvalidAppException;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.presentation.Shell;
import pt.tecnico.myDrive.presentation.Sys;

public class ExecuteFileService extends MyDriveService {

	private long token;
	private String filepath;
	private String[] args;
	private PlainFile file = null;

	public ExecuteFileService(long token, String filepath, String[] args) {
		this.token = token;
		this.filepath = filepath;
		this.args = args;
	}
	
	/*
	 * Resolves the actual file to be executed
	 * */
	private void resolveFile() throws FileIsADirectoryException, DirectoryNotFoundException {
		MyDriveFile file_given;
		MyDrive md = MyDrive.getInstance();
		SessionManager sm = md.getSessionManager();
		Dir dir;
		String filename;
		
		System.out.println("Path passed = "+filepath);
		// Se estiver na directoria actual
		if (!filepath.contains("/")) {
			dir = sm.getCurrentDirByToken(token); 
			filename = filepath;
		}
	
		else {
			String[] tokens = filepath.split("/");
			filename = tokens[tokens.length-1];
			String dir_path = filepath.substring(0, filepath.length()-filename.length());
			System.out.println("DirPath = "+dir_path);
					
			dir = md.browseToRelativeDir(token, dir_path);
			
			//Se for caminho absoluto
			/*if (dir_path.startsWith("/"))
				dir = md.browseToDir(dir_path);
			//Se for caminho relativo
			else 
				dir = md.browseToRelativeDir(token, dir_path);*/
		}
				
		file_given = md.getFileByName(dir.getFileSet(), filename);
		
		if (file_given instanceof Link) {
			MyDriveFile resolved = ((Link) file_given).resolveLink();
			
			if (resolved instanceof PlainFile)
				file = (PlainFile) resolved;
			else {
				throw new FileIsADirectoryException(filename);
			}
			
		} else if (file_given instanceof App || file_given instanceof PlainFile) {
			file = (PlainFile) file_given;
		} else {
			throw new FileIsADirectoryException(filename);
		}
		
	
			
		
	}
	
	@Override
	protected void dispatch() throws MyDriveException {
		MyDrive md = MyDrive.getInstance();
		SessionManager sm = md.getSessionManager();
		resolveFile();
		
		//Se for um plainfile
		if (file instanceof PlainFile && !(file instanceof App)) {
			String ext = file.getExtension();
			User user = sm.getUserByToken(token);
			App app = user.getAppByExtension(ext);
			
			//Se tiver uma extensao e uma app associada
			if (ext != null && app != null) {
				String name = app.getName();
				String[] arg = {file.getName()};			
				
				try {
					Shell.run(name, arg);
				} catch (ClassNotFoundException | SecurityException | NoSuchMethodException | IllegalArgumentException
						| IllegalAccessException | InvocationTargetException e) {
					throw new InvalidAppException();
				}
			} 
			
			//Se nao tiver extensao ou app associada
			else if (ext == null || app == null) {
				String content = file.getText();
				String[] lines = content.split("\n");

				/*try {
					Sys.main(lines);
				} catch (IOException e) {
					throw new InvalidAppException();
				}*/
				
				for (String line : lines) {
					try {
						Sys.main(line.split(" "));
					} catch (IOException e) {
						throw new InvalidAppException();
					}
				}
			}
		}
		
		//Se e app
		else if (file instanceof App) { 
			String method = file.getText();
			
			try {
				Shell.run(method, args);
			} catch (ClassNotFoundException | SecurityException | NoSuchMethodException | IllegalArgumentException
					| IllegalAccessException | InvocationTargetException e) {
				throw new InvalidAppException();
			}
		}
		
	}
}
