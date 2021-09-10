package apple.statistics.blogstatistics;

import java.util.Calendar;

import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

/** グラフウィンドウを管理するコントローラ
 * @author fujii
 */
public class GraphController {
	
	@FXML
	private LineChart<String, Number> lineChart;
	@FXML
	private Axis<String> xAxis;
	@FXML
	private Axis<Number> yAxis;
	
	private XYChart.Series<String, Number> data;
	private AccessCount ac;
    
    
    
    /** 初期化 */
    @FXML
    public void initialize() {
    	xAxis.setLabel("日");
    	yAxis.setLabel("アクセス数");
    	xAxis.setAnimated(false);
    	yAxis.setAnimated(false);
    	
    	data = new XYChart.Series<String, Number>();
    	data.setName("");
    	lineChart.getData().add(data);
    	
//    	data.getData().add(new XYChart.Data<>("1日", 5));
//    	data.getData().add(new XYChart.Data<>("2日", 7));
    }
    
    /** グラフの作成
     * 過去31日分のグラフを作成する
     * @param place 深度1の名前
     * @param place2 深度2の名前
     */
    public void createGraph( String place, String place2 ) {
    	data.setName(ac.getAccessData(place, place2).getName());
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.DAY_OF_MONTH, -31);
    	for( int i=0 ; i<31 ; i++ ) {
    		String year = Integer.toString(calendar.get(Calendar.YEAR));
    		String month = Integer.toString(calendar.get(Calendar.MONTH) +1);
    		String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
	    	long cnt = ac.getCount(new String[] {place, place2, year, month, day});
	    	String label = "";
	    	if(i==0  ||  day.equals("1")) label += month+"月";
	    	label += day+"日";
	    	if(cnt == -1) {
	    		data.getData().add(new XYChart.Data<>(day+"日", 0));
//	    		data.getData().add(new XYChart.Data<>(label, i));	//※仮
	    	} else {
	    		data.getData().add(new XYChart.Data<>(label, cnt));
	    	}
	    	calendar.add(Calendar.DAY_OF_MONTH, 1);
    	}
    }



	public void setAccessCount(AccessCount ac) {
		this.ac = ac;
	}
    
}
