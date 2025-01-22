package tw.pan.utils.connect;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.npdu.ip.IpNetworkBuilder;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.util.RequestUtils;

import tw.pan.utils.SharedUtils;

@Component
public class Bacnet4j {

	@Value("${public.bacnet.local-ip}")
	private String localIP;
	@Value("${public.bacnet.subnet}")
	private String subnet;
	@Value("${public.bacnet.port}")
	private Integer bacnetPort;
	@Value("${public.bacnet.device-id}")
	private Integer localDeviceId;

	// 讀取BacNet設備所需之本機物件
	private LocalDevice localDevice;

	/**
	 * 建立本地設備
	 * 
	 * @param subnet
	 * @param port
	 * @param localDeviceNumber
	 * @return
	 */
	private LocalDevice getLocalDevice(String ip, String subnet, int port, int localDeviceNumber) {
		IpNetwork ipNetwork = new IpNetworkBuilder().withLocalBindAddress(ip).withSubnet(subnet, 24).withPort(port)
				.withReuseAddress(true).build();
		LocalDevice localDevice = new LocalDevice(localDeviceNumber, new DefaultTransport(ipNetwork));
		try {
			localDevice.initialize();
			localDevice.startRemoteDeviceDiscovery();
		} catch (Exception e) {
			SharedUtils.getLogger(this.getClass()).error(e.getLocalizedMessage());
		}
		return localDevice;
	}

	/**
	 * 確保LocalDavice存活
	 * 
	 * @return
	 */
	public LocalDevice checkLocalDevice() {
		if (this.localDevice == null || !this.localDevice.isInitialized()) {
			this.localDevice = getLocalDevice(localIP, subnet, bacnetPort, localDeviceId);
		}
		return this.localDevice;
	}

	/**
	 * 檢查本地設備是否已存在
	 * 
	 * @param localDevice
	 * @return
	 */
	public boolean hasConnect(LocalDevice localDevice) {
		return localDevice.isInitialized();
	}

	/**
	 * 刪除本地設備
	 * 
	 * @param localDevice
	 */
	public void closeConnect(LocalDevice localDevice) {
		localDevice.terminate();
	}

	/**
	 * 取得特定的遠方設備資料
	 * 
	 * @param localDevice
	 * @param instanceNumber
	 * @param timeoutMillis
	 * @return
	 */
	public RemoteDevice getRemoteDevice(LocalDevice localDevice, int instanceNumber, int timeoutMillis) {
		if (localDevice != null) {
			try {
				return localDevice.getRemoteDeviceBlocking(instanceNumber, timeoutMillis);
			} catch (BACnetException e) {
				SharedUtils.getLogger(this.getClass()).error(e.getLocalizedMessage());
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 取得遠方設備的資訊
	 * 
	 * @param localDevice
	 * @param remoteDevice
	 * @param objectType
	 * @param instanceNumber
	 * @return
	 */
	public Object readProperty(LocalDevice localDevice, RemoteDevice remoteDevice, int objectType, int instanceNumber) {
		try {
			return RequestUtils.readProperty(localDevice, remoteDevice,
					new ObjectIdentifier(objectType, instanceNumber), PropertyIdentifier.presentValue, null);
		} catch (BACnetException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 單點寫入狀態方法 2.0</br>
	 * 新加如果有發生錯誤會自動重試功能 </br>
	 * 最多重試兩次，間格1000豪秒 </br>
	 * 如果需一次控制多點請另外重新編輯
	 * 
	 * @param bacnetObjectDevice
	 * @param objectType
	 * @param instanceNumber
	 * @param state
	 * @throws BACnetException
	 */
	@Retryable(retryFor = BACnetException.class, maxAttempts = 2, backoff = @Backoff(delay = 1000))
	public void writeState(Integer bacnetObjectDevice, Integer objectType, Integer instanceNumber, Boolean state)
			throws BACnetException {
		checkLocalDevice();
		RemoteDevice remoteDevice = localDevice.getRemoteDeviceBlocking(bacnetObjectDevice);
		ObjectIdentifier objectIdentifier = new ObjectIdentifier(objectType, instanceNumber);
//		// 判斷更動的是開關或是數值
//		Encodable input = switch (objectType) {
//		case 3, 4, 5 ->
//			// 只有binary是開關，並用0 1判斷開關
//			state ? BinaryPV.inactive : BinaryPV.active;
//		default ->
//			// 否則都是數值
//			new Real(state ? 1 : 0);
//		};
		Encodable input = state ? BinaryPV.inactive : BinaryPV.active;
		RequestUtils.writeProperty(localDevice, remoteDevice, objectIdentifier, PropertyIdentifier.presentValue, input);
	}

	/**
	 * 單點寫入數值方法 2.0</br>
	 * 新加如果有發生錯誤會自動重試功能 </br>
	 * 最多重試兩次，間格1000豪秒 </br>
	 * 如果需一次控制多點請另外重新編輯
	 * 
	 * @param bacnetObjectDevice
	 * @param objectType
	 * @param instanceNumber
	 * @param value
	 * @throws BACnetException
	 */
	@Retryable(retryFor = BACnetException.class, maxAttempts = 2, backoff = @Backoff(delay = 1000))
	public void writeValue(Integer bacnetObjectDevice, Integer objectType, Integer instanceNumber, Integer value)
			throws BACnetException {
		checkLocalDevice();
		RemoteDevice remoteDevice = localDevice.getRemoteDeviceBlocking(bacnetObjectDevice);
		ObjectIdentifier objectIdentifier = new ObjectIdentifier(objectType, instanceNumber);
		// 判斷更動的是開關或是數值
		Encodable input = new Real(value.floatValue());
		RequestUtils.writeProperty(localDevice, remoteDevice, objectIdentifier, PropertyIdentifier.presentValue, input);
	}

	public String objectToStr(Object object) {
		if (object != null) {
			return object.toString();
		} else {
			return null;
		}
	}

}
