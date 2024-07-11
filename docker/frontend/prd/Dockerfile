FROM node:20-slim as builder
WORKDIR /app
COPY ./frontend .
RUN npm install && npm run build

FROM nginx:stable-alpine3.19-slim
COPY --from=builder /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]