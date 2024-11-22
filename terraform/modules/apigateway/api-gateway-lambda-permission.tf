# Define the Lambda permission to allow API Gateway to invoke the Lambda function
resource "aws_lambda_permission" "allow_apigateway_invoke" {
  statement_id  = "AllowExecutionFromAPIGateway-POST${var.lambda_function_name}"
  action        = "lambda:InvokeFunction"
  function_name = var.lambda_function_name
  principal     = "apigateway.amazonaws.com"

  source_arn = "${aws_api_gateway_rest_api.this.execution_arn}/*/POST/api/v1/bank-accounts"
}

# Define the Lambda permission to allow API Gateway to invoke the Lambda function for GET
resource "aws_lambda_permission" "allow_apigateway_invoke_get" {
  statement_id  = "AllowExecutionFromAPIGateway-GET-${var.lambda_function_name}"
  action        = "lambda:InvokeFunction"
  function_name = var.lambda_function_name
  principal     = "apigateway.amazonaws.com"

  source_arn = "${aws_api_gateway_rest_api.this.execution_arn}/*/GET/api/v1/bank-accounts/bank-account-id/*"
}

# Define the Lambda permission to allow API Gateway to invoke the Lambda function
resource "aws_lambda_permission" "allow_apigateway_invoke_account_holders_post" {
  statement_id  = "AllowExecutionFromAPIGateway-POST-account-holders${var.lambda_function_name}"
  action        = "lambda:InvokeFunction"
  function_name = var.lambda_function_name
  principal     = "apigateway.amazonaws.com"

  source_arn = "${aws_api_gateway_rest_api.this.execution_arn}/*/POST/api/v1/bank-accounts/bank-account-id/*/account-holders"
}