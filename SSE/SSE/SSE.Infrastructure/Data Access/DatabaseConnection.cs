using Microsoft.WindowsAzure.Storage;
using System;
using System.Collections.Generic;
using System.Text;

namespace SSE.Infrastructure.Data_Access
{
    class DatabaseConnection
    {
        var connectionString = "DefaultEndpointsProtocol=https;AccountName=b370aca5-0ee0-4-231-b9ee;AccountKey=pC7auBqBQekf9ycuBSaVk9xVlOxuNs1vBpvIZBg7ygKUZK9tKAEDut5fEKDJmFpZ5BVg8aBlrJpF5j6hEKnUcA==;TableEndpoint=https://b370aca5-0ee0-4-231-b9ee.table.cosmosdb.azure.com:443/;";
        CloudStorageAccount storageAccount = CloudStorageAccount.Parse(connectionString);
        CloudTableClient tableClient = storageAccount.CreateCloudTableClient();
    }
}
