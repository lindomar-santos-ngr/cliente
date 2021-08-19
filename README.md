## Documentação de referência
 
###Passos criar imagem
 * mvn clean install  //Cria o jar na pasta target/
 * docker build . -t cliente:0.0.1 
 * docker run -p 8080:9001 cliente:0.0.1       


###Swagger
[Documentação do Swagger](http://localhost:8080/swagger-ui.html)

###Banco em memoria
[Banco em memoria](http://localhost:8080/h2-console)
 * login: sa
 * senha: sa
 
 
###Collections Postman
* ./collectionsPostman/postman_collection.json
