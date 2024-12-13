<script>
    import { Image } from '@sveltestrap/sveltestrap';

    import ClearNight from '../assets/WeatherIcons/amcharts_weather_icons_1.0.0/animated/night.svg';
    import DarkCloud from '../assets/WeatherIcons/amcharts_weather_icons_1.0.0/animated/cloudy.svg';
    import FewCloudsNight from '../assets/WeatherIcons/amcharts_weather_icons_1.0.0/animated/cloudy-night-1.svg';
    import FewClouds from '../assets/WeatherIcons/amcharts_weather_icons_1.0.0/animated/cloudy-day-1.svg';
    import Mist from '../assets/WeatherIcons/amcharts_weather_icons_1.0.0/animated/cloudy.svg';
    import RainMoon from '../assets/WeatherIcons/amcharts_weather_icons_1.0.0/animated/rainy-6.svg';
    import RainSun from '../assets/WeatherIcons/amcharts_weather_icons_1.0.0/animated/rainy-1.svg';
    import ScatteredClouds from '../assets/WeatherIcons/amcharts_weather_icons_1.0.0/animated/cloudy.svg';
    import ShowerRain from '../assets/WeatherIcons/amcharts_weather_icons_1.0.0/animated/rainy-7.svg';
    import Snow from '../assets/WeatherIcons/amcharts_weather_icons_1.0.0/animated/snowy-5.svg';
    import SunnyWeather from '../assets/WeatherIcons/amcharts_weather_icons_1.0.0/animated/day.svg';
    import Thunderstorm from '../assets/WeatherIcons/amcharts_weather_icons_1.0.0/animated/thunder.svg';
    import Sunny from '../assets/WeatherIcons/amcharts_weather_icons_1.0.0/animated/day.svg';
    import drops from '../assets/humidity-white.png'
    

    export let forecastDate = "",  description = "", temperature = "", minTemperature = "", maxTemperature = "", humidity = "", iconCode = "", city="";

    function chooseIcon(iconCode){
        let iconImg = ""
        switch(iconCode){
          case "01n": iconImg = ClearNight;
          break;
          case "01d": iconImg = SunnyWeather;
          break;
          case "02d": iconImg = FewClouds;
          break;
          case "02n": iconImg = FewCloudsNight;
          break;
          case "03d": iconImg = ScatteredClouds;
          break;
          case "03n": iconImg = ScatteredClouds;
          break;
          case "04n": iconImg = DarkCloud;
          break;
          case "04d": iconImg = DarkCloud;
          break;
          case "09d": iconImg = ShowerRain;
          break;
          case "09n": iconImg = ShowerRain;
          break;
          case "10d": iconImg = RainSun;
          break;
          case "10n": iconImg = RainMoon;
          break;
          case "11n": iconImg = Thunderstorm;
          break;
          case "11d": iconImg = Thunderstorm;
          break;
          case "13d": iconImg = Snow;
          break;
          case "13n": iconImg = Snow;
          break;
          case "50n": iconImg = Mist;
          break;
          case "50d": iconImg = Mist;
          break;
          default: iconImg = "";
          break;
      }
      return iconImg;
    }

    function capitalizeFirstLetter(string) {
        return string.charAt(0).toUpperCase() + string.slice(1)
    }

</script>

<main>
    <div class="header-container">
        <h2>{capitalizeFirstLetter(city)}</h2>
        <h1>{Math.round(Number(temperature))}°</h1>
        <h3>{capitalizeFirstLetter(description)}</h3>
        <div class="d-flex justify-content-center mt-2">
            <label for="humidity"><Image src={drops} height=25/></label>
            <h4 class="ms-1" id="humidity">{humidity}%</h4>
        </div>
        
    </div>
</main>

<style>
    .header-container {
        font-family: Arial, Helvetica, sans-serif;
        text-align: center;
    }

    h2 {
        color: white;
        font-size: 3rem;
        margin: 0;
    }

    h1 {
        color: white;
        font-size: 10rem;
        font-weight: lighter;
        margin: 0;
        margin-left: 3.5rem;
    }

    h3 {
        color: white;
        font-size: 1.5rem;
        margin: 0;
    }

    h4 {
        color: white;
        font-size: 1.5rem;
        margin: 0;
    }
</style>