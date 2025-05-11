# commodities-trading-analytics-platform

The responsibility of the Commodities-Trading-Analytics service is to provide a means for consumers (services, web, mobile)
to save trade commodity data as well as to retrieve these and other analytics data that the service calculates.

The service can be started by running the main() function inside **src/main/kotlin/Main.kt** in your IDE or alternatively
on the command line if an executable jar file has been built and its endpoints can be accessed at:
_http://localhost:8080/trades_ and _http://localhost:8080/insights_ by using an HTTP client such as curl.

Included is also a bash script (_test_data_generator.sh_ in the root directory) which when ran will populate the
(in-memory) database with some sample trades and will then retrieve and print to the console these trades as well as
some analytics data (aka insights). The script will run on any GNU/Linux OS and _should_ run on MacOS
(assuming bash is installed) and naturally the application needs to have been started and bound to port 8080. The file
permissions may need to be changed in order to run the script. This can be done by running:
`chmod +x test_data_generator.sh` on a GNU/Linux / Unix / MacOs.

As usual, the project dependencies and their versions can be viewed in the build.gradle.kts project file.

Some future improvements might include:
- **Swagger**:
  Ideally, some form of auto-generating online documentation like Swagger would be nice and would communicate consistently
  the way clients can access the endpoints.
- **Security**:
  The endpoints aren't secured so some form of authentication/authorisation scheme will be required (perhaps OAuth)
- **Performance**:
  It might be useful to capture some performance-related requirements (perhaps, driven by the business) and generate
  automated performance tests which will prove that the required SLA's are made and deployments can be carried-out with
  confidence.
