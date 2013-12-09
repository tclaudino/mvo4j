// Carrega o modulo 'http' para criar um servidor http
var http = require('http');

// Configura o servidor HTTP para responder 'Hello Node' para todas as requisições
var server = http.createServer(function (request, response) {
  response.writeHead(200, {"Content-Type": "text/plain"});
  response.end("Hello Node\n");
});

// Escutando (listen) a porta 8080 para IP padrão 127.0.0.1
server.listen(8080);

// Escrevendo uma mensagem no terminal
console.log("Servidor rodando em http://127.0.0.1:8080/");

