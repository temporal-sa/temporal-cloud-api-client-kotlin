## Temporal Cloud Ops API Usage Example

[Temporal Cloud Ops API](https://docs.temporal.io/ops)

Uses [Temporal's API Protobufs](https://github.com/temporalio/api-cloud) and a Gradle plugin to compile at build time.

### Running the example:

* Create an API key in Temporal Cloud first. See the API docs above for instructions.
* Set a `TEMPORAL_CLOUD_API_KEY` environment variable to your API key.
* To create a new namespace, set a `TEMPORAL_CA_CERT_CONTENTS` to your CA certificate contents.
  * You can trigger a namespace creation by appending `/create-namespace` to your url
  * See `createNamespace()` in `controllers/TestController.kt` for defaults used to create the namespace
* To use the set permissions links for users
  * Set a `TEMPORAL_NAMESPACE` environment variable to the namespace you want to set permissions for
* Run the server:
```
./gradlew -q bootRun -PmainClass=com.example.demo.DemoApplicationKt
```

Then visit `http://localhost:8080/` to see a list of users and namespaces for your Temporal Cloud account.