# Gym-and-fitness-center-membership-system
## A Java GUI application for managing gym memberships, bookings, payments, and financial reporting using JavaFX and SQLite.
### This system follows a 4-layer architecture with SOLID design principles:

## Layers:
* Presentation Layer: JavaFX FXML views + Controllers
* Service Layer: Business logic, calculations, and validations
* DAO Layer: SQLite database CRUD operations
* Model Layer: POJO entity classes

## User Roles
1. Receptionist
Register new members
Renew memberships
Record member payments
2. Member
View membership details
Book fitness classes
View payment history
3. Admin/Manager (Financial Role)
View total income and expenses
Monitor net cash flow
View monthly financial reports
Record expenses

## User Hierarchy
* User (abstract base class)
* Member extends User
* Receptionist extends User
* Admin extends User

## Membership Types
* Membership (abstract)
* MonthlyMembership - No discount
* AnnualMembership - 15% discount
* VIPMembership - 25% discount

## Financial Models
* Payment - Records member payments
* Expense - Tracks gym expenses
* FinancialReport - Monthly financial summaries
