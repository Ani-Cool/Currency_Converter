package comm;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;

public class CurrencyConverter {
    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Type Currency to convert from");
        String convertFrom = scanner.nextLine().toUpperCase();
        System.out.println("Type Currency to convert to");
        String convertTo = scanner.nextLine().toUpperCase();
        System.out.println("Type quantity to convert");
        BigDecimal quantity = scanner.nextBigDecimal();

        String urlString = "https://v6.exchangerate-api.com/v6/YOUR-API-KEY/latest?base=" + convertFrom;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlString)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String stringResponse = response.body().string();

        try {
            JSONObject jsonObject = new JSONObject(stringResponse);

            if (!jsonObject.has("rates")) {
                System.err.println("Error: The response does not contain exchange rate data.");
            } else {
                JSONObject ratesObject = jsonObject.getJSONObject("rates");

                if (!ratesObject.has(convertTo)) {
                    System.err.println("Error: Could not find exchange rate for the specified currency.");
                } else {
                    BigDecimal rate = ratesObject.getBigDecimal(convertTo);
                    BigDecimal result = rate.multiply(quantity);
                    System.out.println(result);
                }
            }
        } catch (JSONException e) {
            System.err.println("Error: Invalid JSON response from the API.");
        }
    }
}
