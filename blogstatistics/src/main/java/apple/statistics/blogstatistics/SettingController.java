package apple.statistics.blogstatistics;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

/** 設定ウィンドウを管理するコントローラ
 * @author fujii
 */
public class SettingController{
	
	@FXML
	private Button ok;
	@FXML
	private Button cancel;
	@FXML
	private Button apply;
	@FXML
	private TextField db_path;
	@FXML
	private Button db_path_reference;
	@FXML
	private TextField siteUrl;
    

    
    @FXML
    public void initialize() {
    	db_path.setText(Setting.getBlogDbPath());
    	siteUrl.setText(Setting.getSiteUrl());
    }
    
    @FXML
    public void ok() {
    	Setting.setBlogDbPath(db_path.getText());
    	Setting.setSiteUrl(siteUrl.getText());
    	Setting.writeProperties();
    	JavaFxApp.closeWindow("setting");
    }
    
    @FXML
    public void cancel() {
    	JavaFxApp.closeWindow("setting");
    }
    
    @FXML
	public void apply() {
    	Setting.setBlogDbPath(db_path.getText());
    	Setting.setSiteUrl(siteUrl.getText());
    	Setting.writeProperties();
	}
	
    @FXML
	public void DbPathReference() {
    	FileChooser fc = new FileChooser();
        fc.setTitle("ファイル選択");
        fc.getExtensionFilters().addAll(
          new FileChooser.ExtensionFilter("sqlite3ファイル", "*.sqlite3"),
          new FileChooser.ExtensionFilter("すべてのファイル", "*.*")
        );
        File dir = new File(System.getProperty("user.dir"));
        fc.setInitialDirectory(dir);
        File file = fc.showOpenDialog(null);
        if(file != null) db_path.setText(file.toString());
    }
    
}