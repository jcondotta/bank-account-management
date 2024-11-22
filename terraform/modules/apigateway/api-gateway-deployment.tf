resource "aws_api_gateway_deployment" "this" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  stage_name  = var.environment

  depends_on = [
    aws_api_gateway_method.post_bank_accounts,
    aws_api_gateway_integration.post_bank_accounts_integration,
    aws_api_gateway_method.get_bank_accounts,
    aws_api_gateway_integration.get_bank_accounts_integration,
    aws_api_gateway_method_response.post_bank_accounts_200,
    aws_lambda_permission.allow_apigateway_invoke
  ]
}