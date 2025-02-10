# PanHouseFarmAssistant

* [English](README.md)
* [繁體中文版README.md](README.zh-TW.md)
* [日本語版README.md](README.jp.md)

## Project Description
PanHouseFarmAssistant is a simple center management system that integrates IoT technology to read sensor data and control devices. The project is designed for soil moisture monitoring in a home garden. A soil moisture sensor is deployed in the garden to measure soil moisture levels, and the data is received by an IO Module. The IO Module then transmits the data to the server via Ethernet, allowing remote control of the water valve to maintain soil moisture.

## Features
This project provides the following features:
1. **Real-time Monitoring** - Monitors garden soil moisture conditions.
2. **Remote Control** - Controls the water valve remotely.
3. **Data Collection** - Stores soil moisture data in a database.
4. **Scheduled Control** - Automates the water valve operation at scheduled times.
5. **Alert System** - Sends alerts when soil moisture levels are too high or too low.
6. **Customizable Alert Thresholds** - Allows users to set a normal soil moisture range (e.g., 60%–80%) and triggers alerts when values exceed this range.
7. **Record Inquiry** - Enables users to review historical data and alert records.

## Technologies Used
This project is developed in Java using the Spring Boot framework and runs on an embedded Jetty server. The following technologies are implemented for various functionalities:

### Communication Protocols
- Supports **Modbus** and **BACnet** (Building Automation and Control Networks) for communication.
- Future updates will include **MQTT** (Message Queuing Telemetry Transport) and **ZigBee** for wireless communication.

### Scheduling and Task Management
- Utilizes **Quartz** for scheduling tasks and provides a simplified tool for implementation.
- Implements **ScheduledExecutorService** to execute specific tasks at scheduled times.

### Real-time Monitoring
- Uses a custom in-memory tool to store sensor data.
- Transfers real-time sensor data to users via **WebSocket** technology.

### Reliable Device Control
- Implements **Spring Retry** to ensure retry mechanisms for device control, enhancing reliability.

### Notification System
- Uses **net.dv8tion.JDA** to send custom alert messages to Discord, notifying users of anomalies.

### Key-Value Mapping
- Enhances Java Map key-value management using a **custom composite key** to store data efficiently, avoiding complex multi-layered Map structures.

### Security and API Management
- Provides **RESTful APIs** for project functionalities.
- **Parameter and Object Validation**: Uses `@RequestParam` and `@RequestBody` with custom validation methods or `jakarta.validation.Valid` to prevent errors.
- **Security Management**:
  - **Spring Security**: Manages authentication and CORS (Cross-Origin Resource Sharing).
  - **JSON Web Token (JWT)**: Requires token authentication for critical API endpoints.

### Exception Handling
- Uses **@ControllerAdvice** to implement a unified exception handling system for centralized error management and debugging.

## Installation Steps
Installation steps are not provided at the moment.

## Usage Instructions
Usage instructions are not provided at the moment.

## Contribution Guidelines
Contributions to this project are welcome! If you would like to contribute, please follow these steps:
1. Fork the repository.
2. Create a new branch for your feature (e.g., `feature/new-functionality`).
3. Submit a Pull Request with a detailed description of the changes.

## License
No specific license is assigned to this project. If you wish to use or distribute this project, please contact the author for permission.

## Additional Information
This project prioritizes system performance and stability:
- Database queries avoid using `*` to optimize data retrieval.
- Uses `switch` instead of nested `if` statements to improve readability and efficiency.
- Strictly manages variable declarations to reduce unnecessary memory consumption and prevent memory leaks.

For any questions or suggestions, feel free to contact the author!
