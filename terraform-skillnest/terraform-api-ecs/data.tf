data "aws_vpc" "main" {
  filter {
    name   = "tag:Name"
    values = ["arka-vpc"]
  }
}

data "aws_subnets" "public" {
  filter {
    name   = "tag:Type"
    values = ["public"]
  }
}

data "aws_subnets" "private" {
  filter {
    name   = "tag:Type"
    values = ["private"]
  }
}
