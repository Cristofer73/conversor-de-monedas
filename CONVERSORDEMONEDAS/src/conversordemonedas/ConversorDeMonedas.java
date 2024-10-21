package conversordemonedas;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ConversorDeMonedas {
    private static final String API_KEY = "TU_API_KEY"; // Si tu API lo requiere
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/4ffc09097ac98efb2e68028a/latest/USD"; // Ajusta según la API

    public static void main(String[] args) {
        try {
            // Hacer la solicitud a la API
            URL url = new URL(API_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            // Leer la respuesta
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parsear JSON
            JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
            System.out.println("Tasa de cambio: " + jsonObject);

            // Obtener las tasas de cambio
            JsonObject rates = jsonObject.getAsJsonObject("rates");

            // Solicitar al usuario las monedas y la cantidad
            Scanner scanner = new Scanner(System.in);
            System.out.print("Ingrese la moneda de origen (por ejemplo, EUR, JPY): ");
            String fromCurrency = scanner.nextLine().toUpperCase();
            System.out.print("Ingrese la moneda de destino (por ejemplo, USD, GBP): ");
            String toCurrency = scanner.nextLine().toUpperCase();
            System.out.print("Ingrese la cantidad a convertir: ");
            double amount = scanner.nextDouble();

            // Realizar la conversión
            if (rates.has(fromCurrency) && rates.has(toCurrency)) {
                double fromRate = rates.get(fromCurrency).getAsDouble();
                double toRate = rates.get(toCurrency).getAsDouble();
                double convertedAmount = (amount / fromRate) * toRate;

                System.out.printf("%.2f %s es igual a %.2f %s%n", amount, fromCurrency, convertedAmount, toCurrency);
            } else {
                System.out.println("Una de las monedas ingresadas no es válida.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
