#!/bin/bash

set -e

echo "🚀 Running LocalStack init script..."

awslocal dynamodb create-table \
  --table-name banking-entities \
  --attribute-definitions \
    AttributeName=partitionKey,AttributeType=S \
    AttributeName=sortKey,AttributeType=S \
    AttributeName=iban,AttributeType=S \
  --key-schema \
    AttributeName=partitionKey,KeyType=HASH \
    AttributeName=sortKey,KeyType=RANGE \
  --billing-mode PAY_PER_REQUEST \
  --global-secondary-indexes \
    '[
      {
        "IndexName": "bank-account-iban-gsi",
        "KeySchema": [
          { "AttributeName": "iban", "KeyType": "HASH" }
        ],
        "Projection": {
          "ProjectionType": "ALL"
        }
      }
    ]'

echo "✅ DynamoDB table created with GSI: banking-entities"

awslocal sns create-topic \
  --name account-holder-created

echo "✅ SNS topic created: account-holder-created"