# Bike Rental System - Member 6 only

A standalone Spring Boot project containing the Member 6 Reviews module, using the layer package layout from the team project. Member 1's account pages, user model, DTOs, and services are not included.

## Run

Requires JDK 21 or newer.

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

macOS/Linux:

```bash
./mvnw spring-boot:run
```

Open `http://localhost:8080/reviews?bikeId=B001&userId=U001`. Enter the customer ID in the page when viewing or writing reviews. Since the Member 1 login module is not part of this project, the user ID is a coursework demo input, not an authenticated identity.

## Member 6 packages

```text
com/Webapp/BikeRentalSystem/
|-- controller/ReviewController.java
|-- dto/ReviewDTO.java
|-- filehandling/BookingFileHandler.java, FileHandler.java, ReviewFileHandler.java
|-- model/Review.java
`-- service/BookingEligibilityService.java, ReviewService.java
```

The Spring Boot entry point is `com.Webapp.BikeRentalSystemApplication`.

## Reviews and data

The module supports listing reviews by bike or customer, creating a review, updating or deleting the review owned by the supplied customer ID, and showing a bike's average rating. Ratings must be whole numbers from 1 to 5, comments are limited to 300 characters, and a customer can have one review per bike.

Files are stored outside the project in `~/BikeRentalData/`. Review rows use:

```text
reviewId|userId|bikeId|rating|comment|reviewDate
```

A matching completed booking is required. Add rows to `bookings.txt` in this format:

```text
bookingId|userId|bikeId|bookingDate|startDate|endDate|status
BK001|U001|B001|2026-10-07|2026-10-10|2026-10-13|Completed
```

The booking eligibility adapter can be replaced by the team's booking service when modules are joined.
