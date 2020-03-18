FROM nginx
ADD .vuepress/dist/assets /usr/share/nginx/html/assets
ADD .vuepress/dist/dev-guide /usr/share/nginx/html/dev-guide
ADD .vuepress/dist/basics-guide /usr/share/nginx/html/basics-guide
ADD .vuepress/dist/advancement-guide /usr/share/nginx/html/advancement-guide
ADD .vuepress/dist/common-problems /usr/share/nginx/html/common-problems
ADD .vuepress/dist/interface-guide /usr/share/nginx/html/interface-guide
ADD .vuepress/dist/best-practices /usr/share/nginx/html/best-practices
ADD .vuepress/dist/install-deployment /usr/share/nginx/html/install-deployment
ADD .vuepress/dist/quick-start /usr/share/nginx/html/quick-start
ADD .vuepress/dist/404.html /usr/share/nginx/html/
ADD .vuepress/dist/index.html /usr/share/nginx/html/
ADD docker-entrypoint.sh /docker-entrypoint.sh

CMD ["sh","docker-entrypoint.sh"]
