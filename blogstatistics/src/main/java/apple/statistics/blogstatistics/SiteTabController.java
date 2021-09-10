package apple.statistics.blogstatistics;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/** サイトタブを管理するコントローラ
 * @author fujii
 */
public class SiteTabController extends TableTabController {
	
	@FXML
	private TableView<AccessData> tableView;
	@FXML
	private TableColumn<AccessData, String> cullumnName;
	@FXML
	private TableColumn<AccessData, Long> cullumnTotal;
	@FXML
	private TableColumn<AccessData, Long> cullumnYear;
	@FXML
	private TableColumn<AccessData, Long> cullumnMonth;
	@FXML
	private TableColumn<AccessData, Long> cullumnDay;
    
    
    
    
    /** 初期化 */
    @FXML
    public void initialize() {
    	super.initialize();
    	
    	this.setContextMenu(tableView);
    	
    	//データのセット
    	cullumnName.setCellValueFactory(new PropertyValueFactory<AccessData, String>("name"));
    	cullumnTotal.setCellValueFactory(new PropertyValueFactory<AccessData, Long>("total"));
    	cullumnYear.setCellValueFactory(new PropertyValueFactory<AccessData, Long>("year"));
    	cullumnMonth.setCellValueFactory(new PropertyValueFactory<AccessData, Long>("month"));
    	cullumnDay.setCellValueFactory(new PropertyValueFactory<AccessData, Long>("day"));
    	
    	dataList = FXCollections.observableArrayList();
    	tableView.itemsProperty().setValue(dataList);
    	tableView.setItems(dataList);
    }
    
    
    public boolean createTable( AccessCount ac ) {
    	//TableViewにデータをセット
    	AccessData t1 = ac.getAccessData( "site", "1" );
    	
    	dataList.clear();
    	dataList.add(t1);
    	saveList.setAll(dataList);		//検索結果を保存
    	return false;
    }
    
    
    @Override
    protected String targetUrl(AccessData ad) {
    	return Setting.getSiteUrl();
    }
    
}
