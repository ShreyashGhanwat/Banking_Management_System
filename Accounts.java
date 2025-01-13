package mybank.com;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Accounts {
	
	private Connection connection;
	private Scanner scanner;

	public Accounts(Connection connection, Scanner scanner) {
		this.connection=connection;
		this.scanner=scanner;
	}
	
	public long open_account(String email) {
		if(!account_exist(email)) {
			String open_account_query="INSERT INTO Accounts(account_number, full_name, email, balance, security_pin) VALUES(?, ?, ?, ?, ?)";
			scanner.nextLine();
			System.out.println("Enter Full Name:");
			String full_name=scanner.nextLine();
			System.out.println("Enter Initial Amount:");
			double balance=scanner.nextDouble();
			scanner.nextLine();
			System.out.println("Enter Security pin:");
			String security_pin=scanner.nextLine();
			
			try {
				long account_number=generateAccount_number();
				PreparedStatement preparedStatement=connection.prepareStatement(open_account_query);
				preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, full_name);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setString(5, security_pin);
                
                int affectedRows=preparedStatement.executeUpdate();
                
                if(affectedRows>0) {
                	return account_number;
                }else {
                	throw new RuntimeException("Account Creation failed!!");
                }
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		}
		throw new RuntimeException("Account Already Exist");
		
	}
	
	public long getAccount_number(String email) {
		String query="select account_number from accounts where email=?";
		
		try {
			PreparedStatement preparedStatement=connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			ResultSet rs=preparedStatement.executeQuery();
			
			if(rs.next()) {
				return rs.getLong("account_number");
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		throw new RuntimeException("Account Number Dose Not Exist!!!"); 
		
	}
	
	private long generateAccount_number() {
		String query="select account_number from accounts order by account_number desc limit 1";
		try {
			Statement statement=connection.createStatement();
			ResultSet rs=statement.executeQuery(query);
			
			if(rs.next()) {
				long last_account_number=rs.getLong("account_number");
				return last_account_number+1;
			}else {
				return 10000100;
			}
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return 10000100;
		
	}
	
	public boolean account_exist(String email) {
		String query="select account_number from accounts where email=?";
		
		try {
		PreparedStatement preparedStatement= connection.prepareStatement(query);
		
		preparedStatement.setString(1, email);
		ResultSet rs=preparedStatement.executeQuery();
		
		if(rs.next()) {
			return true;
		}
		else {
			return false;
		}
		
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
		
	}

}
