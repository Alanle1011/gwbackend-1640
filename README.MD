run in terminal

```angular2html
docker compose up
```

change Active Profiles in Project configuration to 
`
dev, local
`


then run the application in file
`
Backend1640Application
`




Run NGROK to export the application api on WINDOWS

1. Download Ngrok from https://ngrok.com/download 
2. Create an Account to get key
3. run ngrok.exe

![img_1.png](img_1.png)

3. add to token 

`ngrok config add-authtoken <token>`

4. Put your webservice online 

`ngrok http 5001`