<script>
  import WeatherCard from './lib/WeatherCard.svelte';
  import { Container, Row, Input, Col, Form, FormGroup, Image} from '@sveltestrap/sveltestrap';

  let days = 0; 
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
            {#each weatherData as day, index}
              {#if index > 0}
                <WeatherCard
                  city={capitalizeFirstLetter(city)}
                  forecastDate={getDayTime(day.forecastDate)}
                  description={capitalizeFirstLetter(day.description)}
                  temperature={day.temperature}
                  minTemperature={day.minTemperatur}
                  maxTemperature={day.maxTemperature}
                  humidity={day.humidity}
                  iconCode={day.iconCode}
                />
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
      text-align: center;
  }
</style>
