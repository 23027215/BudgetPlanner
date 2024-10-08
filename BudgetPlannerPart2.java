import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

// Abstract class Expense
abstract class Expense {
    protected String name;
    protected double amount;

    public Expense(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    public abstract double getAmount();
    public String getName() {
        return name;
    }
}

// HomeLoan class inheriting from Expense
class HomeLoan extends Expense {
    private double loanAmount;
    private double interestRate;
    private int months;

    public HomeLoan(double loanAmount, double interestRate, int months) {
        super("Home Loan", 0);
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.months = months;
        calculateRepayment();
    }

    // Calculate monthly repayment
    private void calculateRepayment() {
        double monthlyInterestRate = (interestRate / 100) / 12;
        this.amount = loanAmount * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, months)) / (Math.pow(1 + monthlyInterestRate, months) - 1);
    }

    @Override
    public double getAmount() {
        return this.amount;
    }
}

// VehicleLoan class inheriting from Expense
class VehicleLoan extends Expense {
    private double loanAmount;
    private double interestRate;
    private int months;
    private double insurancePremium;

    public VehicleLoan(double loanAmount, double interestRate, double insurancePremium) {
        super("Vehicle Loan", 0);
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.insurancePremium = insurancePremium;
        this.months = 60; // 5 years repayment period
        calculateRepayment();
    }

    // Calculate monthly vehicle repayment
    private void calculateRepayment() {
        double monthlyInterestRate = (interestRate / 100) / 12;
        this.amount = loanAmount * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, months)) / (Math.pow(1 + monthlyInterestRate, months) - 1) + insurancePremium;
    }

    @Override
    public double getAmount() {
        return this.amount;
    }
}

public class BudgetPlannerPart2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Expense> expenses = new ArrayList<>();

        System.out.print("Gross monthly income: R");
        double grossIncome = scanner.nextDouble();

        System.out.print("Monthly tax deducted: R");
        double taxDeducted = scanner.nextDouble();

        // Input general expenses
        String[] expenseTypes = {"Groceries", "Water and lights", "Travel costs", "Cellphone", "Other expenses"};
        for (String expenseType : expenseTypes) {
            System.out.print(expenseType + ": ");
            double expenseAmount = scanner.nextDouble();
            expenses.add(new Expense(expenseType, expenseAmount) {
                @Override
                public double getAmount() {
                    return this.amount;
                }
            });
        }

        // Accommodation: Rent or Buy
        System.out.print("Rent or buy? (type 'rent' or 'buy'): ");
        String accommodationChoice = scanner.next();
        if (accommodationChoice.equalsIgnoreCase("buy")) {
            System.out.print("Property price: R");
            double purchasePrice = scanner.nextDouble();

            System.out.print("Deposit: R");
            double deposit = scanner.nextDouble();

            System.out.print("Interest rate(%): ");
            double interestRate = scanner.nextDouble();

            System.out.print("Repayment months (between 240 and 360): ");
            int months = scanner.nextInt();

            double loanAmount = purchasePrice - deposit;
            HomeLoan homeLoan = new HomeLoan(loanAmount, interestRate, months);
            expenses.add(homeLoan);

            if (homeLoan.getAmount() > grossIncome / 3) {
                System.out.println("Warning: Home loan repayment exceeds a third of your income.");
            }
        } else {
            System.out.print("Rent amount: R");
            double rentAmount = scanner.nextDouble();
            expenses.add(new Expense("Rent", rentAmount) {
                @Override
                public double getAmount() {
                    return this.amount;
                }
            });
        }

        // Vehicle Purchase Option
        System.out.print("Do you want to buy a vehicle? (yes/no): ");
        String vehicleChoice = scanner.next();
        if (vehicleChoice.equalsIgnoreCase("yes")) {
            System.out.print("Vehicle make and model: ");
            String vehicleModel = scanner.next();

            System.out.print("Purchase price: R");
            double vehiclePrice = scanner.nextDouble();

            System.out.print("Deposit: R");
            double vehicleDeposit = scanner.nextDouble();

            System.out.print("Interest rate(%): ");
            double vehicleInterestRate = scanner.nextDouble();

            System.out.print("Insurance premium: R");
            double insurancePremium = scanner.nextDouble();

            double vehicleLoanAmount = vehiclePrice - vehicleDeposit;
            VehicleLoan vehicleLoan = new VehicleLoan(vehicleLoanAmount, vehicleInterestRate, insurancePremium);
            expenses.add(vehicleLoan);
        }

        // Calculate total expenses and available income
        double totalExpenses = 0;
        for (Expense expense : expenses) {
            totalExpenses += expense.getAmount();
        }

        double availableIncome = grossIncome - taxDeducted - totalExpenses;

        // Notify user if expenses exceed 75% of income
        if (totalExpenses > 0.75 * grossIncome) {
            System.out.println("Warning: Your total expenses exceed 75% of your income.");
        }

        // Display expenses in descending order
        Collections.sort(expenses, Comparator.comparingDouble(Expense::getAmount).reversed());
        System.out.println("\nExpenses in descending order:");
        for (Expense expense : expenses) {
            System.out.println(expense.getName() + ": " + expense.getAmount());
        }

        // Display available income
        System.out.println("Money left after expenses: " + availableIncome);
        System.out.println(availableIncome > 0 ? "You have money left." : "You're spending more than you earn.");

        scanner.close();
    }
}

