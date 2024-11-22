# Define the method response for a successful request (200)
resource "aws_api_gateway_method_response" "post_bank_accounts_200" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  resource_id = aws_api_gateway_resource.bank_accounts.id
  http_method = aws_api_gateway_method.post_bank_accounts.http_method
  status_code = "200"

  response_models = {
    "application/json" = "Empty"
  }
}

resource "aws_api_gateway_method_response" "get_bank_accounts_response_200" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  resource_id = aws_api_gateway_resource.bank_account_id_param.id
  http_method = aws_api_gateway_method.get_bank_accounts.http_method
  status_code = "200"

  response_models = {
    "application/json" = "Empty"
  }
}

# Define the method response for a successful request (200)
resource "aws_api_gateway_method_response" "post_account_holders_200" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  resource_id = aws_api_gateway_resource.account_holders.id
  http_method = aws_api_gateway_method.post_account_holders.http_method
  status_code = "200"

  response_models = {
    "application/json" = "Empty"
  }
}