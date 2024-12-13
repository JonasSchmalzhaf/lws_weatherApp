<script>
  import WeatherCard from './lib/WeatherCard.svelte';
  import { Container, Row, Input, Col, Form, FormGroup, Image} from '@sveltestrap/sveltestrap';
  import { onMount } from 'svelte';

  let days = 0; // Anzahl der Tage, für die Wetterdaten angezeigt werden
  let city = '';
  let weatherData = [];
  let error = null;

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
 
  function capitalizeFirstLetter(string) {
  return string.charAt(0).toUpperCase() + string.slice(1)
}


</script>

<main>
  <div class="moving-background">
    <Container>
      <div class="bodyHeader">
        <h1 class="title">Weather App</h1>
        <Container >
          <Form inline>
            <FormGroup>
              <Input
                type="text"
                id="city"
                bind:value="{city}"
                placeholder="Enter city name"
                on:input={() => updateCity()}
              />

              <div class="slidecontainer mt-3">
                <input type="range" min="0" max="5" class="slider" id="days" step="1" bind:value="{days}" on:input={() => updateCity()}>
                <p class="fade-in">{chooseDay(days)}</p>
              </div>
            </FormGroup>
          </Form>
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

  .moving-background {
    display: flex;
    flex-direction: column;
    justify-content: flex-start; /* Inhalt oben ausrichten */
    align-items: center;
    width: 100%;
    min-height: 100vh; /* Mindestens die Höhe des Viewports */
    background-image: url('/src/assets/background.jpg');
    background-size: cover;
    background-position: center center;
    animation: moveBackground 30s linear infinite;
    z-index: -1; /* Hintergrund hinter den anderen Elementen */
  }
  @keyframes moveBackground {
    0% {
      background-position: 0% center;
    }
    100% {
      background-position: 0% center; 
    }
  }

  .title {
      text-align: center;
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
