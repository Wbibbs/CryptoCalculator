import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import org.json.*;

//@author William Bibbs
//Utilizes Org Json parsing, found at http://mvnrepository.com/artifact/org.json/json
//Licensed using the JSON license provided in the project

public class main {

	static DecimalFormat moneyFormat = new DecimalFormat("$#,##0.00");
	static DecimalFormat standardFormat = new DecimalFormat("#,###.########");
	static DecimalFormat df = new DecimalFormat("0.#");
	public static void main(String[] args) throws Exception {

		df.setMaximumFractionDigits(16);
		HashMap<String, Coin> hm = new HashMap<String, Coin>();
		String baseURL = "https://api.coinmarketcap.com/v1/ticker/";
		Scanner sc = new Scanner(System.in);
		String coin = "", amount = "";
		int pos = 0;
		boolean run = true;
		String[][] list = new String[10][2];
		while (run) {
			System.out.println("Enter a currency you want to check (Full coin name, such as Bitcoin, Reddcoin, etc.)"); //Full coin name, ex. bitcoin, reddcoin
			coin = sc.nextLine().toLowerCase();
			System.out.println("How many do you have? n for none");
			amount = sc.nextLine();
			if (amount.equals("n"))
				amount = "0";
			list[pos][0] = coin;
			list[pos][1] = amount;
			System.out.println("Do you have another coin to check? n for no");
			if (sc.nextLine().toLowerCase().equals("n") || pos >= 4) {
				if (pos >= 4)
					System.out.println("Max of 5 coins only");
				run = false;
				break;
			} 
			pos++;

		}

		hm = getInfo(coin, baseURL, amount, pos, list, hm);//Receives information from CoinMarketCap and puts it into a coin object and then into a hashmap for organization
		printInfo(coin, amount, pos, list, hm);//Prints coin information
		//Reading and writing the HashMap to a file works correctly, leaving for reference until later
		serializeHashMap(hm);
		hm = readHashMap();
		printInfo(coin, amount, pos, list, hm);
		//writeSheet(hm, pos, list);
		String[][] writeList = sort(hm, pos, list);//Gets sorted list
		writeSheet(hm, pos, writeList);
		//System.out.println(writeList.toString());
		//writeSheet(hm, pos, list, writeList);//Writes sorted list, currently throws nulls when accessing coin names
		sc.close();
	}

