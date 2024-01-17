## Temporal Cloud Ops API Usage Example

[Temporal Cloud Ops API Docs](https://docs.temporal.io/ops)

Note, requires creating an API key in Temporal Cloud first. See the docs above.

### Running the example:

First, set the  set the TEMPORAL_CLOUD_API_KEY environment variable to your API key.

```
./gradlew -q bootRun -PmainClass=com.example.demo.DemoApplicationKt
```

Then visit `http://localhost:8080/` to see a list of users for your Temporal Cloud account.