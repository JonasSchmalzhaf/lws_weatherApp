<script>
  import WeatherCard from './lib/WeatherCard.svelte';
  import Header from './lib/Header.svelte';
  import { Container, Row, Input, Form, FormGroup, Image, InputGroup} from '@sveltestrap/sveltestrap';
  import ClearNightBG from './assets/Backgrounds/clear-night.jpg';
  import DarkCloudBG from './assets/Backgrounds/dark-cloud.jpg';
  import FewCloudsBG from './assets/Backgrounds/few-clouds.jpg';
  import RainyMoonBG from './assets/Backgrounds/rainy-moon.jpg';
  import RainyBG from './assets/Backgrounds/rainy.jpg';
  import SnowyBG from './assets/Backgrounds/snowy.jpg';
  import SunnyBG from './assets/Backgrounds/sunny.jpg';
  import ThunderstormBG from './assets/Backgrounds/thunderstorm.jpg';
  import MistBG from './assets/Backgrounds/mist.jpg';

  let days = 0; // Anzahl der Tage, für die Wetterdaten angezeigt werden
  let city = "";
  let weatherData = [];
  let backgroundURI = SunnyBG;

  async function getWeather() {
    const encodedCity = encodeURIComponent(city);
    console.log(encodedCity);
    const response = await fetch(`http://localhost:8080/api/weather/${encodedCity}?days=${days}`);
    console.log(response);
    const data = await response.json();
    console.log('API response data:', data); // Überprüfe die Datenstruktur hier
    return data;
  }
      

  function updateCity() {
    weatherData = [];
    getWeather().then(data => data.forEach(day => weatherData = [...weatherData, day]));
  }

  function chooseDay(days){
    switch(days){
      case 0: return  "Heute";
      break;
      case 1: return  "Morgen";
      break;
      case 2: return  "Übermorgen";
      break;
      case 3: return  "Überübermorgen";
      break;
      case 4: return  "In vier Tagen";
      break;
      case 5: return  "In fünf Tagen";
      break;
    }

  }

  $: {
    if(weatherData.length > 0) {
      switch(weatherData[0].iconCode) {
        case "01n": 
          backgroundURI = ClearNightBG;
          break;
        case "01d": 
          backgroundURI = SunnyBG;
          break;
        case "02d": 
          backgroundURI = FewCloudsBG;
          break;
        case "02n": 
          backgroundURI = FewCloudsBG;
          break;
        case "03d": 
          backgroundURI = DarkCloudBG;
          break;
        case "03n": 
          backgroundURI = DarkCloudBG;
          break;
        case "04n": 
          backgroundURI = DarkCloudBG;
          break;
        case "04d": 
          backgroundURI = DarkCloudBG;
          break;
        case "09d": 
          backgroundURI = RainyBG;
          break;
        case "09n": 
          backgroundURI = RainyMoonBG;
          break;
        case "10d": 
          backgroundURI = RainyBG;
          break;
        case "10n": 
          backgroundURI = RainyMoonBG;
          break;
        case "11n": 
          backgroundURI = ThunderstormBG;
          break;
        case "11d": 
          backgroundURI = ThunderstormBG;
          break;
        case "13d": 
          backgroundURI = SnowyBG;
          break;
        case "13n": 
          backgroundURI = SnowyBG;
          break;
        case "50n": 
          backgroundURI = MistBG;
          break;
        case "50d": 
          backgroundURI = MistBG;
          break;
        default: 
          backgroundURI = SunnyBG;
          break;
      }
    }
  
  }
 
  function capitalizeFirstLetter(string) {
  return string.charAt(0).toUpperCase() + string.slice(1)
}


</script>

<main>
  <div class="moving-background" style="background-image: url({backgroundURI});">
    <Container>
      <div class="bodyHeader">
        <h1 class="title">Weather App</h1>
        <Container >
          <Form>
            <InputGroup size="lg">
              <Input class="bg-dark text-white shadow-none border focus-ring" style="--bs-bg-opacity: 0.5; --bs-focus-ring-color: white;" type="text" id="city" bind:value="{city}" placeholder="Enter city name" on:input={() => updateCity()}/>
            </InputGroup>

            <div class="slidecontainer mt-3">
              <input type="range" min="0" max="5" class="slider" id="days" step="1" bind:value="{days}" on:input={() => updateCity()}>
              <p class="fade-in">{chooseDay(days)}</p>
            </div>
          </Form>
        </Container>

        <Container>
          {#if weatherData.length > 0}
            <Header
              bind:city={city}
              bind:forecastDate={weatherData[0].forecastDate}
              bind:description={weatherData[0].description}
              bind:temperature={weatherData[0].temperature}
              bind:minTemperature={weatherData[0].minTemperatur}
              bind:maxTemperature={weatherData[0].maxTemperature}
              bind:humidity={weatherData[0].humidity}
              bind:iconCode={weatherData[0].iconCode}
            />
          {/if}
        </Container>
      </div>

      <Container class="bodyBody mb-4">
        {#if weatherData.length > 0}
          <Row cols={{lg: 3, md: 2, sm: 1}}>
            <!-- svelte-ignore block_empty -->
            {#each weatherData as day}
              <!--<Col sm="12" md="4" lg="3" class="d-flex justify-content-center align-items-center">-->
                <WeatherCard
                city={capitalizeFirstLetter(city)}
                forecastDate={day.forecastDate}
                description={day.description}
                temperature={day.temperature}
                minTemperature={day.minTemperatur}
                maxTemperature={day.maxTemperature}
                humidity={day.humidity}
                iconCode={day.iconCode}
                />
              <!-- </Col> -->
            {/each}
          </Row>
        {/if}
      </Container>
    </Container>
  </div>
</main>

<style>
  @import 'https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css';

  .title {
    color: white;
    text-align: center;
  }

  .fade-in {
    opacity: 0;
    animation: fadeInAnimation 0.5s forwards; /* 0.5s duration, fade-in on load */
  }

  .bodyHeader {
    text-align: center;
  }

  Input:focus {
    outline: none;
    border: none;
  }

  .moving-background {
    display: flex;
    flex-direction: column;
    justify-content: flex-start; /* Inhalt oben ausrichten */
    align-items: center;
    width: 100%;
    min-height: 100vh; /* Mindestens die Höhe des Viewports */
    background-color: rgba(0, 0, 0, 0.3);
    background-blend-mode: darken;
    background-size: cover;
    background-position: center center;
    animation: moveBackground 30s linear infinite;
    z-index: -1; /* Hintergrund hinter den anderen Elementen */
  }
</style>
