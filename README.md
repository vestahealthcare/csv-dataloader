# Testing Backfill
Once you've tested your backfill code locally, it can be run in staging.
1) Build and test the code locally
2) Backup the table(s) you're going to backfill into.
3) Start the backfill EC2 instance in non-prod.

    https://console.aws.amazon.com/ec2/v2/home?region=us-east-1#InstanceDetails:instanceId=i-0723a0b1017da5250
4) Copy the jar to the EC2 instance.

   `scp target/csv-dataloader.jar ec2-user@10.0.44.212:/home/ec2-user/csv-dataloader/
5) SSH into the EC2 instance

    `ssh ec2-user@10.0.44.212`
6) Edit `run.sh`. It already contains the staging credentials. It'll need to be updated for the specific migrator.
7) Execute the shell script.

    `./run.sh`
8) Verify your updates in Master DB Staging.
9) Stop the EC2 instance.

# Running Backfill in Production
This can only be run by engineers with PHI access. Coordinate with your team lead if you don't have access.
1) Build and test the code locally
2) Backup the table(s) you're going to backfill into.
3) Start the backfill EC2 instance in prod.

   https://console.aws.amazon.com/ec2/v2/home?region=us-east-1#InstanceDetails:instanceId=i-045f69767741d649e
4) Copy the jar to the EC2 instance.

   `scp target/csv-dataloader.jar ec2-user@10.0.54.224:/home/ec2-user/csv-dataloader/
5) SSH into the EC2 instance

   `ssh ec2-user@10.0.54.224`
6) Edit `run.sh`. It already contains the production credentials. It'll need to be updated for the specific migrator.
7) Execute the shell script.

   `./run.sh`
8) Verify your updates in Master DB Production.
9) Stop the EC2 instance.
