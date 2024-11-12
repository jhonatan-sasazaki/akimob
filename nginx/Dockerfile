# Build this image with the parent directory as the build context

# Build the Angular project
FROM node:20-alpine AS build
WORKDIR /app
COPY ./frontend/*.json .
COPY ./frontend/src ./src
RUN npm ci
RUN npm run build

# Build the Nginx image
FROM nginx:alpine
COPY ./nginx/nginx.conf.template /etc/nginx/nginx.conf.template
COPY ./nginx/mime.types /etc/nginx/mime.types

# By default it is set in the .env file and passed as a build argument in the compose file
ARG CERT_FILE
ARG CERT_KEY_FILE
COPY ${CERT_FILE} /etc/nginx/ssl/nginx.crt
COPY ${CERT_KEY_FILE} /etc/nginx/ssl/nginx.key

# Substitute environment variables in the Nginx configuration template
ARG SERVER_NAME
ENV SERVER_NAME=${SERVER_NAME}
RUN envsubst '${SERVER_NAME}' < /etc/nginx/nginx.conf.template > /etc/nginx/nginx.conf

COPY --from=build /app/dist/akimob/browser /usr/share/nginx/html
EXPOSE 80
EXPOSE 443
