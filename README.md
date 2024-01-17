## Temporal Cloud Ops API Usage Example

[Temporal Cloud Ops API Docs](https://docs.temporal.io/ops)

Note, requires creating an API key in Temporal Cloud first. See the docs above.

Uses protobufs from https://github.com/temporalio/api-cloud -- and a gradle plugin to compile them at build time.

### Running the example:

First, set the TEMPORAL_CLOUD_API_KEY environment variable to your API key.

```
./gradlew -q bootRun -PmainClass=com.example.demo.DemoApplicationKt
```

Then visit `http://localhost:8080/` to see a list of users for your Temporal Cloud account.