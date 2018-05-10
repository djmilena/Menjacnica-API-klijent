package menjacnica;

	
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map.Entry;

import menjacnica.Conversion;
import menjacnica.Currency;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;





public class ExchangeOffice {
		public static final String service = "/countries";
		public static final String CURRENCY_LAYER_API_URL = "http://free.currencyconverterapi.com/api/v3";
		public static final String service2 = "/convert";
		public static final String CURRENCY_LAYER_API_URL2 = "http://free.currencyconverterapi.com/api/v3";
		
		public static String ucitajSaURL(String url) throws Exception {
			URL obj=new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			
			BufferedReader reader=new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			String content="";
			boolean end=false;
			while(!end) {
				String line=reader.readLine();
				if(line==null) end=true;
				else {
					content+=line;
					
				}
				
			}
			reader.close();
			return content;
			
		}
		public static ArrayList<Currency> vratiValute() throws Exception{
			String url = CURRENCY_LAYER_API_URL + service;
			
			Gson gson=new GsonBuilder().serializeNulls().create();
			ArrayList<Currency> valute=new ArrayList<Currency>();
			JsonObject objContent=gson.fromJson(ucitajSaURL(url), JsonObject.class);
			JsonObject currenciesJson = objContent.get("results").getAsJsonObject();
			
			
			
			for (Entry<String, JsonElement> entry : currenciesJson.entrySet()) {
				JsonObject obj=(JsonObject) entry.getValue();
				Currency d=new Currency();
				d=gson.fromJson(obj, Currency.class);
				
				valute.add(d);
				
				
			}
			
			
			return valute;
		}
		
		
		public static double vratiKurs(String iz, String u)  {
			String url = CURRENCY_LAYER_API_URL2 + service2 + '?' + "q=" + iz + '_' + u;
			String sadrzaj;
			try {
				sadrzaj = ucitajSaURL(url);
				Gson gson=new GsonBuilder().create();
				JsonObject con=gson.fromJson(sadrzaj, JsonObject.class);
				JsonObject query = con.get("query").getAsJsonObject();
				
				if(query.get("count").getAsInt()!=0) {
					JsonObject result = con.get("results").getAsJsonObject();
					JsonObject kon=result.getAsJsonObject(iz+"_"+u).getAsJsonObject();
					double vrednost=kon.get("val").getAsDouble();
					
					return vrednost;
				} 
				else {
					throw new RuntimeException("Ne postoje podaci o konverziji");
					
					
				}
			} catch (Exception e) {
			
				e.printStackTrace();
			}
			return 0;	
			
		}
		
		public static void  serijalizacija(String iz, String u, double kurs){
			
			Conversion k=new Conversion();
			Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
			
			
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss.SSSSSS");
			String date = format.format(now);
			k.setDateTime(date);
			k.setFromCurrency(iz);
			k.setToCurrency(u);
			k.setCourse(kurs);
			JsonArray proslo=null;
			try(FileReader reader=new FileReader("data/log.json")) {
				proslo=gson.fromJson(reader, JsonArray.class);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try(FileWriter writer=new FileWriter("data/log.json")) {
				if(proslo==null) {
					writer.write(gson.toJson(k));
				}
				else {
				String string=gson.toJson(k);
				//iz stringa u json objekat
				JsonObject obj=gson.fromJson(string, JsonObject.class);
				proslo.add(obj);
				writer.write(gson.toJson(proslo));
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
		}

}
