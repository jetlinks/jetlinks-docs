FROM nginx
ADD .vuepress/dist /usr/share/nginx/html
# ADD .vuepress/dist/dev-guide /usr/share/nginx/html/dev-guide
# ADD .vuepress/dist/basics-guide /usr/share/nginx/html/basics-guide
# ADD .vuepress/dist/advancement-guide /usr/share/nginx/html/advancement-guide
# ADD .vuepress/dist/common-problems /usr/share/nginx/html/common-problems
# ADD .vuepress/dist/interface-guide /usr/share/nginx/html/interface-guide
# ADD .vuepress/dist/best-practices /usr/share/nginx/html/best-practices
# ADD .vuepress/dist/install-deployment /usr/share/nginx/html/install-deployment
# ADD .vuepress/dist/quick-start /usr/share/nginx/html/quick-start
# ADD .vuepress/dist/big-screen /usr/share/nginx/html/big-screen
# ADD .vuepress/dist/404.html /usr/share/nginx/html/
# ADD .vuepress/dist/index.html /usr/share/nginx/html/
# ADD .vuepress/dist/protocol /usr/share/nginx/html/protocol
ADD docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

ENTRYPOINT ["sh","docker-entrypoint.sh"]
