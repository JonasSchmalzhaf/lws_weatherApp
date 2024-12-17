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
    const response = await fetch(`http://localhost:8080/api/weather/${encodedCity}?days=5`);
    const data = await response.json();
    console.log('API response data:', data); // Überprüfe die Datenstruktur hier
    return data;
  }
      
  function getCardClass(index) {
    if (index === 1) {
      return 'col-12 single-card'; // Eine Karte: zentrieren
    } else if (index === 2) {
      return 'col-6 col-sm-6 col-md-6 col-lg-6 single-card'; // Zwei Karten nebeneinander
    } else if (index === 3) {
      return 'col-4 col-sm-4 col-md-4 col-lg-4 single-card'; // Drei Karten nebeneinander
    } else if (index === 4) {
      return 'col-6 col-sm-6 col-md-6 col-lg-6 double-card'; // Vier Karten, je zwei nebeneinander
    } else {
      return 'col-4 col-sm-4 col-md-4 col-lg-4 double-card'; // Mehr als vier Karten, je vier nebeneinander
    }
  }

  function updateCity() {
    weatherData = [];
    getWeather().then(data => data.forEach(day => weatherData = [...weatherData, day]));
  }

  function updateCards(){

  }

  function chooseDay(){
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

  function getDayTime(string){
    let date = new Date(string);
    const weekFormat = new Intl.DateTimeFormat('de-DE', {weekday: 'long'});
    const monthFormat = new Intl.DateTimeFormat('de-DE', {day: '2-digit', month: '2-digit', year: 'numeric'});

    const weekDay = weekFormat.format(date);
    const dayAndmonthAndYear = monthFormat.format(date);

    return `${weekDay}, ${dayAndmonthAndYear}`;
  }
</script>

<main>
  <div class="moving-background" style="background-image: url({backgroundURI});">
    <Container>
      <div class="bodyHeader">
        <h1 class="title">Weather App</h1>
        <Container >
          <Form inline>
            <FormGroup>
              <InputGroup size="lg">
                <Input class="bg-dark text-white shadow-none border focus-ring" style="--bs-bg-opacity: 0.5; --bs-focus-ring-color: white;" type="text" id="city" bind:value="{city}" placeholder="Enter city name" on:input={() => updateCity()}/>
              </InputGroup>

              <div class="slidecontainer mt-3">
                <input type="range" min="0" max="4" class="slider" id="days" step="1" bind:value="{days}">
                <p class="fade-in" style="color: white;">{chooseDay()}</p>
              </div>
            </FormGroup>
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
          <Row>
            {#each weatherData as cards, index}
            {#if index > 0 && index <= days}
              <div class="card-wrapper {
              days === 1 ? 'col-12 row-span2 single-card'
              : days === 2 ? 'col-6 col-sm-6 col-md-6 col-lg-6 single-card'
              : days === 3 ? 'col-6 col-sm-6 col-md-6 col-lg-6 single-card'
              : days === 4 ? 'col-6 col-sm-6 col-md-6 col-lg-6 double-card'
              : 'col-12 row-span2 single-card'
            }">
                <WeatherCard
                city={capitalizeFirstLetter(city)}
                forecastDate={getDayTime(cards.forecastDate)}
                description={capitalizeFirstLetter(cards.description)}
                temperature={cards.temperature}
                minTemperature={cards.minTemperatur}
                maxTemperature={cards.maxTemperature}
                humidity={cards.humidity}
                iconCode={cards.iconCode}
              />
              </div>
              {/if}
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
    justify-content: flex-start;
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

  @keyframes fadeInAnimation {
      0% {
          opacity: 0;
      }
      100% {
          opacity: 1;
      }
  }


</style>
