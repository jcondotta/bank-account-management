output "lambda_invoke_uri" {
  description = "The ARN for invoking the Lambda function via API Gateway"
  value       = "arn:aws:apigateway:${var.aws_region}:lambda:path/2015-03-31/functions/${module.lambda.lambda_function_arn}/invocations"
}

output "api_gateway_invoke_url" {
  description = "API Gateway invoke URL based on the environment"
  value       = module.apigateway.api_gateway_invoke_url
}