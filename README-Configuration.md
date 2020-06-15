## Settings for running the backend

### Application settings

| Environment variable | value |
| --- | --- |
| `QUARKUS_HTTP_PORT` | `80` |
| `QUARKUS_MONGODB_CONNECTION_STRING` | `mongodb+srv://<dbuser>:<password>@<clustername>/<db>`
| `QUARKUS_HTTP_CORS_ORIGINS`| Origin of frontend (e.g. `https://foo.com`)
| `SENDGRID_APIKEY` | `XXXXXXX`|
| `SMS4_PASSWORD` | `XXXX` |

When using mongodb atlas ensure that all outgoing ip addresses are whitelisted.
