import { useState, useEffect } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { 
  Search, 
  Cloud, 
  Sun, 
  CloudRain, 
  CloudSnow, 
  Wind, 
  Droplets,
  MapPin,
  RefreshCw,
  CloudSun,
  CloudFog,
  Compass
} from 'lucide-react';

interface WeatherForecast {
  id: string;
  city: string;
  country: string;
  date: string;
  temperatureCelsius: number;
  temperatureFahrenheit: number;
  condition: string;
  humidity: number;
  windSpeedKmh: number;
  windDirection: string;
  description: string;
}

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

function App() {
  const [forecasts, setForecasts] = useState<WeatherForecast[]>([]);
  const [searchCity, setSearchCity] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [useCelsius, setUseCelsius] = useState(true);

  const fetchAllForecasts = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(`${API_BASE_URL}/api/weather`);
      if (!response.ok) throw new Error('Failed to fetch weather data');
      const data = await response.json();
      setForecasts(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'An error occurred');
    } finally {
      setLoading(false);
    }
  };

  const searchByCity = async () => {
    if (!searchCity.trim()) {
      fetchAllForecasts();
      return;
    }
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(`${API_BASE_URL}/api/weather/city/${encodeURIComponent(searchCity)}`);
      if (!response.ok) {
        if (response.status === 404) {
          setForecasts([]);
          setError(`No forecasts found for "${searchCity}"`);
          return;
        }
        throw new Error('Failed to fetch weather data');
      }
      const data = await response.json();
      setForecasts(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'An error occurred');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAllForecasts();
  }, []);

  const getWeatherIcon = (condition: string) => {
    const conditionLower = condition.toLowerCase();
    if (conditionLower.includes('sunny') || conditionLower.includes('clear')) {
      return <Sun className="w-12 h-12 text-yellow-500" />;
    }
    if (conditionLower.includes('rain') || conditionLower.includes('shower')) {
      return <CloudRain className="w-12 h-12 text-blue-500" />;
    }
    if (conditionLower.includes('snow')) {
      return <CloudSnow className="w-12 h-12 text-blue-200" />;
    }
    if (conditionLower.includes('partly') || conditionLower.includes('partial')) {
      return <CloudSun className="w-12 h-12 text-yellow-400" />;
    }
    if (conditionLower.includes('fog') || conditionLower.includes('mist')) {
      return <CloudFog className="w-12 h-12 text-gray-400" />;
    }
    if (conditionLower.includes('cloud') || conditionLower.includes('overcast')) {
      return <Cloud className="w-12 h-12 text-gray-500" />;
    }
    return <Cloud className="w-12 h-12 text-gray-500" />;
  };

  const getWeatherGradient = (condition: string) => {
    const conditionLower = condition.toLowerCase();
    if (conditionLower.includes('sunny') || conditionLower.includes('clear')) {
      return 'from-yellow-400 via-orange-400 to-red-400';
    }
    if (conditionLower.includes('rain') || conditionLower.includes('shower')) {
      return 'from-blue-400 via-blue-500 to-blue-600';
    }
    if (conditionLower.includes('snow')) {
      return 'from-blue-100 via-blue-200 to-blue-300';
    }
    if (conditionLower.includes('partly') || conditionLower.includes('partial')) {
      return 'from-yellow-300 via-blue-300 to-blue-400';
    }
    if (conditionLower.includes('cloud') || conditionLower.includes('overcast')) {
      return 'from-gray-400 via-gray-500 to-gray-600';
    }
    return 'from-blue-400 via-purple-400 to-pink-400';
  };

  const groupedForecasts = forecasts.reduce((acc, forecast) => {
    const key = `${forecast.city}, ${forecast.country}`;
    if (!acc[key]) {
      acc[key] = [];
    }
    acc[key].push(forecast);
    return acc;
  }, {} as Record<string, WeatherForecast[]>);

  const cities = Object.keys(groupedForecasts);

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900">
      <div className="container mx-auto px-4 py-8">
        <header className="text-center mb-12">
          <h1 className="text-5xl font-bold text-white mb-4 tracking-tight">
            <span className="bg-gradient-to-r from-blue-400 via-purple-400 to-pink-400 bg-clip-text text-transparent">
              Weather Forecast
            </span>
          </h1>
          <p className="text-gray-400 text-lg">Real-time weather updates for cities worldwide</p>
        </header>

        <div className="max-w-2xl mx-auto mb-12">
          <div className="flex gap-3">
            <div className="relative flex-1">
              <Search className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
              <Input
                type="text"
                placeholder="Search by city name..."
                value={searchCity}
                onChange={(e) => setSearchCity(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && searchByCity()}
                className="pl-12 h-14 bg-white/10 border-white/20 text-white placeholder:text-gray-400 rounded-xl text-lg backdrop-blur-sm transition-all duration-300 focus:bg-white/20 focus:border-purple-400"
              />
            </div>
            <Button 
              onClick={searchByCity}
              className="h-14 px-8 bg-gradient-to-r from-purple-500 to-pink-500 hover:from-purple-600 hover:to-pink-600 rounded-xl text-lg font-semibold transition-all duration-300 hover:scale-105 hover:shadow-lg hover:shadow-purple-500/25"
            >
              Search
            </Button>
            <Button 
              onClick={() => {
                setSearchCity('');
                fetchAllForecasts();
              }}
              variant="outline"
              className="h-14 px-6 border-white/20 text-white hover:bg-white/10 rounded-xl transition-all duration-300 hover:scale-105"
            >
              <RefreshCw className="w-5 h-5" />
            </Button>
          </div>
          
          <div className="flex justify-center mt-6">
            <div className="inline-flex bg-white/10 rounded-xl p-1 backdrop-blur-sm">
              <Button
                variant={useCelsius ? "default" : "ghost"}
                onClick={() => setUseCelsius(true)}
                className={`rounded-lg px-6 transition-all duration-300 ${useCelsius ? 'bg-white/20 text-white' : 'text-gray-400 hover:text-white'}`}
              >
                Celsius
              </Button>
              <Button
                variant={!useCelsius ? "default" : "ghost"}
                onClick={() => setUseCelsius(false)}
                className={`rounded-lg px-6 transition-all duration-300 ${!useCelsius ? 'bg-white/20 text-white' : 'text-gray-400 hover:text-white'}`}
              >
                Fahrenheit
              </Button>
            </div>
          </div>
        </div>

        {loading && (
          <div className="flex justify-center items-center py-20">
            <div className="relative">
              <div className="w-16 h-16 border-4 border-purple-500/30 rounded-full animate-spin border-t-purple-500"></div>
              <Cloud className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 w-8 h-8 text-purple-400" />
            </div>
          </div>
        )}

        {error && (
          <div className="max-w-md mx-auto">
            <Card className="bg-red-500/10 border-red-500/30 backdrop-blur-sm">
              <CardContent className="pt-6 text-center">
                <p className="text-red-400">{error}</p>
              </CardContent>
            </Card>
          </div>
        )}

        {!loading && !error && forecasts.length > 0 && (
          <Tabs defaultValue={cities[0]} className="w-full">
            <TabsList className="flex flex-wrap justify-center gap-2 bg-transparent mb-8">
              {cities.map((city) => (
                <TabsTrigger
                  key={city}
                  value={city}
                  className="px-6 py-3 rounded-xl bg-white/10 text-gray-300 data-[state=active]:bg-gradient-to-r data-[state=active]:from-purple-500 data-[state=active]:to-pink-500 data-[state=active]:text-white transition-all duration-300 hover:bg-white/20 backdrop-blur-sm"
                >
                  <MapPin className="w-4 h-4 mr-2" />
                  {city}
                </TabsTrigger>
              ))}
            </TabsList>

            {cities.map((city) => (
              <TabsContent key={city} value={city} className="mt-0">
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                  {groupedForecasts[city]
                    .sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime())
                    .map((forecast, index) => (
                    <Card 
                      key={forecast.id}
                      className="group bg-white/10 border-white/20 backdrop-blur-md overflow-hidden transition-all duration-500 hover:scale-105 hover:shadow-2xl hover:shadow-purple-500/20"
                      style={{ animationDelay: `${index * 100}ms` }}
                    >
                      <div className={`h-2 bg-gradient-to-r ${getWeatherGradient(forecast.condition)}`}></div>
                      <CardHeader className="pb-2">
                        <div className="flex justify-between items-start">
                          <div>
                            <CardTitle className="text-white text-xl font-bold">
                              {new Date(forecast.date).toLocaleDateString('en-US', { 
                                weekday: 'short', 
                                month: 'short', 
                                day: 'numeric' 
                              })}
                            </CardTitle>
                            <Badge variant="secondary" className="mt-2 bg-white/20 text-white border-0">
                              {forecast.condition}
                            </Badge>
                          </div>
                          <div className="transform transition-transform duration-500 group-hover:scale-110 group-hover:rotate-12">
                            {getWeatherIcon(forecast.condition)}
                          </div>
                        </div>
                      </CardHeader>
                      <CardContent>
                        <div className="text-center my-6">
                          <div className="text-6xl font-bold text-white tracking-tighter">
                            {useCelsius 
                              ? `${Math.round(forecast.temperatureCelsius)}°C`
                              : `${Math.round(forecast.temperatureFahrenheit)}°F`
                            }
                          </div>
                          <p className="text-gray-400 mt-2 text-sm">
                            {forecast.description || 'No description available'}
                          </p>
                        </div>
                        
                        <div className="grid grid-cols-3 gap-4 pt-4 border-t border-white/10">
                          <div className="text-center group/stat">
                            <div className="flex justify-center mb-2">
                              <Droplets className="w-5 h-5 text-blue-400 transition-transform duration-300 group-hover/stat:scale-125" />
                            </div>
                            <p className="text-white font-semibold">{forecast.humidity}%</p>
                            <p className="text-gray-500 text-xs">Humidity</p>
                          </div>
                          <div className="text-center group/stat">
                            <div className="flex justify-center mb-2">
                              <Wind className="w-5 h-5 text-teal-400 transition-transform duration-300 group-hover/stat:scale-125" />
                            </div>
                            <p className="text-white font-semibold">{forecast.windSpeedKmh} km/h</p>
                            <p className="text-gray-500 text-xs">Wind</p>
                          </div>
                          <div className="text-center group/stat">
                            <div className="flex justify-center mb-2">
                              <Compass className="w-5 h-5 text-purple-400 transition-transform duration-300 group-hover/stat:scale-125" />
                            </div>
                            <p className="text-white font-semibold">{forecast.windDirection || 'N/A'}</p>
                            <p className="text-gray-500 text-xs">Direction</p>
                          </div>
                        </div>
                      </CardContent>
                    </Card>
                  ))}
                </div>
              </TabsContent>
            ))}
          </Tabs>
        )}

        {!loading && !error && forecasts.length === 0 && (
          <div className="text-center py-20">
            <Cloud className="w-24 h-24 text-gray-600 mx-auto mb-6" />
            <h2 className="text-2xl font-semibold text-gray-400 mb-2">No Weather Data</h2>
            <p className="text-gray-500">Try searching for a different city or refresh the page</p>
          </div>
        )}

        <footer className="mt-16 text-center text-gray-500 text-sm">
          <p>Weather Forecast API - Powered by Spring Boot</p>
        </footer>
      </div>
    </div>
  );
}

export default App
