resource "aws_sns_topic" "account_holder_created_topic" {
  name = var.sns_account_holder_created_topic_name

  tags = var.tags
}