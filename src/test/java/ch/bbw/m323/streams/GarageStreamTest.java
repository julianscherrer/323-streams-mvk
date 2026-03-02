package ch.bbw.m323.streams;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GarageStreamTest implements WithAssertions {

	Inventory inventory;

	record Inventory(List<Customer> products) {

		record Customer(String id, String customer, String email, List<Car> cars) {

			record Car(String brand, String price, Wheel wheels, Radio radio) {

				record Wheel(String brand, Integer amount) {
				}

				record Radio(Boolean ukw, Bluetooth bluetooth) {

					record Bluetooth(Integer version, List<Standard> standards) {

						record Standard(String codec, Boolean partial) {
						}
					}
				}
			}
		}
	}

	@BeforeEach
	void readJson() throws IOException {
		// TODO: change to "manynull.json" for a harder experience
		try (var in = GarageStreamTest.class.getClassLoader().getResourceAsStream("fewnull.json")) {
			inventory = new ObjectMapper().readValue(in, Inventory.class);
		}
	}

	//Aufgabe 1 Finde die Namen aller Kunden welche 2 oder mehr Autos gekauft haben.
	@Test
	void customersWithTwoOrMoreCars() {
		//Aufgabe 3 Gehe nochmals durch die gelösten Aufgaben und ersetze alle Lambdas
		//durch Methodenreferenzen oder (benannte) Variablen. Dies erhöht die Lesbarkeit komplexer Streamausdrücke.

		java.util.function.Predicate<Inventory.Customer> hasTwoPlusCars =
				customer -> customer.cars() != null && customer.cars().size() >= 2;

		assertThat(inventory.products().stream()
				.filter(customer -> customer.cars() != null && customer.cars().size() >= 2)
				.map(Inventory.Customer::customer)
				.toList()
		).hasSizeBetween(10, 11);
	}

	//Aufgabe 2 Finde die Anzahl Autos mit verbautem UKW-Radio.
	@Test
	void carsWithUkwRadio() {
		//Aufgabe 3 Gehe nochmals durch die gelösten Aufgaben und ersetze alle Lambdas
		//durch Methodenreferenzen oder (benannte) Variablen. Dies erhöht die Lesbarkeit komplexer Streamausdrücke.

		java.util.function.Predicate<Inventory.Customer.Car> hasUkw =
				car -> car.radio() != null && car.radio().ukw() != null && car.radio().ukw();

		assertThat(inventory.products().stream()
				.filter(customer -> customer.cars() != null)
				.flatMap(customer -> customer.cars().stream())
				.filter(car -> car.radio() != null && car.radio().ukw() != null && car.radio().ukw())
				.count()
		).isIn(8L, 16L);
	}
}
