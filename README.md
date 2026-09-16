# Traffic Signal Optimizer

A full-stack traffic signal optimization platform built with Java, Spring Boot, PostgreSQL, React, and TypeScript.

The application analyzes directional traffic volume at an intersection and generates recommended signal timing using a simplified Webster-inspired optimization model.

## Features

- Analyze northbound, southbound, eastbound, and westbound traffic volumes
- Generate recommended traffic signal cycle lengths
- Recommend north/south and east/west green times
- Estimate current and optimized traffic delay
- Calculate estimated percentage improvement
- Upload CSV files for batch intersection analysis
- Save analysis results in PostgreSQL
- View previous analyses
- Compare current and recommended timing with charts
- REST API built with Spring Boot
- JUnit tests for optimization logic

## Tech Stack

### Backend
- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Maven
- JUnit
- Mockito

### Frontend
- React
- TypeScript
- Vite
- Recharts
- CSS

## Architecture

React / TypeScript Frontend

↓

Spring Boot REST API

↓

Traffic Optimization Service

↓

PostgreSQL Database

## Example Analysis

Example input:

- Northbound: 420 vehicles/hour
- Southbound: 380 vehicles/hour
- Eastbound: 130 vehicles/hour
- Westbound: 160 vehicles/hour
- Current N/S Green: 30 seconds
- Current E/W Green: 30 seconds

Example output:

- Current Cycle: 70 seconds
- Recommended Cycle: 60 seconds
- Recommended N/S Green: 35 seconds
- Recommended E/W Green: 15 seconds
- Current Estimated Delay: 13.93 seconds
- Optimized Estimated Delay: 9.71 seconds
- Estimated Improvement: 30.28%

## API Endpoints

### Analyze Intersection

`POST /api/traffic/analyze`

### Get Previous Analyses

`GET /api/traffic/analyses`

### Get Analysis By ID

`GET /api/traffic/analyses/{id}`

### Upload CSV

`POST /api/traffic/upload`

## Running the Project

### Database

Create a PostgreSQL database named:

`traffic_optimizer`

Set your PostgreSQL password as an environment variable:

```powershell
$env:DB_PASSWORD="your_password"