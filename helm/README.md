## CommandHunt Helm Chart
The helm chat for deploying CommandHunt application on a kubernetes cluster.


### Before you begin
- Create a kubernetes cluster.
- Create a postgres database cluster on cloud platform & if required add the trusted sources to it.
Make sure to also add the kubernetes cluster in the trusted sources.
- Postgres cluster may contain default database created by cloud provider.
If you want you can create a new one with required name.
- Connect to database using pgAdmin and create `commandhunt` schema inside the database.
- Make sure you create the kubernetes cluster and database cluster in the same region to avoid any latency issues.


#### Install ingress controller
```
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
helm install nginx-ingress ingress-nginx/ingress-nginx -n ingress-nginx --create-namespace --set controller.publishService.enabled=true
```

Check if load balancer become available using below command
```
kubectl get svc -o wide nginx-ingress-ingress-nginx-controller -n ingress-nginx
```

**NOTE:** Copy the load balancer's external ip address and point it to A-record using your domain's dns interface.
This needs to be done before installing the application. Verify the updates are available using `nslookup` command.
If not then decrease the TTL of A-record and try `nslookup` again.


#### Install cert-manager
```
helm repo add jetstack https://charts.jetstack.io
helm repo update
helm install cert-manager jetstack/cert-manager -n cert-manager --create-namespace --version v1.2.0 --set installCRDs=true
```

Verify cert-manager pods
```
kubectl get pods -n cert-manager
```


### Install CommandHunt Application

#### Create a namespace for CommandHunt application
```
kubectl create namespace ch
```


#### Create `regcred` secret to allow pulling docker images from the private registry.
```
kubectl create secret generic regcred \
    --from-file=.dockerconfigjson=<path/to/.docker/config.json> \
    --type=kubernetes.io/dockerconfigjson -n ch
```

**NOTE:** Replace `<path/to/.docker/config.json>` token with correct path

To verify the secret, run the below command
```
kubectl get secret regcred -o jsonpath='{.data}' -n ch
```


#### Create `postgres-secret` to allow connecting to postgres database.
```
kubectl create secret generic postgres-secret \
    --from-literal=POSTGRES_USER=<POSTGRES_USER> \
    --from-literal=POSTGRES_PASSWORD=<POSTGRES_PASSWORD> \
    --from-literal=POSTGRES_DB=<POSTGRES_DB> \
    --from-literal=POSTGRES_PORT=<POSTGRES_PORT> \
    --from-literal=POSTGRES_HOST=<POSTGRES_HOST> -n ch
```

**NOTE:** Replace `<POSTGRES_USER>` `<POSTGRES_PASSWORD>` `<POSTGRES_DB>` `<POSTGRES_HOST>` and `<POSTGRES_PORT>` with correct values.

To verify the secret, run the below command
```
kubectl get secret postgres-secret -o jsonpath='{.data}' -n ch
```


#### Create `oauth-api-secret` to allow application to connect to oAuth providers
```
kubectl create secret generic oauth-api-secret \
	--from-literal=GOOGLE_CLIENT_ID=<GOOGLE_CLIENT_ID> \
	--from-literal=GOOGLE_CLIENT_SECRET=<GOOGLE_CLIENT_SECRET> \
	--from-literal=FACEBOOK_CLIENT_ID=<FACEBOOK_CLIENT_ID> \
	--from-literal=FACEBOOK_CLIENT_SECRET=<FACEBOOK_CLIENT_SECRET> \
	--from-literal=GITHUB_CLIENT_ID=<GITHUB_CLIENT_ID> \
	--from-literal=GITHUB_CLIENT_SECRET=<GITHUB_CLIENT_SECRET> \
	--from-literal=APP_JWT_SECRET=<APP_JWT_SECRET> -n=ch
```

**NOTE:** Replace `<GOOGLE_CLIENT_ID>` `<GOOGLE_CLIENT_SECRET>` `<FACEBOOK_CLIENT_ID>` `<FACEBOOK_CLIENT_SECRET>` `<GITHUB_CLIENT_ID>` `<GITHUB_CLIENT_SECRET>` and `<APP_JWT_SECRET>` with correct values.

To verify the secret, run the below command
```
kubectl get secret oauth-api-secret -o jsonpath='{.data}' -n ch
```


#### Install the chart with below command.
```
helm install commandhunt helm -n ch --create-namespace
```
**Note:** Execute this command from the parent directory


#### Verify the installation
Check all the pods are up and running with below command
```
kubectl get pods -n ch
```

Check all the services is in active state
```
kubectl get svc -n ch -o wide
```

Verify Let’s Encrypt certificate status
```
kubectl describe certificate commandhunt-tls -n ch
kubectl describe clusterissuer letsencrypt-prod -n ch
kubectl get certificaterequest -n ch -o wide
kubectl get orders -n ch
kubectl get challenges -n ch
```


#### Point name-servers of your website to cloud provider
Make sure to link the name-servers of your cloud service provider to your domain using the interface provided by your domain registrar.
Then route the incoming requests to load balancer by creating A-record (DNS record) with the interface provided by cloud service provider.

**NOTE:** If you were using an email hosting service previously with the domain,
then you may need to create respective dns records for the email service provider inside your cloud provider.
Otherwise, the incoming service won't work. This is because we have changed the name-servers of domain registrar
with the name-servers of cloud service provider. 
Since the DNS records lives inside name-servers, the old DNS records of domain registrar will no longer used.
So make sure to configure respective dns records after updating name-servers.

You should now be able to access your domain on https with a valid certificate.
