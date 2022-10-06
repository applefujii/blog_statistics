package apple.statistics.blogstatistics;

import java.io.File;
import java.io.IOException;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;

/** 全体を制御するコントローラ
 * @author fujii
 */
public class MainController {
	
	///////////////////////////// メンバ変数 /////////////////////////////
	
	//----- 定数 -----
	final static private String[] DIR_NAME = {"place", "place2", "year", "month", "day"};
	
	//----- 他コントロールクラス -----
	@FXML
	private GeneralTabController generalTabController;
	@FXML
	private SiteTabController siteTabController;
	@FXML
	private BlogTabController blogTabController;
	@FXML
	private ArticleTabController articleTabController;
	@FXML
	private SettingController settingController;
	@FXML
	private SettingController aboutController;
	
	//----- fxml要素 -----
	@FXML
	private MenuItem open;
	@FXML
	private MenuItem close;
	@FXML
	private MenuItem setting;
	@FXML
	private MenuItem about;
	@FXML
    private Label statusMessage;
	@FXML
    private ProgressBar progressBar;
	
	//----- データクラス -----
	private AccessCount ac;
    
    
    
	///////////////////////////////// メソッド ///////////////////////////////////////////////////////
    
    /** 初期化 */
    @FXML
    public void initialize() {
    	//!!! 子のinitializeは自動的に呼ばれる !!!
    	//----- 自身の参照を渡す -----
    	generalTabController.setMainController(this);
    	siteTabController.setMainController(this);
    	blogTabController.setMainController(this);
    	articleTabController.setMainController(this);
    	
    	ac = new AccessCount();
    }
    
    
    /** ファイルオープン */
    @FXML
    public void fileOpen() {
    	//ファイル選択ダイアログ
    	FileChooser fc = new FileChooser();
        fc.setTitle("ファイル選択");
        fc.getExtensionFilters().addAll(
          new FileChooser.ExtensionFilter("XMLファイル", "*.xml", "*.XML"),
          new FileChooser.ExtensionFilter("すべてのファイル", "*.*")
        );
        fc.setInitialDirectory(new File(System.getProperty("user.dir")));
        File file = fc.showOpenDialog(null);
        if(file == null) return;
        
        //ファイル読み込み
    	statusMessage.setText("読み込み中");
    	progressBar.setProgress(0);
    	
		try {
	        Document document;
			document = DocumentBuilderFactory
		        .newInstance()
		        .newDocumentBuilder()
		        .parse(file);
			
			loadFile(document);
			
			/* loadFileに再起関数にしてまとめた
			Element root = document.getDocumentElement();
			NodeList placeList = root.getElementsByTagName("place");
			//place
			for( int i=0 ; i<placeList.getLength() ; i++ ) {
				progressBar.setProgress(i/(double)placeList.getLength());
				Element place = (Element)placeList.item(i);
				String vPlace = place.getAttribute("value");
				ac.addCount( Integer.valueOf(place.getAttribute("count")), vPlace );
				
				//place2
				NodeList place2List = place.getElementsByTagName("place2");
				for( int j=0 ; j<place2List.getLength() ; j++ ) {
					Element place2 = (Element) place2List.item(j);
					String vPlace2 = place2.getAttribute("value");
					ac.addCount(Integer.valueOf(place2.getAttribute("count")), new String[]{vPlace, vPlace2});
					
					//year
					NodeList yearList = place2.getElementsByTagName("year");
					for( int k=0 ; k<yearList.getLength() ; k++ ) {
						Element year = (Element) yearList.item(k);
						String vYear = year.getAttribute("value");
						ac.addCount(Integer.valueOf(year.getAttribute("count")), new String[]{vPlace, vPlace2, vYear});
						
						//month
						NodeList monthList = year.getElementsByTagName("month");
						for( int l=0 ; l<monthList.getLength() ; l++ ) {
							Element month = (Element) monthList.item(l);
							String vMonth = month.getAttribute("value");
							ac.addCount(Integer.valueOf(month.getAttribute("count")), new String[]{vPlace, vPlace2, vYear, vMonth});
							
							//day
							NodeList dayList = month.getElementsByTagName("day");
							for( int n=0 ; n<dayList.getLength() ; n++ ) {
								Element day = (Element) dayList.item(n);
								String vDay = day.getAttribute("value");
								ac.addCount(Integer.valueOf(day.getAttribute("count")), new String[]{vPlace, vPlace2, vYear, vMonth, vDay});
							}
						}
					}
				}
			}
			*/
		
		} catch (SAXException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		//ツリーの作成
		generalTabController.createTree( ac );
    	
		boolean fFail = false;
    	//Siteタブ
		fFail |= siteTabController.createTable( ac );
    	//Blogタブ
		fFail |= blogTabController.createTable( ac );
    	//Articleタブ
		fFail |= articleTabController.createTable( ac );
    	
    	if( fFail ) {
            Alert alert = new Alert(AlertType.ERROR, "読み込み時にエラーが発生しました。処理を中断します。", ButtonType.OK);
            alert.showAndWait();
    	}
    	
    	statusMessage.setText("読み込み完了");
    	progressBar.setProgress(1);

    }
    
    /** 再起でデータ読み込み。
     * @param document Document 読み込むファイル
     */
    private void loadFile( Document document ) {
    	Element root = document.getDocumentElement();
		NodeList list = root.getElementsByTagName("place");
    	Stack<String> dir = new Stack<String>();
    	_loadFile(list, dir);
    }
    
    /** 再起でデータ読み込み。
     * @param list NodeList
     * @param dir Stack<String> カウントした値をAccessCountにセットする階層
     */
    private void _loadFile(NodeList list, Stack<String> dir) {
    	//同じ階層を回す
    	for( int i=0 ; i<list.getLength() ; i++ ) {
			if(dir.size() == 0) progressBar.setProgress(i/(double)list.getLength());	//プログレスバー更新
			Element element = (Element)list.item(i);		//今回のデータ
			dir.push(element.getAttribute("value") );		//今の階層の名前をスタックに追加
			String[] s = dir.toArray(new String[dir.size() -1]);
			int tmp = Integer.valueOf(element.getAttribute("count"));
			ac.addCount( tmp, s );		//アクセス数を保存
			if(dir.size() < 5) _loadFile(element.getElementsByTagName(DIR_NAME[dir.size()]), dir);		//再起で次の階層へ
			dir.pop();		//今の階層の名前を削除
    	}
    }
    
    /** アプリケーション終了 */
    @FXML
    public void close() {
    	Platform.exit();
    }
    
    /** 設定画面ウィンドウを開く */
    @FXML
    public void showSetting() {
		try {
			JavaFxApp.createMordalWindow("setting", "設定", true);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
    }
    
    /** 概要説明ウィンドウを開く */
    @FXML
    public void showAbout() {
    	try {
			JavaFxApp.createMordalWindow("about", "about", false);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
    }
    
    /** グラフウィンドウを開く
     * @param place
     * @param place2
     */
    public void showGraph( String place, String place2 ) {
    	try {
    		FXMLLoader fxmlLoader = JavaFxApp.createNormalWindow("graph", "グラフ");
			GraphController con = (GraphController) fxmlLoader.getController();
			con.setAccessCount(ac);
			con.createGraph(place, place2);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
    }
    
    
}
