package mybank.com;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class User {

	private Connection connection;
	private Scanner scanner;

	public User(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}
	
	public void register() {
		scanner.nextLine();
		System.out.println("Full Name: ");
		String full_name=scanner.nextLine();
		System.out.println("Email: ");
		String email=scanner.nextLine();
		System.out.println("Password: ");
		String password=scanner.nextLine();
		
		if(user_exist(email)) {
			System.out.println("User Already Exist For This Email Address");
			return;
		}
		
		String register_query="Insert into user (full_name,email,password) values (?,?,?)";
		try {
			PreparedStatement preparedstatement=connection.prepareStatement(register_query);
			preparedstatement.setString(1, full_name);
			preparedstatement.setString(2, email);
			preparedstatement.setString(3, password);
			int affectedRows=preparedstatement.executeUpdate();
			
			if(affectedRows>0) {
				System.out.println("Registration Successful");
			}
			else {
				System.out.println("Registration Failed");
			}
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
			
	}

	public String login() {
		scanner.nextLine();
		System.out.println("Email: ");
		String email=scanner.nextLine();
		System.out.println("Password: ");
		String password=scanner.nextLine();
		
		String login_query="select * from user where email=? and password=?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(login_query);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);
			ResultSet rs=preparedStatement.executeQuery();
			
			if(rs.next()) {
				return email;
			}
			else {
				return null;
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
		
	}
	
	public boolean user_exist(String email) {
		String query="select * from user where email=?";
		
		try {
			PreparedStatement preparedStatement=connection.prepareStatement(query);
			preparedStatement.setString(1,email);
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
