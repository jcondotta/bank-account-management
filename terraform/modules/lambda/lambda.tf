resource "aws_lambda_function" "bank_accounts_lambda" {
  function_name = var.lambda_function_name
  runtime       = var.lambda_runtime
  handler       = var.lambda_handler
  role          = aws_iam_role.bank_accounts_lambda_role_exec.arn
  filename      = var.lambda_file
  memory_size   = var.lambda_memory_size
  timeout       = var.lambda_timeout
  architectures = ["arm64"]

  environment {
    variables = merge(
      {
        AWS_DYNAMODB_BANKING_ENTITIES_TABLE_NAME = var.dynamodb_banking_entities_table_name
        AWS_SNS_ACCOUNT_HOLDER_CREATED_TOPIC_ARN = var.sns_account_holder_created_topic_arn
      },
      var.lambda_environment_variables
    )
  }

  tags = var.tags
}