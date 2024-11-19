variable "aws_region" {
  description = "The AWS region where resources will be deployed."
  type        = string
}

variable "environment" {
  description = "The environment the Lambda function is deployed to (e.g., dev, prod or staging)"
  type        = string
}

variable "current_aws_account_id" {
  description = "The current AWS account ID"
  type        = string
}

variable "dynamodb_banking_entities_table_arn" {
  description = "The ARN of the DynamoDB banking entities table the Lambda will interact with"
  type        = string

  validation {
    condition     = can(regex("^arn:aws:dynamodb:[a-z0-9-]+:[0-9]{12}:table/.+", var.dynamodb_banking_entities_table_arn))
    error_message = "The value for dynamodb_table_arn must be a valid DynamoDB table ARN."
  }
}

variable "dynamodb_banking_entities_table_name" {
  description = "The name of the DynamoDB banking entities table the Lambda will interact with"
  type        = string
}

variable "sns_bank_account_created_topic_name" {
  description = "The name of the SNS bank account created topic the Lambda will interact with"
  type        = string
}

variable "sns_bank_account_created_topic_arn" {
  description = "The ARN of the SNS bank account created topic the Lambda will interact with"
  type        = string
}

variable "lambda_function_name" {
  description = "The name of the bank accounts Lambda function"
  type        = string

  validation {
    condition     = length(var.lambda_function_name) > 0
    error_message = "bank_accounts_lambda_function_name must be a non-empty string."
  }
}

variable "lambda_memory_size" {
  description = "The memory size (in MB) for the Lambda function"
  type        = number

  validation {
    condition     = var.lambda_memory_size >= 128 && var.lambda_memory_size <= 10240
    error_message = "lambda_memory_size must be between 128 MB and 10,240 MB."
  }
}

variable "lambda_timeout" {
  description = "The timeout (in seconds) for the Lambda function"
  type        = number

  validation {
    condition     = var.lambda_timeout > 0 && var.lambda_timeout <= 900
    error_message = "lambda_timeout must be a positive number and less than or equal to 900 seconds (15 minutes)."
  }
}

variable "lambda_runtime" {
  description = "The runtime for the Lambda function (e.g., java17, java21)"
  type        = string
}

variable "lambda_handler" {
  description = "The fully qualified handler class for the Lambda function"
  type        = string
}

variable "lambda_file" {
  description = "The path to the file(jar, zip) for the Lambda function"
  type        = string
}

variable "lambda_environment_variables" {
  description = "A key-value map of environment variables for the Lambda function, used to configure dynamic runtime settings."
  type        = map(string)
  default     = {}
}

variable "tags" {
  description = "Tags applied to all resources for organization and cost tracking across environments and projects."
  type        = map(string)
}