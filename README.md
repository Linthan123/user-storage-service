# User Document Storage Service

## Overview
This project provides a REST API for searching and downloading files in a user-specific storage account on Amazon S3. Each user has a dedicated folder, and files are stored in a single S3 bucket.

## Features
- **Search Files (Mandatory)**: Search for files in a specific user’s folder based on filename.
- **Download Files**: Download files from the S3 bucket.
- **Upload Files (Optional)**: Upload files to the user's folder.
- **Optimized API Calls**: Efficient interaction with S3 to enhance performance.
- **Extensible Design**: Easy to expand for additional features.
- **No Authentication Required**: Open API access for simplicity.

## Tech Stack
- **Java**
- **Spring Boot**
- **AWS S3 SDK**
- **JUnit**
- **Maven** (for dependency management)
- **Postman** (for testing)

## API Endpoints
### 1. Search Files (Mandatory)
**Endpoint:** `GET /files/search`

**Request Parameters:**
```json
{
  "userName": "sandy",
  "searchTerm": "logistics"
}
```

**Response:**
```json
{
  "files": [
    "sandy/logistics_report.pdf",
    "sandy/logistics_data.xlsx"
  ]
}
```

### 2. Download File
**Endpoint:** `GET /files/download/{userName}/{fileName}`

**Response:** Returns the requested file as a downloadable stream.

### 3. Upload File (Optional)
**Endpoint:** `POST /files/upload`

**Request:** Multipart Form Data
- `userName` (String)
- `file` (Binary File)

**Response:**
```json
{
  "message": "File uploaded successfully"
}
```

## Project Setup
### Prerequisites
- Java 17+
- Maven
- AWS Account with S3 Access

### Configuration
1. Set up an S3 bucket and update `application.properties`:
```properties
aws.s3.bucket-name=your-bucket-name
aws.s3.access-key=your-access-key
aws.s3.secret-key=your-secret-key
aws.s3.region=your-region
```

2. Build the project:
```sh
mvn clean install
```

3. Run the application:
```sh
mvn spring-boot:run
```

## Testing
- Use **Postman** or **Swagger** to test APIs.
- Run JUnit tests using:
```sh
mvn test
```

## Future Enhancements
- Add authentication using JWT.
- Implement file metadata search.
- Support folder uploads and batch processing.

