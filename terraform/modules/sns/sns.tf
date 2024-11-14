resource "aws_sns_topic" "bank_account_created_topic" {
  name = var.sns_bank_account_created_topic_name

  tags = var.tags
}