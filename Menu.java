import java.io.IOException;
import java.util.Scanner;

public class Menu {
	static Scanner scan;
	public static double LoadFactor() {
		scan=new Scanner(System.in);
		System.out.println(">Please choose Load Factor 0.5 or 0.8");
		double loadFactor = Double.parseDouble(scan.nextLine());
		while (loadFactor != 0.5 && loadFactor != 0.8) {
			System.out.println(">Please choose load factor 0.5 or 0.8");
			loadFactor = Double.parseDouble(scan.nextLine());
		}
		return loadFactor;
   }
	public static String HashType() {
		System.out.println(">Please choose Hash Function SSF or PAF");
		String hashType=scan.nextLine();
		while (!hashType.equalsIgnoreCase("SSF")&&!hashType.equalsIgnoreCase("PAF")) {
			System.out.println(">Please choose Hash Function SSF or PAF");
		    hashType=scan.nextLine();
		}
		return hashType;
	}
	public static String CollisionHandlingType() {
		System.out.println(">Please choose Collision Handling DH or LP");
		String collisionHandling=scan.nextLine();
		while (!collisionHandling.equalsIgnoreCase("LP")&&!collisionHandling.equalsIgnoreCase("DH")) {
			System.out.println(">Please choose Hash Function");
			collisionHandling=scan.nextLine();
		}
		return collisionHandling;
	}
	public static int Operation() throws IOException {
		System.out.println();
		System.out.println("---------------------MENU---------------------");
		System.out.println(">Press 1 to delete an element");
		System.out.println(">Press 2 to see the performance results of the search txt.");
		System.out.println(">Press 3 to search for a word.");
		int num=Integer.parseInt(scan.nextLine());
		while (num!=1 && num!=2 && num!=3) {
			System.out.println(">Press 1 to delete an element");
			System.out.println(">Press 2 to see the performance results of the search txt.");
			System.out.println(">Press 3 to search for a word.");
			 num=Integer.parseInt(scan.nextLine());
		}
		return num;
		
	}
}
