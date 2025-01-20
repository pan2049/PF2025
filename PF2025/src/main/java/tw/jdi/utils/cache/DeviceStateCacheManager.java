package tw.jdi.utils.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.Resource;
import tw.jdi.entity.enumEntity.IoType.ViewType;
import tw.jdi.entity.po.DeviceState;
import tw.jdi.entity.po.PointInfo;
import tw.jdi.utils.SharedUtils;
import tw.jdi.utils.websocket.ControlWebsocketCache;
import tw.jdi.utils.websocket.ViewWebsocketCache;

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
	@Autowired
	private ViewWebsocketCache viewWebsocketCache;
	@Autowired
	private ControlWebsocketCache controlWebsocketCache;
	@Autowired
	private ObjectMapper objectMapper;
	
	public void init() {
		setViewPointSchedule();
		setControlPointSchedule();
	}
	
	public void setViewPointSchedule() {
		pool.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				Map<Integer, DeviceState> viewPointDataStr = getCacheByViewType(ViewType.VIEW);
				try {
					String dataStr = objectMapper.writeValueAsString(viewPointDataStr);
					viewWebsocketCache.sendMessageToAllChannel(dataStr);
				} catch (JsonProcessingException e) {
					SharedUtils.getLogger(this.getClass()).error(e.getLocalizedMessage());
				}
			}
		}, 0, 1, TimeUnit.SECONDS);
	}
	
	public void setControlPointSchedule() {
		pool.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				Map<Integer, DeviceState> controlPointDataStr = getCacheByViewType(ViewType.CONTROL);
				try {
					String dataStr = objectMapper.writeValueAsString(controlPointDataStr);
					controlWebsocketCache.sendMessageToUninitChannel(dataStr);
				} catch (JsonProcessingException e) {
					SharedUtils.getLogger(this.getClass()).error(e.getLocalizedMessage());
				}
			}
		}, 0, 1, TimeUnit.SECONDS);
	}
	
	public void setCache(PointInfo pointInfo, Boolean state) {
		CacheKeyPair pairkey = SharedUtils.createKeyPair(pointInfo);
		DeviceState deviceState;
		if (super.hasData(pairkey)) {
			// 內存更新
			deviceState = super.getData(pairkey);
			switch (pairkey.getViewType()) {
			case VIEW -> {
				deviceState.setState(state);				
			}
			case CONTROL -> {
				if (!deviceState.getState().equals(state)) {
					deviceState.setState(state);
					// 因CONTROL控制點更新故更新前端
					updateControlChannelData(pairkey, deviceState);
				}
			}
			}
		} else {
			// 內存物件初始話
			deviceState = new DeviceState().setState(state);
			super.setData(pairkey, deviceState);
			switch (pairkey.getViewType()) {
			case CONTROL -> {
				// 因CONTROL控制點更新故更新前端
				updateControlChannelData(pairkey, deviceState);			
			}
			default -> {}
			}
		}
	}
	
	public void setCache(PointInfo pointInfo, Float value) {
		CacheKeyPair pairkey = SharedUtils.createKeyPair(pointInfo);
		DeviceState deviceState;
		if (super.hasData(pairkey)) {
			// 內存更新
			deviceState = super.getData(pairkey);
			switch (pairkey.getViewType()) {
			case VIEW -> {
				deviceState.setValue(value);			
			}
			case CONTROL -> {
				if (!deviceState.getValue().equals(value)) {
					deviceState.setValue(value);
					// 因CONTROL控制點更新故更新前端
					updateControlChannelData(pairkey, deviceState);
				}
			}
			}
		} else {
			// 內存物件初始話
			deviceState = new DeviceState().setValue(value);
			super.setData(pairkey, deviceState);
			switch (pairkey.getViewType()) {
			case CONTROL -> {
				// 因CONTROL控制點更新故更新前端
				updateControlChannelData(pairkey, deviceState);			
			}
			default -> {}
			}
		}
	}
	
	public void setDisconnect(PointInfo pointInfo) {
		CacheKeyPair pairkey = SharedUtils.createKeyPair(pointInfo);
		DeviceState deviceState;
		if (super.hasData(pairkey)) {
			// 內存更新
			deviceState = super.getData(pairkey);
			deviceState.disconnectStep();
		}else {
			// 內存物件初始話
			deviceState = new DeviceState().disconnectStep();
			super.setData(pairkey, deviceState);
		}
		
		switch (pairkey.getViewType()) {
		case CONTROL -> {
			// 因CONTROL控制點更新故更新前端
			updateControlChannelData(pairkey, deviceState);
		}
		default -> {}
		}
	}
	
	public Map<Integer, DeviceState> getCacheByViewType(ViewType viewType) {
		Map<Integer, DeviceState> result = new HashMap<>();
		for (CacheKeyPair keyPair : super.getAllKey()) {
			if(keyPair.getViewType().equals(viewType)) {
				result.put(keyPair.getPointId(), super.getData(keyPair));
			}
		}
		return result;
	}
	
	public void updateControlChannelData(CacheKeyPair pairKey, DeviceState deviceState) {
		String message = "";
		Map<Integer, DeviceState> map = new HashMap<>();
		map.put(pairKey.getPointId(), deviceState);
		try {
			message = objectMapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			SharedUtils.getLogger(this.getClass()).error(e.getLocalizedMessage());
			return;
		}
		controlWebsocketCache.sendMessageToAllChannel(message);
	}

}
