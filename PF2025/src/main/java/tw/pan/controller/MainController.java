package tw.pan.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;

import jakarta.validation.Valid;
import tw.pan.dao.AlertSetDao;
import tw.pan.entity.dto.AlertSetDto;
import tw.pan.entity.dto.ControlDto;
import tw.pan.entity.dto.RecordDto;
import tw.pan.entity.po.AlertRecord;
import tw.pan.entity.po.RecordPo;
import tw.pan.service.impl.AlertRecordService;
import tw.pan.service.impl.AlertSetService;
import tw.pan.service.impl.DeviceControlService;
import tw.pan.service.impl.JwtService;
import tw.pan.service.impl.RecordService;
import tw.pan.utils.SharedUtils;
import tw.pan.utils.cache.CustomKeyPair;
import tw.pan.utils.exception.DatabaseOperateException;
import tw.pan.utils.exception.RequestErrorException;

@RestController
@RequestMapping("/index")
public class MainController {

	@Autowired
	private DeviceControlService deviceControlService;
	@Autowired
	private RecordService recordService;
	@Autowired
	private AlertSetService alertSetService;
	@Autowired
	private AlertRecordService alertRecordService;
	@Autowired
	private JwtService jwtService;
	
	// 控制設備(水閥)
	@PostMapping(value = "/control")
	public ResponseEntity<String> controlES(@RequestHeader("Authorization") String token, @Valid @RequestBody ControlDto controlDto) 
			throws RequestErrorException, DatabaseOperateException, ModbusInitException, ModbusTransportException, ErrorResponseException {
		jwtService.verifyToken(token);
		if(deviceControlService.control(controlDto)) {
			return ResponseEntity.ok("device control success");			
		}else {
			return ResponseEntity.ok("device control fail");	
		}
	}
	
	// 紀錄查詢
	@PostMapping(value = "/record")
	public ResponseEntity<Map<CustomKeyPair<String, String>, List<RecordPo>>> getRecord(@RequestHeader("Authorization") String token, @Valid @RequestBody RecordDto recordDto) 
			throws RequestErrorException {
		jwtService.verifyToken(token);
		if(recordDto.getPointIdSet().isEmpty()) throw new RequestErrorException("The point are searching for is empty");
		if(SharedUtils.compareTime(recordDto.getStartTime(), recordDto.getEndTime())) {
			return ResponseEntity.ok(recordService.getRecord(recordDto));			
		}else {
			throw new RequestErrorException("request's start time isn't before the end time");
		}
	}
	
	// 異常設定
	@PostMapping(value = "/alertSet")
	public ResponseEntity<String> editAlertSetting(@RequestHeader("Authorization") String token, @Valid @RequestBody AlertSetDto alertSetDao) 
			throws RequestErrorException {
		jwtService.verifyToken(token);
		alertSetService.editAlertSet(alertSetDao);
		return ResponseEntity.ok("alert setting success");
	}
	
	// 異常紀錄查詢
	@PostMapping(value = "/alertRecord")
	public ResponseEntity<Map<CustomKeyPair<String, String>, List<AlertRecord>>> getAlertRecord(
			@RequestHeader("Authorization") String token, @Valid @RequestBody RecordDto recordDto) 
					throws RequestErrorException {
		jwtService.verifyToken(token);
		if(recordDto.getPointIdSet().isEmpty()) throw new RequestErrorException("The point are searching for is empty");
		if(SharedUtils.compareTime(recordDto.getStartTime(), recordDto.getEndTime())) {
			return ResponseEntity.ok(alertRecordService.getAlertRecord(recordDto));			
		}else {
			throw new RequestErrorException("request's start time isn't before the end time");
		}
	}
	
}
