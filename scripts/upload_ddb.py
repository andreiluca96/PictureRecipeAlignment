import argparse
import json
import sys
from multiprocessing import Process

import boto3
import botocore

from boto3.dynamodb.conditions import Key
from botocore.exceptions import ClientError

import os, time
from dateutil import tz

if os.name == 'nt':
    def _naive_is_dst(self, dt):
        timestamp = tz.tz._datetime_to_timestamp(dt)
        # workaround the bug of negative offset UTC prob
        if timestamp + time.timezone < 0:
            current_time = timestamp + time.timezone + 31536000
        else:
            current_time = timestamp + time.timezone
        return time.localtime(current_time).tm_isdst
tz.tzlocal._naive_is_dst = _naive_is_dst


def is_table_already_created(table):
    try:
        is_table_existing = table.table_status in ("CREATING", "UPDATING",
                                                   "DELETING", "ACTIVE")
        return True
    except ClientError:
        return False


def create_recipe_table():
    table = dynamodb.Table('Recipes')
    if is_table_already_created(table):
        print("Table already exists")
        return table
    else:
        print("Creating table")
        return dynamodb.create_table(
            TableName='Recipes',
            AttributeDefinitions=[
                {
                    'AttributeName': 'id',
                    'AttributeType': 'S'
                },
                {
                    'AttributeName': 'strip',
                    'AttributeType': 'S'
                }
            ],
            KeySchema=[
                {
                    'AttributeName': 'id',
                    'KeyType': 'HASH'  # Partition key
                }
            ],
            GlobalSecondaryIndexes=[
                {
                    'IndexName': 'part-index',
                    'KeySchema': [
                        {
                            'AttributeName': 'strip',
                            'KeyType': 'HASH'
                        }
                    ],
                    'Projection': {
                        'ProjectionType': 'ALL'
                    },
                    'ProvisionedThroughput': {
                        'ReadCapacityUnits': 123,
                        'WriteCapacityUnits': 123
                    }
                }
            ],
            ProvisionedThroughput={
                'ReadCapacityUnits': 1,
                'WriteCapacityUnits': 1
            }
        )


def write_data_to_table_batch(table):
    with open("../data/layer1.json") as json_file:
        print("Opened json file")
        recipes = json.load(json_file)
        print("Loaded json file")
        no_recipes = len(recipes)
        print("%d recipes to upload" % no_recipes)
        with table.batch_writer() as batch:
            for index, item in enumerate(recipes):
                preprocess_recipe(item)
                batch.put_item(Item=item)
                if index % 1000 == 0:
                    print("%f%% done, reached %d" % ((index / no_recipes) * 100, index))


def write_data_to_table_parallel():
    no_processes = 10
    with open("../data/layer1.json") as json_file:
        print("Opened json file")
        recipes = json.load(json_file)
        print("Loaded json file")
        no_recipes = len(recipes)
        batch_size = no_recipes // no_processes
        processes = [Process(target=put_items,
                             args=(
                                 index, recipes[start:start + batch_size]))
                     for (index, start) in enumerate(range(0, no_recipes, batch_size))]
        for p in processes:
            p.start()
        for p in processes:
            p.join()


def put_items(index, recipes):
    dynamodb = boto3.resource('dynamodb', region_name="us-west-2", endpoint_url="http://localhost:8000")
    table = dynamodb.Table('Recipes')

    no_recipes = len(recipes)
    for i, recipe in enumerate(recipes):
        preprocess_recipe(recipe)
        try:
            table.put_item(Item=recipe, ConditionExpression="attribute_not_exists(id)")
        except Exception:
            print("%s already exists" % recipe['id'])

        if i % 100 == 0:
            print("Process %d: %f%% done" % (index, (i / no_recipes) * 100))


def preprocess_recipe(recipe):
    # TODO: inject crypto stuff here
    recipe['strip'] = recipe.pop('partition')


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Uploads recipes to dynamodb')
    parser.add_argument('-pk', '--aws-access-key', help='AWS server public key')
    parser.add_argument('-sk', '--aws-secret-access-key', help='AWS server secret key')
    parser.add_argument('-r', '--region', help='AWS region name', required=True)
    parser.add_argument('-e', '--endpoint-url',
                        help='AWS DynamoDB endpoint url, use http://localhost:8000 for local DynamoDB')

    parse_result = parser.parse_args(sys.argv[1:])

    if parse_result.aws_access_key is not None and parse_result.aws_secret_access_key is not None:
        session = boto3.Session(
            aws_access_key_id=parse_result.aws_access_key,
            aws_secret_access_key=parse_result.aws_secret_access_key,
        )
        dynamodb = session.resource('dynamodb', region_name=parse_result.region, endpoint_url=parse_result.endpoint_url)
    else:
        dynamodb = boto3.resource('dynamodb', region_name=parse_result.region, endpoint_url=parse_result.endpoint_url)

    recipe_table = create_recipe_table()
    write_data_to_table_batch(recipe_table)
