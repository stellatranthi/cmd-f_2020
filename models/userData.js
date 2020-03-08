let docClient = require('../db/db');

function putTest(){
    var params = {
        TableName: 'User-cpca4bqpgnbbndqfqxnzyf5hly-dev',
        Item: {
            'id': "789",
            'name': 'lol'
          }
    };
    docClient.put(params, function(err, data) {
        if (err) {
            console.log("Error", err);
        } else {
            console.log("Success", data);
        }
    });
}

function getTest(){
    var params = {
        TableName: 'User-cpca4bqpgnbbndqfqxnzyf5hly-dev',
    };

    return new Promise(function (resolve, reject) {
        docClient.scan(params, function(err, data) {
            if (err) {
                return reject(err);
            } 
            return resolve(data.Items)
        });
    });
}

module.exports = {
    put : putTest,
    get : getTest
}