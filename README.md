# 🌍 TravelLens

**Analyze your travel behavior and discover eco-friendly alternatives**

TravelLens is a comprehensive travel behavior analysis platform that processes Google Maps Timeline data to provide insights on CO2 emissions, costs, and time efficiency. Get personalized suggestions for greener travel alternatives.

![TravelLens](https://img.shields.io/badge/Status-Active-success)
![Angular](https://img.shields.io/badge/Angular-17-red)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green)

## ✨ Features

- 📊 **Comprehensive Analysis**: Analyze your travel patterns with detailed metrics
- 🌱 **CO2 Score**: Get a personalized sustainability score (0-100) based on emissions
- 💡 **Smart Suggestions**: Receive AI-powered recommendations for eco-friendly alternatives
- 💰 **Cost Savings**: See how much money you could save
- 🌿 **CO2 Tracking**: Monitor your carbon footprint
- 📈 **Visual Insights**: Beautiful charts and visualizations
- 🎨 **Modern UI**: Stunning glassmorphism design with smooth animations

## 🏗️ Architecture

### Backend (Spring Boot)
- **REST API** for data processing and analysis
- **Multi-format support** for Google Maps Timeline JSON (legacy & 2024 formats)
- **Analysis Engine** with CO2, cost, and time calculations
- **Suggestion Algorithm** for personalized recommendations

### Frontend (Angular)
- **Modern SPA** with standalone components
- **Responsive Design** optimized for all devices
- **Real-time Updates** with RxJS
- **Premium UI** with glassmorphism and animations

## 🚀 Getting Started

### Prerequisites

- **Java 17+** (for backend)
- **Node.js 18+** (for frontend)
- **Maven** (for backend build)
- **npm** (for frontend dependencies)

### Backend Setup

1. Navigate to the backend directory:
```bash
cd backend
```

2. Build the project:
```bash
./mvnw clean install
```

3. Run the Spring Boot application:
```bash
./mvnw spring-boot:run
```

The backend API will be available at `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

The frontend will be available at `http://localhost:4200`

## 📱 How to Use

### 1. Export Your Google Maps Timeline

**On Mobile:**
1. Open Google Maps app
2. Tap your profile picture → **Your Timeline**
3. Tap the three dots (⋮) → **Settings and privacy**
4. Scroll down and tap **"Export Timeline data"**
5. Select the time range
6. Export as JSON

**On Desktop (Legacy):**
1. Go to [Google Takeout](https://takeout.google.com/)
2. Select **Location History**
3. Choose JSON format
4. Download your data

### 2. Upload to TravelLens

1. Open TravelLens in your browser
2. Drag and drop your Timeline JSON file or click to browse
3. Wait for automatic analysis

### 3. View Your Results

- **CO2 Score**: See your overall sustainability rating based on emissions
- **Statistics**: View total trips, distance, CO2, and costs
- **Breakdown**: Analyze trips by transport mode
- **Suggestions**: Get personalized recommendations
- **Savings**: See potential CO2 and cost savings

## 🔧 API Endpoints

### Timeline Controller

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/timeline/upload` | Upload Timeline JSON file |
| POST | `/api/timeline/analyze` | Analyze uploaded data |
| GET | `/api/timeline/analysis` | Get analysis results |
| GET | `/api/timeline/suggestions` | Get improvement suggestions |
| GET | `/api/timeline/trips` | Get all parsed trips |
| GET | `/api/timeline/health` | Health check |

## 📊 Analysis Metrics

### Transport Modes
- 🚗 Car
- 🚌 Bus
- 🚆 Train
- 🚊 Tram
- 🚇 Subway
- 🚴 Bicycle
- 🚶 Walking
- 🏍️ Motorcycle
- ✈️ Airplane

### Calculations

**CO2 Emissions (kg/km):**
- Car: 0.192
- Bus: 0.089
- Train: 0.041
- Bicycle/Walking: 0.0

**Cost Estimates (EUR/km):**
- Car: 0.35 (fuel + maintenance)
- Bus/Tram/Subway: 0.10-0.12
- Bicycle: 0.02 (maintenance only)

**CO2 Score:**
- Based 100% on CO2 emissions compared to car-only scenario
- Score = 100 - (actualCO2 / carOnlyCO2 × 100)
- Cost and sustainability scores are displayed for reference only

## 🎨 Design System

### Color Palette
- **Primary Green**: `hsl(142, 71%, 45%)`
- **Secondary Teal**: `hsl(174, 62%, 47%)`
- **Accent Lime**: `hsl(84, 81%, 44%)`

### Features
- Glassmorphism effects
- Smooth animations
- Responsive grid system
- Custom scrollbar styling
- Dark mode optimized

## 🧪 Testing

### Backend Tests
```bash
cd backend
./mvnw test
```

### Frontend Tests
```bash
cd frontend
npm test
```

## 📝 Project Structure

```
TravelLens/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/travellens/
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── model/
│   │   │   │   └── config/
│   │   │   └── resources/
│   │   └── test/
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/
│   │   │   ├── services/
│   │   │   └── models/
│   │   ├── styles.css
│   │   └── index.html
│   ├── angular.json
│   └── package.json
└── README.md
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

This project is licensed under the MIT License.

## 🙏 Acknowledgments

- Google Maps Timeline for providing travel data
- Spring Boot community
- Angular team
- All contributors

## 📧 Support

For issues and questions, please open an issue on GitHub.

---

**Made with 💚 for a greener planet**
