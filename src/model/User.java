package model;

import java.sql.Date;

public class User {

	private int ID;
	private String email;
	private String password;
	private String salt;
	private String name;
	private Date DOB;
	private String phone;
	private boolean male;
	private User() {
		super();
	}
	
	 private User(int iD, String email, String password, String salt,
			 	  String name, Date dOB, String phone, boolean isMale) {
		super();
		ID = iD;
		this.email = email;
		this.password = password;
		this.salt = salt;
		this.name = name;
		DOB = dOB;
		this.phone = phone;
		this.male = isMale;
	}

	// ***** BUILDER CLASS **** 
	public static class Builder{
		
		private int ID;
		private String email;
		private String password;
		private String salt;
		private String name;
		private Date DOB;
		private String phone;
		private boolean male;
		
		Builder() {
			super();
		}


		public Builder email(String email) {
			this.email = email;
			return this;
		}
		
		public Builder password(String password) {
			this.password = password;
			return this;
		}
		
		public Builder dateOfBirth(Date dob) {
			this.DOB = dob;
			return this;
		}
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder phone(String phone) {
			this.phone = phone;
			return this;
		}
		
		public Builder ID(int ID) {
			this.ID = ID;
			return this;
		}
		
		public Builder male(boolean isMale) {
			this.male = isMale;
			return this;
		}
		
		public Builder salt(String salt) {
			this.salt = salt;
			return this;
		}
		
		
		public User build() throws IllegalArgumentException {
			/* REQUIRED DATA */
			if(this.email == null || this.email.trim().isEmpty() ||
			   password == null || this.password.trim().isEmpty() ) {
				throw new IllegalArgumentException("EMAIL AND PASSWORD MUST BE NOT EMPTY.");
			} 
			
			return new User(this.ID, this.email, this.password, this.salt,this.name, this.DOB, this.phone,this.male);
		}
	}
	
	// ***** BUILDER CLASS **** 


	public int getID() {
		return ID;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public Date getDOB() {
		return DOB;
	}

	public String getPhone() {
		return phone;
	}
	
	public boolean isMale() {
		return this.male;
	}
	
	public static Builder builder() {
		
		return new Builder();
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDOB(Date dOB) {
		DOB = dOB;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setMale(boolean male) {
		this.male = male;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}


	public String getSalt() {
		return salt;
	}


	
	
	

}
