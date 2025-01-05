resource "aws_dynamodb_table" "banking_entities" {
  name           = var.banking_entities_table_name
  billing_mode   = var.banking_entities_billing_mode
  read_capacity  = var.banking_entities_read_capacity
  write_capacity = var.banking_entities_write_capacity

  hash_key  = "partitionKey"
  range_key = "sortKey"

  attribute {
    name = "partitionKey"
    type = "S"
  }

  attribute {
    name = "sortKey"
    type = "S"
  }

  tags = var.tags
}
