require('dotenv').config();
let express = require('express');
let app = express();
let bodyParser = require('body-parser');
let path = require('path');
let staticPath = express.static(path.join(__dirname,'public'));
const db = require('./models/userData');

var port = process.env.PORT || 8000;

const expressHbs = require('express-handlebars');
app.engine(
    'hbs',
    expressHbs({
      layoutsDir: 'views/layouts/',
      defaultLayout: 'index.hbs',
      extname: 'hbs'
    })
  );

app.set('view engine', 'hbs');
app.set('views', 'views');

app.use(bodyParser.urlencoded({extended:false}));
app.use(bodyParser.json());
app.use(staticPath);

app.get('/', (req,res) => {
    db.get().then(data => res.render('eachUser', { user: data}));
    
});

app.listen(port, function() {
	console.log('Listening on http://localhost:' + port);
});