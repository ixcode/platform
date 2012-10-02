heroku login
heroku create myapp --buildpack https://github.com/jimbarritt/heroku-ibx.git

heroku config:add IX_CLOAK_USER=test-user
heroku config:add IX_CLOAK_PASSWORD=sw33t



heroku addons:add mongohq:free

git push heroku master
