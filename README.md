# A simple backend for Othello

Includes:
- an API for the React-based browser frontend
- game class for comparing computer players
- a common library including some basic computer players

The project is packaged using `gradle` and uses `Java 11`.

## Running the backed

```gradle api:bootRun```

The server will be listening on port `9000`.

Check the `api/app.log` file for detailed logs. 

## Running the tests

```gradle test```

During the first run, `gradle` will download all the dependencies, so it will take a couple of minutes.
