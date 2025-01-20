package tw.jdi.utils.cache;

import java.util.Objects;

import lombok.Getter;
import tw.jdi.entity.enumEntity.IoType.ViewType;

@Getter
public class CacheKeyPair {

	private ViewType viewType;
	private Integer pointId;
	
	public CacheKeyPair(ViewType viewType, Integer pointId) {
		this.viewType = viewType;
		this.pointId = pointId;
	}
	
	// 重寫 equals 方法
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		CacheKeyPair keyPair = (CacheKeyPair) obj;
		return Objects.equals(viewType, keyPair.viewType) && Objects.equals(pointId, keyPair.pointId);
	}

	// 重寫 hashCode 方法
	@Override
	public int hashCode() {
		return Objects.hash(viewType, pointId);
	}
}
