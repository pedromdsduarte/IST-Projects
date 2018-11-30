package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.SessionManager;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.domain.EnvVar;

import pt.tecnico.myDrive.exception.InvalidTokenException;

import java.util.TreeMap;


public class AddEnvVariableService extends MyDriveService {
	
	private long _token;
	private String _varName;
	private String _value;
	private TreeMap<String,String> _varsList;
	
	
	public AddEnvVariableService(long token, String varName, String value) throws InvalidTokenException {
		MyDrive md = MyDrive.getInstance();
		SessionManager sessionManager = md.getSessionManager();
		if(!sessionManager.isTokenValid(token))
			throw new InvalidTokenException();
		_token = token;
		_varName = varName;
		_value = value;
		_varsList = new TreeMap<String, String>();
	}
	
	@Override
	public void dispatch() {
	 
		MyDrive md = MyDrive.getInstance();
		Session session = md.getSessionManager().getSessionByToken(_token);
	 
		EnvVar newVar = new EnvVar(_varName, _value);
		session.addEnvVar(newVar);
	}
	 
	public TreeMap<String,String> getVarList() {
		MyDrive md = MyDrive.getInstance();
		Session session = md.getSessionManager().getSessionByToken(_token);
		
		_varsList.clear();
		
		for(EnvVar var : session.getEnvVarSet()) 
			_varsList.put(var.getName(), var.getValue());
		
		return _varsList;
	}
	
	
}
