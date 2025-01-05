variable "aws_region" {
  description = "The AWS region where resources will be deployed."
  type        = string
  default = "us-east-1"
}

variable "aws_profile" {
  description = "The AWS profile to use for deployment."
  type        = string
}

variable "environment" {
  description = "The environment to deploy to (e.g., dev, localstack, staging, prod)"
  type        = string
  default = "localstack"

  validation {
    condition     = contains(["dev", "localstack", "staging", "prod"], var.environment)
    error_message = "Environment must be one of 'dev', 'localstack', 'staging', or 'prod'."
  }
}

variable "tags" {
  description = "Tags applied to all resources for organization and cost tracking across environments and projects."
  type        = map(string)
  default = {
    "project" = "bank-account-management"
  }
}