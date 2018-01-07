import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
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
			getInfo(coin, baseURL, Double.parseDouble(amount));
		}

	}

	public static void getInfo(String coin, String baseURL, Double amount) throws Exception {
		String base = baseURL  + coin;
		URL URL = new URL(base);
		BufferedReader br = new BufferedReader(new InputStreamReader(URL.openStream()));

		//Reads in entire json from web
		String inputLine, tempLine = null;
		while ((inputLine = br.readLine()) != null) {
			System.out.println(inputLine);
			tempLine += inputLine;
		}
		br.close();

		//Parses json information, gives null exception
		StringBuilder sb = new StringBuilder(tempLine);
		for (int i = 0; i < 5; i++) {//Deletes a null and [ from beginning string
			sb.deleteCharAt(0);
		}
		String newString = sb.toString();
		JSONObject results = new JSONObject(newString);
		String nameCoin = results.getString("name");
		int rank = results.getInt("rank");
		String symbolCoin = results.getString("symbol");
		Float priceCoin = results.getFloat("price_usd");
		Float priceBtc = results.getFloat("price_btc");
		Double dayVol = results.getDouble("24h_volume_usd");
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
		Double currentValue = amount * priceCoin;
		DecimalFormat moneyFormat = new DecimalFormat("$#,##0.00");
		DecimalFormat standardFormat = new DecimalFormat("#,###.########");
		System.out.println("Rank #" + rank + ": " + nameCoin + " - " + symbolCoin);
		if (maxSupply.equals("None"))
			System.out.println("Max Supply: " + maxSupply + " - This generally means the coin is not minable, or has all coins mined");
		else
			System.out.println("Max Supply: " + standardFormat.format(Double.parseDouble(maxSupply)));
		System.out.println("Total Supply: " + standardFormat.format(Double.parseDouble(totalSupply)));
		System.out.println("Available Supply: " + standardFormat.format(Double.parseDouble(availableSupply)));
		System.out.println("Percent available: " + ((Double.parseDouble(availableSupply) / Double.parseDouble(totalSupply) * 100)));
		if (priceCoin <= 0.01) 
			System.out.println("USD Value per coin: " + "$" + standardFormat.format(priceCoin));
		else
			System.out.println("USD Value per coin: " + moneyFormat.format(priceCoin));

		System.out.println("Approximate value of your held coins: " + moneyFormat.format(currentValue));
		
		//System.out.println(newString);
	}
}
