import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import org.json.*;

//Utilizes Org Json parsing, found at http://mvnrepository.com/artifact/org.json/json
//Licensed using the JSON license provided in the project

public class main {

	static HashMap<String, Coin> hm = new HashMap<String, Coin>();
	static DecimalFormat moneyFormat = new DecimalFormat("$#,##0.00");
	static DecimalFormat standardFormat = new DecimalFormat("#,###.########");
	public static void main(String[] args) throws Exception {

		String baseURL = "https://api.coinmarketcap.com/v1/ticker/";
		Scanner sc = new Scanner(System.in);
		String coin = "", amount = "", another = "";
		int pos = 0;
		boolean run = true;
		String[][] list = new String[10][2];
		while (run) {
			System.out.println("Enter a currency you want to check (Full coin name, such as Bitcoin, Reddcoin, etc.)"); //Full coin name, ex. bitcoin, reddcoin
			coin = sc.nextLine();
			System.out.println("How many do you have? n for none");
			amount = sc.nextLine();
			list[pos][0] = coin;
			list[pos][1] = amount;
			System.out.println("Do you have another coin to check? n for no");
			if (sc.nextLine().equals("n") || pos >= 10) {
				if (pos >= 10)
					System.out.println("Max of 10 coins only");
				run = false;
				break;
			} 
			pos++;


		}

		getInfo(coin, baseURL, amount, pos, list);//Receives information from CoinMarketCap and puts it into a coin object and then into a hashmap for organization
		printInfo(coin, amount, pos, list);//Prints coin information

	}

	public static void printInfo(String coin, String amount, int num, String[][] list) {
		for (int i = 0; i <= num; i++) {
			try {
				System.out.println("\nRank #" + hm.get(list[i][0]).getRank() + ": " + hm.get(list[i][0]).getName() + " - " + hm.get(list[i][0]).getSymbol());
				if (!hm.get(list[i][0]).getMaxSupply().equals("Not available")) {
					if (hm.get(list[i][0]).getMaxSupply().equals("None"))
						System.out.println("Max Supply: " + hm.get(list[i][0]).getMaxSupply() + " - This generally means the coin is not minable, or has all coins mined");
					else
						System.out.println("Max Supply: " + standardFormat.format(Double.parseDouble(hm.get(list[i][0]).getMaxSupply())));
				}
				if (!hm.get(list[i][0]).getTotalSupply().equals("Not available"))
					System.out.println("Total Supply: " + standardFormat.format(Double.parseDouble(hm.get(list[i][0]).getTotalSupply())));
				else
					System.out.println("Total Supply: Not available");
				if (!hm.get(list[i][0]).getAvailableSupply().equals("Not available"))
					System.out.println("Available Supply: " + standardFormat.format(Double.parseDouble(hm.get(list[i][0]).getAvailableSupply())));
				else
					System.out.println("Available Supply: Not available");
				if (!(hm.get(list[i][0]).getAvailableSupply().equals("Not available") || hm.get(list[i][0]).getTotalSupply().equals("Not available")))
					System.out.println("Percent available: " + ((Double.parseDouble(hm.get(list[i][0]).getAvailableSupply()) / Double.parseDouble(hm.get(list[i][0]).getTotalSupply()) * 100)));
				else
					System.out.println("Percent available: Not available");
				if (Double.parseDouble(hm.get(list[i][0]).getPrice()) <= 0.01) 
					System.out.println("USD Value per coin: " + "$" + standardFormat.format(Double.parseDouble(hm.get(list[i][0]).getPrice())));
				else
					System.out.println("USD Value per coin: " + moneyFormat.format(Double.parseDouble(hm.get(list[i][0]).getPrice())));

				if (!list[i][1].equals("n"))
					System.out.println("Approximate value of held coins: " + moneyFormat.format((Double.parseDouble(list[i][1])) * Double.parseDouble(hm.get(list[i][0]).getPrice())));
			} catch(Exception e) {
				System.out.println(e);
			}
		}
	}

	public static void getInfo(String coin, String baseURL, String amount, int num, String[][] list) throws Exception {
		for (int i = 0; i <= num; i++) {
			Thread.sleep(5);//To prevent websites from getting angry
			String base = baseURL  + list[i][0];
			URL URL = new URL(base);
			BufferedReader br = new BufferedReader(new InputStreamReader(URL.openStream()));

			//Reads in entire json from web
			String inputLine, tempLine = "";
			while ((inputLine = br.readLine()) != null) {
				tempLine += inputLine;
			}
			br.close();

			//Parses json information
			StringBuilder sb = new StringBuilder(tempLine);
			sb.deleteCharAt(0);
			String newString = sb.toString();
			JSONObject results = new JSONObject(newString);
			String nameCoin = results.getString("name");
			String rank = results.getString("rank");
			String symbolCoin = results.getString("symbol");
			String priceCoin = results.getString("price_usd");
			String priceBtc = results.getString("price_btc");
			String dayVol = null;
			try {
				dayVol = results.getString("24h_volume_usd");
			} catch (Exception e) {
				dayVol = "Not available";
			}
			String marketCap = null;
			try {
				marketCap = results.getString("market_cap_usd");
			} catch (Exception e) {
				marketCap = "Not available";
			}
			String availableSupply = null;
			try {
				availableSupply = results.getString("available_supply");
			} catch (Exception e) {
				availableSupply = "Not available";
			}
			String totalSupply = null;
			try {
				totalSupply = results.getString("total_supply");
			} catch (Exception e) {
				totalSupply = "Not available";
			}
			String maxSupply = null;
			try{
				maxSupply = results.getString("max_supply");
			} catch (Exception e) {
				maxSupply = "None";
			}
			String percentHour = null;
			try{
				percentHour = results.getString("percent_change_1h");
			} catch (Exception e) {
				percentHour = "Not available";
			}
			String percentDay = null;
			try{
				percentDay = results.getString("percent_change_24h");
			} catch (Exception e) {
				percentDay = "Not available";
			}
			String percentWeek = null;
			try{
				percentWeek = results.getString("percent_change_7d");
			} catch (Exception e) {
				percentWeek = "Not available";
			}
			if (!amount.equals("n")) //Throws an ex ception, seems to still pass in n as object
				//currentValue = Double.parseDouble(list[i][1]) * Double.parseDouble(priceCoin);

			hm.put(nameCoin.toLowerCase(), new Coin(nameCoin, rank, symbolCoin, priceCoin, priceBtc, dayVol, marketCap, availableSupply, totalSupply, maxSupply, percentHour, percentDay, percentWeek));
		}
	}
}
