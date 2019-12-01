variable "region" {
  description = "aws region to use"
}

variable "app-name" {
  default = "app"
}

variable "lambda-role" {
  description = "lambda role"
}

variable "package-file" {
  description = "app jar file"
}