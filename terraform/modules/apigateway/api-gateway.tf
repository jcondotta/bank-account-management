resource "aws_api_gateway_rest_api" "this" {
  name        = "bank_accounts_api-${var.environment}"
  description = "API Gateway for bank accounts service in ${var.environment} environment"

  tags = var.tags
}