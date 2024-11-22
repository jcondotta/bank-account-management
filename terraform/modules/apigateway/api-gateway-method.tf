## Define the POST method for /api/v1/bank-accounts
resource "aws_api_gateway_method" "post_bank_accounts" {
  rest_api_id   = aws_api_gateway_rest_api.this.id
  resource_id   = aws_api_gateway_resource.bank_accounts.id
  http_method   = "POST"
  authorization = "NONE"

}

resource "aws_api_gateway_integration" "post_bank_accounts_integration" {
  rest_api_id             = aws_api_gateway_rest_api.this.id
  resource_id             = aws_api_gateway_resource.bank_accounts.id
  http_method             = aws_api_gateway_method.post_bank_accounts.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = var.lambda_invoke_uri

  depends_on = [
    aws_api_gateway_method.post_bank_accounts
  ]
}

## Define the POST method for /api/v1/bank-accounts/bank-account-id/{bank-account-id}
resource "aws_api_gateway_method" "get_bank_accounts" {
  rest_api_id   = aws_api_gateway_rest_api.this.id
  resource_id   = aws_api_gateway_resource.bank_account_id_param.id
  http_method   = "GET"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "get_bank_accounts_integration" {
  rest_api_id             = aws_api_gateway_rest_api.this.id
  resource_id             = aws_api_gateway_resource.bank_account_id_param.id
  http_method             = aws_api_gateway_method.get_bank_accounts.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = var.lambda_invoke_uri

  depends_on = [
    aws_api_gateway_method.get_bank_accounts
  ]
}

## Define the POST method for /api/v1/bank-accounts
resource "aws_api_gateway_method" "post_account_holders" {
  rest_api_id   = aws_api_gateway_rest_api.this.id
  resource_id   = aws_api_gateway_resource.account_holders.id
  http_method   = "POST"
  authorization = "NONE"

}

resource "aws_api_gateway_integration" "post_account_holders_integration" {
  rest_api_id             = aws_api_gateway_rest_api.this.id
  resource_id             = aws_api_gateway_resource.account_holders.id
  http_method             = aws_api_gateway_method.post_account_holders.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = var.lambda_invoke_uri

  depends_on = [
    aws_api_gateway_method.post_account_holders
  ]
}