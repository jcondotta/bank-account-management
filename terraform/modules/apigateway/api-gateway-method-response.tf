# Define the method response for a successful request (200)
resource "aws_api_gateway_method_response" "post_bank_accounts_200" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  resource_id = aws_api_gateway_resource.bank-accounts.id
  http_method = aws_api_gateway_method.post_bank_accounts.http_method
  status_code = "200"

  response_models = {
    "application/json" = "Empty"
  }
}