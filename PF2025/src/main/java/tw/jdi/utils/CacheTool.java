package tw.jdi.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 內存工具
 * 
 * @author PAN
 */

@Component
public class CacheTool {

//	@Value("${public.device-state.has-all-area-system-name}")
//	private List<String> hasAllAreaSystemName;
//
//	/**
//	 * 組合內存Key方法，將只有[all]區域的系統areaName替換成[all]
//	 * 
//	 * @param PointInfo pointInfo
//	 * @return CacheKeyPair
//	 */
//	public CacheKeyPair createKeyPair(PointInfo pointInfo) {
//		if (hasAllAreaSystemName.contains(pointInfo.getSystemName())) {
//			return new CacheKeyPair(pointInfo.getSystemName(), "all", pointInfo.getSignalType().getType(),
//					pointInfo.getPointNum());
//		} else {
//			return new CacheKeyPair(pointInfo.getSystemName(), pointInfo.getAreaName(),
//					pointInfo.getSignalType().getType(), pointInfo.getPointNum());
//		}
//	}

}
