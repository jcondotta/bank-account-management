output "sns_account_holder_created_topic_name" {
  description = "The name of the SNS topic for account holder creation events."
  value       = aws_sns_topic.account_holder_created_topic.name
}

output "sns_account_holder_created_topic_arn" {
  description = "The ARN of the SNS topic for account holder creation events."
  value       = aws_sns_topic.account_holder_created_topic.arn
}