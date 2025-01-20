package tw.jdi.utils.cache;

import java.util.concurrent.ScheduledExecutorService;

import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import tw.jdi.entity.po.DeviceState;

/**
 * 所有即時數值內存 V2.0 </br>
 * 變更儲存方式->平面鍵值映射 </br>
 * 
 * @author PAN
 */
@Component
public class DeviceStateCacheManager extends Cache<CacheKeyPair, DeviceState>{

	@Resource(name = "schedulePool")
	private ScheduledExecutorService pool;
	
	
}
