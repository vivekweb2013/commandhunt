FROM nginx:stable-alpine
EXPOSE 3000
COPY ./app/nginx/default.conf /etc/nginx/conf.d/default.conf
COPY ./app/webapp /usr/share/nginx/html