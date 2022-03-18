export MASTER_DB_URL="jdbc:postgresql://localhost:5433/vesta"
export MASTER_DB_USERNAME="vesta"
export MASTER_DB_PASSWORD="test"

export filePath="/Users/vamsinekkanti/Documents/repos/csv-dataloader/elaraTexasSubgroup.csv"

java -Dmigrators=ElaraTexasAdd -jar target/csv-dataloader.jar
