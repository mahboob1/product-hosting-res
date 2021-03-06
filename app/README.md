**JSK-based service** generated by **Intuit JSK Microservice Initializr**
###  Preparing to interact with the service on your local machine in HTTPS mode
A web service that enforces tickets will require an intuit "Authorization" header, which is usually supplied by API
Gateway. If a browser (or another client) makes a request to a service with cookie or OAUTH credentials, API Gateway
will normalize them to an Intuit standard "Authorization" header. Since we don't run API Gateway locally we configured
[default application properties](src/main/resources/application-default.properties) to do the following:
1. Check cookies in addition to "Authorization" header.
2. Start on port 8443 as an HTTPS service in order to consume Intuit auth cookies since they are marked **secure**.
3. Reference a provided self-signed certificate for the HTTPS connection

#### Editing /etc/hosts
You will have to take an additional action to edit your /etc/hosts file so that your local service runs in the intuit.com
domain so that your Intuit cookies will be forwarded by the browser.
Open your **/etc/hosts** file by running this command:

```
sudo <your favorite editor> /etc/hosts

```

Your **/etc/hosts** file should look something like this:

```
#
# Host Database
#
# localhost is used to configure the loopback interface
# when the system is booting.  Do not change this entry.
##
127.0.0.1	localhost
255.255.255.255	broadcasthost
::1             localhost
127.0.0.1	localhost.intuit.com
::1             localhost.intuit.com

```

To build and run, execute the following from the project directory:
	
       mvn spring-boot:run -s settings.xml

To build and run in Kubernetes environment, please refer to [this document](KUBERNETESREADME.md)
	
If you are running in **HTTPS mode** locally, you need an extra step:

1. In your favorite browser navigate to **https://localhost.intuit.com:8443/v4/graphql**
2. Accept the self-signed certificate that the site presents
3. Notice that you are presented with a **401 Unauthorized** error.
4. In the same browser navigate to an e2e page that allows login: **https://devportal-e2e.intuit.com**
5. Log in with your corp credentials
6. In the same browser navigate to **https://localhost.intuit.com:8443/v4/graphql** again
7. Any command line tools such as **curl** need to be invoked in a way to handle **self-signed certificate**. For instance **curl** needs to be invoked with option **-k**
    
### Interacting with the service

1. Via **BATCH API**, from the command line execute the following:
    
    ```
	  curl -k -X POST -H "Content-Type: application/json" -d '[{"$type": "/Query", "type": "/samples/definitions/SampleGreeting"}]' https://localhost:8443/v4/entities
	  
    ```  

2. Via **GRAPHQL API** point your browser to **https://localhost:8443/v4/graphql**, which will bring up an interactive **GraphQL** console 
  
3. Execute the following query in the console:
    
	```
	{
	  sample {
		greetings {
		  edges {
			node {
			  id
			  greetingMessage
			}
		  }
		}
	  }
	}
	```
		
   It will return all greetings:
	   
	```
		{
		  "data": {
			"sample": {
			  "greetings": {
				"edges": [
				  {
					"node": {
					  "greetingMessage": "Greeting # 4",
					  "id": "djQuMToxMjM0NTY3ODkwOjgyZWJkNzI2NjA:fe732f715cd8408c9a24b8e2770efe14"
					}
				  },
				  {
					"node": {
					  "greetingMessage": "Greeting # 8",
					  "id": "djQuMToxMjM0NTY3ODkwOjgyZWJkNzI2NjA:3475c63781934e129495a7c0ab094cf2"
					}
				  },
				  {
					"node": {
					  "greetingMessage": "Greeting # 0",
					  "id": "djQuMToxMjM0NTY3ODkwOjgyZWJkNzI2NjA:789d7bd3d2cf455f8fefec8fcfea7fa3"
					}
				  },
				  {
					"node": {
					  "greetingMessage": "Greeting # 3",
					  "id": "djQuMToxMjM0NTY3ODkwOjgyZWJkNzI2NjA:9fef156a77f94c11af8012945eca6e3e"
					}
          },
	```	

4. Grab one of the Greeting entity ids, e.g. **"id": "djQuMToxMjM0NTY3ODkwOjgyZWJkNzI2NjA:9fef156a77f94c11af8012945eca6e3e"**	

5. Use it via **REST API**: https://localhost:8443/v4/samples/djQuMToxMjM0NTY3ODkwOjgyZWJkNzI2NjA:9fef156a77f94c11af8012945eca6e3e

### How to call your deployed service

If your service is deployed in **IKS** or **AWS EC2**, you'll need to pass an appropriate authorization header when making requests. Here's an example of an authorization header:

```
Intuit_IAM_Authentication intuit_token_type=IAM-Ticket,intuit_appid=CLIENT_APP_ID,intuit_app_secret=CLIENT_APP_SECRET,intuit_userid=SOME_USER_ID,intuit_realmid=SOME_REALM_ID,intuit_token=SOME_USER_TICKET
```
**CLIENT\_APP\_ID** - Application ID of a client calling your service

**CLIENT\_APP\_SECRET** - Application Secret of a client calling your service

**SOME\_USER\_ID** - User ID of a person calling your service

**SOME\_USER\_TICKET** - User token of a person calling your service

For more info, please refer to [this](https://devportal.intuit.com/a1/index.html?legacy#/main/cookbook/cookbook/?page=955_Appendix_Sending_Authenticated_Requests%2FSending_a_Request_Using_PrivateAuth)


### How to configure JSK component(s) in your service

1. [JSK Spring Security](https://github.intuit.com/java-service-kit/jsk-spring-security) 

2. [JSK Health Check](https://github.intuit.com/java-service-kit/jsk-servlet-health)

3. [JSK Last Mile Filter](https://github.intuit.com/java-service-kit/jsk-servlet-gateway-lastmile-filter/tree/master)

#### Note about included certificate

The packaged certificate is OK for local development. However, we encourage you to use Intuit CA-signed or generate your own self-signed certificate for **TLS** termination