	public static void printInfo(String coin, String amount, int num, String[][] list, HashMap<String, Coin> hm) {
		for (int i = 0; i <= num; i++) {
			try {
				System.out.println("\nRank #" + hm.get(list[i][0]).getRank() + ": " + hm.get(list[i][0]).getName() + " - " + hm.get(list[i][0]).getSymbol());
				if (!hm.get(list[i][0]).getMaxSupply().equals("Not available")) {
					if (hm.get(list[i][0]).getMaxSupply().equals("None"))
						System.out.println("Max Supply: " + hm.get(list[i][0]).getMaxSupply() + " - This generally means the coin is not minable, or the information is not retrievable");
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
				if (!hm.get(list[i][0]).get24HrVol().equals("Not available"))
					System.out.println("24 Hour Volume: " + moneyFormat.format(Double.parseDouble(hm.get(list[i][0]).get24HrVol())));
				else
					System.out.println("24 Hour Volume: " + hm.get(list[i][0]).get24HrVol());
				if (!hm.get(list[i][0]).getPercentHour().equals("Not available"))
					System.out.println("Percent Hour Change: " + hm.get(list[i][0]).getPercentHour() + "%");
				else
					System.out.println("Percent Hour Change: " + hm.get(list[i][0]).getPercentHour());
				if (!hm.get(list[i][0]).getPercentDay().equals("Not available"))
					System.out.println("Percent 24 Hour Change: " + hm.get(list[i][0]).getPercentDay() + "%");
				else
					System.out.println("Percent 24 Hour Change: " + hm.get(list[i][0]).getPercentHour());
				if (!hm.get(list[i][0]).getPercentWeek().equals("Not available"))
					System.out.println("Percent 7 Day Change " + hm.get(list[i][0]).getPercentWeek() + "%");
				else
					System.out.println("Percent 7 Day Change " + hm.get(list[i][0]).getPercentWeek());
				if (Double.parseDouble(hm.get(list[i][0]).getPrice()) <= 0.01) 
					System.out.println("USD Value per coin: " + "$" + standardFormat.format(Double.parseDouble(hm.get(list[i][0]).getPrice())));
				else
					System.out.println("USD Value per coin: " + moneyFormat.format(Double.parseDouble(hm.get(list[i][0]).getPrice())));
				if (!hm.get(list[0][i]).getBtcPrice().equals("Not available")) 
					System.out.println("Bitcoin Value per coin: " + df.format(new BigDecimal(Double.parseDouble(hm.get(list[i][0]).getBtcPrice()))));
					else
						System.out.println("Bitcoin Value Per Coin: Not available");
				if (!list[i][1].equals("n")) {
					System.out.println("\nApproximate USD value of held coins: " + moneyFormat.format((Double.parseDouble(list[i][1])) * Double.parseDouble(hm.get(list[i][0]).getPrice())));
					System.out.println("Approximate Bitcoin value of held coins: " + df.format(new BigDecimal((Double.parseDouble(list[i][1])) * Double.parseDouble(hm.get(list[i][0]).getBtcPrice()))));
				}
			} catch(Exception e) {
			}
		}
	}

	public static HashMap<String, Coin> getInfo(String coin, String baseURL, String amount, int num, String[][] list, HashMap<String, Coin> hm) throws Exception {
		for (int i = 0; i <= num; i++) {
			Thread.sleep(5);//To prevent websites from getting angry
			String base = baseURL  + list[i][0];
			URL URL = new URL(base);
			BufferedReader br;
			try{
				br = new BufferedReader(new InputStreamReader(URL.openStream()));
			} catch (Exception e) {
				System.out.println("That coin does not exist! - " + coin);//Very rough resolution to a coin not existing. Should make an error for this and throw it, and adjust the coins accordingly to not break the whole thing
				break;
			}

			//Reads in entire json from web
			String inputLine, tempLine = "";
			while ((inputLine = br.readLine()) != null)
				tempLine += inputLine;
			br.close();

			//Parses json information
			StringBuilder sb = new StringBuilder(tempLine);
			sb.deleteCharAt(0);//Deletes leading [ to properly format json
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

			hm.put(nameCoin.toLowerCase(), new Coin(nameCoin, rank, symbolCoin, priceCoin, priceBtc, dayVol, marketCap, availableSupply, totalSupply, maxSupply, percentHour, percentDay, percentWeek));
		}
		return hm;

	}

	public static void serializeHashMap(HashMap<String, Coin> hm) {//Serializes HashMap to coins.coin

		System.out.println("Serializing HashMap...\n");
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("coins.coin"))) {
			oos.writeObject(hm);
			System.out.println("Serialization succesful!");
		} catch (Exception e) {
			System.out.println("Serializing failed!");
			e.printStackTrace();
		}
	}

	public static HashMap<String, Coin> readHashMap() throws Exception {//Reads in HashMap from file coins.coin
		System.out.println("Reading HashMap...\n");
		FileInputStream fileIn = new FileInputStream("coins.coin");
		ObjectInputStream in = new ObjectInputStream(fileIn);
		HashMap<String, Coin> hm = (HashMap<String, Coin>)in.readObject();
		System.out.println("HashMap read successfully!");
		return hm;
	}

	public static String[][] swap(String[][] a){//Making a bug a feature, need to swap values correctly and remove this 'fix'
		for (int i = 0; i <= 5; i++) {
			a[i][0] = a[0][i];
			a[i][1] = a[i][1];
		}
		return a;
	}
	public static void writeSheet(HashMap<String, Coin> hm, int num, String[][] list) throws IOException {
	//	SimpleDateFormat d = new SimpleDateFormat("dd/M/yyyy");
	//	File file = new File (d.format(new Date()) + " coinsheet.xls");
		list = swap(list);
		File file = new File ("coinsheet.xls");
		BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
		Date date = new Date();
		bw.append(date + "\n");
		for (int i = 0; i <= num; i++) {
			try {
				bw.append("Rank, Name, Max Supply, Total Supply, Available Supply, Percent Available, 24 Hour Volume, Percent Change: Hour, Percent Change: 24 Hour, Percent Change: 7 Day, USD Value Per Coin, Bitcoin Value Per Coin, Number of Coins Held, Approx. USD value of Held Coins, Approx. Bitcoin value of Held Coins\n");//Writes header line
				bw.append(hm.get(list[i][0]).getRank() + ",");//Writes everything else from here below
				bw.append(hm.get(list[i][0]).getName() + " - " + hm.get(list[i][0]).getSymbol() + ",");
				if (!hm.get(list[i][0]).getMaxSupply().equals("Not available")) {
					if (hm.get(list[i][0]).getMaxSupply().equals("None"))
						bw.append(hm.get(list[i][0]).getMaxSupply()  + ",");
					else
						bw.append(hm.get(list[i][0]).getMaxSupply() + ",");
				}
				if (!hm.get(list[i][0]).getTotalSupply().equals("Not available"))
					bw.append((hm.get(list[i][0]).getTotalSupply()) + ",");
				else
					bw.append("Not available" + ",");
				if (!hm.get(list[i][0]).getAvailableSupply().equals("Not available"))
					bw.append((hm.get(list[i][0]).getAvailableSupply()) + ",");
				else
					bw.append("Not available" + ",");
				if (!(hm.get(list[i][0]).getAvailableSupply().equals("Not available") || hm.get(list[i][0]).getTotalSupply().equals("Not available")))
					bw.append(((Double.parseDouble(hm.get(list[i][0]).getAvailableSupply()) / Double.parseDouble(hm.get(list[i][0]).getTotalSupply()) * 100)) + ",");
				else
					bw.append("Not available" + ",");
				if (!hm.get(list[i][0]).get24HrVol().equals(",Not available"))
					bw.append((hm.get(list[i][0]).get24HrVol()) + ",");
				else
					bw.append(hm.get(list[i][0]).get24HrVol() + ",");
				if (!hm.get(list[i][0]).getPercentHour().equals("Not available"))
					bw.append( hm.get(list[i][0]).getPercentHour() + "%" + ",");
				else
					bw.append(hm.get(list[i][0]).getPercentHour() + ",");
				if (!hm.get(list[i][0]).getPercentDay().equals("Not available"))
					bw.append(hm.get(list[i][0]).getPercentDay() + "%" + ",");
				else
					bw.append(hm.get(list[i][0]).getPercentHour() + ",");
				if (!hm.get(list[i][0]).getPercentWeek().equals("Not available"))
					bw.append(hm.get(list[i][0]).getPercentWeek() + "%" + ",");
				else
					bw.append(hm.get(list[i][0]).getPercentWeek() + ",");
				if (Double.parseDouble(hm.get(list[i][0]).getPrice()) <= 0.01) 
					bw.append((Double.parseDouble(hm.get(list[i][0]).getPrice())) + ",");
				else
					bw.append(hm.get(list[i][0]).getPrice() + ",");
				if (Double.parseDouble(hm.get(list[0][i]).getBtcPrice()) <= 0.01) 
					bw.append((Double.parseDouble(hm.get(list[0][i]).getBtcPrice())) + ",");
				else
					bw.append(hm.get(list[0][i]).getBtcPrice() + ",");
				if (!list[i][1].equals("n")) {
					bw.append(list[i][1] + ",");
					bw.append((moneyFormat.format((Double.parseDouble(list[i][1])) * Double.parseDouble(hm.get(list[i][0]).getPrice()))) + ",");
					bw.append(((Double.parseDouble(list[i][1])) * Double.parseDouble(hm.get(list[i][0]).getBtcPrice())) + " BTC\n\n");
				} else
					bw.append("None, None, None\n\n");
				
			} catch(Exception e) {
			}
		}
		bw.close();
	}

	public static void writeSheet(HashMap<String, Coin> hm, int num, String[][] list, String[][] writeList) throws IOException {//Write a second writeSheet method using the organized writeList
		File file = new File ("sheet.xls");
		BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
		for (int i = 0; i <= num; i++) {
			try {
				bw.append("Rank, Name, Max Supply, Total Supply, Available Supply, Percent Available, 24 Hour Volume, Percent Change: Hour, Percent Change: 24 Hour, Percent Change: 7 Day, USD Value Per Coin, Bitcoin Value Per Coin, Number of Coins Held, Approx. USD value of Held Coins, Approx. Bitcoin value of Held Coins\n");//Writes header line
				bw.append(hm.get(writeList[i][0]).getRank() + ",");//Writes everything else from here below
				bw.append(hm.get(writeList[i][0]).getName() + " - " + hm.get(writeList[i][0]).getSymbol() + ",");
				if (!hm.get(writeList[i][0]).getMaxSupply().equals("Not available")) {
					if (hm.get(writeList[i][0]).getMaxSupply().equals("None"))
						bw.append(hm.get(writeList[i][0]).getMaxSupply()  + ",");
					else
						bw.append(hm.get(writeList[i][0]).getMaxSupply() + ",");
				}
				if (!hm.get(writeList[i][0]).getTotalSupply().equals("Not available"))
					bw.append((hm.get(writeList[i][0]).getTotalSupply()) + ",");
				else
					bw.append("Not available" + ",");
				if (!hm.get(writeList[i][0]).getAvailableSupply().equals("Not available"))
					bw.append((hm.get(writeList[i][0]).getAvailableSupply()) + ",");
				else
					bw.append("Not available" + ",");
				if (!(hm.get(writeList[i][0]).getAvailableSupply().equals("Not available") || hm.get(writeList[i][0]).getTotalSupply().equals("Not available")))
					bw.append(((Double.parseDouble(hm.get(writeList[i][0]).getAvailableSupply()) / Double.parseDouble(hm.get(writeList[i][0]).getTotalSupply()) * 100)) + ",");
				else
					bw.append("Not available" + ",");
				if (!hm.get(writeList[i][0]).get24HrVol().equals(",Not available"))
					bw.append((hm.get(writeList[i][0]).get24HrVol()) + ",");
				else
					bw.append(hm.get(writeList[i][0]).get24HrVol() + ",");
				if (!hm.get(writeList[i][0]).getPercentHour().equals("Not available"))
					bw.append( hm.get(writeList[i][0]).getPercentHour() + "%" + ",");
				else
					bw.append(hm.get(writeList[i][0]).getPercentHour() + ",");
				if (!hm.get(writeList[i][0]).getPercentDay().equals("Not available"))
					bw.append(hm.get(writeList[i][0]).getPercentDay() + "%" + ",");
				else
					bw.append(hm.get(writeList[i][0]).getPercentHour() + ",");
				if (!hm.get(writeList[i][0]).getPercentWeek().equals("Not available"))
					bw.append(hm.get(writeList[i][0]).getPercentWeek() + "%" + ",");
				else
					bw.append(hm.get(writeList[i][0]).getPercentWeek() + ",");
				if (Double.parseDouble(hm.get(writeList[i][0]).getPrice()) <= 0.01) 
					bw.append((Double.parseDouble(hm.get(writeList[i][0]).getPrice())) + ",");
				else
					bw.append(hm.get(writeList[i][0]).getPrice() + ",");
				if (Double.parseDouble(hm.get(writeList[0][i]).getBtcPrice()) <= 0.01) 
					bw.append((Double.parseDouble(hm.get(writeList[0][i]).getBtcPrice())) + ",");
				else
					bw.append(hm.get(writeList[0][i]).getBtcPrice() + ",");
				if (!writeList[i][1].equals("n")) {
					bw.append(writeList[i][1] + ",");
					bw.append((moneyFormat.format((Double.parseDouble(writeList[i][1])) * Double.parseDouble(hm.get(writeList[i][0]).getPrice()))) + ",");
					bw.append(((Double.parseDouble(writeList[i][1])) * Double.parseDouble(hm.get(writeList[i][0]).getBtcPrice())) + " BTC\n\n");
				} else
					bw.append("None, None, None\n\n");
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		bw.close();
	}
	public static String[][] sort(HashMap<String, Coin> hm, int num, String[][] list) {//Write sorting function for ranks to sort when printing to file
		String[][] l = new String[5][2];
		for (int i = 0; i <= num; i++) {
			l[i][0] = hm.get(list[i][0]).getRank();
			l[i][1] = hm.get(list[i][0]).getName();
		}

		for (int i = 0; i < num; i++) {
			for (int n = 0; n <= num - 1; n++) {
				//if (n < num && l[i+1][0] != null) {
					if (Integer.parseInt(l[n][0]) > Integer.parseInt(l[n+1][0])) {
						String temp = l[n][1];
						String temp2 = l[n][0];
						l[n][0] = l[n + 1][0];
						l[n][1] = l[n + 1][1];
						l[n + 1][0] = temp2;
						l[n + 1][1] = temp;
					}
				//}
			}
		}

		return l;
	}
}
