package apple.statistics.blogstatistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/** 全般タブを管理するコントローラ
 * @author fujii
 */
public class GeneralTabController extends TabController {
	
	final static public String[] DEPTH_NAME = {"", "Place", "Place2", "Year", "Month", "Day"};
	
	@FXML
    private TreeView<String> generalTree;
	@FXML
    private Button buttonFold;
	
	
	List<TreeItem<String>> listTree;
    
    
    
    
    /** 初期化 */
    @FXML
    public void initialize() {
    	listTree = new ArrayList<TreeItem<String>>();
    	
    	//ツリーの作成
    	TreeItem<String> root = new TreeItem<String>("AccessCount");
//    	root.getChildren().addAll(
//    	        new TreeItem<String>("Site"),
//    	        new TreeItem<String>("Blog"),
//    	        new TreeItem<String>("Item 3"));
    	generalTree.setRoot(root);
    	
    }
    
    
    public void createTree( AccessCount ac ) {	
		//ツリーの作成
		TreeItem<String> root = new TreeItem<String>("AccessCount");
    	generalTree.setRoot(root);
    	listTree.add(root);
    	createTree(root, listTree, ac.getMap());
    	
    	

    }
    
    /** mapを元にツリービューの作成。
	 * @param parent 吊り下げ元のTreeItem
	 * @param listTree 全てのTreeItemの参照を格納するリスト。
	 */
	private void createTree( TreeItem<String> parent, List<TreeItem<String>> listTree, MapWithValue map ) {
		_createTree( parent, listTree, map, 1 );
	}
	
	/** mapを元にツリービューの作成。
	 * @param parent 吊り下げ元のTreeItem
	 * @param listTree 全てのTreeItemの参照を格納するリスト。
	 * @param tMap 再起で使用。呼び出し時にはnullを入力。
	 * @param depth 再起で使用。呼び出し時には1を指定。
	 */
	@SuppressWarnings("unchecked")
	private void _createTree( TreeItem<String> parent, List<TreeItem<String>> listTree, MapWithValue tMap, int depth ) {
		for(Map.Entry<String, MapWithValue> entry : tMap.map.entrySet()) {
			//単位形式に変換
			String unit = "";
			double val = entry.getValue().value;
			if( val >= 1000000000000000L ) { unit = "P"; val /= 1000000000000000L; }
			else if( val >= 1000000000000L ) { unit = "T"; val /= 1000000000000L; }
			else if( val >= 1000000000 ) { unit = "G"; val /= 1000000000; }
			else if( val >= 1000000 ) { unit = "M"; val /= 1000000; }
			else if( val >= 1000 ) { unit = "k"; val /= 1000; }
			
			//吊り下げ
			TreeItem<String> child;
			if( unit == "" ) child = new TreeItem<String>("[" + DEPTH_NAME[depth] + "] " + entry.getKey() + "\t "+ (int)val);
			else child = new TreeItem<String>("[" + DEPTH_NAME[depth] + "] " + entry.getKey() + "\t "+ String.format("%.3f", val) + unit);
		    parent.getChildren().addAll(child);
		    listTree.add(child);
		    
			//子があれば再起
		    if( entry.getValue().map.size() != 0 ) {
		    	_createTree( child, listTree, entry.getValue(), depth +1 );
		    }
		}
	}

    /**
     * treeFold
     * ツリーをすべて閉じる
     */
    @FXML
    private void treeFold() {
    	for( TreeItem<String> ti: listTree ) {
    		ti.setExpanded(false);
    	}
    }
    
    
}
