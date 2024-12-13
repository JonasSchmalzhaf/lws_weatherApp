<script>
    import {Card, CardBody, CardImg, CardText, CardTitle, Image, Label} from '@sveltestrap/sveltestrap'
    import { fade } from 'svelte/transition';

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
    import MinTemperatureIcon from '../assets/min_temperature.svg';
    import MaxTemperatureIcon from '../assets/max_temperature.svg';
    import HumidityIcon from '../assets/humidity.svg';

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

    let iconImg = chooseIcon(iconCode)
</script>

<main class="mt-4">
    <div class="card fade-in p-4 text-bg-white text-white" style="max-width:400px; background-color: rgba(255,255,255,0.1); display:flex; flex-direction:column; align-items: center; justify-content: center;">
        <CardTitle class="mx-auto" style="font-size: 40px; font-weight:900">{city}</CardTitle>
        <CardText class="mx-auto" style="font-size: 20px;">{forecastDate}</CardText>
        <CardImg src={iconImg} style="height: 15rem; width: auto; object-fit: contain; margin-top: -2rem;"/>
            <div style="display:flex; align-items:center; justify-content:center; margin-top:-3rem;">
                <CardText class="mx-auto" style="font-size:30px">{description}</CardText>
            </div>
        <CardBody>
            <div style="display:flex; align-items:center; justify-content:center; margin-top:1rem;">
                <CardImg src="{MinTemperatureIcon}" style="height: 6rem; width: 3rem;"></CardImg>
                <CardText style="font-size:30px;">{minTemperature}°C</CardText>
                <CardImg src="{MaxTemperatureIcon}" style="height: 6rem; width: 3rem;"></CardImg>
                <CardText style="font-size:30px;">{maxTemperature}°C</CardText>
                <CardImg src="{HumidityIcon}" style="height: 6rem; width: 3rem"></CardImg>
                <CardText style="margin-top: -1rem; font-size:30px;">{humidity}%</CardText>
            </div>
        </CardBody>
    </div>
</main>

<style>
    @import 'https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css';
    
    .fade-in {
        opacity: 0;
        animation: fadeInAnimation 0.5s forwards;
    }

    @keyframes fadeInAnimation {
        0% {
            opacity: 0;
        }
        100% {
            opacity: 1;
        }
    } 
</style>