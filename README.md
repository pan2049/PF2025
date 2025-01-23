# PanHouseFarmAssistant

## 專案描述

PanHouseFarmAssistant 是一個結合物聯網（IoT）技術的簡易中央監控系統（Center Management System），用於讀取感測器狀態與數值並對設備進行控制，實現家庭庭院的土壤濕度監控。系統設置了土壤濕度感測器（Soil Moisture Sensor），透過 IO 接收模組（IO Module）接收感測器訊號，並經由 Ethernet 傳輸至 Server。此外，用戶可透過遠端控制水閥以補充土壤水分。

## 功能介紹

1. **現場狀況監控**：監控庭院土壤濕度。
2. **遠端控制**：控制水閥開關以調節土壤濕度。
3. **數據收集**：將土壤濕度數據存入資料庫。
4. **排程控制**：設定固定時間開啟水閥進行灌溉。
5. **異常發報**：在土壤濕度過高或過低時觸發警報並記錄異常。
6. **異常範圍設定**：用戶可自定義濕度正常範圍（例如：60%-80%），超出範圍時觸發異常處理。
7. **紀錄查詢**：可調閱歷史數據與異常記錄。

## 使用技術

本專案使用 Java 開發，基於 Spring Boot 框架，並使用內置的 Jetty Server 運行。
以下為主要功能與使用技術的詳細介紹：

### 通訊技術

- **Modbus 與 BACnet**：提供這兩種通訊方式來進行資料交換。
- **未來拓展**：後續將加入 MQTT（Message Queuing Telemetry Transport）與 ZigBee 等無線通訊技術。

### 排程任務

- 使用 **Quartz** 與 **ScheduledExecutorService**，實現特定任務的定時執行。
- 自行編輯工具方法以簡化排程任務的設定與管理。

### 現場監控

- **自定義內存工具**：暫存感測器數據。
- **WebSocket 技術**：實現數據的即時傳輸與顯示。

### 確保設備控制

- 使用 **Spring Retry**，在控制設備時實現錯誤重試機制，確保操作執行成功。

### 訊息推送

- 使用 **net.dv8tion.JDA**，實現異常訊息推送至 Discord，提醒用戶處理。

### 鍵值映射

- 在 Java Map 基礎上自定義複合型 Key，簡化多層次資料的儲存方式。

### 安全性管理

- **Spring Security**：進行權限審核與跨域資源共享（CORS）。
- **JSON Web Token (JWT)**：針對部分重要 API 進行權限驗證，確保資料安全。

### RESTful API

- 提供 API 以供用戶使用系統功能。
- 使用 `@RequestParam` 與 `@RequestBody` 搭配 `jakarta.validation.Valid` 驗證請求參數，避免錯誤發生。

### 異常處理

- 使用 `@ControllerAdvice` 自定義異常攔截器，統一管理異常並方便除錯。

## 安裝步驟

目前暫不提供安裝步驟。

## 使用方式

目前暫不提供使用方式。

## 貢獻指南

歡迎對本專案進行貢獻！如需貢獻代碼，請遵循以下指導：

1. Fork 此專案到你的儲存庫。
2. 建立新分支進行功能開發或修復（例如：`feature/功能名稱`）。
3. 提交 Pull Request 時，請提供詳細的修改描述。

## 授權條款

目前未指定授權條款。如需使用或分發此專案，請聯繫作者取得授權。

## 其他資訊

本專案重視系統效能與穩定性：

- 資料庫查詢避免使用 `*`，確保高效資料提取。
- 使用 `switch` 替代巢狀 `if` 結構，簡化條件判斷。
- 嚴格管理變數宣告，避免不必要的記憶體消耗與內存洩漏。

如果有任何問題或建議，歡迎聯繫作者！


