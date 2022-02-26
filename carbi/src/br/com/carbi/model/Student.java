package br.com.carbi.model;

public class Student {

	private int id;
	private int registration;
	private String name;
	private String course;
	
	public Student() {
		
	}

	public Student(int registration, String name, String course) {
		this.registration = registration;
		this.name = name;
		this.course = course;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getRegistration() {
		return registration;
	}

	public void setRegistration(int registration) {
		this.registration = registration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}
}
