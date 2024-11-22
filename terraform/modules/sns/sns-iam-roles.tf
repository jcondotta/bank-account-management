resource "aws_sns_topic_policy" "lambda_publish_policy" {
  arn = aws_sns_topic.account_holder_created_topic.arn

  policy = jsonencode(
    {
      Version   = "2012-10-17",
      Statement = [
        {
          Effect    = "Allow",
          Principal = {
            "Service": "lambda.amazonaws.com"
          },
          Action    = "sns:Publish",
          Resource  = aws_sns_topic.account_holder_created_topic.arn,
          Condition = {
            ArnEquals = {
              "aws:SourceArn": var.bank_account_lambda_function_arn
            }
          }
        }
      ]
    }
  )
}