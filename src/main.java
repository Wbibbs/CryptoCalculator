import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;
import org.json.*;

//Utilizes Org Json parsing, found at http://mvnrepository.com/artifact/org.json/json
//Licensed using the JSON license provided in the project

public class main {

	public static void main(String[] args) throws Exception {
		
		String baseURL = "https://api.coinmarketcap.com/v1/ticker/";
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter a currency you want to check"); //Full coin name, ex. bitcoin, reddcoin
		String coin = sc.nextLine();
		System.out.println("How many do you have? n for just information"); //n does nothing for now
		String amount = sc.nextLine();
		
		if (!amount.toLowerCase().equals("n")) {
			 getInfo(coin, baseURL);
		}
		
	}
	
	public static void getInfo(String coin, String baseURL) throws Exception {
		String base = baseURL  + coin;
		URL URL = new URL(base);
		BufferedReader br = new BufferedReader(new InputStreamReader(URL.openStream()));
		
		//Reads in entire json from web
        String inputLine;
        while ((inputLine = br.readLine()) != null)
            System.out.println(inputLine);
        br.close();
        
        //Parses json information, gives null exception
        JSONObject results = new JSONObject(inputLine);
        String n = results.getString("name");
        int a = results.getInt("price_usd");
        System.out.println(n + " " + a);
        System.out.println(inputLine);
	}
}
