package apple.statistics.blogstatistics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/** 記事タブを管理するコントローラ
 * @author fujii
 */
public class ArticleTabController extends TableTabController {
	
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
    	super.initialize("article");
    	
    	this.setContextMenu(tableView);
    	
    	//データのセット
    	cullumnName.setCellValueFactory(new PropertyValueFactory<AccessData, String>("name"));
    	cullumnTotal.setCellValueFactory(new PropertyValueFactory<AccessData, Long>("total"));
    	cullumnYear.setCellValueFactory(new PropertyValueFactory<AccessData, Long>("year"));
    	cullumnMonth.setCellValueFactory(new PropertyValueFactory<AccessData, Long>("month"));
    	cullumnDay.setCellValueFactory(new PropertyValueFactory<AccessData, Long>("day"));
    	
    	tableView.itemsProperty().setValue(dataList);
    	tableView.setItems(dataList);
    }
    

    /** テーブル作成 */
    public boolean createTable( AccessCount ac ) {
    	//TableViewにデータをセット
    	dataList.clear();
    	final String URL = "jdbc:sqlite:" + Setting.getBlogDbPath();
//        final String USER = "";
//        final String PASS = "";
		String sql = "select count(*) as countCul from article;";
        
        try(Connection conn = 
                DriverManager.getConnection(URL);
            PreparedStatement ps = conn.prepareStatement(sql)){
            
            try(ResultSet rs = ps.executeQuery()){
            	//取得完了
            	for( int i=1 ; i<=rs.getInt("countCul") ; i++ ) {
	        		AccessData row = ac.getAccessData( "article", Integer.toString(i) );
	        		dataList.add(row);
            	}
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        saveList.setAll(dataList);		//検索結果を保存
        
        return false;
	}
    
    @Override
    protected String targetUrl(AccessData ad) {
    	return Setting.getSiteUrl() + ad.getUserId() + "/entry.php?id=" + ad.getId() + "/";
    }
    
}
