
public class Coin {

	//Make a coin object with all of these properties to clean up main

	private String nameCoin;
	private String rank;
	private String symbolCoin;
	private String priceCoin;
	private String priceBtc;
	private String dayVol;
	private String marketCap;
	private String availableSupply;
	private String totalSupply;
	private String maxSupply;
	
	Coin(String nameCoin, String rank, String symbolCoin, String priceCoin, String priceBtc, String dayVol, String marketCap, String availableSupply, String totalSupply, String maxSupply){
		this.nameCoin = nameCoin;
		this.rank = rank;
		this.symbolCoin = symbolCoin;
		this.priceCoin = priceCoin;
		this.priceBtc = priceBtc;
		this.dayVol = dayVol;
		this.marketCap = marketCap;
		this.availableSupply = availableSupply;
		this.totalSupply = totalSupply;
		this.maxSupply = maxSupply;
	}
	
	String getName() {
		return nameCoin;
	}
	
	String getRank() {
		return rank;
	}
	
	String getSymbol() {
		return symbolCoin;
	}
	
	String getPrice() {
		return priceCoin;
	}
	
	String getBtcPrice() {
		return priceBtc;
	}
	
	String get24HrVol() {
		return dayVol;
	}
	
	String getMarketCap() {
		return marketCap;
	}
	
	String getAvailableSupply() {
		return availableSupply;
	}
	
	String getTotalSupply() {
		return totalSupply;
	}
	
	String getMaxSupply() {
		return maxSupply;
	}

}
