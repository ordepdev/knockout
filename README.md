# Knockout Tournament

A simple command-line tool to run a knockout tournament.

## Usage

To run a tournament, you can use the following command-line options:

| Option | Long Option | Description | Default |
|---|---|---|---|
| `-f` | `--file` | File with contestant names | `resources/names.txt` |
| `-s` | `--seed` | Seed for random number generator | `nil` |
| `-i` | `--interactive` | Run in interactive mode | `false` |
| `-h` | `--help` | Show help | |

To run the tournament with the example names:
```bash
lein run
```

You can also specify a file with contestant names:
```bash
lein run --file resources/names.txt
```

To run in interactive mode:
```bash
lein run --interactive
```

To provide a seed for the random number generator:
```bash
lein run --seed 12345
```

You can combine the options:
```bash
lein run --file resources/names.txt --interactive --seed 12345
```

## Running the Tests

To run the tests, use the `lein test` command:
```bash
lein test
```
