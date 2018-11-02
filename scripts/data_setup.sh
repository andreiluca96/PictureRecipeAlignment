#!/usr/bin/env bash
wget "http://data.csail.mit.edu/im2recipe/recipe1M_layers.tar.gz" -P ../data
tar -xvzf recipe1M_layers.tar.gz
rm recipe1M_layers.tar.gz
cd ../scripts
nohup python3 upload_ddb.py --region="us-east-1" &