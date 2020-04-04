# Vagrant Deployment

There is a vagrant file available to test packaging and deployment related changes.

To run exchange-rate-service in the vagrant box make sure that the server is packaged properly via

```../exchange-rate-service/server/mvnw package```

After that you may cd to exchange-rate-service-ansible & start the vagrant box via

```vagrant up```

and find the app running on localhost:8080 (as there is a port forward in the vagrant vm)



