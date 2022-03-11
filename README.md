# vaccine-portal
A mock vaccine registration portal for COMP47660 Module.

## Instructions

1. Start a local mysql server, and ensure the config in application.yml is correct.
2. To run the app run the command `.\gradlew bootRun`
3. Once the app has initialized the database, you can run `python database_init/init.py` to populate the database with initial data. (note that this requires mysql-connector-python and requests package)

Killian Callaghan 18332783
•	Implemented registered users can see their personal information and appointments.
•	Implemented statistics and graphs.
•	Improved the look and feel of the site.

## Contributions diary

### Killian Callaghan - 18332783
- Implemented registered users can see their personal information and appointments.
- Implemented statistics and graphs.
- Improved the look and feel of the site.

### David Loftus - 18329671
- Users can register to the Vaccination System.
- Implemented the forum where users can ask questions.
- Implemented that an admin can respond to a user question on the forum.
- Implemented that a registered user can login and logout from the system.
- Implemented registered users can see a record of their last activity.

### Joe Clarke - 18400934
- Registered users can book a vaccination appointment.
- Implemented admin controls. The admin can update appointments for each centre.
- Implemented the ability to view/cancel appointments.
