# Define the /api resource
resource "aws_api_gateway_resource" "api" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  parent_id   = aws_api_gateway_rest_api.this.root_resource_id
  path_part   = "api"
}

# Define the /v1 resource under /api
resource "aws_api_gateway_resource" "v1" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  parent_id   = aws_api_gateway_resource.api.id
  path_part   = "v1"
}

# Define the /bank-accounts resource under /v1
resource "aws_api_gateway_resource" "bank_accounts" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  parent_id   = aws_api_gateway_resource.v1.id
  path_part   = "bank-accounts"
}

# Define the /bank-account-id resource under /bank-accounts
resource "aws_api_gateway_resource" "bank_account_id" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  parent_id   = aws_api_gateway_resource.bank_accounts.id
  path_part   = "bank-account-id"
}

# Define the {bank-account-id} path parameter
resource "aws_api_gateway_resource" "bank_account_id_param" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  parent_id   = aws_api_gateway_resource.bank_account_id.id
  path_part   = "{bank-account-id}"
}

# Define the /account-holders resource under {bank-account-id}
resource "aws_api_gateway_resource" "account_holders" {
  rest_api_id = aws_api_gateway_rest_api.this.id
  parent_id   = aws_api_gateway_resource.bank_account_id_param.id
  path_part   = "account-holders"
}
