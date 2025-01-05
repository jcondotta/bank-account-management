variable "environment" {
  description = "The environment to deploy to (e.g., dev, staging, prod)"
  type        = string
}

variable "banking_entities_table_name" {
  description = "The name of the DynamoDB banking entities table."
  type        = string
}

variable "banking_entities_billing_mode" {
  description = "banking entities DynamoDB billing mode (PROVISIONED or PAY_PER_REQUEST)"
  type        = string
}

variable "banking_entities_read_capacity" {
  description = "banking entities DynamoDB read capacity units"
  type        = number
}

variable "banking_entities_write_capacity" {
  description = "banking entities DynamoDB write capacity units"
  type        = number
}

variable "tags" {
  description = "Tags applied to all resources for organization and cost tracking across environments and projects."
  type        = map(string)
}
