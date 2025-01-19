package tw.jdi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import tw.jdi.entity.dto.AlertSetDto;
import tw.jdi.entity.dto.ControlDto;
import tw.jdi.entity.dto.RecordDto;

@RestController
@RequestMapping("/index")
public class MainController {

	// 控制設備(水閥)
	@PostMapping(value = "/control/es")
	public ResponseEntity<String> controlES(@Valid @RequestBody ControlDto controlDto) {
		return ResponseEntity.ok("");
	}
	
	// 紀錄查詢
	@PostMapping(value = "/record")
	public ResponseEntity<String> getRecord(@Valid @RequestBody RecordDto recordDto) {
		return ResponseEntity.ok("");
	}
	
	// 異常設定
	@PostMapping(value = "/alertSet")
	public ResponseEntity<String> editAlertSetting(@Valid @RequestBody AlertSetDto alertSetDao) {
		return ResponseEntity.ok("");
	}
	
	// 異常紀錄查詢
	@PostMapping(value = "/alertRecord")
	public ResponseEntity<String> getAlertRecord(@Valid @RequestBody RecordDto recordDto) {
		return ResponseEntity.ok("");
	}
	
}
