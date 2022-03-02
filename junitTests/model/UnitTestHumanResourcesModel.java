package model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import model.HumanResourcesModel.Employee;
import model.HumanResourcesModel.EmployeeNumbers;
import model.HumanResourcesModel.EmployeeStrings;
import model.HumanResourcesModel.PersonStrings;

/**
 * Unit tests of both abstract superclass Person and its concrete subclass Employee
 * 
 * @author Peter Marley
 * @StudentNumber 13404067
 * @Email pmarley03@qub.ac.uk
 * @GitHub https://github.com/PeterMarley
 *
 */
class UnitTestHumanResourcesModel {

	// Person valid data
	private String nameValid1, nameValid2LowerCaseTrailingSpace, nameValid3MixedCaseLeadingSpace;
	private String emailValid1, emailValid2SpaceAtEnd, emailValid3SpaceAtStart;
	private String phoneNumberValid1, phoneNumberValid2WithPlusSymbol, phoneNumberValid3;

	// Person invalid data
	private String emailInvalidLocalStart, emailInvalidLocalEnd, emailInvalidLocalChars;
	private String emailInvalidDomainStart, emailInvalidDomainEnd, emailInvalidDomainChars;
	private String emailInvalidSubdomain, emailInvalidNoLocal, emailInvalidNoDomain;
	private String emailInvalidNoAtSymbol, emailInvalidNoAtNoLocal, emailInvalidNoAtNoDomain;

	// Employee valid data
	private int hourlyRateInPenceValid1LowerBound, hourlyRateInPenceValid2, hourlyRateInPenceValid3;
	private double weeklyHoursValid1, weeklyHoursValid2, weeklyHoursValid3;
	private LocalDate startDateValid1, startDateValid2, startDateValid3;
	private LocalDate endDateValid1, endDateValid2, endDateValid3;

	// Employee invalid data
	private int hourlyRateInPenceInvalid1BVAInvalidUpper, hourlyRateInPenceInvalid2BVAInvalidNegative;

	// test objects
	private Employee employee;
	private static HumanResourcesModel model;

	@BeforeAll
	static void setUpBeforeClass() {
		model = new HumanResourcesModel();
	}

	@BeforeEach
	void setUp() throws Exception {

		// Person valid data
		nameValid1 = "Julian";
		nameValid2LowerCaseTrailingSpace = "ricky ";
		nameValid3MixedCaseLeadingSpace = " buBblEs";

		emailValid1 = "test@email.one";
		emailValid2SpaceAtEnd = "another@test.email.two ";
		emailValid3SpaceAtStart = " andanother@test.email.three";

		phoneNumberValid1 = "1";
		phoneNumberValid2WithPlusSymbol = "+123";
		phoneNumberValid3 = "01232666999";

		// Person invalid data
		emailInvalidNoAtSymbol = "localATdomain1.domain2.tld";

		emailInvalidLocalStart = ".local@domain.tld";
		emailInvalidLocalEnd = "local.@domain.tld";
		emailInvalidLocalChars = "loc:al@domain.tld";

		emailInvalidDomainStart = "local@-domain.tld";
		emailInvalidDomainEnd = "local@domain.tld-";
		emailInvalidDomainChars = "local@doma+in.tld";

		emailInvalidSubdomain = "local@domain.dom^ain.tld";

		emailInvalidNoLocal = "@domain1.domain2.tld";
		emailInvalidNoDomain = "local@";

		emailInvalidNoAtNoLocal = "domain.tld";
		emailInvalidNoAtNoDomain = "local";

		// Employee valid data
		hourlyRateInPenceValid1LowerBound = 800;
		hourlyRateInPenceValid2 = 1200;
		hourlyRateInPenceValid3 = 660066;

		weeklyHoursValid1 = 1.0;
		weeklyHoursValid2 = 22.6786;
		weeklyHoursValid3 = 37.5;

		startDateValid1 = LocalDate.of(2000, 06, 25);
		startDateValid2 = LocalDate.of(2010, 01, 15);
		startDateValid3 = LocalDate.of(2020, 10, 1);

		endDateValid1 = LocalDate.of(2000, 06, 25);
		endDateValid2 = LocalDate.of(2010, 01, 16);
		endDateValid3 = LocalDate.of(2021, 01, 26);

		// Employee invalid data
		hourlyRateInPenceInvalid1BVAInvalidUpper = 799;
		hourlyRateInPenceInvalid2BVAInvalidNegative = -201;
		
		// test objects
		employee = model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
	}

