package apple.statistics.blogstatistics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

/** prorertiesファイルを読み込み、保持する。
 * 使うときに準備は必要なく、「Setting.get000」で各データを取得できる。
 * @author fujii
 */
public class Setting{
    
	private static ResourceBundle rb;
	public static final String sourcePath;
	private static String blogDbPath = ".\\blog_terminal_db.sqlite3";
	private static String siteUrl = "";

    
    public Setting() {
    }
    
    /** prorertiesファイル読み込み */
    static {
    	sourcePath = isProgramRunnedFromJar() ? "/resources/apple/statistics/blogstatistics/" : "";
    	
    	try {
	    	File dicDir = Paths.get("./").toFile();
	    	URLClassLoader urlLoader = new URLClassLoader(new URL[]{dicDir.toURI().toURL()});
	    	
	    	rb = ResourceBundle.getBundle("setting", Locale.getDefault(), urlLoader);
	    	blogDbPath = rb.getString("blog_db_path");
	    	siteUrl = rb.getString("url");
    	} catch(MissingResourceException e) {
    		e.printStackTrace();
    	} catch (MalformedURLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
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
    private static File getCurrentJarFileLocation() {
            try {
                return new File(JavaFxApp.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            } catch(URISyntaxException e){
                e.printStackTrace();
                return null;
            }
    }
    
    /** プロパティファイルに書き込む
     * @param key String キー
     * @param value String 値
     * @return boolean false:通常終了  true:エラー終了
     */
    public static boolean writeProperties() {
		Properties properties = new Properties();
		 
		String path = "setting.properties";
		properties.setProperty("blog_db_path", blogDbPath);
		properties.setProperty("url", siteUrl);
		try {
			properties.store(new FileOutputStream(path), "Comments");
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return true;
		}
		return false;
    }
    
    
    //----- getter -----
	public static String getBlogDbPath() {
		return blogDbPath;
	}
	public static String getSiteUrl() {
		return siteUrl;
	}
	
	//----- setter -----
	public static void setBlogDbPath(String blogDbPath) {
		Setting.blogDbPath = blogDbPath;
	}
	public static void setSiteUrl(String url) {
		Setting.siteUrl = url;
	}
    
}