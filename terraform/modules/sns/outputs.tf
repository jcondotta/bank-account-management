output "sns_bank_account_created_topic_name" {
  description = "The name of the SNS topic for bank account creation events."
  value       = aws_sns_topic.bank_account_created_topic.name
}

output "sns_bank_account_created_topic_arn" {
  description = "The ARN of the SNS topic for bank account creation events."
  value       = aws_sns_topic.bank_account_created_topic.arn
}