	@Test
	void testConstructor_Employee_Valid_AllArgs() {
		assertEquals(nameValid1, employee.getForename());
		assertEquals(nameValid2LowerCaseTrailingSpace, employee.getSurname());
		assertEquals(emailValid1, employee.getEmail());
		assertEquals(phoneNumberValid1, employee.getPhoneNumber());
		assertEquals(hourlyRateInPenceValid2, employee.getHourlyRate());
		assertEquals(weeklyHoursValid1, employee.getWeeklyHours());
		assertEquals(startDateValid1, employee.getStartDateAsLocalDate());
		assertEquals(endDateValid1, employee.getEndDateAsLocalDate());
	}

	@Test
	void testConstructor_Employee_Valid_ForenameAndSurnameArgs() {
		fail("stub");
	}

	@Test
	void testConstructor_Employee_Invalid_ForenameAndSurnameArgs() {
		IllegalArgumentException e;

		// forename blank
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee("", nameValid2LowerCaseTrailingSpace, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(PersonStrings.MSG_NAME_BLANK.get(), e.getMessage());

		// forename null
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(null, nameValid2LowerCaseTrailingSpace, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(PersonStrings.MSG_NAME_NULL.get(), e.getMessage());

		// surname blank
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, "", emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(PersonStrings.MSG_NAME_BLANK.get(), e.getMessage());

		// surname null
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, null, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(PersonStrings.MSG_NAME_NULL.get(), e.getMessage());

	}

	@Test
	void testConstructor_Employee_Valid_Email() {
		Employee e = model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, "", phoneNumberValid1, hourlyRateInPenceValid3, weeklyHoursValid1, startDateValid1, endDateValid2);
		assertNull(e.getEmail());

		e = model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, null, phoneNumberValid1, hourlyRateInPenceValid3, weeklyHoursValid1, startDateValid1, endDateValid2);
		assertNull(e.getEmail());

		e = model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailValid1 + " ", phoneNumberValid1, hourlyRateInPenceValid3, weeklyHoursValid1, startDateValid1, endDateValid2);
		assertNotNull(e.getEmail());

