package apple.statistics.blogstatistics;

import java.util.Iterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.WindowEvent;

/** テーブル表示をするタブの抽象クラス
 * @author fujii
 */
abstract public class TableTabController extends TabController {
	
	@FXML
	private TextField searchPlaceString;
	@FXML
	private Button searchButton;
	
	protected ObservableList<AccessData> dataList;		//表示用
	protected ObservableList<AccessData> saveList;		//保存用
    
    
    
    public void initialize() {
    	dataList = FXCollections.observableArrayList();
    	saveList = FXCollections.observableArrayList();
    }

    abstract public boolean createTable( AccessCount ac );
    
    @FXML
    public long searchTable() {
    	dataList.setAll(saveList);		//元データを復元
    	String word = searchPlaceString.getText();
    	if(word.length() == 0) return -1;
    	
    	//絞り込み
    	long count = 0;
    	for(Iterator<AccessData> ite = dataList.iterator(); ite.hasNext(); ) {
    		AccessData ad = ite.next();
			if(!ad.getName().matches(".*"+word+".*")) {
				ite.remove();
			} else {
				count++;
			}
		}
    	
    	return count;
    }
    
    /** コンテクストメニューの設定 */
    protected void setContextMenu( TableView<AccessData> table ){
    	  //----- メニューのセット -----
    	  ContextMenu menu = new ContextMenu();
    	  MenuItem[] menui = new MenuItem[2];
    	  menui[0] = new MenuItem( "グラフ" );
    	  menui[1] = new MenuItem( "ブラウザで開く" );
    	  menu.getItems().addAll( menui );
    	 
    	  //----- 動作のセット -----
    	  //グラフウィンドウを開く
    	  menui[0].addEventHandler( ActionEvent.ACTION , e -> {
    		  AccessData ad = table.getSelectionModel().selectedItemProperty().get();
    		  mainController.showGraph("article", Integer.toString(ad.getId()));
	  	  });
    	  //ブラウザで開く
    	  menui[1].addEventHandler( ActionEvent.ACTION , new EventHandler<ActionEvent>() {
	  		  	public void handle(ActionEvent e) {
  		  			AccessData ad = table.getSelectionModel().selectedItemProperty().get();
    	            openUri(targetUrl(ad));
	  		  	}
    	  });
    	  
    	  //----- 出現条件のセット -----
    	  table.addEventHandler( MouseEvent.MOUSE_CLICKED , new EventHandler<MouseEvent>() {
	  		  	public void handle(MouseEvent e) {
	  		  		//左クリックだと消す
	  		  		if(e.getButton().equals(MouseButton.PRIMARY)) {
	  		  			menu.hide();
	  		  		}
	  		  		//右クリックでカーソル下の表の内容があれば表示、無ければ消す
			  		if(e.getButton().equals(MouseButton.SECONDARY) == false) return;
	  		  		if(table.getSelectionModel().selectedItemProperty().getValue() == null) {
	  		  			menu.hide();
	  		  			return;
	  		  		}
		  			menu.show( table , e.getScreenX() , e.getScreenY() );
  		  		}
		  }); 
    	  
    	  //----- 動作の微調整 -----
    	  menu.addEventHandler( WindowEvent.WINDOW_SHOWING, new EventHandler<WindowEvent>() {
    			@Override
    			public void handle(WindowEvent windowEvent) {
    				for( MenuItem me : menui) {
    		    		  Node meNode = me.getStyleableNode();

    		    		 //マウスが外れたら選択状態を解除
    		    		 meNode.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {
    		    			 Node contextMenuNode = menu.getStyleableNode();
    		    			 contextMenuNode.requestFocus();
    		    		 });
    		    		 
    		    		 //クリックを離したとき、選択されているものがなければ動作しない
    		    		 meNode.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
    		    				if (!meNode.isFocused()) {
    		    					mouseEvent.consume();
    		    				}
    		    			});
    		    	 }
    				menu.removeEventHandler(WindowEvent.WINDOW_SHOWING,this);
    			}
		});
    }
    
    /** コンテクストメニューのブラウザで開くのURLを返す
     * Overrideで変更する
     */
    protected String targetUrl(AccessData ad) {
    	return Setting.getSiteUrl();
    }
    
    
}
