## Define the POST method for /api/v1/bank-accounts
resource "aws_api_gateway_method" "post_bank_accounts" {
  rest_api_id   = aws_api_gateway_rest_api.this.id
  resource_id   = aws_api_gateway_resource.bank-accounts.id
  http_method   = "POST"
  authorization = "NONE"

}

resource "aws_api_gateway_integration" "post_bank_accounts_integration" {
  rest_api_id             = aws_api_gateway_rest_api.this.id
  resource_id             = aws_api_gateway_resource.bank-accounts.id
  http_method             = aws_api_gateway_method.post_bank_accounts.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = var.lambda_invoke_uri

  depends_on = [
    aws_api_gateway_method.post_bank_accounts
  ]
}