## Settings for deploying as Azure App Service

### Application settings

| Environment variable | value |
| --- | --- |
| `QUARKUS_HTTP_PORT` | `80` |
| `QUARKUS_MONGODB_CONNECTION_STRING` | `mongodb+srv://<dbuser>:<password>@<clustername>/<db>`
| `SENDGRID_APIKEY` | `XXXXXXX`|
| `SMS4_PASSWORD` | `XXXX` |

When using mongodb atlas ensure that all outgoing ip addresses are whitelisted.
