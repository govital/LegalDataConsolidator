# Legal Data Consolidator

### Project Overview
This project implements a data consolidation service that merges legal data from two different vendors (CourtListener and Westlaw) based on a set of predefined rules. The merged data is then stored in a database table consolidated_legal_data and exported into a CSV file. The service also creates an audit table that tracks the source of each column in the merged data.

### Features:
Merges data from two CSV files containing legal docket information.
Applies specific rules for different fields, such as selecting the earliest or latest date, prioritizing one source over another, or applying certain conditions.
Saves the merged data to a local database.
Exports the final consolidated data to a CSV file.
Tracks the source of each field in an audit table.

### Files Included:
docket_wl.csv: Sample file from the Westlaw vendor.
docket_cl.csv: Sample file from the CourtListener vendor.
ConsolidatedLegalData.java: JPA Entity representing the consolidated legal data.
DataMergeService.java: Service responsible for merging data based on the defined rules.
DataSaveService.java: Service responsible for saving the merged data to the database.
DataController.java: REST Controller exposing the API to trigger the data processing and export.

### Setup and Installation:

##### Prerequisites:
Java: Ensure that Java 8 or later is installed.
Maven: Install Maven to manage project dependencies.
Spring Boot: The project is built using Spring Boot, which provides a lightweight framework for developing Java applications.

##### Steps to Run:

bash:
1. Navigate to the Project Directory.

2. Install Dependencies: Use Maven to install dependencies and package the project.
mvn clean install

3. Run the Application: You can run the Spring Boot application using the following command:
mvn spring-boot:run

Alternatively, you can run the JAR file generated in the target directory:
java -jar target/LegalDataConsolidator-0.0.1-SNAPSHOT.jar

4. Trigger the Data Processing: 
5. Once the application is running, navigate to the following URL to trigger the data processing:
(make sure to change file location under fileLocation param accordingly)
http://localhost:8080/process?fileLocation=/Users/vitalik/IdeaProjects/LegalDataConsolidator/src/main/resources


Check the Consolidated Data: The consolidated data is saved in the database and also exported to a CSV file named consolidated_legal_data.csv in the project directory.

### Design and Implementation

##### Data Schema:
The schema for the consolidated legal data is as follows:

Column Name	 |      Data Type |	    Description
ddid	            varchar(32)	        Unique docket ID.
date_filed	        date	            Date when the case was filed.
date_terminated	    date	            Date when the case was terminated.
jurisdiction	    varchar(256)	    Jurisdiction of the case.
district	        varchar(512)	    District where the case was heard.
case_name	        varchar(512)	    Name of the case.
case_length_days	integer	            Duration of the case in days.
court_id	        varchar(32)	        ID of the court where the case was filed.
allegation	        varchar(16384)	    Allegations made in the case.
damages	            varchar(1024)	    Claimed damages in the case.
case_type	        varchar(1024)	    Type of case.
link	            varchar(2048)	    Link to the case documents.
status	            varchar(2048)	    Status of the case.

### Merging Rules:
The data merging follows specific rules based on each column:

##### Default Rule Flow:

Take the most common answer.
If there is a conflict, take the latest option (by date).
If no decision can be made, choose randomly.
Column-Specific Rules:

date_filed: Take the earliest filing date.
date_terminated: Take the latest termination date.
jurisdiction: Take the jurisdiction from the row with the latest termination date.
district: Priority source is courtlisteners, then westlaw.
case_name: Priority source is westlaw, then courtlisteners.
case_length_days: Priority source is westlaw, then courtlisteners.
court_id: Priority source is courtlisteners, then westlaw.
allegation: Take the value from westlaw if available.
damages: Take the value from westlaw if available.
case_type: Apply the following conditions:
Select if the value is "Other statutory damages".
Exclude if it contains "nos" or "Contract".
link: Priority source is westlaw, then courtlisteners.
status: Priority source is westlaw, then courtlisteners.

### Design Decisions:

##### CSV Parsing:
OpenCSV is used to read CSV files for docket_wl.csv and docket_cl.csv.
Each file is parsed into memory as a map of rows with the ddid as the key.

##### Data Consolidation:
The data consolidation happens based on the rules described above.
For each column, we apply the specific rules and make a decision about which value to take for the consolidated row.

##### Persistence:
The merged data is persisted using Spring Data JPA into a local or remote database.
The entity ConsolidatedLegalData defines the structure of the final table.

##### Export:
The consolidated data is also exported to a CSV file for easy access.
Assumptions

##### Date Formats:
It is assumed that date fields (date_filed and date_terminated) follow the yyyy-MM-dd format.

##### Source Rank:
For certain fields, such as district, case_name, court_id, etc., we assume that the rank of sources (westlaw vs. courtlisteners) is predefined as per the rules.

##### Case Type Exclusions:
We exclude values containing the strings "nos" or "Contract" from the case_type column based on the rules provided.

##### Data Validity:
It is assumed that the input data is valid and does not contain major inconsistencies beyond the conflicts handled by the rules.

##### Future Improvements:
Error Handling: Add robust error handling for invalid data formats or missing data.
Performance Optimization: Optimize large data set processing, especially if the CSV files grow in size.
Testing: Implement unit and integration tests to ensure data consolidation accuracy and correctness.
API Enhancements: Provide additional API endpoints to allow querying of consolidated data dynamically.

##### License:
This project and its contents are proprietary and confidential.
Unauthorized copying, distribution, or use of the code or any part of the project is strictly prohibited
without express written permission from Gokhberg tal.

For licensing inquiries, please contact me at:
govital3@gmail.com