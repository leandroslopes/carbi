package br.com.carbi.collection;

import java.util.Collection;
import java.util.Vector;

import br.com.carbi.model.Student;

public class StudentsCollection {

	private Vector<Student> colletion;
	
	public StudentsCollection() {
		colletion = new Vector<Student>();
	}
	
	public void add(Student student) {
		colletion.add(student);
	}
	
	public Collection<Student> getColletion() {
		return colletion;
	}
	
	public void removeAllElements() {
		colletion.clear();
	}
}
