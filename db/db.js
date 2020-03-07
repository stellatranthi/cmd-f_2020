const AWS = require('aws-sdk');

AWS.config.update({region: 'us-west-2'});
var docClient = new AWS.DynamoDB.DocumentClient({
    apiVersion: '2012-08-10',
    accessKeyId: process.env.DB_KEY,
    secretAccessKey: process.env.DB_SECRET
});

module.exports = docClient;