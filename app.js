let express = require('express');
let app = express();
let bodyParser = require('body-parser');
let path = require('path');
let staticPath = express.static(path.join(__dirname,'public'));

var port = process.env.PORT || 8000;

app.use(bodyParser.urlencoded({extended:false}));
app.use(bodyParser.json());

app.get('/', (req,res) => {
    res.sendFile(path.join(__dirname,'views','index.html'));
});

app.listen(port, function() {
	console.log('Listening on http://localhost:' + port);
});