#!/bin/bash

set -e

echo "🚀 Running LocalStack init script..."

awslocal dynamodb create-table \
  --table-name banking-entities \
  --attribute-definitions \
    AttributeName=partitionKey,AttributeType=S \
    AttributeName=sortKey,AttributeType=S \
  --key-schema \
    AttributeName=partitionKey,KeyType=HASH \
    AttributeName=sortKey,KeyType=RANGE \
  --billing-mode PAY_PER_REQUEST

echo "✅ DynamoDB table created with GSI: banking-entities"

awslocal sns create-topic \
  --name account-holder-created

echo "✅ SNS topic created: account-holder-created"