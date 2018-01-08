import java.io.Serializable;

public class Coin implements Serializable{

	private static final long serialVersionUID = 1L;
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
	private String percentHour;
	private String percentDay;
	private String percentWeek;
	
	Coin(String nameCoin, String rank, String symbolCoin, String priceCoin, String priceBtc, String dayVol, String marketCap, String availableSupply, String totalSupply, String maxSupply, String percentHour, String percentDay, String percentWeek){
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
		this.percentHour = percentHour;
		this.percentDay = percentDay;
		this.percentWeek = percentWeek;
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
	
	String getPercentDay() {
		return percentDay;
	}
	
	String getPercentHour() {
		return percentHour;
	}
	
	String getPercentWeek() {
		return percentWeek;
	}

}
