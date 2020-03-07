let docClient = require('../db/db');

var params = {
    TableName: 'User-cpca4bqpgnbbndqfqxnzyf5hly-dev',
    Item: {
        'id': "789",
        'name': 'lol'
      }
   };

function putTest(){
    docClient.put(params, function(err, data) {
        if (err) {
            console.log("Error", err);
        } else {
            console.log("Success", data);
        }
    });
}

module.exports = {
    put : putTest,
}