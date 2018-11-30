package pt.tecnico.myDrive.domain;


import pt.tecnico.myDrive.exception.InvalidEnvVarAttributes;

public class EnvVar extends EnvVar_Base {
    
    public EnvVar(String varName, String varValue) throws InvalidEnvVarAttributes {
        super();
        if(varName == null || varValue == null || varName.equals("") || varValue.equals(""))
			throw new InvalidEnvVarAttributes();
        setName(varName);
        setValue(varValue);
    }
    
}
