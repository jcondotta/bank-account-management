output "banking_entities_table_arn" {
  description = "The ARN of the DynamoDB banking entities table created"
  value       = aws_dynamodb_table.banking_entities.arn
}

output "banking_entities_table_name" {
  description = "The name of the DynamoDB banking entities table created"
  value       = aws_dynamodb_table.banking_entities.name
}