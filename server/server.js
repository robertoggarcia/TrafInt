// --- Variables ---

var express = require('express'),
	app = express();

// -- Secure Variables ---
/*
var fs = require('fs');
var options = {
  key: fs.readFileSync('/etc/letsencrypt/live/"domain"/privkey.pem'),
  cert: fs.readFileSync('/etc/letsencrypt/live/"domain"/cert.pem')
};
var server = require('https').createServer(options, app);
*/
var server = require('http').createServer(app);

var io = require('socket.io').listen(server);

// --- Mongo variables ---

var MongoClient = require('mongodb').MongoClient;
var urlDatabase = "mongodb://localhost:27017/traffic";

var	data_users = [];



// --- Settings ---
app.use(express.static(__dirname + '/'));

server.listen(process.env.PORT || 3001);
console.log('Server Running...');


// --- routing ---
app.get('/traffic/', function(req, res){
	res.sendFile(__dirname + '/datos.html');
});

app.get('/traffic/ubicacion', function(req, res){
	res.sendFile(__dirname + '/ubicacion.html');
});

// --- client connection ---
io.sockets.on('connection', function(socket){
	console.log('Socket Connected...');

	// Send Message
	socket.on('data', function(data){
		console.log(data);
		io.sockets.emit('location', data);
		//{lat:data.latitude, lon:data.longitude}
	});
	
});

// == TODO: data base connet
/*
MongoClient.connect(urlDatabase, function(err, db) {
  if(!err) {
    var collection = db.collection('data');
	//TODO save(data);
	db.close();
  } else {
  	console.log("Error, try to connect to database: ",err);
  }
});*/	