require('dotenv').config();
let express = require('express');
let app = express();
let bodyParser = require('body-parser');
let path = require('path');
let staticPath = express.static(path.join(__dirname,'public'));
const db = require('./models/userData');

var port = process.env.PORT || 8000;

app.use(bodyParser.urlencoded({extended:false}));
app.use(bodyParser.json());
app.use(staticPath);

app.get('/', (req,res) => {
    db.put();
    res.sendFile(path.join(__dirname,'views','index.html'));
});

app.listen(port, function() {
	console.log('Listening on http://localhost:' + port);
});