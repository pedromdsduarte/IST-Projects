package pt.tecnico.myDrive;

public class Test {
	public static void main (String[] args) {
		for (String s : args)
			System.out.println(s);
	}
	
	public static void echo (String[] args) {
		for (String s : args)
			System.out.print(s + " ");
		System.out.println();
	}
}
