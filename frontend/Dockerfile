FROM node:20-alpine AS build
WORKDIR /app
COPY ./*.json .
RUN npm install -g @angular/cli
RUN npm ci

EXPOSE 4200
CMD [ "ng", "serve", "--host", "0.0.0.0"]