project     = "arka"
environment = "dev"

default_tags = {
  Owner      = "Jonathan"
  Department = "Engineering"
}

artifacts_bucket_name = "arka-dev-artifacts"

ecr_repositories = [
  {
    name             = "catalog-service"
    images_to_keep   = 10
    allowed_accounts = []
  }
]
