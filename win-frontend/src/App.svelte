<script>
  import WeatherCard from './lib/WeatherCard.svelte';
  import { Container, Row, Input, Col, Form, FormGroup, Image} from '@sveltestrap/sveltestrap';

  let days = 0; 
  let city = '';
  let weatherData = [];
  let error = null;

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
 
  function capitalizeFirstLetter(string) {
  return string.charAt(0).toUpperCase() + string.slice(1)
}

  function getDayTime(string){
    let date = new Date(string);
    const weekFormat = new Intl.DateTimeFormat('de-DE', {weekday: 'long'});
    const monthFormat = new Intl.DateTimeFormat('de-DE', {month: '2-digit', year: 'numeric'});

    const weekDay = weekFormat.format(date);
    const monthAndYear = monthFormat.format(date);

    return `${weekDay}, ${monthAndYear}`;
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
                <input type="range" min="0" max="4" class="slider" id="days" step="1" bind:value="{days}">
                <p class="fade-in">{chooseDay()}</p>
              </div>
            </FormGroup>
          </Form>
        </Container>
      </div>
      <Container class="bodyBody mb-4">
        {#if weatherData.length > 0}
          <Row>
            {#each weatherData as cards, index}
            {#if index > 0 && index <= days}
              <div class="card-wrapper {getCardClass(index)}">
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

  .moving-background {
    display: flex;
    flex-direction: column;
    justify-content: flex-start; 
    align-items: center;
    width: 100%;
    min-height: 100vh; 
    background-image: url('/src/assets/background.jpg');
    background-size: cover;
    background-position: center center;
    animation: moveBackground 30s linear infinite;
    z-index: -1; 
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

  .single-card {
    display: flex;
    justify-content: center;
  }
</style>
