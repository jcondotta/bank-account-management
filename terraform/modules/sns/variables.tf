variable "aws_region" {
  description = "Specifies the AWS region where resources, including the SNS topic, will be deployed (e.g., us-east-1)."
  type        = string
}

variable "environment" {
  description = "Indicates the environment for deployment (e.g., dev, staging, prod), used for resource naming and organizational purposes."
  type        = string
}

variable "sns_bank_account_created_topic_name" {
  description = "The name of the SNS topic for the bank-account-created event."
  type        = string
}

variable "bank_account_lambda_function_arn" {
  description = "The ARN of the bank account lambda function."
  type        = string
}

variable "tags" {
  description = "A map of tags to apply to all resources for organizational tracking, cost allocation, and resource identification across environments and projects."
  type        = map(string)
}
