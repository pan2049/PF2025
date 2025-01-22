package tw.pan.service.impl.schedule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.util.RequestUtils;

import jakarta.annotation.Resource;
import tw.pan.utils.connect.Bacnet4j;
import tw.pan.utils.quartz.ScheduleMission;

/**
 * Bacnet設備讀取，這邊國震設備比較單純，每台處理機底下只有一個點，所以之後經過測試後可以選擇將線程合併減少開銷
 * 
 * @author PAN
 */

@Service
public class BacnetReadService implements ScheduleMission {

	@Resource(name = "schedulePool")
	private ScheduledExecutorService pool;
	@Autowired
	private Bacnet4j bacnet4j;
//	@Autowired
//	private GatewayBacnetDao gatewayBacnetDao;
//	@Autowired
//	private PointInfoBacnetDao pointInfoBacnetDao;
//	@Autowired
//	private DeviceStateCacheManager deviceStateCacheManager;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void doJob() {
//		List<GatewayBacnet> gatewayBacnetList = gatewayBacnetDao.selectGatewayBacnet();
//		for (GatewayBacnet gateway : gatewayBacnetList) {
//			// 依照處理機數設定讀取線程
//			setSchedule(gateway);
//		}
	}

	/**
	 * 讀取獨立線程
	 * 
	 * @param GatewayBacnet gateway
	 */
//	private void setSchedule(GatewayBacnet gateway) {
//		pool.scheduleWithFixedDelay(new Runnable() {
//			@Override
//			public void run() {
//				// 先取得處理機連線
//				RemoteDevice remoteDevice = null;
//				try {
//					remoteDevice = bacnet4j.checkLocalDevice().getRemoteDeviceBlocking(gateway.getBacnetObjectDevice());
//				} catch (BACnetException e) {
//					logger.error(e.getLocalizedMessage());
//				}
//				// 取得該處理機的所有點
//				// 國震這邊比較特別，因為一個處理機裡面只有一個點位，之後依看情況修改
//				List<PointInfoBacnet> pointInfoList = pointInfoBacnetDao.selectPointBacnet(gateway.getBacnetNum());
//				for (PointInfoBacnet pointInfo : pointInfoList) {
//					saveData(bacnet4j.checkLocalDevice(), remoteDevice, pointInfo);
//				}
//			}
//		}, 0, 1, TimeUnit.SECONDS);
//	}
//
//	/**
//	 * 讀取設備點位資料並存內存
//	 * 
//	 * @param LocalDevice     localDavice
//	 * @param RemoteDevice    remoteDevice
//	 * @param PointInfoBacnet pointInfo
//	 */
//	public void saveData(LocalDevice localDavice, RemoteDevice remoteDevice, PointInfoBacnet pointInfo) {
//		Object result = null;
//		try {
//			result = RequestUtils.readProperty(localDavice, remoteDevice,
//					new ObjectIdentifier(pointInfo.getBacnetObjectType(), pointInfo.getBacnetInstanceNumber()),
//					PropertyIdentifier.presentValue, null);
//		} catch (BACnetException e) {
//			logger.error(e.getLocalizedMessage());
//		}
//		if (result == null) {
//			// 表示斷線或讀取失敗，輸入斷線後提前結束
//			deviceStateCacheManager.setDisconnect(pointInfo);
//			return;
//		}
//		// 國震只有兩種type的點，所以這樣寫，之後的專案需個別判斷
//		switch (pointInfo.getBacnetObjectType()) {
//		case 19 -> {
//			deviceStateCacheManager.setCache(pointInfo, Double.valueOf(result.toString()));
//		}
//		case 5 -> {
//			deviceStateCacheManager.setCache(pointInfo, result.toString().equals("0") ? false : true);
//		}
//		}
//	}
//
//	/**
//	 * 讀取到之設備數值進行四則運算，並四捨五入到小數點後兩位
//	 * 
//	 * @param BigDecimal readValue
//	 * @param String     calSign
//	 * @param Float      convertMerge
//	 * @return Number
//	 */
//	public Number convertValue(BigDecimal readValue, String calSign, Float convertMerge) {
//		switch (calSign) {
//		case "+" -> {
//			return readValue.add(new BigDecimal(convertMerge)).setScale(2, RoundingMode.HALF_UP);
//		}
//		case "-" -> {
//			return readValue.subtract(new BigDecimal(convertMerge)).setScale(2, RoundingMode.HALF_UP);
//		}
//		case "*" -> {
//			return readValue.multiply(new BigDecimal(convertMerge)).setScale(2, RoundingMode.HALF_UP);
//		}
//		case "/" -> {
//			return readValue.divide(new BigDecimal(convertMerge), 2, RoundingMode.HALF_UP);
//		}
//		default -> {
//			return readValue.setScale(2, RoundingMode.HALF_UP);
//		}
//		}
//	}

}
