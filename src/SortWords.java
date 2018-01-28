import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class SortWords {

	public static <E> void main(String[] args) throws IOException {
		//Create an algorithm to iterate through a given word, check if the spelling is correct and
		//If it is not, calculate weights for a dictionary of words, and suggest the closest match
		//Ex NRM is typed, but NEM is the intended word. The program would calculate weights for other
		//Coins in a complete dictionary, and the word that's closest matched is suggested
		
		LinkedList<E> l = new LinkedList<E>();
		
		HashMap<String, String> hm = new HashMap<String, String>();
		Scanner sc = new Scanner(System.in);
		String input = sc.nextLine();
		loadCoins(hm);
		ArrayList<String> al = loadCoins();
		compareWeights(hm, input, al);
	}
	
	private static void compareWeights(HashMap<String, String> hm, String input, ArrayList<String> l) {
		int num2 = Integer.parseInt(getValue(input));
		for (int i = 0; i < hm.size(); i++) {
			for (int n = 0; n < hm.size() - i; n++) {
				int num1 = Integer.parseInt(getValue(l.get(i)));
				if (num1 != num2) {
					//Code to traverse hashmap and find values, add to arraylist, and then proceed to compair all gathered
					//values to the input value, and find closest
				}
			}
		}
		
	}

	public static ArrayList<String> loadCoins() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("coinlist.txt"));
		ArrayList<String> l = new ArrayList<String>();
		for (int i = 0; i < 1; i++) 
			l.add(br.readLine());
		return l;
	}
	public static HashMap<String, String> loadCoins(HashMap<String, String> hm) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("coinlist.txt"));
		for (int i = 0; i < 1; i++) {
			String temp = br.readLine();
			hm.put(temp, getValue(temp));
		}
		return hm;
	}
	
	public static String getValue(String t) {
		char[] a = t.toCharArray();
		int value = 0;
		for (int i = 0; i < a.length; i++) {
			value += Character.getNumericValue(a[i]);
		}
		return String.valueOf(value);
	}
}