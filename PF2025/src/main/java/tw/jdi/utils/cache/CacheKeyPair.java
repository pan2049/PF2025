package tw.jdi.utils.cache;

import java.util.Objects;

import lombok.Getter;
import tw.jdi.entity.enumEntity.IoType;
import tw.jdi.entity.enumEntity.IoType.ViewType;

/**
 * 平面鍵值映射複合Key V2.0 </br>
 * 用泛型拼接兩種自定義之物件
 * 
 * @param <T> entity 1
 * @param <E> entity 2
 * @author PAN
 */
@Getter
public class CacheKeyPair<T, E> {

	private T entity1;
	private E entity2;
	
	public CacheKeyPair(T entity1, E entity2) {
		this.entity1 = entity1;
		this.entity2 = entity2;
	}
	
	// 重寫 equals 方法
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		CacheKeyPair<T, E> keyPair = (CacheKeyPair<T, E>) obj;
		return Objects.equals(entity1, keyPair.entity1) && Objects.equals(entity2, keyPair.entity2);
	}

	// 重寫 hashCode 方法
	@Override
	public int hashCode() {
		return Objects.hash(entity1, entity2);
	}
}
