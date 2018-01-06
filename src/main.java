import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;
import org.json.*;

//Utilizes Org Json parsing, found at http://mvnrepository.com/artifact/org.json/json
//Licensed using the JSON license provided in the project

public class main {

	static HashMap<String, Coin> hm = new HashMap<String, Coin>();
	static DecimalFormat moneyFormat = new DecimalFormat("$#,##0.00");
	static DecimalFormat standardFormat = new DecimalFormat("#,###.########");
	static Double currentValue;
	public static void main(String[] args) throws Exception {

		String baseURL = "https://api.coinmarketcap.com/v1/ticker/";
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter a currency you want to check"); //Full coin name, ex. bitcoin, reddcoin
		String coin = sc.nextLine();
		System.out.println("How many do you have? n for just information"); //n does nothing for now
		String amount = sc.nextLine();

		if (!amount.toLowerCase().equals("n")) {
			getInfo(coin, baseURL, Double.parseDouble(amount));
		}
		
		printInfo();

	}

	public static void printInfo() {
		System.out.println("Rank #" + hm.get("reddcoin") + ": " + hm.get("reddcoin").getRank() + " - " + hm.get("reddcoin").getSymbol());
		if (hm.get("reddcoin").getMaxSupply().equals("None"))
			System.out.println("Max Supply: " + hm.get("reddcoin").getMaxSupply() + " - This generally means the coin is not minable, or has all coins mined");
		else
			System.out.println("Max Supply: " + standardFormat.format(Double.parseDouble(hm.get("reddcoin").getMaxSupply())));
		System.out.println("Total Supply: " + standardFormat.format(Double.parseDouble(hm.get("reddcoin").getTotalSupply())));
		System.out.println("Available Supply: " + standardFormat.format(Double.parseDouble(hm.get("reddcoin").getAvailableSupply())));
		System.out.println("Percent available: " + ((Double.parseDouble(hm.get("reddcoin").getAvailableSupply()) / Double.parseDouble(hm.get("reddcoin").getTotalSupply()) * 100)));
		if (Double.parseDouble(hm.get("reddcoin").getPrice()) <= 0.01) 
			System.out.println("USD Value per coin: " + "$" + standardFormat.format(hm.get("reddcoin").getPrice()));
		else
			System.out.println("USD Value per coin: " + moneyFormat.format(hm.get("reddcoin").getPrice()));

		System.out.println("Approximate value of your held coins: " + moneyFormat.format(currentValue));
	}
	
	public static void getInfo(String coin, String baseURL, Double amount) throws Exception {
		String base = baseURL  + coin;
		URL URL = new URL(base);
		BufferedReader br = new BufferedReader(new InputStreamReader(URL.openStream()));

		//Reads in entire json from web
		String inputLine, tempLine = "";
		while ((inputLine = br.readLine()) != null) {
			System.out.println(inputLine);
			tempLine += inputLine;
		}
		br.close();

		//Parses json information, gives null exception
		StringBuilder sb = new StringBuilder(tempLine);
		sb.deleteCharAt(0);
		String newString = sb.toString();
		JSONObject results = new JSONObject(newString);
		String nameCoin = results.getString("name");
		String rank = results.getString("rank");
		String symbolCoin = results.getString("symbol");
		String priceCoin = results.getString("price_usd");
		String priceBtc = results.getString("price_btc");
		String dayVol = results.getString("24h_volume_usd");
		String marketCap = results.getString("market_cap_usd");
		String availableSupply = results.getString("available_supply");
		String totalSupply = results.getString("total_supply");
		String maxSupply = null;
		try{
			maxSupply = results.getString("max_supply");
		} catch (Exception e) {
			maxSupply = "None";
		}
		String percentHour = results.getString("percent_change_1h");
		String percentDay = results.getString("percent_change_24h");
		String percentWeek = results.getString("percent_change_7d");
		currentValue = amount * Double.parseDouble(priceCoin);

		hm.put(nameCoin, new Coin(nameCoin, rank, symbolCoin, priceCoin, priceBtc, dayVol, marketCap, availableSupply, totalSupply, maxSupply));
	}
}
