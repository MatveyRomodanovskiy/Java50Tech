package telran.students;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

import telran.students.dto.*;
import telran.students.exceptions.*;
import telran.students.model.StudentDoc;
import telran.students.repo.StudentRepo;
import telran.students.service.StudentsService;

import static org.junit.jupiter.api.Assertions.*;
import static telran.students.TestDb.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class StudentsServiceTests {
	private static final long ID_NOT_EXISTS = 100_000;
	private static final String PHONE_NOT_EXISTS = "055-5555555";
	public static final Student STUDENT_NOT_EXISTS = new Student(ID_NOT_EXISTS, PHONE_NOT_EXISTS);
	public static final Student STUDENT_UPDATED = new Student(ID1, PHONE_NOT_EXISTS);
	public static final Mark MARK_NOT_EXISTS = new Mark(SUBJECT1, 100, DATE4);
	public static final String PREFIX_ONE_STUDENT = "052";
	public static final String PREFIX_FEW_STUDENTS = "051";
	public static final String PREFIX_NO_STUDENT = "060";
	public static final LocalDate DATE_NO_MARKS = LocalDate.now().minusYears(10);
	
	@Autowired
	StudentRepo studentRepo;
	@Autowired
	StudentsService studentsService;
	@Autowired
	TestDb testDb;
	
	
	@BeforeEach
	void setUp() {
		testDb.createDb();
	}

	@Test
	void addStudent_correctFlow_success() {
		assertNull(studentRepo.findById(ID_NOT_EXISTS).orElse(null));
		
		Student student = studentsService.addStudent(STUDENT_NOT_EXISTS);
		assertEquals(STUDENT_NOT_EXISTS, student);
		
		assertEquals(STUDENT_NOT_EXISTS, studentRepo.findById(ID_NOT_EXISTS).orElseThrow().build());
	}
	
	@Test
	void addStudent_studentAlreadyExists_throwsException() {
		long count = studentRepo.count();
		StudentDoc existingStudent = studentRepo.findById(ID1).orElseThrow();
		
		assertThrowsExactly(DuplicateKeyException.class, () -> studentsService.addStudent(students[0]));
		assertEquals(count, studentRepo.count());

		assertArrayEquals(marks[0], existingStudent.getMarks().toArray(Mark[]::new));
	}
	
	@Test
	void updatePhoneNumber_correctFlow_success() {
		assertEquals(students[0], studentRepo.findById(ID1).orElse(null).build());
		Student student = studentsService.updatePhoneNumber(ID1, PHONE_NOT_EXISTS);
		assertEquals(STUDENT_UPDATED, student);
		
		assertEquals(STUDENT_UPDATED, studentRepo.findById(ID1).orElseThrow().build());
	}
	
	@Test
	void updatePhoneNumber_studentNotExists_throwsException() {
		long count = studentRepo.count();
		assertThrowsExactly(StudentNotFoundException.class, () -> studentsService.updatePhoneNumber(ID_NOT_EXISTS, PHONE_NOT_EXISTS));
		
		assertEquals(count, studentRepo.count());
		assertNull(studentRepo.findById(ID_NOT_EXISTS).orElse(null));
	}
	
	@Test
	void addMark_correctFlow_success() {
		StudentDoc studentDoc = studentRepo.findById(ID1).orElseThrow();
		assertFalse(studentDoc.getMarks().contains(MARK_NOT_EXISTS));
		
		Mark mark = studentsService.addMark(ID1, MARK_NOT_EXISTS);
		assertEquals(MARK_NOT_EXISTS, mark);
		
		ArrayList<Mark> marks = new ArrayList<>(studentDoc.getMarks());
		marks.add(mark);
		
		assertIterableEquals(marks, studentRepo.findById(ID1).orElseThrow().getMarks());
	}
	
	@Test
	void addMark_studentNotExists_throwsException() {
		long count = studentRepo.count();
		assertThrowsExactly(StudentNotFoundException.class, () -> studentsService.addMark(ID_NOT_EXISTS, MARK_NOT_EXISTS));
		
		assertEquals(count, studentRepo.count());
		assertNull(studentRepo.findById(ID_NOT_EXISTS).orElse(null));
	}
	
	@Test
	void addMark_markAlreadyExists_throwsException() {
//		assertThrowsExactly(MarkIllegalStateException.class, () -> studentsService.addMark(ID1, marks[0][0]));
		assertArrayEquals(marks[0], studentRepo.findById(ID1).orElseThrow().getMarks().toArray(Mark[]:: new));
	}
	
	@Test
	void getStudent_correctFlow_success() {
		assertEquals(students[0], studentsService.getStudent(ID1));
	}
	
	@Test 
	void getStudent_studentNotExists_throwsException() {
		long count = studentRepo.count();
		assertThrowsExactly(StudentNotFoundException.class, () -> studentsService.getStudent(ID_NOT_EXISTS));
		assertEquals(count, studentRepo.count());
	}
	
	@Test
	void getMarks_correctFlow_success() {
		assertArrayEquals(marks[0], studentsService.getMarks(ID1).toArray(Mark[]::new));
	}
	
	@Test
	void getMarks_noMarks_emptyList() {
		assertArrayEquals(marks[6], studentsService.getMarks(ID7).toArray(Mark[]::new));
	}
	
	@Test
	void getMarks_studentNotExists_throwsException() {
		long count = studentRepo.count();
		assertThrowsExactly(StudentNotFoundException.class, () -> studentsService.getMarks(ID_NOT_EXISTS));
		assertEquals(count, studentRepo.count());
	}
	
	@Test
	void getStudentByPhoneNumber_correctFlow_success() {
		assertEquals(students[0], studentsService.getStudentByPhoneNumber(PHONE1));
	}
	
	@Test
	void getStudentByPhoneNumber_studentNotExists_returnsNull() {
		long count = studentRepo.count();
		assertNull(studentsService.getStudentByPhoneNumber(PHONE_NOT_EXISTS));
		assertEquals(count, studentRepo.count());
	}
	
	@Test
	void getStudentsByPhonePrefix_oneStudent_success() {
		List<Student> expected = List.of(students[1]);
		assertIterableEquals(expected, studentsService.getStudentsByPhonePrefix(PREFIX_ONE_STUDENT));
	}
	
	@Test
	void removeStudentTest() {
		assertEquals(students[0], studentsService.removeStudent(ID1));
		assertNull(studentRepo.findById(ID1).orElse(null));
		assertThrowsExactly(StudentNotFoundException.class, () -> studentsService.removeStudent(ID1));
	}
	@Test
	void getStudentsByPhonePrefix_fewStudents_success() {
		List<Student> expected = List.of(students[0], students[6]);
		assertIterableEquals(expected, studentsService.getStudentsByPhonePrefix(PREFIX_FEW_STUDENTS));
	}
	
	@Test
	void getStudentsByPhonePrefix_studentNotExists_emptyList() {
		assertIterableEquals(new ArrayList<Student>(), studentsService.getStudentsByPhonePrefix(PREFIX_NO_STUDENT));
	}
	
	@Test
	void getStudentsMarksDate_correctFlow_success() {
		List<Student> expected = List.of(students[0], students[1], students[2], students[5]);
		assertIterableEquals(expected, studentsService.getStudentsMarksDate(DATE1));
	}
	
	@Test
	void getStudentsMarksDate_noStudents_emptyList() {
		assertArrayEquals(new Student[0], studentsService.getStudentsMarksDate(DATE_NO_MARKS).toArray(Student[]::new));
	}
	
	@Test
	void getStudentsMarksMonthYear_correctFlow_success() {
		List<Student> expected = List.of(students[0], students[1], students[2], students[5]);
		assertIterableEquals(expected, studentsService.getStudentsMarksMonthYear(DATE1.getMonthValue(), DATE1.getYear()));
	}
	
	@Test
	void getStudentsMarksMonthYear_noStudents_emptyList() {
		assertArrayEquals(new Student[0], studentsService.getStudentsMarksMonthYear(DATE_NO_MARKS.getMonthValue(), DATE_NO_MARKS.getYear()).toArray(Student[]::new));
	}
	
	@Test
	void getStudentsGoodSubjectMark_correctFlow_success() {
		List<Student> expected = List.of(students[0], students[5]);
		assertIterableEquals(expected, studentsService.getStudentsGoodSubjectMark(SUBJECT1, 75));
	}
	
	@Test
	void getStudentsGoodSubjectMark_noStudents_emptyList() {
		assertArrayEquals(new Student[0], studentsService.getStudentsGoodSubjectMark(SUBJECT1, 100).toArray(Student[]::new));
	}
	
	@Test 
	void getStudentsAllGoodMarks_correctFlow_success() {
		List<Student> expected = List.of(students[4], students[5]);
		assertIterableEquals(expected, studentsService.getStudentsAllGoodMarks(70));
	}
	
	@Test 
	void getStudentsAllGoodMarks_noStudent_emptyList() {
		assertTrue(studentsService.getStudentsAllGoodMarks(100).isEmpty());
	}
	
	@Test
	void getStudentMarksSubject_correctFlow_success() {
		List<Mark> expected = List.of(marks[0][0], marks[0][1]);
		assertIterableEquals(expected, studentsService.getStudentMarksSubject(ID1, SUBJECT1));
	}
	
	@Test
	void getStudentMarksSubject_noMark_emptyList() {
		assertTrue(studentsService.getStudentMarksSubject(ID1, SUBJECT3).isEmpty());
	}
	
	@Test
	void getStudentMarksSubject_noStudent_throwsException() {
		assertThrowsExactly(StudentNotFoundException.class, () -> studentsService.getStudentMarksSubject(ID_NOT_EXISTS, SUBJECT1));
	}
	
	@Test
	void getStudentsFewMarks_correctFlow_success() {
		List<Student> expected = List.of(students[6]);
		assertIterableEquals(expected, studentsService.getStudentsFewMarks(1));
	}
	
	@Test
	void getStudentsFewMarks_noStudent_emptyList() {
		assertTrue(studentsService.getStudentsFewMarks(0).isEmpty());
	}
	
	@Test
	void getStudentsAvgScoreGreater_correctFlow_success() {
		List<StudentAvgScore> expected = List.of(new StudentAvgScore(ID6, 100), new StudentAvgScore(ID5, 95));
		assertIterableEquals(expected, studentsService.getStudentsAvgScoreGreater(90));
	}
	
	@Test
	void getStudentsAllGoodMarksSubject_correctFlow_success() {
		List<Student> expected = List.of(students[0], students[5]);
		assertIterableEquals(expected, studentsService.getStudentsAllGoodMarksSubject(SUBJECT2, 70));
	}
	
	@Test
	void getStudentsAllGoodMarksSubject_noStudent_emptyList() {
		assertTrue(studentsService.getStudentsAllGoodMarksSubject(SUBJECT2, 100).isEmpty());
	}
	
	@Test
	void getStudentsMarksAmountBetween_correctFlow_success() {
		List<Student> expected = List.of(students[3], students[4]);
		assertIterableEquals(expected, studentsService.getStudentsMarksAmountBetween(1, 2));
	}
	
	@Test
	void getStudentsMarksAmountBetween_noStudent_emptyList() {
		assertTrue(studentsService.getStudentsMarksAmountBetween(10, 20).isEmpty());
	}
	
	@Test
	void getStudentMarksAtDates_correctFlow_success() {
		List<Mark> expected = List.of(marks[0][0], marks[0][1]);
		assertIterableEquals(expected, studentsService.getStudentMarksAtDates(ID1, DATE1, DATE2));
	}
	
	@Test
	void getStudentMarksAtDates_noMarks_emptyList() {
		assertTrue(studentsService.getStudentMarksAtDates(ID1, DATE_NO_MARKS, DATE_NO_MARKS).isEmpty());
	}
	
	@Test
	void getStudentMarksAtDates_studentNotExists_throwsException() {
		assertThrowsExactly(StudentNotFoundException.class, () -> studentsService.getStudentMarksAtDates(ID_NOT_EXISTS, DATE1, DATE2));
	}
	
	@Test
	void getBestStudents_correctFlow_success() {
		List<Long> expected = List.of(ID6);
		assertIterableEquals(expected, studentsService.getBestStudents(1));
	}
	
	@Test
	void getWorstStudents_correctFlow_success() {
		List<Long> expected = List.of(ID7);
		assertIterableEquals(expected, studentsService.getWorstStudents(1));
	}
	


}