package tw.pan.utils.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.Resource;
import tw.pan.entity.enumEntity.IoType.ViewType;
import tw.pan.entity.po.DeviceState;
import tw.pan.entity.po.PointInfo;
import tw.pan.utils.SharedUtils;
import tw.pan.utils.quartz.ScheduleMission;
import tw.pan.utils.websocket.ControlWebsocketCache;
import tw.pan.utils.websocket.ViewWebsocketCache;

/**
 * 所有即時數值內存 V2.0 </br>
 * 變更儲存方式->平面鍵值映射 </br>
 * 
 * @author PAN
 */
@Service
public class DeviceStateCacheManager extends Cache<CustomKeyPair<ViewType, Integer>, DeviceState> implements ScheduleMission {

	@Resource(name = "schedulePool")
	private ScheduledExecutorService pool;
	@Autowired
	private ViewWebsocketCache viewWebsocketCache;
	@Autowired
	private ControlWebsocketCache controlWebsocketCache;
	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public void doJob() {
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
		CustomKeyPair<ViewType, Integer> pairkey = 
				new CustomKeyPair<ViewType, Integer>(pointInfo.getIoType().getViewType(), pointInfo.getPointId());
		DeviceState deviceState;
		if (super.hasData(pairkey)) {
			// 內存更新
			deviceState = super.getData(pairkey);
			switch (pairkey.getEntity1()) {
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
			switch (pairkey.getEntity1()) {
			case CONTROL -> {
				// 因CONTROL控制點更新故更新前端
				updateControlChannelData(pairkey, deviceState);			
			}
			default -> {}
			}
		}
	}
	
	public void setCache(PointInfo pointInfo, Float value) {
		CustomKeyPair<ViewType, Integer> pairkey = 
				new CustomKeyPair<ViewType, Integer>(pointInfo.getIoType().getViewType(), pointInfo.getPointId());
		DeviceState deviceState;
		if (super.hasData(pairkey)) {
			// 內存更新
			deviceState = super.getData(pairkey);
			switch (pairkey.getEntity1()) {
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
			switch (pairkey.getEntity1()) {
			case CONTROL -> {
				// 因CONTROL控制點更新故更新前端
				updateControlChannelData(pairkey, deviceState);			
			}
			default -> {}
			}
		}
	}
	
	public void setDisconnect(PointInfo pointInfo) {
		CustomKeyPair<ViewType, Integer> pairkey = 
				new CustomKeyPair<ViewType, Integer>(pointInfo.getIoType().getViewType(), pointInfo.getPointId());
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
		
		switch (pairkey.getEntity1()) {
		case CONTROL -> {
			// 因CONTROL控制點更新故更新前端
			updateControlChannelData(pairkey, deviceState);
		}
		default -> {}
		}
	}
	
	public Map<Integer, DeviceState> getCacheByViewType(ViewType viewType) {
		Map<Integer, DeviceState> result = new HashMap<>();
		for (CustomKeyPair<ViewType, Integer> keyPair : super.getAllKey()) {
			if(keyPair.getEntity1().equals(viewType)) {
				result.put(keyPair.getEntity2(), super.getData(keyPair));
			}
		}
		return result;
	}
	
	public void updateControlChannelData(CustomKeyPair<ViewType, Integer> pairKey, DeviceState deviceState) {
		String message = "";
		Map<Integer, DeviceState> map = new HashMap<>();
		map.put(pairKey.getEntity2(), deviceState);
		try {
			message = objectMapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			SharedUtils.getLogger(this.getClass()).error(e.getLocalizedMessage());
			return;
		}
		controlWebsocketCache.sendMessageToAllChannel(message);
	}

}
