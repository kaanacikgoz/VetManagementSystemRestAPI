# VetManagementSystemRestAPI Project

This project is a veterinary management system that allows the management of a veterinary clinic's operations through an API. 

## UML Diagram

![VetAPI](https://github.com/user-attachments/assets/cbee2c5a-5263-4603-918e-939cabe913e3)

## Entities and Relationships

### Customer
- **Attributes**: id, name, phone, mail, address, city
- **Relationships**: 
  - One-to-Many with **Animal**
  - One-to-Many with **Appointment**

### Animal
- **Attributes**: id, name, species, breed, gender, colour, dateOfBirth
- **Relationships**: 
  - Many-to-One with **Customer**
  - Many-to-Many with **Vaccine**
  - One-to-Many with **Appointment**

### Vaccine
- **Attributes**: id, name, code, protectionStartDate, protectionFinishDate
- **Relationships**: 
  - Many-to-Many with **Animal**

### Doctor
- **Attributes**: id, name, phone, mail, address, city
- **Relationships**: 
  - Many-to-Many with **AvailableDate**
  - One-to-Many with **Appointment**

### AvailableDate
- **Attributes**: id, availableDate
- **Relationships**: 
  - Many-to-Many with **Doctor**

### Appointment
- **Attributes**: id, appointmentDate
- **Relationships**: 
  - Many-to-One with **Doctor**
  - Many-to-One with **Animal**
  - Many-to-One with **Customer**
