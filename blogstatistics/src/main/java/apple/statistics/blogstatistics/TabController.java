package apple.statistics.blogstatistics;

import java.awt.Desktop;
import java.net.URI;

/** 各種タブの抽象クラス
 * @author fujii
 */
abstract public class TabController {
	
	protected MainController mainController;
    
    
    
    @SuppressWarnings("unused")
	protected void openUri( String uriString ) {
    	Desktop desktop = Desktop.getDesktop();
    	try{
    	  URI uri = new URI( uriString );
    	  desktop.browse( uri );
    	}catch( Exception e ){
    	  e.printStackTrace();
    	}
    }


	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}
    
}
