# Car Booking Application
Welcome to the Car Booking Application repository! This application allows users to book cars for various purposes. To get started, follow the instructions below to set up and run the application.

Endpoints
- __/users__: This endpoint allows you to view and edit user information.

- __/cars__: Use this endpoint to view and edit car information.

- __/brands__: The /brands endpoint is designed to manage car brands.

- __/bookings__: This endpoint enables you to view and manage car bookings.

### Table of Contents
- Requirements
- Installation
- Configuration
- Usage

## Requirements
Before running the application, make sure you have the following installed:

Docker - to manage containerization for the application and the database.
## Installation
To install the Car Booking Application, follow these steps:

1. Clone this repository to your local machine using the following command:

```bash
git clone https://github.com/ana1999-code/Car-Booking-Application.git
```
2. Change into the project directory:

```bash
cd Car-Booking-Application
```
### Configuration
The application uses a PostgreSQL database to store car booking information. Before running the app, you need to set up the database:

1. Make sure you have Docker installed and running on your machine.

2. Use Docker Compose to set up the PostgreSQL container:

```bash
docker-compose up -d
```
Run the Car Booking Application.

The application should now be running locally at http://localhost:8090/api/v1.

Open your web browser and navigate to http://localhost:8090/api/v1 to access the Car Booking Application.

_Thank you for using the Car Booking Application! I hope you find it useful and enjoy using it. If you have any feedback or suggestions, I'd love to hear from you. Happy car booking!_
