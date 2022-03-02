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
import model.ModelEnums.Numbers;
import model.ModelEnums.Messages;

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
	private String nameValid1, nameValid2LowerCaseTrailingSpace, nameValid3MixedCaseLeadingSpace, nameValid3Sanitised, nameValid4LengthTwo, nameValid4Sanitised;
	private String emailValid1, emailValid2SpaceAtEnd, emailValid2Sanitised, nameValid2Sanitised, emailValid3SpaceAtStart, emailValid3Sanitised, emailValid4;
	private String phoneNumberValid1, phoneNumberValid2WithPlusSymbol, phoneNumberValid3;

	// Person invalid data
	private String emailInvalidLocalStart, emailInvalidLocalEnd, emailInvalidLocalChars;
	private String emailInvalidDomainStart, emailInvalidDomainEnd, emailInvalidDomainChars;
	private String emailInvalidSubdomain, emailInvalidNoLocal, emailInvalidNoDomain;
	private String emailInvalidNoAtSymbol, emailInvalidNoAtNoLocal, emailInvalidNoAtNoDomain;

	// Employee valid data
	private int hourlyRateInPenceValid1LowerBound, hourlyRateInPenceValid2, hourlyRateInPenceValid3;
	private double weeklyHoursValid1, weeklyHoursValid2, weeklyHoursValid3, weeklyHoursValid4Zero, weeklyHoursValid5Max;
	private LocalDate startDateValid1, startDateValid2, startDateValid3;
	private LocalDate endDateValid1, endDateValid2, endDateValid3;

	// Employee invalid data
	private int hourlyRateInPenceInvalid1BVAInvalidUpper, hourlyRateInPenceInvalid2BVAInvalidNegative;
	private double weeklyHoursInvalid1Negative, weeklyHoursInvalid2OverMax;

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
		nameValid2LowerCaseTrailingSpace = "ricKy ";
		nameValid2Sanitised = "Ricky";
		nameValid3MixedCaseLeadingSpace = " buBblEs";
		nameValid3Sanitised = "Bubbles";
		nameValid4LengthTwo = "ab";
		nameValid4Sanitised = "Ab";

		emailValid1 = "test@email.one";
		emailValid2SpaceAtEnd = "another@test.email.two ";
		emailValid2Sanitised = "another@test.email.two";
		emailValid3SpaceAtStart = " andanother@test.email.three";
		emailValid3Sanitised = "andanother@test.email.three";
		emailValid4 = "standard@valid.email";

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
		weeklyHoursValid4Zero = 0.0;
		weeklyHoursValid5Max = Numbers.EMPLOYEE_MAX_WEEKLY_HOURS.get();

		startDateValid1 = LocalDate.of(2000, 06, 25);
		startDateValid2 = LocalDate.of(2010, 01, 15);
		startDateValid3 = LocalDate.of(2020, 10, 1);

		endDateValid1 = LocalDate.of(2000, 06, 25);
		endDateValid2 = LocalDate.of(2010, 01, 16);
		endDateValid3 = LocalDate.of(2021, 01, 26);

		// Employee invalid data
		hourlyRateInPenceInvalid1BVAInvalidUpper = 799;
		hourlyRateInPenceInvalid2BVAInvalidNegative = -201;

		weeklyHoursInvalid1Negative = -1;
		weeklyHoursInvalid2OverMax = Numbers.EMPLOYEE_MAX_WEEKLY_HOURS.get() + 1;

		// test objects
		employee = model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		System.out.println(startDateValid1);
	}

	/**
	 * Check Employee object from setUp()
	 */
	@Test
	void testConstructor_Person_Valid_AllArgs() {
		// capitalised first letter of name, and rest lower case, no leading or trailing spaces

		assertEquals(nameValid1, employee.getForename());
		assertEquals(nameValid2Sanitised, employee.getSurname());
		assertEquals(emailValid1, employee.getEmail());
		assertEquals(phoneNumberValid1, employee.getPhoneNumber());
		assertEquals(hourlyRateInPenceValid2, employee.getHourlyRate());
		assertEquals(weeklyHoursValid1, employee.getHoursPerWeek());
		assertEquals(startDateValid1, employee.getStartDateAsLocalDate());
		assertEquals(endDateValid1, employee.getEndDateAsLocalDate());
	}

	/**
	 * Forename and surname parameters in constructor valid
	 */
	@Test
	void testConstructor_Person_Valid_ForenameAndSurnameArgs() {
		// valid names with leading and trailing spaces
		Employee e = model.new Employee(nameValid3MixedCaseLeadingSpace, nameValid2LowerCaseTrailingSpace, emailValid3SpaceAtStart, phoneNumberValid2WithPlusSymbol, hourlyRateInPenceValid2,
				weeklyHoursValid4Zero, startDateValid3, endDateValid3);
		assertEquals(nameValid3Sanitised, e.getForename());
		assertEquals(nameValid2Sanitised, e.getSurname());

		// minimum length names
		e = model.new Employee(nameValid4LengthTwo, nameValid4LengthTwo, emailValid3SpaceAtStart, phoneNumberValid2WithPlusSymbol,
				hourlyRateInPenceValid2,
				weeklyHoursValid4Zero, startDateValid3, endDateValid3);
		assertEquals(nameValid4Sanitised, e.getForename());
		assertEquals(nameValid4Sanitised, e.getSurname());

	}

	/**
	 * Forename and surname parameters in constructor invalid
	 */
	@Test
	void testConstructor_Person_Invalid_ForenameAndSurnameArgs() {
		IllegalArgumentException e;

		// forename blank
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee("", nameValid2LowerCaseTrailingSpace, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(Messages.PERSON_SET_NAME_BLANK.get(), e.getMessage());

		// forename null
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(null, nameValid2LowerCaseTrailingSpace, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(Messages.PERSON_SET_NAME_NULL.get(), e.getMessage());

		// surname blank
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, "", emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(Messages.PERSON_SET_NAME_BLANK.get(), e.getMessage());

		// surname null
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, null, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(Messages.PERSON_SET_NAME_NULL.get(), e.getMessage());

		// forename 1 char long
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee("a", nameValid1, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(Messages.PERSON_SET_NAME_TOO_FEW_CHARS.get(), e.getMessage());

		// surname 1 char long
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, "z ", emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(Messages.PERSON_SET_NAME_TOO_FEW_CHARS.get(), e.getMessage());

	}

	/**
	 * Email parameter in constructor valid
	 */
	@Test
	void testConstructor_Person_Valid_Email() {
		// email set to null if blank 0-length string
		Employee e = model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, "", phoneNumberValid1, hourlyRateInPenceValid3, weeklyHoursValid1, startDateValid1, endDateValid2);
		assertNull(e.getEmail());

		// email set to null if blank 1-length string
		e = model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, " ", phoneNumberValid1, hourlyRateInPenceValid3, weeklyHoursValid1, startDateValid1, endDateValid2);
		assertNull(e.getEmail());

		// email set to null if null
		e = model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, null, phoneNumberValid1, hourlyRateInPenceValid3, weeklyHoursValid1, startDateValid1, endDateValid2);
		assertNull(e.getEmail());

		// email set to sanitised valid (trailing space)
		e = model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailValid2SpaceAtEnd, phoneNumberValid1, hourlyRateInPenceValid3, weeklyHoursValid1, startDateValid1, endDateValid2);
		assertNotNull(e.getEmail());
		assertEquals(emailValid2Sanitised, e.getEmail());

		// email set to sanitised valid (leading space)
		e = model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailValid3SpaceAtStart, phoneNumberValid1, hourlyRateInPenceValid3, weeklyHoursValid1, startDateValid1, endDateValid2);
		assertNotNull(e.getEmail());
		assertEquals(emailValid3Sanitised, e.getEmail());

		// email set to valid email
		e = model.new Employee(nameValid1, nameValid1, emailValid4, phoneNumberValid2WithPlusSymbol, hourlyRateInPenceValid2, weeklyHoursValid3, startDateValid1, endDateValid1);
		assertNotNull(e.getEmail());
		assertEquals(emailValid4, e.getEmail());
	}

	/**
	 * Email parameter in constructor invalid
	 */
	@Test
	void testConstructor_Person_Invalid_EmailArg() {
		IllegalArgumentException e;

		/*
		 * LOCAL
		 */
		// email : local start invalid
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidLocalStart, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(Messages.PERSON_SET_EMAIL_INVALID.get() + emailInvalidLocalStart, e.getMessage());

		// email : local end invalid
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidLocalEnd, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(Messages.PERSON_SET_EMAIL_INVALID.get() + emailInvalidLocalEnd, e.getMessage());

		// email : local chars invalid
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidLocalChars, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(Messages.PERSON_SET_EMAIL_INVALID.get() + emailInvalidLocalChars, e.getMessage());

		/*
		 * DOMAIN
		 */
		// email : domain start invalid
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidDomainStart, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(Messages.PERSON_SET_EMAIL_INVALID.get() + emailInvalidDomainStart, e.getMessage());

		// email : domain end invalid
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidDomainEnd, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(Messages.PERSON_SET_EMAIL_INVALID.get() + emailInvalidDomainEnd, e.getMessage());

		// email : domain chars invalid
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidDomainChars, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(Messages.PERSON_SET_EMAIL_INVALID.get() + emailInvalidDomainChars, e.getMessage());

		/*
		 * SUB-DOMAIN
		 */
		// email : domain start invalid
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidSubdomain, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(Messages.PERSON_SET_EMAIL_INVALID.get() + emailInvalidSubdomain, e.getMessage());

		/**
		 * MISSING LOCAL OR DOMAIN
		 */
		// email : no domain
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidNoDomain, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(Messages.PERSON_SET_EMAIL_INVALID.get() + emailInvalidNoDomain, e.getMessage());

		// email : no local
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidNoLocal, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(Messages.PERSON_SET_EMAIL_INVALID.get() + emailInvalidNoLocal, e.getMessage());

		/*
		 * NO @ SYMBOL
		 */
		// email : no @
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidNoAtSymbol, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(Messages.PERSON_SET_EMAIL_INVALID.get() + emailInvalidNoAtSymbol, e.getMessage());

		// email : no @ nor domain
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidNoAtNoDomain, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(Messages.PERSON_SET_EMAIL_INVALID.get() + emailInvalidNoAtNoDomain, e.getMessage());

		// email : no @ nor local
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailInvalidNoAtNoLocal, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		});
		assertEquals(Messages.PERSON_SET_EMAIL_INVALID.get() + emailInvalidNoAtNoLocal, e.getMessage());
	}

	/**
	 * PhoneNumber parameter in constructor valid
	 */
	@Test
	void testConstructor_Person_Valid_PhoneNumberArg() {
		// phoneNumber with + symbol
		employee = model.new Employee(nameValid1, nameValid1, emailValid1, phoneNumberValid2WithPlusSymbol, hourlyRateInPenceValid3, weeklyHoursValid2, startDateValid2, startDateValid2);
		assertNotNull(employee.getPhoneNumber());
		assertEquals(phoneNumberValid2WithPlusSymbol, employee.getPhoneNumber());

		// phoneNumber null
		employee = model.new Employee(nameValid1, nameValid1, emailValid1, null, hourlyRateInPenceValid3, weeklyHoursValid2, startDateValid2, startDateValid2);
		assertNull(employee.getPhoneNumber());

		// phoneNumber blank 0-length String
		employee = model.new Employee(nameValid1, nameValid1, emailValid1, "", hourlyRateInPenceValid3, weeklyHoursValid2, startDateValid2, startDateValid2);
		assertNull(employee.getPhoneNumber());

		// phoneNumber blank 1-length String
		employee = model.new Employee(nameValid1, nameValid1, emailValid1, " ", hourlyRateInPenceValid3, weeklyHoursValid2, startDateValid2, startDateValid2);
		assertNull(employee.getPhoneNumber());
	}

	/**
	 * HourlyRateInPence parameter in constructor valid
	 */
	@Test
	void testConstructor_Employee_Valid_HourlyRateInPence() {
		Employee e;
		// minimum wage BVA boundary
		e = model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailValid1, phoneNumberValid1, hourlyRateInPenceValid1LowerBound, weeklyHoursValid1, startDateValid1, endDateValid1);
		assertEquals(hourlyRateInPenceValid1LowerBound, e.getHourlyRate());

		// minimum wage EP value
		e = model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		assertEquals(hourlyRateInPenceValid2, e.getHourlyRate());

		// minimum wage EP value
		e = model.new Employee(nameValid1, nameValid2LowerCaseTrailingSpace, emailValid1, phoneNumberValid1, hourlyRateInPenceValid3, weeklyHoursValid1, startDateValid1, endDateValid1);
		assertEquals(hourlyRateInPenceValid3, e.getHourlyRate());
	}

	/**
	 * HourlyRateInPence parameter in constructor invalid
	 */
	@Test
	void testConstructor_Employee_Invalid_HourlyRateInPenceArg() {
		IllegalArgumentException e;

		// hourlyRateInPence below minimum wage (BVA)
		e = assertThrows(IllegalArgumentException.class, () -> {
			employee = model.new Employee(nameValid1, nameValid3MixedCaseLeadingSpace, emailValid1, phoneNumberValid1, hourlyRateInPenceInvalid1BVAInvalidUpper, weeklyHoursValid1, startDateValid1,
					endDateValid1);
		});
		assertEquals(String.format("Hourly rate cannot be set to below minimum wage. Minimum wage is £%.2f but specified Hourly Rate was £%.2f.",
				(Numbers.INT_MIN_WAGE.get() / 100.0), (hourlyRateInPenceInvalid1BVAInvalidUpper / 100.0)), e.getMessage());

		// hourlyRateInPence negative
		e = assertThrows(IllegalArgumentException.class, () -> {
			employee = model.new Employee(nameValid1, nameValid3MixedCaseLeadingSpace, emailValid1, phoneNumberValid1, hourlyRateInPenceInvalid2BVAInvalidNegative, weeklyHoursValid1, startDateValid1,
					endDateValid1);
		});
		assertEquals(String.format("Hourly rate cannot be set to below minimum wage. Minimum wage is £%.2f but specified Hourly Rate was £%.2f.",
				(Numbers.INT_MIN_WAGE.get() / 100.0), (hourlyRateInPenceInvalid2BVAInvalidNegative / 100.0)), e.getMessage());
	}

	/**
	 * HoursPerWeek parameter in constructor valid
	 */
	@Test
	void testConstructor_Employee_Valid_HoursPerWeek() {
		Employee e;

		// hoursPerWeek valid value 1
		e = model.new Employee(nameValid1, nameValid1, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		assertEquals(weeklyHoursValid1, e.getHoursPerWeek());

		// hoursPerWeek valid value 2
		e = model.new Employee(nameValid1, nameValid1, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid2, startDateValid1, endDateValid1);
		assertEquals(weeklyHoursValid2, e.getHoursPerWeek());

		// hoursPerWeek valid value 3
		e = model.new Employee(nameValid1, nameValid1, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid3, startDateValid1, endDateValid1);
		assertEquals(weeklyHoursValid3, e.getHoursPerWeek());

		// hoursPerWeek valid value 4 (Zero!)
		e = model.new Employee(nameValid1, nameValid1, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid4Zero, startDateValid1, endDateValid1);
		assertEquals(weeklyHoursValid4Zero, e.getHoursPerWeek());

		// hoursPerWeek valid value 5 (Max)
		e = model.new Employee(nameValid1, nameValid1, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid5Max, startDateValid1, endDateValid1);
		assertEquals(weeklyHoursValid5Max, e.getHoursPerWeek());
	}

	/**
	 * HoursPerWeek parameter in constructor invalid
	 */
	@Test
	void testConstructor_Employee_Invalid_HoursPerWeek() {
		IllegalArgumentException e;

		// weeklyHours negative
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid1, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursInvalid1Negative, startDateValid1, endDateValid1);
		});
		assertEquals(Messages.EMPLOYEE_SET_WEEKLY_HOURS_NEGATIVE.get(), e.getMessage());

		// weeklyHours over maximum allowed
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid1, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursInvalid2OverMax, startDateValid1, endDateValid1);
		});
		assertEquals(Messages.EMPLOYEE_SET_WEEKLY_HOURS_OVER_MAX.get(), e.getMessage());
	}

	/**
	 * StartDate/ EndDate parameter in constructor valid
	 */
	@Test
	void testConstructor_Employee_Valid_StartDateEndDate() {
		Employee e;

		// valid start/end date 1
		e = model.new Employee(nameValid1, nameValid1, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, endDateValid1);
		assertEquals(startDateValid1, e.getStartDateAsLocalDate());
		assertEquals(endDateValid1, e.getEndDateAsLocalDate());

		// valid start/end date 2
		e = model.new Employee(nameValid1, nameValid1, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid2, endDateValid2);
		assertEquals(startDateValid2, e.getStartDateAsLocalDate());
		assertEquals(endDateValid2, e.getEndDateAsLocalDate());

		// valid start/end date 3
		e = model.new Employee(nameValid1, nameValid1, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid3, endDateValid3);
		assertEquals(startDateValid3, e.getStartDateAsLocalDate());
		assertEquals(endDateValid3, e.getEndDateAsLocalDate());

		// valid startDate 1 - endDate Null
		e = model.new Employee(nameValid1, nameValid1, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid1, null);
		assertEquals(startDateValid1, e.getStartDateAsLocalDate());
		assertNull(e.getEndDateAsLocalDate());

		// valid startDate 2 - endDate Null
		e = model.new Employee(nameValid1, nameValid1, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid2, null);
		assertEquals(startDateValid2, e.getStartDateAsLocalDate());
		assertNull(e.getEndDateAsLocalDate());

		// valid startDate 2 - endDate Null
		e = model.new Employee(nameValid1, nameValid1, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid3, null);
		assertEquals(startDateValid3, e.getStartDateAsLocalDate());
		assertNull(e.getEndDateAsLocalDate());
	}

	/**
	 * StartDate parameter in constructor invalid
	 */
	@Test
	void testConstructor_Employee_Invalid_StartDate() {
		IllegalArgumentException e;

		// start date null
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid1, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, null, null);
		});
		assertEquals(Messages.EMPLOYEE_SET_DATE_OF_START_NULL.get(), e.getMessage());
	}

	@Test
	void testConstructor_Employee_Invalid_EndDate() {
		IllegalArgumentException e;

		// end date before start date 1
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid1, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid3, endDateValid2);
		});
		assertEquals(Messages.EMPLOYEE_SET_DATE_OF_END_IMPOSSIBLE.get(), e.getMessage());

		// end date before start date 2
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid1, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, startDateValid2, endDateValid1);
		});
		assertEquals(Messages.EMPLOYEE_SET_DATE_OF_END_IMPOSSIBLE.get(), e.getMessage());

		// end date before start date 2
		e = assertThrows(IllegalArgumentException.class, () -> {
			model.new Employee(nameValid1, nameValid1, emailValid1, phoneNumberValid1, hourlyRateInPenceValid2, weeklyHoursValid1, LocalDate.now(), LocalDate.now().minusDays(1));
		});
		assertEquals(Messages.EMPLOYEE_SET_DATE_OF_END_IMPOSSIBLE.get(), e.getMessage());
	}
}
