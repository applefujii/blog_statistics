package apple.statistics.blogstatistics;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/** 実質一番上位のクラス。システム部分の処理。
 * @author fujii
 */
public class JavaFxApp extends Application {

	@SuppressWarnings("exports")
	public static Stage stage;
    private static Scene scene;
    
    @SuppressWarnings("exports")
	public static Map<String,Stage> subStage;
    private static Map<String,Scene> subScene;
    

    /** メインウィンドウの作成  */
    @Override
    public void start(@SuppressWarnings("exports") Stage stage) throws IOException {
    	subStage = new HashMap<String,Stage>();
    	subScene = new HashMap<String,Scene>();
    	
    	JavaFxApp.stage = stage;
		Image icon = new Image( "file:/C:/Users/apple_umeda/Desktop/icon.png" );
		stage.getIcons().add( icon );
//    	ScalableContentPane scp = new ScalableContentPane(root);
//      scp.setAspectScale(false); アス比無視
		
		//メインウィンドウが閉じられるときの動作
		stage.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue == true && newValue == false) {
            	//全ての子ウィンドウを閉じる
            	for(Map.Entry<String, Stage> entry : subStage.entrySet()) {
            		entry.getValue().close();
            	}
            }
        });
		
        scene = new Scene(loadFXML("main"));
        stage.setScene(scene);
        stage.setTitle("アクセス履歴ビュアー");
//      stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    /** モーダルウィンドウの作成
     * createMordalWindow
     * @param fxmlName
     * @param pageName
     * @param resizable
     * @throws IOException
     */
	public static void createMordalWindow(String fxmlName, String pageName, boolean resizable) throws IOException {
    	Stage st = new Stage();
    	subStage.put(fxmlName, st);
    	Scene sc = new Scene(loadFXML(fxmlName));
    	subScene.put(fxmlName, sc);
    	st.setScene(sc);
    	st.setTitle(pageName);
    	st.initModality(Modality.APPLICATION_MODAL);
    	st.initStyle(StageStyle.UTILITY);
    	st.setResizable(resizable);
    	st.setX( stage.getX() + (stage.getWidth() /2) - 250);
    	st.setY( stage.getY() + (stage.getHeight() /2) - 100);
    	st.show();
    }
    
	/** 通常ウィンドウの作成。
	 * @param fxmlName
	 * @param pageName
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("exports")
	public static FXMLLoader createNormalWindow(String fxmlName, String pageName) throws IOException {
		Stage st = new Stage();
    	subStage.put(fxmlName, st);
    	FXMLLoader fxmlLoader = new FXMLLoader(JavaFxApp.class.getResource(Setting.sourcePath + fxmlName + ".fxml"));
    	Scene sc = new Scene(fxmlLoader.load());
    	subScene.put(fxmlName, sc);
    	st.setScene(sc);
    	st.setTitle(pageName);
    	st.setX( stage.getX() + 50);
    	st.setY( stage.getY() + 50);
    	st.show();
    	return fxmlLoader;
    }
	
	public static void closeWindow(String stageName) {
		subStage.get(stageName).close();
	}
    
    

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFxApp.class.getResource(Setting.sourcePath + fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    
    
    /**
     * jarから実行しているか
     * @return boolean true:jarから false:jarからではない
     */
    public static boolean isProgramRunnedFromJar() {
        File x = getCurrentJarFileLocation();
        if(x.getAbsolutePath().contains("target"+File.separator+"classes")){
            return false;
        } else {
            return true;
        }
    }
    public static File getCurrentJarFileLocation() {
            try {
                return new File(JavaFxApp.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            } catch(URISyntaxException e){
                e.printStackTrace();
                return null;
            }
    }

}