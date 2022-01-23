# Weather

A simple app to demonstrate pair-programming over JetBrain's [CodeWithMe](https://www.jetbrains.com/code-with-me/) plugin

## The plan

### Design
We are keeping it simple! The plan is to recreate the following UI (courtesy of [Muhammad Noufal](https://dribbble.com/muhammad_noufal))
<img src="https://cdn.dribbble.com/users/3650618/screenshots/14328625/media/3d782315cfb915bbe367f32550cd6cfc.png?compress=1&resize=1600x1200"/>

### Data
We will be consuming data straight from OpenWeatherMap API. For this particular screen we will only need [geo endpoint](https://openweathermap.org/current#geo)
API key can be found in `app/build.gradle.kts` already configured as `API_KEY`

### Architecture 
Whatever your ❤️ desire!

### Task breakdown
1. Create a web service to integrate with OpenWeatherMap API
2. Create UI as specified by the design
3. Get the data from the webservice and display on the UI
