resource "aws_db_subnet_group" "default" {
  name       = "arka-db-subnet-group"
  subnet_ids = var.subnet_ids

  tags = {
    Name = "ArkaDBSubnetGroup"
  }
}

resource "aws_security_group" "rds_sg" {
  name        = "arka-rds-sg"
  description = "Allow inbound access to PostgreSQL"
  vpc_id      = var.vpc_id

  ingress {
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # ⚠️ Apertura total como solicitaste
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "ArkaRdsSecurityGroup"
  }
}

resource "aws_db_instance" "rds_instance" {
  identifier        = var.db_identifier
  engine            = "postgres"
  instance_class    = var.instance_class
  username          = var.db_username
  password          = var.db_password
  db_name           = var.db_name
  allocated_storage = var.allocated_storage
  storage_type      = "gp2"

  vpc_security_group_ids = [aws_security_group.rds_sg.id]
  db_subnet_group_name   = aws_db_subnet_group.default.name

  skip_final_snapshot = true

  publicly_accessible = false
  multi_az            = false

  tags = {
    Name = "ArkaPostgres"
  }
}
