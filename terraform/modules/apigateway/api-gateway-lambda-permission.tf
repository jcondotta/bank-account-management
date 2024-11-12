# Define the Lambda permission to allow API Gateway to invoke the Lambda function
resource "aws_lambda_permission" "allow_apigateway_invoke" {
  statement_id  = "AllowExecutionFromAPIGateway-${var.lambda_function_name}"
  action        = "lambda:InvokeFunction"
  function_name = var.lambda_function_name
  principal     = "apigateway.amazonaws.com"

  # Updated source_arn to specify the exact path and method
  source_arn = "${aws_api_gateway_rest_api.this.execution_arn}/POST/api/v1/bank-accounts"
}