provider "aws" {
  region  = var.aws_region
  profile = var.aws_profile
}

data "aws_caller_identity" "current" {

}

module "dynamodb" {
  source = "./modules/dynamodb"

  environment = var.environment
  tags        = merge(var.tags, { "environment" = var.environment })

  banking_entities_table_name     = "banking-entities-${var.environment}"
  banking_entities_billing_mode   = "PROVISIONED"
  banking_entities_read_capacity  = 4
  banking_entities_write_capacity = 4
}

module "sns" {
  source = "./modules/sns"

  aws_region  = var.aws_region
  environment = var.environment
  tags        = local.common_tags

  sns_account_holder_created_topic_name = "account-holder-created-${var.environment}"
  bank_account_lambda_function_arn      = module.lambda.lambda_function_arn
}

module "lambda" {
  source = "./modules/lambda"

  aws_region             = var.aws_region
  current_aws_account_id = data.aws_caller_identity.current.account_id
  environment            = var.environment
  tags                   = merge(var.tags, { "environment" = var.environment })


  dynamodb_banking_entities_table_arn  = module.dynamodb.banking_entities_table_arn
  dynamodb_banking_entities_table_name = module.dynamodb.banking_entities_table_name

  sns_account_holder_created_topic_name = module.sns.sns_account_holder_created_topic_name
  sns_account_holder_created_topic_arn  = module.sns.sns_account_holder_created_topic_arn

  lambda_function_name         = "bank-accounts-${var.environment}"
  lambda_memory_size           = 1024
  lambda_timeout               = 30
  lambda_runtime               = "provided.al2023" #Amazon Linux 2023
  lambda_handler               = "io.micronaut.function.aws.proxy.MicronautLambdaHandler"
  lambda_file                  = "../target/function.zip"
  lambda_environment_variables = {}
}

module "apigateway" {
  source = "./modules/apigateway"

  aws_region  = var.aws_region
  environment = var.environment
  tags        = merge(var.tags, { "environment" = var.environment })

  lambda_function_arn  = module.lambda.lambda_function_arn
  lambda_function_name = module.lambda.lambda_function_name
  lambda_invoke_uri    = module.lambda.lambda_invoke_uri
}