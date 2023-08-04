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

server.listen(8081,'192.168.1.152', () => {
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
      io.emit("startMatch",true);
     
    
  })
    socket.on('Imena', () => {
      const person = {a:aime, b:bime};
      io.emit("podaci",JSON.stringify(person)); 
     
    
  })
   socket.on('turn', () => {

    if(socket.id == a)
    {
      io.to(b).emit("changeturn");
    }
    if(socket.id == b)
    {
      io.to(a).emit("changeturn");
    }
     
    
  })
    socket.on('open', (ab) => {

        if(socket.id == a)
        {
            io.to(b).emit("opens",ab);
        }
        if(socket.id == b)
        {
            io.to(a).emit("opens",ab);
        }


    })
    socket.on('openColumn', (ab) => {

        if(socket.id == a)
        {
            io.to(b).emit("opencs",ab);
        }
        if(socket.id == b)
        {
            io.to(a).emit("opencs",ab);
        }


    })

    socket.on('Pobeda', (ab) => {

        if(socket.id == a)
        {
            console.log("a");
            io.to(b).emit("ennemywin");
        }
        if(socket.id == b)
        {
            io.to(a).emit("ennemywin");
        }


    })
    socket.on('enemyguess', (ab) => {

        if(socket.id == a)
        {
            console.log("a");
            io.to(b).emit("enemyguessc",ab);
        }
        if(socket.id == b)
        {
            io.to(a).emit("enemyguessc",ab);
        }


    })
    socket.on('points', (ab) => {

        if(socket.id == a)
        {
           // console.log("a");
            io.to(b).emit("pointsc");
        }
        if(socket.id == b)
        {
            io.to(a).emit("pointsc");
        }


    })
    socket.on('nextgamec', (ab) => {

        if(socket.id == a)
        {
            console.log("a");
            io.to(b).emit("nextgamecc");
        }
        if(socket.id == b)
        {
            io.to(a).emit("nextgamecc");
        }


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