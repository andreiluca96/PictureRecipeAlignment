## Useful scripts

pictures_fetcher - Downloads pictures from dataset into a local directory


❗ You should download from [here](http://data.csail.mit.edu/im2recipe/recipe1M_layers.tar.gz) and provide the script with its path, the directory where to download the data and the number of processes to use.  
For more info call the script with the -h option.


---

upload_ddb - Creates if not existing and writes objects to recipe table in DynamoDB

❗ You should setup aws credentials using [aws cli configure command](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html) or pass them as parameters to the script (for more info call the script with -h option)  
👍 You can run a local version of DynamoDB for testing purposes [here](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.DownloadingAndRunning.html)  
👍Useful commands for dynamodb [aws-cli-dynamodb](https://docs.aws.amazon.com/cli/latest/reference/dynamodb/index.html)
📙 Library used for communication [boto3](https://boto3.amazonaws.com/v1/documentation/api/latest/index.html)  


---

data_setup.sh - Downloads dataset in /data directory