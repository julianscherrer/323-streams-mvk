package ch.bbw.m323.streams;

import java.util.List;

import ch.bbw.m323.streams.PersonStreamTest.Person.Country;
import ch.bbw.m323.streams.PersonStreamTest.Person.Gender;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

class PersonStreamTest implements WithAssertions {

	record Person(String name, int age, Gender gender, Country country) {

		enum Gender {
			MALE, FEMALE, NON_BINARY
		}

		record Country(String name, long population) {
		}

		public boolean isAdult() {
			return age >= 18;
		}
	}

	final Country france = new Country("France", 65_235_184L);

	final Country canada = new Country("Canada", 37_653_095L);

	final Country uk = new Country("United Kingdom", 67_791_734L);

	final List<Person> people = List.of(
			new Person("Brent", 50, Gender.MALE, canada),
			new Person("Luca", 22, Gender.MALE, canada),
			new Person("May", 12, Gender.FEMALE, france),
			new Person("Jojo", 23, Gender.NON_BINARY, uk),
			new Person("Maurice", 15, Gender.MALE, france),
			new Person("Alice", 15, Gender.FEMALE, france),
			new Person("Laurence", 22, Gender.MALE, france),
			new Person("Samantha", 67, Gender.FEMALE, canada));

	// tag::sample[]
	@Test
	void allNamesUppercase() { // Alle Namen UPPERCASE.
		// Dies ist eine Beispielimplementation, wie eine Lösung auszusehen hat.
		// Die Spielregel wurde eingehalten: nur ein `;` am Ende der Funktion
		assertThat(people.stream() // ein Stream<Person>
				.map(Person::name) // ein Stream<String> mit allen Namen. Dasselbe wie `.map(x -> x,name())`.
				.map(String::toUpperCase) // ein Stream<String> mit UPPERCASE-Namen
				.toList() // eine List<String>
		).containsExactly("BRENT", "LUCA", "MAY", "JOJO", "MAURICE", "ALICE", "LAURENCE", "SAMANTHA");
	}
	// end::sample[]
	//Aufgabe 1 Alle Namen mit maximal 4 Buchstaben - nutze map(…) und filter(…)
	@Test
	void namesMaxFourChars() {
		assertThat(people.stream()
				.map(Person::name)
				.filter(name -> name.length() <= 4)
				.toList()
		).containsOnly("Luca", "May", "Jojo");
	}
	//Aufgabe 2 Die Summe des Alters aller Personen – nutze mapToInt(…)
	@Test
	void sumOfAges() {
		assertThat(people.stream()
				.mapToInt(Person::age)
				.sum()
		).isEqualTo(226);
	}
	//Aufgabe 3 Das Alter der ältesten Person
	@Test
	void oldestAge() {
		assertThat(people.stream()
				.mapToInt(Person::age)
				.max()
				.getAsInt()
		).isEqualTo(67);
	}
	//Aufgabe 4 Alle kanadischen Männer
	@Test
	void canadianMales() {
		assertThat(people.stream()
				.filter(p -> p.gender() == Gender.MALE && p.country().equals(canada))
				.toList()
		).hasSize(2).allSatisfy(x -> assertThat(x).isInstanceOf(Person.class));
	}
	//Aufgabe 5 Alle Namen mit Underline zusammengehängt – finde einen passenden Collector für .collect(…)
	@Test
	void namesJoinedWithUnderscore() {
		assertThat(people.stream()
				.map(Person::name)
				.collect(java.util.stream.Collectors.joining("_"))
		).hasSize(51).contains("_");
	}
	//Aufgabe 6 Alle Frauen aus Ländern mit maximal 1 Mio. Einwohnern
	@Test
	void womenFromSmallCountries() {
		assertThat(people.stream()
				.filter(p -> p.gender() == Gender.FEMALE && p.country().population() <= 1_000_000)
				.toList()
		).isEmpty();
	}
	//Aufgabe 7 Die Namen aller Männer nach Alter sortiert – nutze .sorted(…)
	@Test
	void maleNamesSortedByAge() {
		assertThat(people.stream()
				.filter(p -> p.gender() == Gender.MALE)
				.sorted(java.util.Comparator.comparingInt(Person::age))
				.map(Person::name)
				.toList()
		).containsExactly("Maurice", "Luca", "Laurence", "Brent");
	}
	//Aufgabe 8 Die zweitälteste Frau in der Liste - nutze limit(…), skip(…) und findFirst(…)
	@Test
	void secondOldestWoman() {
		assertThat(people.stream()
				.filter(p -> p.gender() == Gender.FEMALE)
				.sorted(java.util.Comparator.comparingInt(Person::age))
				.skip(1)
				.limit(1)
				.findFirst()
				.orElseThrow()
		).extracting(Person::name).isEqualTo("Alice");
	}
}
