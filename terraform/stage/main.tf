
module "lambda-warmer-module" {
  source = "../modules/services"
  region = "sa-east-1"
  app-name = "lambda-warmer"
  lambda-role = "arn:aws:iam::205303771310:role/LambdaRoleTest"
  package-file = "lambda-warmer.jar"
}