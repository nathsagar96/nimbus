# ⛅ Weather Now

A reactive Spring Boot application that provides real-time weather information by city name using the OpenWeatherMap
API.

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)

## 📑 Table of Contents

- [Features](#-features)
- [Installation](#-installation)
- [Usage](#-usage)
- [Technologies](#-technologies)
- [Contributing](#-contributing)
- [License](#-license)
- [Contact](#-contact)
- [Acknowledgements](#-acknowledgements)

## ✨ Features

- Fetch current weather data by city name
- Reactive endpoints using Spring WebFlux
- Error handling for invalid requests and city not found scenarios
- Health monitoring via Spring Actuator
- Detailed logging for debugging and monitoring
- Clean architecture with separation of concerns

## 🚀 Installation

1. Clone the repository:

```bash
git clone https://github.com/nathsagar96/weather-now.git
cd weather-now
```

2. Configure OpenWeatherMap API key in `application.yml`:

```yaml
openweathermap:
  api-key: your_api_key_here
  units: metric
```

3. Build the project:

```bash
mvn clean install
```

4. Run the application:

```bash
mvn spring-boot:run
```

## 🎯 Usage

Get weather for a city:

```bash
curl "http://localhost:8080/api/v1/weather?city=London"
```

Example response:

```json
{
  "city": "London",
  "country": "GB",
  "temperature": 15.5,
  "feelsLike": 14.2,
  "description": "clear sky",
  "humidity": 76,
  "windSpeed": 5.2,
  "timestamp": "2024-03-15T10:30:00Z"
}
```

## 🛠 Technologies

- Java 21
- Spring Boot 3.4.4
- Spring WebFlux
- Spring Actuator
- Project Reactor
- Project Lombok
- Maven

## 👥 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📫 Contact

Sagar Nath - [@nathsagar96](https://github.com/nathsagar96)

## 🙏 Acknowledgements

- [OpenWeatherMap API](https://openweathermap.org/api) for providing weather data
- [Spring Framework](https://spring.io/) for the excellent reactive web framework
- [Project Reactor](https://projectreactor.io/) for reactive programming support