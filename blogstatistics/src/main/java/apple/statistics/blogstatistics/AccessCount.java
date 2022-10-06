package apple.statistics.blogstatistics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Map;

/**
 * AccessCount XMLから読み込んだアクセスカウントを保持
 * @author fujii
 *
 */
public class AccessCount {
	
	///////////////////////////// メンバ変数 ///////////////////////////////////////////////////
	private MapWithValue map;
	
	
	/////////////////////////////// 関数 ///////////////////////////////////////////////////////
	
	//コンストラクタ
	public AccessCount() {
		map = new MapWithValue();
	}
	
	
	
	/** 階層を指定してカウントの値をセットする。
	 * @param cnt カウント
	 * @param dir 階層指定
	 */
	public void addCount( int cnt, String... dir) {
		MapWithValue tMap = map;
		
		for( int i=0 ; i<dir.length ; i++ ) {
			String d = dir[i];
			//最後
			if( i == dir.length-1 ) {
				tMap = tMap.map.put( d, new MapWithValue(cnt) );
			}
			//最後以外
			else {
				tMap = tMap.map.get(d);
			}
		}
	}
	
	/** アクセスカウントを取得
	 * @param dir 階層
	 * @return long アクセスカウント
	 */
	public long getCount( String... dir ) {
		MapWithValue tMap = map;
		
		for( String d: dir ) {
			tMap = tMap.map.get(d);
			if(tMap == null) return -1;
		}
		return tMap.value;
	}
	
	/** 特定の場所のAccessDataを生成し取得する
	 * @param dir1 String 深度1の名前
	 * @param dir2 String 深度2の名前
	 * @return
	 */
	public AccessData getAccessData( String dir1, String dir2 ) {
		AccessData ad = new AccessData();
		MapWithValue tMap = map;
		String[] dir = {dir1, dir2};
//		ad.setId(Integer.parseInt(dir2));
//		ad.setUserId(getUserId(ad.getId()));
		_setAccountDataFromDb(ad, Integer.parseInt(dir2), dir1);
		
		if(dir2.equals("2")) {
			System.out.println("in");
		}
		for( String d: dir ) {
			tMap = tMap.map.get(d);
		}
		
		System.out.println(ad.getName());
		System.out.println(ad.getId());
		if( tMap == null ) {
			ad.setTotal(0);
			ad.setYear(0);
			ad.setMonth(0);
			ad.setDay(0);
			return ad;
		}
		
		ad.setTotal(tMap.value);
		_setAccessData( ad, tMap, 2, 0x00 );
		return ad;
	}
	
	/** 第一引数のAccessDataにDBから取得したデータをセットする(name,id, userId)
	 * @param ad AccessData
	 * @param id 深度2のID
	 * @param section 深度1の名前
	 */
	private void _setAccountDataFromDb( AccessData ad, int id, String section ) {
		final String URL = "jdbc:sqlite:" + Setting.getBlogDbPath();
//        final String USER = "";
//        final String PASS = "";
        String sql = "";
		switch( section ) {
			case "site":
				ad.setName("サイト全体");
				return;
			case "blog":
				sql = "select * from blog where blog_id =?;";
				break;
			case "article":
				sql = "select * from article where article_id =?;";
				break;
		}
		
        try(Connection conn = 
                DriverManager.getConnection(URL);
            PreparedStatement ps = conn.prepareStatement(sql)){
            
            ps.setInt(1,id);
            
            try(ResultSet rs = ps.executeQuery()){
            	
            	if( rs.isAfterLast() ) {
            		ad.setName("削除");
            		ad.setUserId("-1");
	            	if(section == "article") ad.setId(-1);
	            	return;
            	}
            	
            	//取得完了
            	ad.setName(rs.getString("title"));
            	//ブログの場合はAccessData.idにアカウントのIDを代入
            	if(section == "blog") ad.setId(rs.getInt("account_id"));
            	
    			sql = "select user_id from account where account_id =?;";
    			try(PreparedStatement ps2 = conn.prepareStatement(sql)){
    	            
    	            ps2.setInt(1,rs.getInt("account_id"));
    	            
    	            try(ResultSet rs2 = ps2.executeQuery()){
    	            	//取得完了
    	            	ad.setUserId(rs2.getString("user_id"));
    	            	//記事の場合はAccessData.idに記事IDを代入
    	            	if(section == "article") ad.setId(rs.getInt("article_id"));
    	            }
    			}
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ad.setName("取得失敗");
        } catch (Exception e) {
            e.printStackTrace();
            ad.setName("取得失敗");
        }
        
	}
	
	/** 第一引数のAccessDataにデータをセットする(アクセス数)
	 * @param ad AccessData
	 * @param tMap MapWithValue 深さ2のMapWithValue
	 * @param depth int 2を指定。深さが2からなので
	 * @param flag 0を指定。動作制御用フラグ
	 */
	private void _setAccessData( AccessData ad, MapWithValue tMap, int depth, int flag ) {
		Calendar calendar = Calendar.getInstance();
		@SuppressWarnings("unused")
		long val;
		for(Map.Entry<String, MapWithValue> entry : tMap.map.entrySet()) {
			//数値のセット
			switch( depth ) {
				case 2:
					int year = calendar.get(Calendar.YEAR);
					val = entry.getValue().value;
					if( year == Integer.parseInt(entry.getKey()) ) {
						ad.setYear(entry.getValue().value);
						flag |= 0x01;
					}
					break;
				case 3:
					int month = calendar.get(Calendar.MONTH) +1;
					val = entry.getValue().value;
					if( month == Integer.parseInt(entry.getKey())  &&  (flag & 0x01) != 0 ) {
						ad.setMonth(entry.getValue().value);
						flag |= 0x02;
					}
					break;
				case 4:
					int day = calendar.get(Calendar.DAY_OF_MONTH);
					val = entry.getValue().value;
					if( day == Integer.parseInt(entry.getKey())  &&  (flag & 0x03) != 0 ) {
						ad.setDay(entry.getValue().value);
						flag |= 0x04;
					}
					break;
			}
			
			//子があれば再起
			if( entry.getValue().map.size() != 0 ) {
				_setAccessData( ad, entry.getValue(), depth +1, flag );
			}
		}
	}

	/** ユーザーIDを取得 */
	@SuppressWarnings("unused")
	private String getUserId( int accountId ) {
		final String URL 
        = "jdbc:sqlite:C:\\Users\\apple_umeda\\Desktop\\blog_terminal_db.sqlite3";
//        final String USER = "";
//        final String PASS = "";
        String sql = "select * from account where account_id =?;";
        
        try(Connection conn = 
                DriverManager.getConnection(URL);
            PreparedStatement ps = conn.prepareStatement(sql)){
            
            ps.setInt(1,accountId);
            
            try(ResultSet rs = ps.executeQuery()){
            	//取得完了
            	return rs.getString("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "取得失敗";
	}


	public MapWithValue getMap() {
		return map;
	}

}
