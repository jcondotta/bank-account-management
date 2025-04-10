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

  attribute {
    name = "iban"
    type = "S"
  }

  global_secondary_index {
    name               = "bank-account-iban-gsi"
    hash_key           = "iban"
    projection_type    = "ALL"
    read_capacity      = var.banking_entities_read_capacity
    write_capacity     = var.banking_entities_write_capacity
  }

  tags = var.tags
}
