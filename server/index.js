let a,b;
let aime = 1,bime = 1;


const express = require('express');
const app = express();
const http = require('http');
const socket = require('socket.io');
const server = http.createServer(app);
const io = socket(server);
app.get('/', (req, res) => {
  res.send('<h1>Hello world</h1>');
});

server.listen(4001,'192.168.0.12', () => {
  console.log('listening on *:3000');
});


io.on('connection', (socket) => {
    console.log("New socket connection: " + socket.id);
    if(a == null )
    {
        a = socket.id;
        io.to(socket.id).emit("pleyer1",true);
        console.log("peyer1");
    }
    else{
      if(b == null){
        b = socket.id;
        io.to(socket.id).emit("pleyer2",true);
        console.log("peyer2");
       
      }
    }
    socket.on('start', () => {
      io.to(socket.id).emit("startMatch",true);
     
    
  })
    socket.on('Imena', () => {
      const person = {a:aime, b:bime};
      io.emit("podaci",JSON.stringify(person)); 
     
    
  })
   
    socket.on('Ime', (zika) => {
       
      if(a != null && aime == 1)
    {
      aime = zika;
    }
    else{
      if(b != null){
       bime = zika;
      }
    }
  })
    socket.on('reset', () => {
       
        a= null;
        b= null;
    })
})