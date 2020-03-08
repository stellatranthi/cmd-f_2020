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
    db.get().then(data => {
      let countMissed = 0;
      let countUrgent = 0;
      let total = 0;

      data.forEach(function(user){
        if(user.misseddates == 4){
          countMissed += 1
        }
        if(user.responsetype == "urgent"){
          countUrgent += 1;
        }
        total += 1;
      });

      statOne = {"count": countMissed, "total": total}
      res.render('eachUser', { user: data, data1: statOne, data2: countUrgent, data3: total})
    });
});

app.listen(port, function() {
	console.log('Listening on http://localhost:' + port);
});