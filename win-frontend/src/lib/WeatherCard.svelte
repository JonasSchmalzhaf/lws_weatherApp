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
    import Sunny from '../assets/mist.svg'

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


    <div class="card fade-in p-3 text-bg-secondary">
        <CardTitle class="mx-auto big-text" style="font-size: 40px; font-weight:900">{city}</CardTitle>
        <CardText class="mx-auto" style="font-size: 20px;">{forecastDate}</CardText>
        <CardImg src={iconImg} style="height: 20rem; width: auto; object-fit: contain; "/>
        
        <CardBody>
            <div>
                <CardTitle style="text-align:center; margin:2rem; font-size:40px; font-style:oblique">{temperature}°C</CardTitle>
            </div>                       
            <CardText>{description}</CardText>
            <CardText>Temperature: {temperature}°C</CardText>
            <p class="blue"> Minimal Temperature: {minTemperature}°C</p>
            <p class="red">Maximum Temperature : {maxTemperature}°C</p>
            <p class="yellow">Humidity: {humidity}% </p>
        </CardBody>
    </div>
</main>

<style>
    @import 'https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css';

    .blue{
        color: #256FFF !important;
    }

    .red{
        color: red !important;
    }

    .yellow{
        color: #FAD5A5 !important;
    }

    .fade-in {
        opacity: 0;
        animation: fadeInAnimation 0.5s forwards; /* 0.5s duration, fade-in on load */
    }

    /* Keyframes for fading in */
    @keyframes fadeInAnimation {
        0% {
            opacity: 0;
        }
        100% {
            opacity: 1;
        }
    }

    
</style>