		e = model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, " " + emailValid1, phoneNumberValid1, hourlyRateInPenceValid3, weeklyHoursValid1, startDateValid1, endDateValid2);
		assertNotNull(e.getEmail());
		assertEquals(emailValid1, e.getEmail());

		e = model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailValid3SpaceAtStart, phoneNumberValid1, hourlyRateInPenceValid3, weeklyHoursValid1, startDateValid1, endDateValid2);
		assertNotNull(e.getEmail());
		assertEquals(emailValid3SpaceAtStart.trim(), e.getEmail());
	}

	@Test
	void testConstructor_Employee_Invalid_EmailArg() {
		IllegalArgumentException e;

		/*
		 * LOCAL
		 */
		// email : local start invalid
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidLocalStart, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(PersonStrings.MSG_EMAIL_INVALID.get() + emailInvalidLocalStart, e.getMessage());

		// email : local end invalid
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidLocalEnd, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(PersonStrings.MSG_EMAIL_INVALID.get() + emailInvalidLocalEnd, e.getMessage());

		// email : local chars invalid
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidLocalChars, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(PersonStrings.MSG_EMAIL_INVALID.get() + emailInvalidLocalChars, e.getMessage());

		/*
		 * DOMAIN
		 */
		// email : domain start invalid
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidDomainStart, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(PersonStrings.MSG_EMAIL_INVALID.get() + emailInvalidDomainStart, e.getMessage());

		// email : domain end invalid
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidDomainEnd, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(PersonStrings.MSG_EMAIL_INVALID.get() + emailInvalidDomainEnd, e.getMessage());

		// email : domain chars invalid
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidDomainChars, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(PersonStrings.MSG_EMAIL_INVALID.get() + emailInvalidDomainChars, e.getMessage());

		/*
		 * SUB-DOMAIN
		 */
		// email : domain start invalid
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidSubdomain, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(PersonStrings.MSG_EMAIL_INVALID.get() + emailInvalidSubdomain, e.getMessage());

		/**
		 * MISSING LOCAL OR DOMAIN
		 */
		// email : no domain
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidNoDomain, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(PersonStrings.MSG_EMAIL_INVALID.get() + emailInvalidNoDomain, e.getMessage());

		// email : no local
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidNoLocal, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(PersonStrings.MSG_EMAIL_INVALID.get() + emailInvalidNoLocal, e.getMessage());

		/*
		 * NO @ SYMBOL
		 */
		// email : no @
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidNoAtSymbol, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(PersonStrings.MSG_EMAIL_INVALID.get() + emailInvalidNoAtSymbol, e.getMessage());

		// email : no @ nor domain
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidNoAtNoDomain, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(PersonStrings.MSG_EMAIL_INVALID.get() + emailInvalidNoAtNoDomain, e.getMessage());

		// email : no @ nor local
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidNoAtNoLocal, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(PersonStrings.MSG_EMAIL_INVALID.get() + emailInvalidNoAtNoLocal, e.getMessage());
	}

	@Test
	void testConstructor_Employee_Valid_PhoneNumberArg() {
		employee = model.new Employee(nameValid1, nameValid1, emailValid1, phoneNumberValid2WithPlusSymbol, hourlyRateInPenceValid3, weeklyHoursValid2, startDateValid2, startDateValid2);
		assertNotNull(employee.getPhoneNumber());
		assertEquals(phoneNumberValid2WithPlusSymbol, employee.getPhoneNumber());

		employee = model.new Employee(nameValid1, nameValid1, emailValid1, null, hourlyRateInPenceValid3, weeklyHoursValid2, startDateValid2, startDateValid2);
		assertNull(employee.getPhoneNumber());

		employee = model.new Employee(nameValid1, nameValid1, emailValid1, "", hourlyRateInPenceValid3, weeklyHoursValid2, startDateValid2, startDateValid2);
		assertNull(employee.getPhoneNumber());
	}

	@Test
	void testConstructor_Employee_Invalid_PhoneNumberArg() {
		fail("stub");
	}

	@Test
	void testContructor_Employee_Invalid_HourlyRateInPenceArg() {
		IllegalArgumentException e;
		
		// just below minimum wage
		e = assertThrows(IllegalArgumentException.class, () -> {
			employee = model.new Employee(nameValid1, nameValid3MixedCaseLeadingSpace, emailValid1, phoneNumberValid1, hourlyRateInPenceInvalid1BVAInvalidUpper, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(String.format("Hourly rate cannot be set to below minimum wage. Minimum wage is £%.2f but specified Hourly Rate was £%.2f.",
				(EmployeeNumbers.INT_MIN_WAGE.get() / 100.0), (hourlyRateInPenceInvalid1BVAInvalidUpper / 100.0)), e.getMessage());
		
		// just above zero
		e = assertThrows(IllegalArgumentException.class, () -> {
			employee = model.new Employee(nameValid1, nameValid3MixedCaseLeadingSpace, emailValid1, phoneNumberValid1, hourlyRateInPenceInvalid2BVAInvalidNegative, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(String.format("Hourly rate cannot be set to below minimum wage. Minimum wage is £%.2f but specified Hourly Rate was £%.2f.",
				(EmployeeNumbers.INT_MIN_WAGE.get() / 100.0), (hourlyRateInPenceInvalid2BVAInvalidNegative / 100.0)), e.getMessage());
	}
}
