package apple.statistics.blogstatistics;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * AccessData
 * TableListに表示するデータ形式
 * @author fujii
 *
 */
public class AccessData {
	
	///////////////////////////// メンバ変数 /////////////////////////////
	private StringProperty name;
	private LongProperty total;
	private LongProperty year;
	private LongProperty month;
	private LongProperty day;
	
	private int id;					//articleのID
	private String userId;
	
	
	///////////////////////////// 関数 /////////////////////////////
	
	//コンストラクタ
	public AccessData() {
		name = new SimpleStringProperty("");
		total = new SimpleLongProperty(0);
		year = new SimpleLongProperty(0);
		month = new SimpleLongProperty(0);
		day = new SimpleLongProperty(0);
	}
	public AccessData( String fName, long fTotal, long fYear, long fMonth, long fDay ) {
		name = new SimpleStringProperty(fName);
		total = new SimpleLongProperty(fTotal);
		year = new SimpleLongProperty(fYear);
		month = new SimpleLongProperty(fMonth);
		day = new SimpleLongProperty(fDay);
	}
	
	
	@SuppressWarnings("exports")
	public StringProperty nameProperty() {
		return name;
	}
	public String getName() {
		return name.get();
	}
	@SuppressWarnings("exports")
	public LongProperty totalProperty() {
		return total;
	}
	@SuppressWarnings("exports")
	public LongProperty yearProperty() {
		return year;
	}
	@SuppressWarnings("exports")
	public LongProperty monthProperty() {
		return month;
	}
	@SuppressWarnings("exports")
	public LongProperty dayProperty() {
		return day;
	}
	public int getId() {
		return id;
	}
	public String getUserId() {
		return userId;
	}
	
	public void setName(String name) {
		this.name = new SimpleStringProperty(name);
	}
	public void setTotal(long total) {
		this.total = new SimpleLongProperty(total);
	}
	public void setYear(long year) {
		this.year = new SimpleLongProperty(year);
	}
	public void setMonth(long month) {
		this.month = new SimpleLongProperty(month);
	}
	public void setDay(long day) {
		this.day = new SimpleLongProperty(day);
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	
}
