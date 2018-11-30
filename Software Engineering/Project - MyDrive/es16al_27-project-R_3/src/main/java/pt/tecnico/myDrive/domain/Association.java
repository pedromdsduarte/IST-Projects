package pt.tecnico.myDrive.domain;

import pt.tecnico.myDrive.exception.InvalidExtensionException;

public class Association extends Association_Base {
    
    public Association(User u, String extension, App app) {
    	
        super();
        setUser(u);
        setExtension(extension);
        setApp(app);
    }
    
    
    @Override
    public void setUser(User user) throws IllegalArgumentException {
    	
    	if (user == null)
    		throw new IllegalArgumentException("User cannot be null!");
    	else
    		user.addAssociation(this);
    }
    
    @Override
    public void setExtension (String extension) throws InvalidExtensionException {
    	
    	if(extension == null || extension.isEmpty())
    		throw new InvalidExtensionException();
    	else
    		super.setExtension(extension);
    }
    
    
    @Override
    public void setApp(App app) throws IllegalArgumentException {
    
    	if (app == null)
    		throw new IllegalArgumentException("App file cannot be null!");
    	else
    		app.addAssociation(this);
    }
}

