This is a Spring Boot application that integrates with a third-party API to retrieve weather data. The application allow users to search for weather data by zip code or city name.

1. Get Weather by City Name:
You can fetch weather data for a specific city by sending a GET request in the following format: /weather/city/{cityName}. Replace {cityName} with the name of the actual city.

For example, if users want to get weather data for London, they would navigate to /weather/city/London in their web browser. Alternatively, they could use a REST client like Postman to send the GET request.

The server will return weather data for the specified city in the response body. If no data for the specified city is found, it will return a 404 Not Found HTTP status code.

2. Get Weather by Country and Zip Code:
Users can fetch weather data for a specific zip code within a country by sending a GET request in the following format: /weather/zipcode/{countryCode}/{zipCode}. Replace {countryCode} and {zipCode} with the respective country and zip code.

For example, if users want to get weather data for the zip code 90210 in the United States, they would navigate to /weather/zipcode/US/90210 in their web browser. Alternatively, they could use a REST client like Postman to send the GET request.

The server will return weather data in the response body. If no data for the specified zip code is found, it will return a 404 Not Found HTTP status code.


Feedback:
1. Was it easy to complete the task using AI?
- Yes, it was much easier.
2. How long did task take you to complete?
- I spent 4 hours to complete this task.
3. Was the code ready to run after generation? What did you have to change to make it usable?
- Only code for tests had little mistakes.
4. Which challenges did you face during completion of the task?
- There were no real challenges.
5. Which specific prompts you learned as a good practice to complete the task?
- I didn't use any specific prompts. All my prompts were pretty simple.