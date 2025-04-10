locals {

  common_tags = {
    "project"     = "bank-account-management",
    "owner"       = var.aws_profile,
    "environment" = var.environment
  }

}