package apple.statistics.blogstatistics;

import java.util.HashMap;
import java.util.Map;

/** Mapと数値をセットにしたもの。自身を吊り下げていく。
 * @author fujii
 *
 */
public class MapWithValue {
	
	//---------------- メンバ変数 ------------------------------------
	public long value;
	public Map<String, MapWithValue> map;
	
	
	//---------------- 関数 ------------------------------------
	
	//コンストラクタ
	public MapWithValue() {
		value = 0;
		map = new HashMap<String,MapWithValue>();
	}
	public MapWithValue( int value ) {
		this.value = value;
		map = new HashMap<String,MapWithValue>();
	}
	
	
	public long addValue( int val ) {
		return this.value += val;
	}
	
	public MapWithValue addMap( String name, MapWithValue argMap ) {
		return map.put(name, argMap);
	}

}